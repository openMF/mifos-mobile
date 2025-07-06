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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.auth.generated.resources.Res
import mifos_mobile.feature.auth.generated.resources.feature_signup_error_customer_account_empty
import mifos_mobile.feature.auth.generated.resources.feature_signup_error_customer_account_not_valid
import mifos_mobile.feature.auth.generated.resources.feature_signup_error_first_name_empty
import mifos_mobile.feature.auth.generated.resources.feature_signup_error_invalid_email
import mifos_mobile.feature.auth.generated.resources.feature_signup_error_invalid_name
import mifos_mobile.feature.auth.generated.resources.feature_signup_error_last_name_empty
import mifos_mobile.feature.auth.generated.resources.feature_signup_error_middle_name_empty
import mifos_mobile.feature.auth.generated.resources.feature_signup_error_password_mismatch
import mifos_mobile.feature.auth.generated.resources.feature_signup_error_password_required_error
import mifos_mobile.feature.auth.generated.resources.feature_signup_error_password_short
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.model.IgnoredOnParcel
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.ui.PasswordStrengthState
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.PasswordChecker
import org.mifos.mobile.core.ui.utils.PasswordStrength
import org.mifos.mobile.core.ui.utils.PasswordStrengthResult
import org.mifos.mobile.core.ui.utils.ValidationHelper

private const val KEY_STATE = "signup_state"

