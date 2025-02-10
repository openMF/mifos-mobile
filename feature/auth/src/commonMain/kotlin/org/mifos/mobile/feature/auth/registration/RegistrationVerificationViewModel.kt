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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.auth.generated.resources.Res
import mifos_mobile.feature.auth.generated.resources.could_not_register_user_error
import mifos_mobile.feature.auth.generated.resources.verified
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.UserAuthRepository
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.ui.utils.BaseViewModel

private const val KEY_STATE = "verification_state"

class RegistrationVerificationViewModel(
    private val userAuthRepositoryImpl: UserAuthRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<VerificationState, VerificationEvent, VerificationAction>(
    initialState = savedStateHandle[KEY_STATE] ?: VerificationState(),
) {

    private fun updateState(update: (VerificationState) -> VerificationState) {
        mutableStateFlow.update(update)
    }

    override fun handleAction(action: VerificationAction) {
        when (action) {
            is VerificationAction.AuthenticationTokenChange -> updateState { it.copy(authenticationToken = action.authenticationToken) }
            is VerificationAction.RequestIdChange -> updateState { it.copy(requestId = action.requestId) }
            is VerificationAction.RequestIdError -> updateState { it.copy(requestIdError = false) }
            is VerificationAction.ConfirmationDialog -> {
                updateState { it ->
                    it.copy(
                        confirmationDialog = action.confirmationDialog,
                        dialogState = if (action.confirmationDialog) {
                            VerificationState.VerificationDialog.ConfirmationDialog(
                                title = "Cancel Registration?",
                                message = "Are you sure you want to cancel registration?",
                                confirmText = "Yes",
                                cancelText = "No",
                                onConfirm = {
                                    updateState { it.copy(dialogState = null, confirmationDialog = false) }
                                    sendEvent(VerificationEvent.NavigateToRegistration)
                                },
                            )
                        } else {
                            null
                        },
                    )
                }
            }
            is VerificationAction.Internal.ReceiveRegisterResult -> handleVerificationResult(action)
            is VerificationAction.SubmitClick -> handleSubmitClick()
            is VerificationAction.NavigateToRegistration -> sendEvent(VerificationEvent.NavigateToRegistration)
            VerificationAction.ErrorDialogDismiss -> updateState { it.copy(dialogState = null) }
        }
    }

    private fun handleSubmitClick() {
        val errorMessage = validateForm()
        if (errorMessage != null) {
            sendEvent(VerificationEvent.ShowToast(errorMessage))
        } else {
            verifyUser()
        }
    }

    private fun handleVerificationResult(action: VerificationAction.Internal.ReceiveRegisterResult) {
        when (val result = action.registerResult) {
            is DataState.Success -> {
                updateState { it.copy(dialogState = null) }
                sendEvent(VerificationEvent.NavigateToLogin(result.data))
            }

            is DataState.Error -> {
                updateState {
                    it.copy(dialogState = VerificationState.VerificationDialog.Error(result.exception.message ?: "An error occurred."))
                }
            }

            DataState.Loading -> updateState { it.copy(dialogState = VerificationState.VerificationDialog.Loading) }

            else -> {}
        }
    }

    // TODO:: move error messages to strings.xml
    private fun validateForm(): String? {
        return when {
            state.authenticationToken.isEmpty() -> "Authentication Token cannot be empty"
            state.requestId.isEmpty() -> "Request ID cannot be empty"
            else -> null
        }
    }

    private fun verifyUser() {
        viewModelScope.launch {
            try {
                userAuthRepositoryImpl.verifyUser(
                    authenticationToken =
                    state.authenticationToken,
                    requestId = state.requestId,
                )

                sendEvent(VerificationEvent.ShowToast(Res.string.verified.toString()))
                sendAction(
                    VerificationAction.Internal.ReceiveRegisterResult(
                        DataState.Success(Res.string.verified.toString()),
                    ),
                )
            } catch (e: Exception) {
                updateState { it.copy(dialogState = VerificationState.VerificationDialog.Error((e.message ?: Res.string.could_not_register_user_error).toString())) }
            }
        }
    }
}

@Parcelize
data class VerificationState(
    val authenticationToken: String = "",
    val requestId: String = "",
    val requestIdError: Boolean = false,
    val confirmationDialog: Boolean = false,
    val dialogState: VerificationDialog? = null,
) : Parcelable {
    sealed class VerificationDialog : Parcelable {
        @Parcelize
        data object Loading : VerificationDialog()

        @Parcelize
        data class Error(val message: String) : VerificationDialog()

        @Parcelize
        data class ConfirmationDialog(
            val title: String,
            val message: String,
            val confirmText: String,
            val cancelText: String,
            val onConfirm: () -> Unit,
        ) : VerificationDialog()
    }
}

sealed class VerificationEvent {
    data class ShowToast(val message: String) : VerificationEvent()
    data class NavigateToLogin(val username: String) : VerificationEvent()
    data object NavigateToRegistration : VerificationEvent()
}

sealed class VerificationAction {
    data class RequestIdChange(val requestId: String) : VerificationAction()
    data class AuthenticationTokenChange(val authenticationToken: String) : VerificationAction()
    data class ConfirmationDialog(val confirmationDialog: Boolean) : VerificationAction()
    data object RequestIdError : VerificationAction()
    data object SubmitClick : VerificationAction()
    data object ErrorDialogDismiss : VerificationAction()
    data object NavigateToRegistration : VerificationAction()
    sealed class Internal : VerificationAction() {
        data class ReceiveRegisterResult(
            val registerResult: DataState<String>,
        ) : Internal()
    }
}
