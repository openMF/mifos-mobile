/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.auth.otpAuthentication

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import mifos_mobile.feature.auth.generated.resources.Res
import mifos_mobile.feature.auth.generated.resources.feature_otp_invalid_error
import mifos_mobile.feature.auth.generated.resources.feature_otp_required_error
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.feature.auth.login.LoginRoute

internal class OtpAuthenticationViewModel : BaseViewModel<OtpAuthState, OtpAuthEvent, OtpAuthAction>(
    initialState = OtpAuthState(dialogState = null),
) {

    private var validationJob: Job? = null

    override fun handleAction(action: OtpAuthAction) {
        when (action) {
            is OtpAuthAction.OnCancelClick -> { }

            is OtpAuthAction.OnNextClick -> handleNextClick()

            is OtpAuthAction.OnOtpChange -> handleOtpChange(action.otp)

            is OtpAuthAction.OnResendClick -> handleResendOtp()

            is OtpAuthAction.OnDismissDialog -> dismissDialog()
        }
    }

    private fun handleOtpChange(otp: String) {
        mutableStateFlow.update {
            it.copy(
                otp = otp,
                otpError = null,
            )
        }

        debounceValidation {
            val result = validateOtp(otp)
            mutableStateFlow.update {
                it.copy(
                    otpError = result,
                )
            }
        }
    }

    private fun validateOtp(otp: String): StringResource? {
        return when {
            otp.isBlank() -> Res.string.feature_otp_required_error
            otp.length != 6 -> Res.string.feature_otp_invalid_error
            else -> null
        }
    }

    private fun handleNextClick() {
        val otpError = validateOtp(state.otp)
        mutableStateFlow.update {
            it.copy(
                otpError = otpError,
            )
        }

        if (otpError == null) {
            registerUser()
        }
    }

    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
    private fun registerUser() {
        viewModelScope.launch {
            mutableStateFlow.update {
                it.copy(
                    dialogState = OtpAuthState.DialogState.Loading,
                )
            }
            delay(3000)
            dismissDialog()
            sendEvent(
                OtpAuthEvent.NavigateToStatus(
                    EventType.SUCCESS,
                    LoginRoute::class.serializer().descriptor.serialName,
                ),
            )
        }
    }

    private fun handleResendOtp() {
        // TODO implement resend logic
    }

    private fun dismissDialog() {
        mutableStateFlow.update {
            it.copy(
                dialogState = null,
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

internal data class OtpAuthState(
    val otp: String = "",
    val otpError: StringResource? = null,

    val dialogState: DialogState?,
) {
    sealed interface DialogState {

        data class Error(val message: String) : DialogState

        data object Loading : DialogState
    }
}

internal sealed interface OtpAuthAction {

    data class OnOtpChange(val otp: String) : OtpAuthAction

    data object OnResendClick : OtpAuthAction

    data object OnCancelClick : OtpAuthAction

    data object OnNextClick : OtpAuthAction

    data object OnDismissDialog : OtpAuthAction
}

internal sealed interface OtpAuthEvent {
    data class NavigateToStatus(
        val eventType: EventType,
        val eventDestination: String,
    ) : OtpAuthEvent
}