@Suppress("TooManyFunctions")
class RegistrationViewModel(
//    private val userAuthRepositoryImpl: UserAuthRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<SignUpState, SignUpEvent, SignUpAction>(
    initialState = savedStateHandle[KEY_STATE] ?: SignUpState(),
) {

    private var validationJob: Job? = null
    private var passwordStrengthJob: Job = Job()

    private fun updateState(update: (SignUpState) -> SignUpState) {
        mutableStateFlow.update(update)
    }

    override fun handleAction(action: SignUpAction) {
        when (action) {
            is SignUpAction.OnCustomerAccountChange -> {
                handleCustomerAccountChange(action.customerAccount)
            }

            is SignUpAction.OnFirstNameChange -> {
                handleFirstNameChange(action.firstName)
            }

            is SignUpAction.OnLastNameChange -> {
                handleLastNameChange(action.lastName)
            }

            is SignUpAction.OnEmailChange -> {
                handleEmailChange(action.email)
            }

//            is SignUpAction.OnMobileNumberChange -> {
//                handleMobileNumberChange(action.mobileNumber)
//            }

            is SignUpAction.OnPasswordChange -> {
                handlePasswordChange(action.password)
            }

            is SignUpAction.OnConfirmPasswordChange -> {
                handleConfirmPasswordChange(action.confirmPassword)
            }

            is SignUpAction.OnMiddleNameChange -> {
                handleMiddleNameChange(action.middleName)
            }

            is SignUpAction.IsPasswordChanges -> updateState { it.copy(isPasswordChanged = true) }

            is SignUpAction.TogglePasswordVisibility -> togglePasswordVisibility()

            is SignUpAction.ConfirmTogglePasswordVisibility -> toggleConfirmPasswordVisibility()

            is SignUpAction.Internal.ReceivePasswordStrengthResult -> handlePasswordStrengthResult(
                action,
            )

            is SignUpAction.Internal.ReceiveRegisterResult -> handleRegisterResult(action)

            is SignUpAction.SubmitClick -> handleSubmit()

            is SignUpAction.OnNavigateToLogin -> sendEvent(SignUpEvent.NavigateToLogin)

            SignUpAction.ErrorDialogDismiss -> updateState { it.copy(dialogState = null) }
        }
    }

//  First Name

    private fun handleFirstNameChange(name: String) {
        mutableStateFlow.update {
            it.copy(
                firstName = name,
                firstNameError = null,
            )
        }

        debounceValidation {
            val result = validateName(name, "first")
            mutableStateFlow.update {
                it.copy(
                    firstNameError = if (result is ValidationResult.Error) result.message else null,
                )
            }
        }
    }

//    Middle Name

    private fun handleMiddleNameChange(name: String) {
        mutableStateFlow.update {
            it.copy(
                middleName = name,
                middleNameError = null,
            )
        }

        debounceValidation {
            val result = validateName(name, "middle")
            mutableStateFlow.update {
                it.copy(
                    middleNameError = if (result is ValidationResult.Error) result.message else null,
                )
            }
        }
    }

//  Last Name

    private fun handleLastNameChange(name: String) {
        mutableStateFlow.update {
            it.copy(
                lastName = name,
                lastNameError = null,
            )
        }

        debounceValidation {
            val result = validateName(name, "last")
            mutableStateFlow.update {
                it.copy(
                    lastNameError = if (result is ValidationResult.Error) result.message else null,
                )
            }
        }
    }

    @Suppress("ReturnCount")
    private fun validateName(name: String, nameType: String): ValidationResult? {
        if (name.isEmpty()) {
            return when (nameType) {
                "first" -> ValidationResult.Error(Res.string.feature_signup_error_first_name_empty)
                "middle" -> ValidationResult.Error(Res.string.feature_signup_error_middle_name_empty)
                "last" -> ValidationResult.Error(Res.string.feature_signup_error_last_name_empty)
                else -> ValidationResult.Error(Res.string.feature_signup_error_invalid_name)
            }
        }

        if (!ValidationHelper.isValidName(name)) {
            return ValidationResult.Error(Res.string.feature_signup_error_invalid_name)
        }

        return ValidationResult.Success
    }

//  Email

    private fun handleEmailChange(email: String) {
        mutableStateFlow.update {
            it.copy(
                email = email,
                emailError = null,
            )
        }

        debounceValidation {
            val result = validateEmail(email)
            mutableStateFlow.update {
                it.copy(
                    emailError = if (result is ValidationResult.Error) result.message else null,
                )
            }
        }
    }

    private fun validateEmail(email: String): ValidationResult? {
        return if (!ValidationHelper.isValidEmail(email)) {
            ValidationResult.Error(Res.string.feature_signup_error_invalid_email)
        } else {
            ValidationResult.Success
        }
    }

//  Mobile Number

//    private fun handleMobileNumberChange(mobileNumber: String) {
//        mutableStateFlow.update {
//            it.copy(
//                mobileNumber = mobileNumber,
//                mobileNumberError = null,
//            )
//        }
//
//        debounceValidation {
//            val result = validateMobileNumber(mobileNumber)
//            mutableStateFlow.update {
//                it.copy(
//                    mobileNumberError = if (result is ValidationResult.Error) result.message else null,
//                )
//            }
//        }
//    }

//    private fun validateMobileNumber(mobileNumber: String): ValidationResult? {
//        return if (!ValidationHelper.isValidPhoneNumber(mobileNumber)) {
//            ValidationResult.Error(Res.string.feature_signup_error_invalid_email)
//        } else {
//            ValidationResult.Success
//        }
//    }

//  Customer Account

    private fun handleCustomerAccountChange(account: String) {
        mutableStateFlow.update {
            it.copy(
                customerAccount = account,
                customerAccountError = null,
            )
        }
        debounceValidation {
            val result = validateCustomerAccount(account)
            mutableStateFlow.update {
                it.copy(
                    customerAccountError = if (result is ValidationResult.Error) result.message else null,
                )
            }
        }
    }

    private fun validateCustomerAccount(account: String): ValidationResult? = when {
        account.isBlank() -> ValidationResult.Error(
            Res.string.feature_signup_error_customer_account_empty,
        )
        account.length != 10 || !account.all { it.isDigit() } -> ValidationResult.Error(
            Res.string.feature_signup_error_customer_account_not_valid,
        )

        else -> ValidationResult.Success
    }

//  Password

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
                trySendAction(SignUpAction.Internal.ReceivePasswordStrengthResult(result))

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
                ValidationResult.Success
            }

            mutableStateFlow.update {
                it.copy(
                    passwordError = if (newResult is ValidationResult.Error) newResult.message else null,
                    confirmPasswordError = if (confirmResult is ValidationResult.Error) confirmResult.message else null,
                )
            }
        }
    }

    @Suppress("ReturnCount")
    private fun validatePassword(password: String): ValidationResult? {
        if (password.isEmpty()) {
            return ValidationResult.Error(
                Res.string.feature_signup_error_password_required_error,
            )
        }

        return when (val result = PasswordChecker.getPasswordStrengthResult(password)) {
            is PasswordStrengthResult.Error -> {
                ValidationResult.Error(result.message)
            }

            is PasswordStrengthResult.Success -> {
                ValidationResult.Success
            }
        }
    }

