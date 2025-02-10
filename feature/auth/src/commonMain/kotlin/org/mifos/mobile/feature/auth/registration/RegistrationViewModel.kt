/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.auth.registration

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.auth.generated.resources.Res
import mifos_mobile.feature.auth.generated.resources.could_not_register_user_error
import mifos_mobile.feature.auth.generated.resources.error_enter_account_number
import mifos_mobile.feature.auth.generated.resources.error_enter_country
import mifos_mobile.feature.auth.generated.resources.error_enter_email
import mifos_mobile.feature.auth.generated.resources.error_enter_first_name
import mifos_mobile.feature.auth.generated.resources.error_enter_last_name
import mifos_mobile.feature.auth.generated.resources.error_enter_mobile_number
import mifos_mobile.feature.auth.generated.resources.error_enter_user_name
import mifos_mobile.feature.auth.generated.resources.error_invalid_email
import mifos_mobile.feature.auth.generated.resources.error_invalid_password
import mifos_mobile.feature.auth.generated.resources.error_mobile_length
import mifos_mobile.feature.auth.generated.resources.error_password_not_match
import mifos_mobile.feature.auth.generated.resources.invalid_phn_number
import mifos_mobile.feature.auth.generated.resources.password_strength_weak
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.utils.isValidEmail
import org.mifos.mobile.core.data.repository.UserAuthRepository
import org.mifos.mobile.core.model.IgnoredOnParcel
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.ui.PasswordStrengthState
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.PasswordChecker
import org.mifos.mobile.core.ui.utils.PasswordStrength
import org.mifos.mobile.core.ui.utils.PasswordStrengthResult

private const val KEY_STATE = "signup_state"

class RegistrationViewModel(
    private val userAuthRepositoryImpl: UserAuthRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<SignUpState, SignUpEvent, SignUpAction>(
    initialState = savedStateHandle[KEY_STATE] ?: SignUpState(),
) {

    private var passwordStrengthJob: Job = Job().apply { complete() }

    private fun updateState(update: (SignUpState) -> SignUpState) {
        mutableStateFlow.update(update)
    }

    override fun handleAction(action: SignUpAction) {
        when (action) {
            is SignUpAction.AccountInputChange -> updateState { it.copy(accountNumber = action.accountNumber) }
            is SignUpAction.FirstNameInputChange -> updateState { it.copy(firstNameInput = action.firstName) }
            is SignUpAction.LastNameInputChange -> updateState { it.copy(lastNameInput = action.lastName) }
            is SignUpAction.EmailInputChange -> updateState { it.copy(emailInput = action.email) }
            is SignUpAction.MobileNumberInputChange -> updateState { it.copy(mobileNumberInput = action.mobileNumber) }
            is SignUpAction.PasswordInputChange -> handlePasswordInput(action.password)
            is SignUpAction.ConfirmPasswordInputChange -> updateState {
                it.copy(confirmPasswordInput = action.confirmPassword)
            }
            is SignUpAction.UserNameInputChange -> updateState { it.copy(userNameInput = action.username) }
            is SignUpAction.CountryInputChange -> updateState { it.copy(countryInput = action.country) }
            is SignUpAction.IsPasswordChanges -> updateState { it.copy(isPasswordChanged = true) }
            is SignUpAction.AuthenticationMode -> updateState {
                it.copy(
                    authenticationMode = action.authenticationMode,
                )
            }

            is SignUpAction.TogglePasswordVisibility -> updateState {
                it.copy(
                    isPasswordVisible = !it.isPasswordVisible,
                )
            }

            is SignUpAction.ConfirmTogglePasswordVisibility -> updateState {
                it.copy(
                    isConfirmPasswordVisible = !it.isPasswordVisible,
                )
            }

            is SignUpAction.Internal.ReceivePasswordStrengthResult -> handlePasswordStrengthResult(
                action,
            )

            is SignUpAction.Internal.ReceiveRegisterResult -> handleRegisterResult(action)
            is SignUpAction.SubmitClick -> handleSubmitClick()
            SignUpAction.ErrorDialogDismiss -> updateState { it.copy(dialogState = null) }
        }
    }

    private fun handlePasswordInput(password: String) {
        updateState { it.copy(passwordInput = password) }
        passwordStrengthJob.cancel()

        if (password.isEmpty()) {
            updateState { it.copy(passwordStrengthState = PasswordStrengthState.NONE) }
        } else {
            passwordStrengthJob = viewModelScope.launch {
                val result = PasswordChecker.getPasswordStrengthResult(password)
                trySendAction(SignUpAction.Internal.ReceivePasswordStrengthResult(result))
            }
        }
    }

    private fun handlePasswordStrengthResult(action: SignUpAction.Internal.ReceivePasswordStrengthResult) {
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

            is PasswordStrengthResult.Error -> {}
        }
    }

    private fun handleSubmitClick() {
        val errorMessage = validateForm()
        if (errorMessage != null) {
            updateState { it.copy(dialogState = SignUpDialog.Error(errorMessage)) }
        } else {
            registerUser()
        }
    }

    private fun handleRegisterResult(action: SignUpAction.Internal.ReceiveRegisterResult) {
        when (val result = action.registerResult) {
            is DataState.Success -> {
                updateState { it.copy(dialogState = null) }
                sendEvent(SignUpEvent.NavigateToLogin(result.data))
            }

            is DataState.Error -> {
                updateState {
                    it.copy(
                        dialogState = SignUpDialog.Error(
                            result.exception.message ?: "An error occurred.",
                        ),
                    )
                }
            }

            DataState.Loading -> updateState { it.copy(dialogState = SignUpDialog.Loading) }

            else -> {}
        }
    }

    // TODO:: move error messages to strings.xml
    private fun validateForm(): String? {
        return when {
            state.accountNumber.isEmpty() -> Res.string.error_enter_account_number.toString()
            state.firstNameInput.isEmpty() -> Res.string.error_enter_first_name.toString()
            state.lastNameInput.isEmpty() -> Res.string.error_enter_last_name.toString()
            state.userNameInput.isEmpty() -> Res.string.error_enter_user_name.toString()
            state.emailInput.isEmpty() -> Res.string.error_enter_email.toString()
            !state.emailInput.isValidEmail() -> Res.string.error_invalid_email.toString()
            state.mobileNumberInput.isEmpty() -> Res.string.error_enter_mobile_number.toString()
            state.mobileNumberInput.length < 10 -> Res.string.error_mobile_length.toString()
            isPhoneNumberValid(state.mobileNumberInput) -> Res.string.invalid_phn_number.toString()
            state.passwordInput.length < 8 -> Res.string.error_invalid_password.toString()
            !state.isPasswordMatch -> Res.string.error_password_not_match.toString()
            !state.isPasswordStrong -> Res.string.password_strength_weak.toString()
            state.countryInput.isEmpty() -> Res.string.error_enter_country.toString()
            else -> null
        }
    }

    private fun isPhoneNumberValid(fieldText: String?): Boolean {
        if (fieldText.isNullOrBlank()) {
            return false
        }

        val phoneNumberPattern = "^\\+?[0-9]{10,15}\$"
        val regex = phoneNumberPattern.toRegex()
        return regex.matches(fieldText.trim())
    }

    private fun registerUser() {
        viewModelScope.launch {
            updateState { it.copy(dialogState = SignUpDialog.Loading) }
            try {
                userAuthRepositoryImpl.registerUser(
                    accountNumber = state.accountNumber,
                    authenticationMode = state.authenticationMode,
                    email = state.emailInput,
                    firstName = state.firstNameInput,
                    lastName = state.lastNameInput,
                    mobileNumber = state.countryInput + state.mobileNumberInput,
                    password = state.passwordInput,
                    username = state.userNameInput,
                )
                sendEvent(SignUpEvent.ShowToast("Registration successful."))
                sendAction(
                    SignUpAction.Internal.ReceiveRegisterResult(
                        DataState.Success("Registration successful."),
                    ),
                )
            } catch (e: Exception) {
                updateState {
                    it.copy(
                        dialogState = SignUpDialog.Error(
                            (e.message ?: Res.string.could_not_register_user_error).toString(),
                        ),
                    )
                }
            }
        }
    }
}

