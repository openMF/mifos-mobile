/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.auth.otpAuthentication

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import mifos_mobile.feature.auth.generated.resources.Res
import mifos_mobile.feature.auth.generated.resources.feature_common_next
import mifos_mobile.feature.auth.generated.resources.feature_otp_request_id_error
import mifos_mobile.feature.auth.generated.resources.feature_otp_required_error
import mifos_mobile.feature.auth.generated.resources.feature_signup_user_registered_failed
import mifos_mobile.feature.auth.generated.resources.feature_signup_user_registered_failed_tip
import mifos_mobile.feature.auth.generated.resources.feature_signup_user_registered_successfully
import mifos_mobile.feature.auth.generated.resources.feature_signup_user_registered_successfully_tip
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.UserAuthRepository
import org.mifos.mobile.core.model.EventType
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.feature.auth.login.LoginRoute

internal class OtpAuthenticationViewModel(
    private val userAuthRepositoryImpl: UserAuthRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<OtpAuthState, OtpAuthEvent, OtpAuthAction>(
    initialState = OtpAuthState(dialogState = null),
) {
    init {
        val nextRoute = savedStateHandle.toRoute<OtpAuthenticationRoute>()

        mutableStateFlow.update {
            it.copy(nextRoute = nextRoute.nextRoute ?: "")
        }
    }

    private var validationJob: Job? = null

    override fun handleAction(action: OtpAuthAction) {
        when (action) {
            is OtpAuthAction.OnCancelClick -> sendEvent(OtpAuthEvent.NavigateBack)

            is OtpAuthAction.OnNextClick -> handleNextClick()

            is OtpAuthAction.OnOtpChange -> handleOtpChange(action.otp)

            is OtpAuthAction.OnResendClick -> handleResendOtp()

            is OtpAuthAction.OnDismissDialog -> dismissDialog()

            is OtpAuthAction.OnRequestIdChange -> handleRequestIdChange(action.requestId)

            is OtpAuthAction.Internal.ReceiveOtpResult -> {
                viewModelScope.launch {
                    handleOtpResult(action.dataState)
                }
            }
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
            else -> null
        }
    }

    private fun handleRequestIdChange(requestId: String) {
        mutableStateFlow.update {
            it.copy(
                requestId = requestId,
                requestIdError = null,
            )
        }

        debounceValidation {
            val result = validateRequestId(requestId)
            mutableStateFlow.update {
                it.copy(
                    requestIdError = result,
                )
            }
        }
    }

    private fun validateRequestId(otp: String): StringResource? {
        return when {
            otp.isBlank() -> Res.string.feature_otp_request_id_error
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
            when {
                state.nextRoute == Constants.SET_PASSWORD -> handleRecoverPassword()
                else -> registerUser()
            }
        }
    }

    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
    private fun handleRecoverPassword() {
        viewModelScope.launch {
            mutableStateFlow.update {
                it.copy(
                    showOverlay = true,
                )
            }
            delay(3000)
            dismissDialog()
            sendEvent(OtpAuthEvent.NavigateNext)
        }
    }

    private fun registerUser() {
        // TODO change according to new ui later
//        viewModelScope.launch {
//            mutableStateFlow.update {
//                it.copy(
//                    dialogState = OtpAuthState.DialogState.Loading,
//                )
//            }
//            delay(3000)
//            dismissDialog()
//            sendEvent(
//                OtpAuthEvent.NavigateToStatus(
//                    eventType = EventType.SUCCESS.name,
//                    eventDestination = LoginRoute::class.serializer().descriptor.serialName,
//                    title = getString(Res.string.feature_signup_user_registered_successfully),
//                    subtitle = getString(Res.string.feature_signup_user_registered_successfully_tip),
//                    buttonText = getString(Res.string.feature_common_next),
//                ),
//            )
//        }
        mutableStateFlow.update { it.copy(showOverlay = true) }
        viewModelScope.launch {
            val result = userAuthRepositoryImpl.verifyUser(state.otp, state.requestId)
            sendAction(OtpAuthAction.Internal.ReceiveOtpResult(result))
        }
    }

    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
    private suspend fun handleOtpResult(action: DataState<String>) {
        when (action) {
            is DataState.Error -> {
                mutableStateFlow.update {
                    it.copy(
                        showOverlay = false,
                        dialogState = OtpAuthState.DialogState.Error(action.message),
                    )
                }
                delay(1500)
                sendEvent(
                    OtpAuthEvent.NavigateToStatus(
                        eventType = if (action.exception.cause is ServerResponseException) {
                            EventType.SERVER_EXCEPTION.name
                        } else {
                            EventType.FAILURE.name
                        },
                        eventDestination = LoginRoute::class.serializer().descriptor.serialName,
                        title = getString(Res.string.feature_signup_user_registered_failed),
                        subtitle = getString(Res.string.feature_signup_user_registered_failed_tip),
                        buttonText = getString(Res.string.feature_common_next),
                    ),
                )
            }
            DataState.Loading -> {
                mutableStateFlow.update {
                    it.copy(
                        showOverlay = true,
                    )
                }
            }
            is DataState.Success -> {
                sendEvent(
                    OtpAuthEvent.NavigateToStatus(
                        eventType = EventType.SUCCESS.name,
                        eventDestination = LoginRoute::class.serializer().descriptor.serialName,
                        title = getString(Res.string.feature_signup_user_registered_successfully),
                        subtitle = getString(Res.string.feature_signup_user_registered_successfully_tip),
                        buttonText = getString(Res.string.feature_common_next),
                    ),
                )
            }
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

    val nextRoute: String = "",

    val otp: String = "",
    val otpError: StringResource? = null,
    val requestId: String = "",
    val requestIdError: StringResource? = null,

    val dialogState: DialogState? = null,
    val uiState: ScreenUiState? = ScreenUiState.Success,
    val showOverlay: Boolean = false,
) {
    sealed interface DialogState {

        data class Error(val message: String) : DialogState
    }

    val isNextButtonEnabled
        get() = otp.isNotBlank()
}

internal sealed interface OtpAuthAction {

    data class OnOtpChange(val otp: String) : OtpAuthAction

    data class OnRequestIdChange(val requestId: String) : OtpAuthAction

    data object OnResendClick : OtpAuthAction

    data object OnCancelClick : OtpAuthAction

    data object OnNextClick : OtpAuthAction

    data object OnDismissDialog : OtpAuthAction

    sealed interface Internal : OtpAuthAction {
        data class ReceiveOtpResult(val dataState: DataState<String>) : Internal
    }
}

internal sealed interface OtpAuthEvent {
    data object NavigateNext : OtpAuthEvent

    data object NavigateBack : OtpAuthEvent

    data class NavigateToStatus(
        val eventType: String,
        val eventDestination: String,
        val title: String,
        val subtitle: String,
        val buttonText: String,
    ) : OtpAuthEvent
}