//  Confirm Password

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
                    confirmPasswordError = if (result is ValidationResult.Error) {
                        result.message
                    } else {
                        null
                    },
                )
            }
        }
    }

    private fun validateConfirmPassword(confirmPassword: String, password: String): ValidationResult? = when {
        confirmPassword.isEmpty() -> ValidationResult.Error(Res.string.feature_signup_error_password_required_error)
        confirmPassword.length < 8 -> ValidationResult.Error(Res.string.feature_signup_error_password_short)
        password != confirmPassword -> ValidationResult.Error(Res.string.feature_signup_error_password_mismatch)
        else -> ValidationResult.Success
    }

//  Password visible toggle

    private fun togglePasswordVisibility() {
        mutableStateFlow.update { it.copy(isPasswordVisible = !state.isPasswordVisible) }
    }

    private fun toggleConfirmPasswordVisibility() {
        mutableStateFlow.update { it.copy(isConfirmPasswordVisible = !state.isConfirmPasswordVisible) }
    }

//  Password strength Result

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

    private fun isSuccess(result: ValidationResult?) = result is ValidationResult.Success

    private fun handleSubmit() {
        validationJob?.cancel()

        val firstNameError = validateName(state.firstName, "first")
        val middleNameError = validateName(state.middleName, "middle")
        val lastNameError = validateName(state.lastName, "last")
        val emailError = validateEmail(state.email)
//        val mobileNumberError = validateMobileNumber(state.mobileNumber)
        val accountError = validateCustomerAccount(state.customerAccount)
        val passwordResult = validatePassword(state.password)
        val confirmPasswordResult = validateConfirmPassword(
            state.confirmPassword,
            state.password,
        )

        mutableStateFlow.update {
            it.copy(
                firstNameError = if (firstNameError is ValidationResult.Error) firstNameError.message else null,
                middleNameError = if (middleNameError is ValidationResult.Error) {
                    middleNameError.message
                } else {
                    null
                },
                lastNameError = if (lastNameError is ValidationResult.Error) lastNameError.message else null,
                emailError = if (emailError is ValidationResult.Error) emailError.message else null,
                customerAccountError = if (accountError is ValidationResult.Error) {
                    accountError
                        .message
                } else {
                    null
                },
                passwordError = if (passwordResult is ValidationResult.Error) passwordResult.message else null,
                confirmPasswordError = if (confirmPasswordResult is ValidationResult.Error) {
                    confirmPasswordResult.message
                } else {
                    null
                },
            )
        }

        val errorFree = isSuccess(firstNameError) &&
            isSuccess(middleNameError) &&
            isSuccess(lastNameError) &&
            isSuccess(emailError) &&
            isSuccess(accountError) &&
            isSuccess(passwordResult) &&
            isSuccess(confirmPasswordResult)

        if (errorFree) {
            registerUser()
        }
    }

    private fun registerUser() {
        viewModelScope.launch {
            updateState { it.copy(dialogState = SignUpState.SignUpDialog.Loading) }

            delay(3000)

            sendEvent(SignUpEvent.NavigateToUploadDocuments)
        }
//              TODO make api call
//            viewModelScope.launch {
//                val response = userAuthRepositoryImpl.registerUser(
//                    accountNumber = state.customerAccount,
//                    authenticationMode = "email",
//                    email = state.email,
//                    firstName = state.firstName,
//                    lastName = state.lastName,
//                    mobileNumber = state.mobileNumber,
//                    password = state.password,
//                    username = state.firstName,
//                )
//                sendAction(
//                    SignUpAction.Internal.ReceiveRegisterResult(
//                        response,
//                    ),
//                )
//            }
    }

    // TODO: Use this in the above [registerUser] function when API is available
    private fun handleRegisterResult(action: SignUpAction.Internal.ReceiveRegisterResult) {
        when (val result = action.registerResult) {
            is DataState.Success -> {
                updateState { it.copy(dialogState = null) }
            }

            is DataState.Error -> {
                updateState {
                    it.copy(
                        dialogState = null,
                    )
                }
                updateState {
                    it.copy(
                        dialogState = SignUpState.SignUpDialog.Error(result.message),
                    )
                }
            }

            DataState.Loading -> updateState { it.copy(dialogState = SignUpState.SignUpDialog.Loading) }
        }
    }

    private fun debounceValidation(validation: suspend () -> Unit) {
        validationJob?.cancel()
        validationJob = viewModelScope.launch {
            delay(300)
            validation()
        }
    }
}