@Parcelize
data class SignUpState(
    val accountNumber: String = "",
    val userNameInput: String = "",
    val firstNameInput: String = "",
    val lastNameInput: String = "",
    val emailInput: String = "",
    val passwordInput: String = "",
    val confirmPasswordInput: String = "",
    val mobileNumberInput: String = "",
    val countryInput: String = "",
    val dialogState: SignUpDialog? = null,
    val authenticationMode: String = "Email",
    val isPasswordChanged: Boolean = false,
    val passwordStrengthState: PasswordStrengthState = PasswordStrengthState.NONE,
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
) : Parcelable {
    @IgnoredOnParcel
    val isPasswordStrong: Boolean
        get() = when (passwordStrengthState) {
            PasswordStrengthState.NONE,
            PasswordStrengthState.WEAK_1,
            PasswordStrengthState.WEAK_2,
            PasswordStrengthState.WEAK_3,
            -> false

            PasswordStrengthState.GOOD,
            PasswordStrengthState.STRONG,
            PasswordStrengthState.VERY_STRONG,
            -> true
        }

    @IgnoredOnParcel
    val isPasswordMatch: Boolean
        get() = passwordInput == confirmPasswordInput
}

sealed interface SignUpDialog : Parcelable {
    @Parcelize
    data object Loading : SignUpDialog

    @Parcelize
    data class Error(val message: String) : SignUpDialog
}

sealed interface SignUpEvent {
    data class ShowToast(val message: String) : SignUpEvent
    data class NavigateToLogin(val username: String) : SignUpEvent
}

sealed interface SignUpAction {
    data class AccountInputChange(val accountNumber: String) : SignUpAction
    data class UserNameInputChange(val username: String) : SignUpAction
    data class FirstNameInputChange(val firstName: String) : SignUpAction
    data class LastNameInputChange(val lastName: String) : SignUpAction
    data class EmailInputChange(val email: String) : SignUpAction
    data class PasswordInputChange(val password: String) : SignUpAction
    data class ConfirmPasswordInputChange(val confirmPassword: String) : SignUpAction
    data class MobileNumberInputChange(val mobileNumber: String) : SignUpAction
    data class CountryInputChange(val country: String) : SignUpAction
    data class AuthenticationMode(val authenticationMode: String) : SignUpAction
    data class IsPasswordChanges(val isPasswordChanged: Boolean) : SignUpAction
    data object TogglePasswordVisibility : SignUpAction
    data object ConfirmTogglePasswordVisibility : SignUpAction
    data object SubmitClick : SignUpAction
    data object ErrorDialogDismiss : SignUpAction

    sealed class Internal : SignUpAction {
        data class ReceiveRegisterResult(
            val registerResult: DataState<String>,
        ) : Internal()

        data class ReceivePasswordStrengthResult(
            val result: PasswordStrengthResult,
        ) : Internal()
    }
}
