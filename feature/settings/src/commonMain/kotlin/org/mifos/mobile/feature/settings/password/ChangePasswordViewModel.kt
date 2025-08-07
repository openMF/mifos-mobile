package org.mifos.mobile.feature.settings.password

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.core.ui.generated.resources.Res
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.UserDataRepository
import org.mifos.mobile.core.ui.PasswordStrengthState
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.PasswordChecker
import org.mifos.mobile.core.ui.utils.PasswordStrength
import org.mifos.mobile.core.ui.utils.PasswordStrengthResult

@Suppress("CyclomaticComplexMethod")
internal class ChangePasswordViewModel(
    private val passwordRepository: PasswordRepository,
    private val userDataRepository: UserDataRepository,
) : BaseViewModel<PasswordState, PasswordEvent, PasswordAction>(
    initialState = PasswordState(),
) {
    private var validationJob: Job? = null
    private var passwordStrengthJob: Job = Job()
    private var failedAttempts = 0
    private val maxFailedAttempts = 3

    init {
//        userDataRepository.account.map {
//            it.password
//        }.onEach {
//            trySendAction(PasswordAction.Internal.OldPasswordReceived(it))
//        }.launchIn(viewModelScope)
    }

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
            is PasswordAction.NavigateToLogin -> sendEvent(PasswordEvent.OnNavigateToLogin)
        }
    }

    private fun validateCurrentPassword(password: String): ValidationResult = when {
        password.isEmpty() -> ValidationResult.Error(Res.string.password_empty_error)
        password.length < 8 -> ValidationResult.Error(Res.string.password_length_error)
        password != state.currentPassword -> ValidationResult.Error(Res.string.password_current_incorrect_error)
        else -> ValidationResult.Success
    }

    @Suppress("ReturnCount")
    private fun validateNewPassword(password: String): ValidationResult {
        if (password.isEmpty()) {
            return ValidationResult.Error(Res.string.password_empty_error)
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

    private fun validateConfirmPassword(
        confirmPassword: String,
        newPassword: String,
    ): ValidationResult = when {
        confirmPassword.isEmpty() -> ValidationResult.Error(Res.string.password_empty_error)
        confirmPassword.length < 8 -> ValidationResult.Error(Res.string.password_length_error)
        newPassword != confirmPassword -> ValidationResult.Error(Res.string.password_confirm_mismatch_error)
        else -> ValidationResult.Success
    }

    private fun onCurrentPasswordChange(newValue: String) {
        mutableStateFlow.update {
            it.copy(
                oldPassword = newValue,
                oldPasswordError = null,
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

    private fun onNewPasswordChange(newValue: String) {
        mutableStateFlow.update {
            it.copy(
                newPassword = newValue,
                newPasswordError = null,
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

    private fun onConfirmPasswordChange(newValue: String) {
        mutableStateFlow.update {
            it.copy(
                confirmPassword = newValue,
                confirmPasswordError = null,
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

    private fun handleSubmitClick() {
        mutableStateFlow.update {
            it.copy(dialogState = PasswordState.DialogState.Loading)
        }

        viewModelScope.launch {
            try {
                val result = passwordRepository.updatePassword(
                    UserPassword(
                        password = state.newPassword,
                        repeatPassword = state.confirmPassword,
                    ),
                )
                trySendAction(
                    UpdatePasswordResult(
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

    private fun handleUpdatePasswordResult(action: UpdatePasswordResult) {
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
            userDataRepository.logout(
                reason = LogoutReason.Click("Settings Logout"),
            )
        }
    }

    private fun handleOldPasswordReceived(action: PasswordAction.Internal.OldPasswordReceived) {
        action.password?.let {
            mutableStateFlow.update { it.copy(currentPassword = action.password) }
        }
    }

    private fun debounceValidation(validation: suspend () -> Unit) {
        validationJob?.cancel()
        validationJob = viewModelScope.launch {
            delay(300)
            validation()
        }
    }

    private fun toggleCurrentPasswordVisibility() {
        mutableStateFlow.update {
            it.copy(oldPasswordVisible = !it.oldPasswordVisible)
        }
    }

    private fun toggleNewPasswordVisibility() {
        mutableStateFlow.update {
            it.copy(newPasswordVisible = !it.newPasswordVisible)
        }
    }

    private fun toggleConfirmPasswordVisibility() {
        mutableStateFlow.update {
            it.copy(confirmPasswordVisible = !it.confirmPasswordVisible)
        }
    }

    private fun dismissDialog() {
        mutableStateFlow.update {
            it.copy(dialogState = null)
        }
    }

    private fun navigateBack() {
        clearSensitiveData()
        sendEvent(PasswordEvent.OnNavigateBack)
    }

    private fun resetFailedAttempts() {
        failedAttempts = 0
    }

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
    internal sealed interface DialogState {
        data object Loading : DialogState
        data class Success(val message: StringResource) : DialogState
        data class Error(val message: StringResource) : DialogState
    }
}

internal sealed interface PasswordEvent {
    data object OnNavigateBack : PasswordEvent
    data object OnNavigateToLogin : PasswordEvent
}

internal sealed interface PasswordAction {
    data class OnOldPasswordChange(val currentPassword: String) : PasswordAction
    data class OnNewPasswordChange(val newPassword: String) : PasswordAction
    data class OnConfirmPasswordChange(val confirmPassword: String) : PasswordAction

    data object OldPasswordVisibleClick : PasswordAction
    data object NewPasswordVisibleClick : PasswordAction
    data object ConfirmPasswordVisibleClick : PasswordAction

    data object SubmitClick : PasswordAction
    data object RetrySubmit : PasswordAction
    data object NavigateBack : PasswordAction
    data object DismissDialog : PasswordAction
    data object NavigateToLogin : PasswordAction

    sealed interface Internal : PasswordAction {
        data class UpdatePasswordResult(val result: DataState<Unit>) : Internal
        data class ReceivePasswordStrengthResult(val result: PasswordStrengthResult) : Internal
        data class OldPasswordReceived(val password: String?) : Internal
    }
}

sealed class ValidationResult {
    data object Success : ValidationResult()
    data class Error(val message: StringResource) : ValidationResult()
}