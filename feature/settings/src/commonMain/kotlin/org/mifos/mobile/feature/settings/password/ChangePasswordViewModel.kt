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
 * ViewModel for the "Change Password" screen.
 *
 * This class manages the state and business logic for changing a user's password. It handles
 * real-time input validation, password strength checking, and communication with the
 * repositories to update the password. It also implements security features like
 * submission attempt throttling and automatic logout upon successful password change.
 * It follows a Unidirectional Data Flow (UDF) pattern.
 *
 * @param repository The repository for handling user authentication and password updates.
 * @param userDataRepository The repository for accessing user-specific data, including the current password.
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
        // Observes the current password from user data and pre-populates the state.
        userDataRepository.userData.map {
            it.data?.password ?: ""
        }.onEach {
            trySendAction(PasswordAction.Internal.OldPasswordReceived(it))
        }.launchIn(viewModelScope)
    }

    /**
     * Central handler for all incoming actions from the UI or internal events.
     * It delegates each action to the appropriate business logic function.
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
     * Validates the current password input field.
     * @param password The password string to validate.
     * @return A [ValidationResult] indicating success or failure with an error message.
     */
    private fun validateCurrentPassword(password: String): ValidationResult = when {
        password.isEmpty() -> ValidationResult.Error(Res.string.password_empty_error)
        password.length < 8 -> ValidationResult.Error(Res.string.password_length_error)
        password != state.currentPassword -> ValidationResult.Error(Res.string.password_current_incorrect_error)
        else -> ValidationResult.Success
    }

    /**
     * Validates the new password against a set of rules, including checks for empty strings,
     * repeating characters, and minimum strength requirements.
     * @param password The new password string to validate.
     * @return A [ValidationResult] indicating success or failure.
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
     * Validates the confirmed password to ensure it matches the new password and meets basic requirements.
     * @param confirmPassword The password from the confirmation field.
     * @param newPassword The password from the new password field.
     * @return A [ValidationResult] indicating success or failure.
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
     * Handles changes to the current password field with debounced validation.
     * @param newValue The latest value from the input field.
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
     * Handles changes to the new password field, triggering password strength analysis
     * and debounced validation for both the new and confirm password fields.
     * @param newValue The latest value from the input field.
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
     * Handles changes to the confirm password field with debounced validation.
     * @param newValue The latest value from the input field.
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
     * Processes the result from the password strength checker and updates the UI state accordingly.
     * @param action The internal action containing the [PasswordStrengthResult].
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
     * Validates all input fields upon submission. If valid, proceeds to update the password.
     * If invalid, increments the failed attempt counter. It also blocks submissions
     * if the maximum number of attempts has been reached.
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
     * Initiates the password update process by showing a loading dialog and calling
     * the repository to update the password.
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
     * Handles the result of the password update operation from the repository.
     * On success, it shows a success dialog, clears sensitive data, and triggers a delayed logout.
     * On failure, it shows an error dialog.
     * @param action The internal action containing the [DataState] result.
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
     * Updates the state with the current password received from the data repository.
     * @param action The internal action containing the optional password string.
     */
    private fun handleOldPasswordReceived(action: PasswordAction.Internal.OldPasswordReceived) {
        action.password?.let {
            mutableStateFlow.update { it.copy(currentPassword = action.password) }
        }
    }

    /**
     * A utility function to debounce validation logic, preventing excessive processing
     * on every keystroke.
     * @param validation The suspendable validation block to execute after a delay.
     */
    private fun debounceValidation(validation: suspend () -> Unit) {
        validationJob?.cancel()
        validationJob = viewModelScope.launch {
            delay(300)
            validation()
        }
    }

    /**
     * Checks if a string contains consecutive repeating characters.
     * @param input The string to check.
     * @return `true` if consecutive characters are found, `false` otherwise.
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

    /** Hides any currently displayed dialog. */
    private fun dismissDialog() {
        mutableStateFlow.update {
            it.copy(dialogState = null)
        }
    }

    /** Clears sensitive data and sends a navigation back event. */
    private fun navigateBack() {
        clearSensitiveData()
        sendEvent(PasswordEvent.OnNavigateBack)
    }

    /** Resets the counter for failed submission attempts. */
    private fun resetFailedAttempts() {
        failedAttempts = 0
    }

    /**
     * Clears all sensitive user input and validation state from the ViewModel.
     * This is used after a successful update or when navigating away from the screen.
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

    /**
     * Overridden to ensure all coroutine jobs are cancelled and sensitive data is cleared
     * when the ViewModel is no longer in use.
     */
    override fun onCleared() {
        super.onCleared()
        clearSensitiveData()
        validationJob?.cancel()
        passwordStrengthJob.cancel()
    }
}

