/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.password

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.settings.generated.resources.Res
import mifos_mobile.feature.settings.generated.resources.password_confirm_mismatch_error
import mifos_mobile.feature.settings.generated.resources.password_current_incorrect_error
import mifos_mobile.feature.settings.generated.resources.password_empty_error
import mifos_mobile.feature.settings.generated.resources.password_empty_error_repeat
import mifos_mobile.feature.settings.generated.resources.password_length_error
import mifos_mobile.feature.settings.generated.resources.password_same_as_current_error
import mifos_mobile.feature.settings.generated.resources.password_too_many_attempts
import mifos_mobile.feature.settings.generated.resources.password_update_failed
import mifos_mobile.feature.settings.generated.resources.password_update_success
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.UserAuthRepository
import org.mifos.mobile.core.data.repository.UserDataRepository
import org.mifos.mobile.core.ui.PasswordStrengthState
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.PasswordChecker
import org.mifos.mobile.core.ui.utils.PasswordStrength
import org.mifos.mobile.core.ui.utils.PasswordStrengthResult

/**
 * ViewModel for the Change Password screen. It manages the state, validation logic,
 * and interactions for updating a user's password.
 *
 * @param repository The repository for handling user authentication operations.
 * @param userDataRepository The repository for accessing user-specific data.
 */
