/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.auth.recoverPassword

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import mifos_mobile.feature.auth.generated.resources.Res
import mifos_mobile.feature.auth.generated.resources.feature_recover_now_email_format_error
import mifos_mobile.feature.auth.generated.resources.feature_recover_now_email_required
import mifos_mobile.feature.auth.generated.resources.feature_recover_now_invalid_phone_number_error
import mifos_mobile.feature.auth.generated.resources.feature_recover_now_phone_number_error
import mifos_mobile.feature.auth.generated.resources.feature_recover_now_phone_number_required
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.core.ui.utils.ValidationHelper
import org.mifos.mobile.feature.auth.setNewPassword.SetPasswordRoute

const val PHONE_NUMBER_LENGTH = 10

internal class RecoverPasswordViewModel :
    BaseViewModel<RecoverPasswordState, RecoverPasswordEvent, RecoverPasswordAction>(
        initialState = RecoverPasswordState(),
    ) {

    private var validationJob: Job? = null

    override fun handleAction(action: RecoverPasswordAction) {
        when (action) {
            RecoverPasswordAction.OnDismissDialog -> dismissDialog()

            is RecoverPasswordAction.OnEmailChange -> handleEmailChange(action.email)

            is RecoverPasswordAction.OnPhoneNumberChange -> handlePhoneNumberChange(action.phone)

            is RecoverPasswordAction.OnLogin -> sendEvent(RecoverPasswordEvent.NavigateToLogin)

            is RecoverPasswordAction.OnRecoverClicked -> verifyEmailAndPassword()
        }
    }

    private fun dismissDialog() {
        mutableStateFlow.update {
            it.copy(dialogState = null)
        }
    }

    private fun handleEmailChange(email: String) {
        mutableStateFlow.update {
            it.copy(
                email = email,
                emailError = null,
            )
        }

        debounceValidation {
            val result = getEmailValidationError(email)
            mutableStateFlow.update {
                it.copy(
                    emailError = result,
                )
            }
        }
    }

    private fun handlePhoneNumberChange(phone: String) {
        mutableStateFlow.update {
            it.copy(
                phoneNumber = phone,
                phoneNumberError = null,
            )
        }

        debounceValidation {
            val result = getPhoneValidationError(phone)
            mutableStateFlow.update {
                it.copy(
                    phoneNumberError = result,
                )
            }
        }
    }

    private fun verifyEmailAndPassword() {
        val emailError = getEmailValidationError(state.email)
        val phoneError = getPhoneValidationError(state.phoneNumber)

        mutableStateFlow.update {
            it.copy(
                emailError = emailError,
                phoneNumberError = phoneError,
            )
        }

        if (emailError == null && phoneError == null) {
            requestRecoveryCode()
        }
    }

    private fun getEmailValidationError(email: String): StringResource? {
        return when {
            email.isEmpty() -> Res.string.feature_recover_now_email_required
            !ValidationHelper.isValidEmail(email) -> Res.string.feature_recover_now_email_format_error
            else -> null
        }
    }

    private fun getPhoneValidationError(phone: String): StringResource? {
        return when {
            phone.isEmpty() -> Res.string.feature_recover_now_phone_number_required

            phone.any { it.isLetter() || !it.isDigit() } ->
                Res.string.feature_recover_now_invalid_phone_number_error

            phone.length <= PHONE_NUMBER_LENGTH ->
                Res.string.feature_recover_now_phone_number_error

            !ValidationHelper.isValidPhoneNumber(phone) ->
                Res.string
                    .feature_recover_now_phone_number_error

            else -> null
        }
    }

    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
    private fun requestRecoveryCode() {
        viewModelScope.launch {
            mutableStateFlow.update { it.copy(showOverlay = true) }
            delay(3000)
            mutableStateFlow.update { it.copy(showOverlay = false) }
            sendEvent(
                RecoverPasswordEvent.NavigateToOtpAuth(
                    nextRoute = SetPasswordRoute::class.serializer().descriptor.serialName,
                ),
            )
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

data class RecoverPasswordState(
    var phoneNumber: String = "",
    var email: String = "",

    var phoneNumberError: StringResource? = null,
    var emailError: StringResource? = null,

    val dialogState: DialogState? = null,
    val uiState: ScreenUiState? = ScreenUiState.Success,
    val showOverlay: Boolean = false,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
    }

    val isRecoverButtonEnabled: Boolean
        get() = phoneNumber.isNotBlank() && email.isNotBlank()
}

internal sealed interface RecoverPasswordEvent {
    data class NavigateToOtpAuth(
        val nextRoute: String,
    ) : RecoverPasswordEvent
    data object NavigateToLogin : RecoverPasswordEvent
}

sealed interface RecoverPasswordAction {
    data class OnPhoneNumberChange(val phone: String) : RecoverPasswordAction
    data class OnEmailChange(val email: String) : RecoverPasswordAction
    data object OnLogin : RecoverPasswordAction
    data object OnRecoverClicked : RecoverPasswordAction
    data object OnDismissDialog : RecoverPasswordAction
}
