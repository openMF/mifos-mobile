/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.update.password

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.UserAuthRepository
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.ui.PasswordStrengthState
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.PasswordChecker
import org.mifos.mobile.core.ui.utils.PasswordStrength
import org.mifos.mobile.core.ui.utils.PasswordStrengthResult

private const val KEY_STATE = "state"
private const val MIN_PASSWORD_LENGTH = 6

internal class UpdatePasswordViewModel(
    private val repository: UserAuthRepository,
    savedStateHandle: SavedStateHandle,
//    private val clientRepositoryImp: ClientRepository,
) : BaseViewModel<EditPasswordState, EditPasswordEvent, EditPasswordAction>(
    initialState = savedStateHandle[KEY_STATE] ?: EditPasswordState(),
) {
    private var passwordStrengthJob: Job = Job().apply { complete() }

    init {
        stateFlow
            .onEach { savedStateHandle[KEY_STATE] = it }
            .launchIn(viewModelScope)
    }

    override fun handleAction(action: EditPasswordAction) {
        when (action) {
            EditPasswordAction.SubmitClick -> handleSubmitClick()

            is EditPasswordAction.NavigateBackClick -> {
                sendEvent(EditPasswordEvent.NavigateBack)
            }

            is EditPasswordAction.Internal.ReceivePasswordStrengthResult -> {
                handlePasswordStrengthResult(action)
            }

            is EditPasswordAction.Internal.ReceiveUpdatePasswordResult -> {
                handleResult(action)
            }

            is EditPasswordAction.NewPasswordChange -> {
                handleNewPasswordInput(action)
            }

            is EditPasswordAction.ConfirmPasswordChange -> {
                mutableStateFlow.update {
                    it.copy(confirmPasswordInput = action.confirmPassword)
                }
            }

            EditPasswordAction.ErrorDialogDismiss -> {
                mutableStateFlow.update {
                    it.copy(dialogState = null)
                }
            }
        }
    }

    private fun handleNewPasswordInput(action: EditPasswordAction.NewPasswordChange) {
        // Update input:
        mutableStateFlow.update { it.copy(newPasswordInput = action.newPassword) }
        // Update password strength:
        passwordStrengthJob.cancel()
        if (action.newPassword.isEmpty()) {
            mutableStateFlow.update {
                it.copy(passwordStrengthState = PasswordStrengthState.NONE)
            }
        } else {
            passwordStrengthJob = viewModelScope.launch {
                val result = PasswordChecker.getPasswordStrengthResult(action.newPassword)
                trySendAction(EditPasswordAction.Internal.ReceivePasswordStrengthResult(result))
            }
        }
    }

    private fun handleResult(action: EditPasswordAction.Internal.ReceiveUpdatePasswordResult) {
        when (val result = action.result) {
            is DataState.Success -> {
                mutableStateFlow.update { it.copy(dialogState = null) }
                sendEvent(EditPasswordEvent.ShowToast(result.data))
                sendEvent(EditPasswordEvent.NavigateBack)
            }

            is DataState.Error -> {
                mutableStateFlow.update {
                    it.copy(dialogState = EditPasswordDialog.Error(""))
                }
            }

            DataState.Loading -> {
                mutableStateFlow.update { it.copy(dialogState = EditPasswordDialog.Loading) }
            }
        }
    }

    private fun handleSubmitClick() = when {
        state.newPasswordInput.length < MIN_PASSWORD_LENGTH -> {
            mutableStateFlow.update {
                it.copy(
                    dialogState = EditPasswordDialog.Error(
                        "Password must be at least $MIN_PASSWORD_LENGTH characters long.",
                    ),
                )
            }
        }

        !state.isPasswordMatch -> {
            mutableStateFlow.update {
                it.copy(dialogState = EditPasswordDialog.Error("Passwords do not match."))
            }
        }

        !state.isPasswordStrong -> {
            mutableStateFlow.update {
                it.copy(dialogState = EditPasswordDialog.Error("Password is weak."))
            }
        }

        else -> initiateUpdatePassword()
    }

    private fun handlePasswordStrengthResult(action: EditPasswordAction.Internal.ReceivePasswordStrengthResult) {
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

    private fun initiateUpdatePassword() {
        mutableStateFlow.update {
            it.copy(dialogState = EditPasswordDialog.Loading)
        }

        updatePassword(state.newPasswordInput, state.confirmPasswordInput)
    }

    private fun updatePassword(newPassword: String, confirmPassword: String) {
        viewModelScope.launch {
            val result = repository.updateAccountPassword(newPassword, confirmPassword)

            sendAction(EditPasswordAction.Internal.ReceiveUpdatePasswordResult(result))
        }
    }
}

@Parcelize
internal data class EditPasswordState(
    val newPasswordInput: String = "",
    val confirmPasswordInput: String = "",
    val dialogState: EditPasswordDialog? = null,
    val passwordStrengthState: PasswordStrengthState = PasswordStrengthState.NONE,
) : Parcelable {
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

    val isPasswordMatch: Boolean
        get() = newPasswordInput == confirmPasswordInput
}

internal sealed interface EditPasswordDialog : Parcelable {
    @Parcelize
    data object Loading : EditPasswordDialog

    @Parcelize
    data class Error(val message: String) : EditPasswordDialog
}

internal sealed interface EditPasswordEvent {
    data object NavigateBack : EditPasswordEvent
    data class ShowToast(val message: String) : EditPasswordEvent
}

internal sealed interface EditPasswordAction {
    data class NewPasswordChange(val newPassword: String) : EditPasswordAction
    data class ConfirmPasswordChange(val confirmPassword: String) : EditPasswordAction

    data object SubmitClick : EditPasswordAction
    data object NavigateBackClick : EditPasswordAction
    data object ErrorDialogDismiss : EditPasswordAction

    sealed class Internal : EditPasswordAction {
        data class ReceiveUpdatePasswordResult(
            val result: DataState<String>,
        ) : Internal()

        data class ReceivePasswordStrengthResult(
            val result: PasswordStrengthResult,
        ) : Internal()
    }
}
