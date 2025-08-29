/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.auth.setNewPassword

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import mifos_mobile.feature.auth.generated.resources.Res
import mifos_mobile.feature.auth.generated.resources.feature_common_next
import mifos_mobile.feature.auth.generated.resources.feature_recover_now_recovered_successfully
import mifos_mobile.feature.auth.generated.resources.feature_recover_now_recovered_successfully_tip
import mifos_mobile.feature.auth.generated.resources.feature_signup_error_password_mismatch
import mifos_mobile.feature.auth.generated.resources.feature_signup_error_password_required_error
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.model.EventType
import org.mifos.mobile.core.ui.PasswordStrengthState
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.PasswordChecker
import org.mifos.mobile.core.ui.utils.PasswordStrength
import org.mifos.mobile.core.ui.utils.PasswordStrengthResult
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.feature.auth.login.LoginRoute

internal class SetPasswordViewModel : BaseViewModel<SetPasswordState, SetPasswordEvent, SetPasswordAction>(
    initialState = SetPasswordState(dialogState = null),
) {

    private var validationJob: Job? = null
    private var passwordStrengthJob: Job = Job()

    override fun handleAction(action: SetPasswordAction) {
        when (action) {
            is SetPasswordAction.OnConfirmPasswordChange -> handleConfirmPasswordChange(action.confirmPassword)

            is SetPasswordAction.OnLogIn -> sendEvent(SetPasswordEvent.NavigateToLogin)

            is SetPasswordAction.OnPasswordChange -> handlePasswordChange(action.password)

            is SetPasswordAction.OnSubmit -> handleSubmit()

            is SetPasswordAction.OnToggleConfirmPassword -> toggleConfirmPasswordVisibility()

            is SetPasswordAction.OnTogglePassword -> togglePasswordVisibility()

            is SetPasswordAction.Internal.ReceivePasswordStrengthResult -> handlePasswordStrengthResult(
                action,
            )

            SetPasswordAction.OnDismissDialog -> dismissDialog()
        }
    }

    private fun handlePasswordChange(password: String) {
        mutableStateFlow.update { it.copy(password = password, passwordError = null) }

        passwordStrengthJob.cancel()

        if (password.isEmpty()) {
            mutableStateFlow.update {
                it.copy(
                    passwordStrengthState = PasswordStrengthState.NONE,
                    passwordFeedback = emptyList(),
                )
            }
        } else {
            passwordStrengthJob = viewModelScope.launch {
                val result = PasswordChecker.getPasswordStrengthResult(password)
                val feedback = PasswordChecker.getPasswordFeedback(password)
                trySendAction(SetPasswordAction.Internal.ReceivePasswordStrengthResult(result))

                mutableStateFlow.update {
                    it.copy(passwordFeedback = feedback)
                }
            }
        }

        validationJob?.cancel()
        validationJob = viewModelScope.launch {
            delay(300)

            val newResult = validatePassword(password)
            val confirmResult = if (state.confirmPassword.isNotEmpty()) {
                validateConfirmPassword(state.confirmPassword, password)
            } else {
                null
            }

            mutableStateFlow.update {
                it.copy(
                    passwordError = newResult,
                    confirmPasswordError = confirmResult,
                )
            }
        }
    }

    @Suppress("ReturnCount")
    private fun validatePassword(password: String): StringResource? {
        if (password.isEmpty()) {
            return Res.string.feature_signup_error_password_required_error
        }

        return when (val result = PasswordChecker.getPasswordStrengthResult(password)) {
            is PasswordStrengthResult.Error -> {
                result.message
            }

            is PasswordStrengthResult.Success -> {
                null
            }
        }
    }

    private fun handleConfirmPasswordChange(confirmPassword: String) {
        mutableStateFlow.update {
            it.copy(
                confirmPassword = confirmPassword,
                confirmPasswordError = null,
            )
        }

        debounceValidation {
            val result = validateConfirmPassword(confirmPassword, state.password)
            mutableStateFlow.update {
                it.copy(
                    confirmPasswordError = result,
                )
            }
        }
    }

    private fun validateConfirmPassword(confirmPassword: String, password: String): StringResource? =
        when {
            confirmPassword.isEmpty() -> Res.string.feature_signup_error_password_required_error
            password != confirmPassword -> Res.string.feature_signup_error_password_mismatch
            else -> null
        }

    private fun handlePasswordStrengthResult(
        action: SetPasswordAction.Internal
            .ReceivePasswordStrengthResult,
    ) {
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
                        passwordError = result.message,
                        passwordStrengthState = PasswordStrengthState.NONE,
                    )
                }
            }
        }
    }

    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
    private fun handleSubmit() {
        viewModelScope.launch {
            mutableStateFlow.update { it.copy(showOverlay = true) }
            delay(3000)
            mutableStateFlow.update { it.copy(showOverlay = false) }
            sendEvent(
                SetPasswordEvent.NavigateToStatus(
                    eventType = EventType.SUCCESS.name,
                    eventDestination = LoginRoute::class.serializer().descriptor.serialName,
                    title = getString(Res.string.feature_recover_now_recovered_successfully),
                    subtitle = getString(Res.string.feature_recover_now_recovered_successfully_tip),
                    buttonText = getString(Res.string.feature_common_next),
                ),
            )
        }
    }

    private fun togglePasswordVisibility() {
        mutableStateFlow.update { it.copy(isPasswordVisible = !state.isPasswordVisible) }
    }

    private fun toggleConfirmPasswordVisibility() {
        mutableStateFlow.update { it.copy(isConfirmPasswordVisible = !state.isConfirmPasswordVisible) }
    }

    private fun debounceValidation(validation: suspend () -> Unit) {
        validationJob?.cancel()
        validationJob = viewModelScope.launch {
            delay(300)
            validation()
        }
    }

    private fun dismissDialog() {
        mutableStateFlow.update {
            it.copy(dialogState = null)
        }
    }
}

internal data class SetPasswordState(
    val password: String = "",
    val passwordError: StringResource? = null,

    val confirmPassword: String = "",
    val confirmPasswordError: StringResource? = null,

    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,

    val passwordFeedback: List<StringResource> = emptyList(),
    val passwordStrengthState: PasswordStrengthState = PasswordStrengthState.NONE,

    val dialogState: DialogState? = null,
    val uiState: ScreenUiState? = ScreenUiState.Success,
    val showOverlay: Boolean = false,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
    }

    val isSubmitButtonEnabled: Boolean
        get() = password.isNotBlank() && confirmPassword.isNotBlank()
}

internal sealed interface SetPasswordEvent {

    data object NavigateToLogin : SetPasswordEvent

    data class NavigateToStatus(
        val eventType: String,
        val eventDestination: String,
        val buttonText: String,
        val title: String,
        val subtitle: String,
    ) : SetPasswordEvent
}

internal sealed interface SetPasswordAction {
    data class OnPasswordChange(val password: String) : SetPasswordAction
    data class OnConfirmPasswordChange(val confirmPassword: String) : SetPasswordAction

    data object OnTogglePassword : SetPasswordAction
    data object OnToggleConfirmPassword : SetPasswordAction

    data object OnSubmit : SetPasswordAction
    data object OnLogIn : SetPasswordAction

    data object OnDismissDialog : SetPasswordAction

    sealed class Internal : SetPasswordAction {
        data class ReceivePasswordStrengthResult(
            val result: PasswordStrengthResult,
        ) : Internal()
    }
}