@Suppress("CyclomaticComplexMethod", "TooManyFunctions")
internal class ChangePasswordViewModel(
    private val repository: UserAuthRepository,
    private val userDataRepository: UserDataRepository,
) : BaseViewModel<PasswordState, PasswordEvent, PasswordAction>(
    initialState = PasswordState(),
) {
    private var validationJob: Job? = null
    private var passwordStrengthJob: Job = Job()
    private var failedAttempts = 0
    private val maxFailedAttempts = 5

    init {
        // Observe user data to get the current password.
        userDataRepository.userData.map {
            it.data?.password ?: ""
        }.onEach {
            trySendAction(PasswordAction.Internal.OldPasswordReceived(it))
        }.launchIn(viewModelScope)
    }

    /**
     * Handles incoming actions from the UI, such as input changes, button clicks,
     * and internal events.
     *
     * @param action The [PasswordAction] to be processed.
     */
    override fun handleAction(action: PasswordAction) {
        when (action) {
            is PasswordAction.OnOldPasswordChange -> {
                onCurrentPasswordChange(action.currentPassword)
            }
            is PasswordAction.OnNewPasswordChange -> {
                onNewPasswordChange(action.newPassword)
            }
            is PasswordAction.OnConfirmPasswordChange -> {
                onConfirmPasswordChange(action.confirmPassword)
            }

            PasswordAction.OldPasswordVisibleClick -> toggleCurrentPasswordVisibility()
            PasswordAction.NewPasswordVisibleClick -> toggleNewPasswordVisibility()
            PasswordAction.ConfirmPasswordVisibleClick -> toggleConfirmPasswordVisibility()

            PasswordAction.SubmitClick -> validateAndSubmit()
            PasswordAction.DismissDialog -> dismissDialog()
            PasswordAction.NavigateBack -> navigateBack()
            PasswordAction.RetrySubmit -> resetFailedAttempts()

            is PasswordAction.Internal.UpdatePasswordResult -> handleUpdatePasswordResult(action)
            is PasswordAction.Internal.ReceivePasswordStrengthResult -> handlePasswordStrengthResult(action)
            is PasswordAction.Internal.OldPasswordReceived -> handleOldPasswordReceived(action)
        }
    }

    /**
     * Validates the provided current password.
     * @param password The current password string to validate.
     * @return [ValidationResult.Success] if valid, otherwise [ValidationResult.Error].
     */
    private fun validateCurrentPassword(password: String): ValidationResult = when {
        password.isEmpty() -> ValidationResult.Error(Res.string.password_empty_error)
        password.length < 8 -> ValidationResult.Error(Res.string.password_length_error)
        password != state.currentPassword -> ValidationResult.Error(Res.string.password_current_incorrect_error)
        else -> ValidationResult.Success
    }

    /**
     * Validates the new password based on strength and other rules.
     * @param password The new password string to validate.
     * @return [ValidationResult.Success] if valid, otherwise [ValidationResult.Error].
     */
    @Suppress("ReturnCount")
    private fun validateNewPassword(password: String): ValidationResult {
        if (password.isEmpty()) {
            return ValidationResult.Error(Res.string.password_empty_error)
        }
        if (hasConsecutiveRepeatingChars(password)) {
            return ValidationResult.Error(Res.string.password_empty_error_repeat)
        }

        when (val result = PasswordChecker.getPasswordStrengthResult(password)) {
            is PasswordStrengthResult.Error -> {
                return ValidationResult.Error(result.message)
            }

            is PasswordStrengthResult.Success -> {
                // Additional custom validations
                if (password == state.currentPassword) {
                    return ValidationResult.Error(Res.string.password_same_as_current_error)
                }
            }
        }

        return ValidationResult.Success
    }

    /**
     * Validates that the confirmed password matches the new password.
     * @param confirmPassword The confirmed password string.
     * @param newPassword The new password string.
     * @return [ValidationResult.Success] if they match, otherwise [ValidationResult.Error].
     */
    private fun validateConfirmPassword(
        confirmPassword: String,
        newPassword: String,
    ): ValidationResult = when {
        confirmPassword.isEmpty() -> ValidationResult.Error(Res.string.password_empty_error)
        confirmPassword.length < 8 -> ValidationResult.Error(Res.string.password_length_error)
        newPassword != confirmPassword -> ValidationResult.Error(Res.string.password_confirm_mismatch_error)
        else -> ValidationResult.Success
    }

    /**
     * Handles changes to the current password input field with debounced validation.
     * @param newValue The updated password string.
     */
    private fun onCurrentPasswordChange(newValue: String) {
        mutableStateFlow.update {
            it.copy(
                oldPassword = newValue,
            )
        }

        debounceValidation {
            val result = validateCurrentPassword(newValue)
            mutableStateFlow.update {
                it.copy(
                    oldPasswordError = if (result is ValidationResult.Error) result.message else null,
                )
            }
        }
    }

    /**
     * Handles changes to the new password field, updates strength, and runs debounced validation.
     * @param newValue The updated password string.
     */
    private fun onNewPasswordChange(newValue: String) {
        mutableStateFlow.update {
            it.copy(
                newPassword = newValue,
            )
        }

        passwordStrengthJob.cancel()
        if (newValue.isEmpty()) {
            mutableStateFlow.update {
                it.copy(
                    passwordStrengthState = PasswordStrengthState.NONE,
                    passwordFeedback = emptyList(),
                )
            }
        } else {
            passwordStrengthJob = viewModelScope.launch {
                val result = PasswordChecker.getPasswordStrengthResult(newValue)
                val feedback = PasswordChecker.getPasswordFeedback(newValue)
                trySendAction(PasswordAction.Internal.ReceivePasswordStrengthResult(result))

                // Update feedback in state
                mutableStateFlow.update {
                    it.copy(passwordFeedback = feedback)
                }
            }
        }

        debounceValidation {
            val newResult = validateNewPassword(newValue)
            val confirmResult = if (state.confirmPassword.isNotEmpty()) {
                validateConfirmPassword(state.confirmPassword, newValue)
            } else {
                ValidationResult.Success
            }

            mutableStateFlow.update {
                it.copy(
                    newPasswordError = if (newResult is ValidationResult.Error) newResult.message else null,
                    confirmPasswordError = if (confirmResult is ValidationResult.Error) confirmResult.message else null,
                )
            }
        }
    }

    /**
     * Handles changes to the confirm password input field with debounced validation.
     * @param newValue The updated password string.
     */
    private fun onConfirmPasswordChange(newValue: String) {
        mutableStateFlow.update {
            it.copy(
                confirmPassword = newValue,
            )
        }

        debounceValidation {
            val result = validateConfirmPassword(newValue, state.newPassword)
            mutableStateFlow.update {
                it.copy(
                    confirmPasswordError = if (result is ValidationResult.Error) result.message else null,
                )
            }
        }
    }

    /**
     * Updates the password strength state based on the validation result.
     * @param action The internal action containing the password strength result.
     */
    private fun handlePasswordStrengthResult(action: PasswordAction.Internal.ReceivePasswordStrengthResult) {
        when (val result = action.result) {
            is PasswordStrengthResult.Success -> {
                val updatedState = when (result.passwordStrength) {
                    PasswordStrength.LEVEL_0 -> PasswordStrengthState.WEAK_1
                    PasswordStrength.LEVEL_1 -> PasswordStrengthState.WEAK_2
                    PasswordStrength.LEVEL_2 -> PasswordStrengthState.WEAK_3
                    PasswordStrength.LEVEL_3 -> PasswordStrengthState.GOOD
                    PasswordStrength.LEVEL_4 -> PasswordStrengthState.STRONG
                    PasswordStrength.LEVEL_5 -> PasswordStrengthState.VERY_STRONG
                }
                mutableStateFlow.update { oldState ->
                    oldState.copy(passwordStrengthState = updatedState)
                }
            }

            is PasswordStrengthResult.Error -> {
                mutableStateFlow.update {
                    it.copy(
                        newPasswordError = result.message,
                        passwordStrengthState = PasswordStrengthState.NONE,
                    )
                }
            }
        }
    }

    /**
     * Validates all password fields and submits the change if they are all valid.
     * Tracks failed attempts to prevent brute-forcing.
     */
    private fun validateAndSubmit() {
        if (failedAttempts >= maxFailedAttempts) {
            mutableStateFlow.update {
                it.copy(
                    dialogState = PasswordState.DialogState.Error(
                        Res.string.password_too_many_attempts,
                    ),
                )
            }
            return
        }

        val currentResult = validateCurrentPassword(state.oldPassword)
        val newResult = validateNewPassword(state.newPassword)
        val confirmResult = validateConfirmPassword(state.confirmPassword, state.newPassword)

        mutableStateFlow.update {
            it.copy(
                oldPasswordError = if (currentResult is ValidationResult.Error) currentResult.message else null,
                newPasswordError = if (newResult is ValidationResult.Error) newResult.message else null,
                confirmPasswordError = if (confirmResult is ValidationResult.Error) confirmResult.message else null,
            )
        }

        val isValid =
            listOf(currentResult, newResult, confirmResult).all { it is ValidationResult.Success }

        if (isValid) {
            handleSubmitClick()
        } else {
            failedAttempts++
        }
    }

    /**
     * Initiates the password update process by calling the repository.
     */
    private fun handleSubmitClick() {
        mutableStateFlow.update {
            it.copy(dialogState = PasswordState.DialogState.Loading)
        }

        viewModelScope.launch {
            try {
                val result = repository.updateAccountPassword(
                    state.newPassword,
                    state.confirmPassword,
                )
                trySendAction(
                    PasswordAction.Internal.UpdatePasswordResult(
                        result,
                    ),
                )
            } catch (e: Exception) {
                mutableStateFlow.update {
                    it.copy(
                        dialogState = PasswordState.DialogState.Success(Res.string.password_update_failed),
                    )
                }
            }
        }
    }

    /**
     * Handles the result of the password update operation, showing a success or error dialog.
     * Logs the user out on success.
     * @param action The internal action containing the result of the update operation.
     */
    private fun handleUpdatePasswordResult(action: PasswordAction.Internal.UpdatePasswordResult) {
        when (action.result) {
            is DataState.Error -> {
                mutableStateFlow.update {
                    it.copy(
                        dialogState = PasswordState.DialogState.Error(
                            Res.string
                                .password_update_failed,
                        ),
                    )
                }
            }

            is DataState.Success -> {
                mutableStateFlow.update {
                    it.copy(
                        dialogState = PasswordState.DialogState.Success(Res.string.password_update_success),
                    )
                }

                // Clear sensitive data after successful update
                clearSensitiveData()
                viewModelScope.launch {
                    delay(3000)
                    userDataRepository.logOut()
                }
            }
            else -> {
                failedAttempts++
                mutableStateFlow.update {
                    it.copy(
                        dialogState = PasswordState.DialogState.Error(Res.string.password_update_failed),
                    )
                }
            }
        }

        viewModelScope.launch {
            delay(2500)
            userDataRepository.logOut()
        }
    }

    /**
     * Updates the state with the user's current password received from the repository.
     * @param action The internal action containing the user's password.
     */
    private fun handleOldPasswordReceived(action: PasswordAction.Internal.OldPasswordReceived) {
        action.password?.let {
            mutableStateFlow.update { it.copy(currentPassword = action.password) }
        }
    }

    /**
     * A helper function to debounce validation logic.
     * @param validation The validation logic to execute after a delay.
     */
    private fun debounceValidation(validation: suspend () -> Unit) {
        validationJob?.cancel()
        validationJob = viewModelScope.launch {
            delay(300)
            validation()
        }
    }

    /**
     * Checks if a string has consecutive repeating characters.
     * @param input The string to check.
     * @return `true` if consecutive repeating characters are found, `false` otherwise.
     */
    private fun hasConsecutiveRepeatingChars(input: String): Boolean {
        for (i in 0 until input.length - 1) {
            if (input[i] == input[i + 1]) {
                return true
            }
        }
        return false
    }

    /** Toggles the visibility of the current password field. */
    private fun toggleCurrentPasswordVisibility() {
        mutableStateFlow.update {
            it.copy(oldPasswordVisible = !it.oldPasswordVisible)
        }
    }

    /** Toggles the visibility of the new password field. */
    private fun toggleNewPasswordVisibility() {
        mutableStateFlow.update {
            it.copy(newPasswordVisible = !it.newPasswordVisible)
        }
    }

    /** Toggles the visibility of the confirm password field. */
    private fun toggleConfirmPasswordVisibility() {
        mutableStateFlow.update {
            it.copy(confirmPasswordVisible = !it.confirmPasswordVisible)
        }
    }

    /** Dismisses any currently shown dialog. */
    private fun dismissDialog() {
        mutableStateFlow.update {
            it.copy(dialogState = null)
        }
    }

    /** Clears sensitive data and navigates back. */
    private fun navigateBack() {
        clearSensitiveData()
        sendEvent(PasswordEvent.OnNavigateBack)
    }

    /** Resets the counter for failed submission attempts. */
    private fun resetFailedAttempts() {
        failedAttempts = 0
    }

    /**
     * Clears all sensitive password data and validation errors from the state.
     */
    private fun clearSensitiveData() {
        mutableStateFlow.update {
            it.copy(
                oldPassword = "",
                newPassword = "",
                confirmPassword = "",
                oldPasswordError = null,
                newPasswordError = null,
                confirmPasswordError = null,
                passwordStrengthState = PasswordStrengthState.NONE,
                passwordFeedback = emptyList(),
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        clearSensitiveData()
        validationJob?.cancel()
        passwordStrengthJob.cancel()
    }
}

/**
 * Represents the state of the Change Password screen.
 *
 * @property currentPassword The user's actual current password (internal use).
 * @property oldPassword The value entered in the "current password" field.
 * @property newPassword The value entered in the "new password" field.
 * @property confirmPassword The value entered in the "confirm password" field.
 * @property oldPasswordError A string resource for the current password validation error, if any.
 * @property newPasswordError A string resource for the new password validation error, if any.
 * @property confirmPasswordError A string resource for the confirm password validation error, if any.
 * @property oldPasswordVisible Whether the current password text is visible.
 * @property newPasswordVisible Whether the new password text is visible.
 * @property confirmPasswordVisible Whether the confirm password text is visible.
 * @property passwordStrengthState The calculated strength of the new password.
 * @property passwordFeedback A list of suggestions for improving password strength.
 * @property dialogState The state of any dialog to be shown (e.g., loading, success, error).
 * @property isEnabled Whether the submit button should be enabled.
 */
internal data class PasswordState(
    internal val currentPassword: String = "",
    val oldPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",

    val oldPasswordError: StringResource? = null,
    val newPasswordError: StringResource? = null,
    val confirmPasswordError: StringResource? = null,

    val oldPasswordVisible: Boolean = false,
    val newPasswordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,

    val passwordStrengthState: PasswordStrengthState = PasswordStrengthState.NONE,
    val passwordFeedback: List<StringResource> = emptyList(),
    val dialogState: DialogState? = null,
) {
    /** Represents the state of the dialog shown on the screen. */
    internal sealed interface DialogState {
        data object Loading : DialogState
        data class Success(val message: StringResource) : DialogState
        data class Error(val message: StringResource) : DialogState
    }

    /** Determines if the submit button should be enabled based on input validity. */
    internal val isEnabled = oldPasswordError == null &&
        newPasswordError == null &&
        confirmPasswordError == null &&
        oldPassword.isNotEmpty() &&
        newPassword.isNotEmpty() &&
        confirmPassword.isNotEmpty()
}

/**
 * Represents events that can be sent from the ViewModel to the UI, typically for navigation.
 */
internal sealed interface PasswordEvent {
    /** Event to navigate back to the previous screen. */
    data object OnNavigateBack : PasswordEvent
}

/**
 * Represents actions that can be dispatched from the UI to the ViewModel.
 */
internal sealed interface PasswordAction {
    /** Action for when the current password input changes. */
    data class OnOldPasswordChange(val currentPassword: String) : PasswordAction

    /** Action for when the new password input changes. */
    data class OnNewPasswordChange(val newPassword: String) : PasswordAction

    /** Action for when the confirm password input changes. */
    data class OnConfirmPasswordChange(val confirmPassword: String) : PasswordAction

    /** Action to toggle visibility of the current password. */
    data object OldPasswordVisibleClick : PasswordAction

    /** Action to toggle visibility of the new password. */
    data object NewPasswordVisibleClick : PasswordAction

    /** Action to toggle visibility of the confirm password. */
    data object ConfirmPasswordVisibleClick : PasswordAction

    /** Action to submit the password change request. */
    data object SubmitClick : PasswordAction

    /** Action to retry submission after too many failed attempts. */
    data object RetrySubmit : PasswordAction

    /** Action to navigate back. */
    data object NavigateBack : PasswordAction

    /** Action to dismiss the current dialog. */
    data object DismissDialog : PasswordAction

    /** Represents internal actions used within the ViewModel. */
    sealed interface Internal : PasswordAction {
        /** Action containing the result of the password update operation. */
        data class UpdatePasswordResult(val result: DataState<String>) : Internal

        /** Action containing the calculated password strength result. */
        data class ReceivePasswordStrengthResult(val result: PasswordStrengthResult) : Internal

        /** Action containing the user's current password from the repository. */
        data class OldPasswordReceived(val password: String?) : Internal
    }
}

/**
 * A sealed class to represent the result of a validation check.
 */
sealed class ValidationResult {
    /** Represents a successful validation. */
    data object Success : ValidationResult()

    /** Represents a failed validation with an error message. */
    data class Error(val message: StringResource) : ValidationResult()
}