@Parcelize
data class SignUpState(
    val customerAccount: String = "",
    val firstName: String = "",
    val middleName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val mobileNumber: String = "",

    val dialogState: SignUpDialog? = null,

    val isPasswordChanged: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    @IgnoredOnParcel
    val passwordFeedback: List<StringResource> = emptyList(),
    @IgnoredOnParcel
    val passwordStrengthState: PasswordStrengthState = PasswordStrengthState.NONE,

    @IgnoredOnParcel
    val firstNameError: StringResource? = null,
    @IgnoredOnParcel
    val middleNameError: StringResource? = null,
    @IgnoredOnParcel
    val lastNameError: StringResource? = null,
    @IgnoredOnParcel
    val emailError: StringResource? = null,
    @IgnoredOnParcel
    val mobileNumberError: StringResource? = null,
    @IgnoredOnParcel
    val customerAccountError: StringResource? = null,
    @IgnoredOnParcel
    val passwordError: StringResource? = null,
    @IgnoredOnParcel
    val confirmPasswordError: StringResource? = null,

) : Parcelable {
    sealed interface SignUpDialog : Parcelable {
        @Parcelize
        data object Loading : SignUpDialog

        @Parcelize
        data class Error(val message: String) : SignUpDialog
    }
    val isSubmitButtonEnabled: Boolean
        get() = customerAccount.isNotBlank() &&
            firstName.isNotBlank() &&
            middleName.isNotBlank() &&
            lastName.isNotBlank() &&
            email.isNotBlank() &&
            password.isNotBlank() &&
            confirmPassword.isNotBlank()
}

sealed interface SignUpEvent {
    data class ShowToast(val message: String) : SignUpEvent
    data object NavigateToUploadDocuments : SignUpEvent
    data object NavigateToLogin : SignUpEvent
}

internal sealed class ValidationResult {
    data object Success : ValidationResult()
    data class Error(val message: StringResource) : ValidationResult()
}

sealed interface SignUpAction {
    data class OnCustomerAccountChange(val customerAccount: String) : SignUpAction
    data class OnFirstNameChange(val firstName: String) : SignUpAction
    data class OnMiddleNameChange(val middleName: String) : SignUpAction
    data class OnLastNameChange(val lastName: String) : SignUpAction
    data class OnEmailChange(val email: String) : SignUpAction
    data class OnPasswordChange(val password: String) : SignUpAction
    data class OnConfirmPasswordChange(val confirmPassword: String) : SignUpAction

//    data class OnMobileNumberChange(val mobileNumber: String) : SignUpAction
    data class IsPasswordChanges(val isPasswordChanged: Boolean) : SignUpAction
    data object TogglePasswordVisibility : SignUpAction
    data object ConfirmTogglePasswordVisibility : SignUpAction
    data object SubmitClick : SignUpAction
    data object OnNavigateToLogin : SignUpAction
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