/**
 * Represents the UI state for the "Change Password" screen.
 *
 * @property currentPassword The user's actual current password (internal use).
 * @property oldPassword The value entered in the "Old Password" field.
 * @property newPassword The value entered in the "New Password" field.
 * @property confirmPassword The value entered in the "Confirm Password" field.
 * @property oldPasswordError An optional error message for the old password field.
 * @property newPasswordError An optional error message for the new password field.
 * @property confirmPasswordError An optional error message for the confirm password field.
 * @property oldPasswordVisible Toggles the visibility of the old password.
 * @property newPasswordVisible Toggles the visibility of the new password.
 * @property confirmPasswordVisible Toggles the visibility of the confirm password.
 * @property passwordStrengthState The calculated strength of the new password.
 * @property passwordFeedback A list of suggestions to improve password strength.
 * @property dialogState The current state of any dialogs (loading, success, error).
 * @property isEnabled A computed property to determine if the submit button should be enabled.
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
    /** Defines the possible states for dialogs on the screen. */
    internal sealed interface DialogState {
        /** The dialog is showing a loading indicator. */
        data object Loading : DialogState

        /** The dialog is showing a success message. */
        data class Success(val message: StringResource) : DialogState

        /** The dialog is showing an error message. */
        data class Error(val message: StringResource) : DialogState
    }

    /** Determines if the submit button should be enabled based on input validity and presence. */
    internal val isEnabled = oldPasswordError == null &&
        newPasswordError == null &&
        confirmPasswordError == null &&
        oldPassword.isNotEmpty() &&
        newPassword.isNotEmpty() &&
        confirmPassword.isNotEmpty()
}

/**
 * Defines one-time events that can be sent from the ViewModel to the UI, such as navigation.
 */
internal sealed interface PasswordEvent {
    /** Signals that the UI should navigate back. */
    data object OnNavigateBack : PasswordEvent
}

/**
 * Defines all possible actions that can be sent from the UI to the ViewModel or used internally.
 */
internal sealed interface PasswordAction {
    /** Action triggered when the old password input changes. */
    data class OnOldPasswordChange(val currentPassword: String) : PasswordAction

    /** Action triggered when the new password input changes. */
    data class OnNewPasswordChange(val newPassword: String) : PasswordAction

    /** Action triggered when the confirm password input changes. */
    data class OnConfirmPasswordChange(val confirmPassword: String) : PasswordAction

    /** Action to toggle the visibility of the old password. */
    data object OldPasswordVisibleClick : PasswordAction

    /** Action to toggle the visibility of the new password. */
    data object NewPasswordVisibleClick : PasswordAction

    /** Action to toggle the visibility of the confirm password. */
    data object ConfirmPasswordVisibleClick : PasswordAction

    /** Action triggered when the user clicks the submit button. */
    data object SubmitClick : PasswordAction

    /** Action to retry submission after being blocked. */
    data object RetrySubmit : PasswordAction

    /** Action to navigate back from the screen. */
    data object NavigateBack : PasswordAction

    /** Action to dismiss the current dialog. */
    data object DismissDialog : PasswordAction

    /** Defines internal actions that are not directly triggered by the user. */
    sealed interface Internal : PasswordAction {
        /** Internal action representing the result of the password update operation. */
        data class UpdatePasswordResult(val result: DataState<String>) : Internal

        /** Internal action carrying the result of a password strength check. */
        data class ReceivePasswordStrengthResult(val result: PasswordStrengthResult) : Internal

        /** Internal action for when the current password is fetched from the repository. */
        data class OldPasswordReceived(val password: String?) : Internal
    }
}

/**
 * A sealed class to represent the result of a validation check.
 */
sealed class ValidationResult {
    /** Represents a successful validation. */
    data object Success : ValidationResult()

    /** Represents a failed validation with an associated error message. */
    data class Error(val message: StringResource) : ValidationResult()
}
