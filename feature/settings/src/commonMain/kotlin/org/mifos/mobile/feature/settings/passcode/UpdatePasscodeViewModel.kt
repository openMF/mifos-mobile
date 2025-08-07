/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.passcode

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.settings.generated.resources.Res
import mifos_mobile.feature.settings.generated.resources.feature_settings_passcode_confirm_error
import mifos_mobile.feature.settings.generated.resources.feature_settings_passcode_empty_error
import mifos_mobile.feature.settings.generated.resources.feature_settings_passcode_invalid_error
import mifos_mobile.feature.settings.generated.resources.feature_settings_passcode_length_error
import mifos_mobile.feature.settings.generated.resources.feature_settings_passcode_old_passcode_error
import mifos_mobile.feature.settings.generated.resources.feature_settings_passcode_updated_successfully
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.ui.utils.BaseViewModel

internal class UpdatePasscodeViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
) :
    BaseViewModel<PasscodeState, PasscodeEvent, PasscodeAction>(
        initialState = PasscodeState(),
    ) {
    init {
        viewModelScope.launch {
            userPreferencesRepository.passcode.collect {
                trySendAction(PasscodeAction.Internal.CurrentPasscodeReceived(it))
            }
        }
    }

    override fun handleAction(action: PasscodeAction) {
        when (action) {
            is PasscodeAction.OnConfirmPasscodeChange -> onConfirmPasscodeChange(action.confirmPasscode)

            is PasscodeAction.OnNewPasscodeChange -> onNewPasscodeChange(action.newPasscode)

            is PasscodeAction.OnOldPasscodeChange -> onOldPasscodeChange(action.oldPasscode)

            PasscodeAction.ConfirmPasscodeVisibleClick -> {
                mutableStateFlow.update {
                    it.copy(isConfirmPasscodeVisible = !it.isConfirmPasscodeVisible)
                }
            }

            PasscodeAction.NewPasscodeVisibleClick -> {
                mutableStateFlow.update {
                    it.copy(isNewPasscodeVisible = !it.isNewPasscodeVisible)
                }
            }

            PasscodeAction.OldPasscodeVisibleClick -> {
                mutableStateFlow.update {
                    it.copy(isOldPasscodeVisible = !it.isOldPasscodeVisible)
                }
            }

            PasscodeAction.DismissDialog -> {
                mutableStateFlow.update {
                    it.copy(dialogState = null)
                }
            }

            PasscodeAction.NavigateBackClick -> {
                sendEvent(PasscodeEvent.OnNavigateBack)
            }

            PasscodeAction.SubmitClick -> validateSubmitClick()

            is PasscodeAction.Internal.UpdatePasscodeResult -> handleUpdatePasscodeResult(action)

            is PasscodeAction.NavigateToPasscodeScreen -> {
                sendEvent(PasscodeEvent.OnNavigateToPasscodeScreen)
            }

            is PasscodeAction.Internal.CurrentPasscodeReceived -> {
                val currentPasscode = action.passcode
                mutableStateFlow.update {
                    it.copy(
                        currentPasscode = currentPasscode,
                    )
                }
            }
        }
    }

    // Validation functions
    private fun validateOldPasscode(passcode: String): StringResource? = when {
        passcode.isEmpty() -> Res.string.feature_settings_passcode_empty_error
        passcode.length != 4 -> Res.string.feature_settings_passcode_length_error
        passcode.any { it.isLetter() } -> Res.string.feature_settings_passcode_invalid_error
        passcode != state.currentPasscode -> Res.string.feature_settings_passcode_old_passcode_error
        else -> null
    }

    private fun validateNewPasscode(passcode: String): StringResource? = when {
        passcode.isEmpty() -> Res.string.feature_settings_passcode_empty_error
        passcode.length != 4 -> Res.string.feature_settings_passcode_length_error
        passcode.any { it.isLetter() } -> Res.string.feature_settings_passcode_invalid_error
        else -> null
    }

    private fun validateConfirmPasscode(
        confirmPasscode: String,
        newPasscode: String,
    ): StringResource? = when {
        confirmPasscode.isEmpty() -> Res.string.feature_settings_passcode_empty_error
        confirmPasscode.length != 4 -> Res.string.feature_settings_passcode_length_error
        confirmPasscode.any { it.isLetter() } -> Res.string.feature_settings_passcode_invalid_error
        newPasscode != confirmPasscode -> Res.string.feature_settings_passcode_confirm_error
        else -> null
    }

    private var validationJob: Job? = null

    private fun onOldPasscodeChange(newValue: String) {
        // Immediately update the value without validation
        mutableStateFlow.update {
            it.copy(oldPasscode = newValue)
        }

        // Debounce validation
        validationJob?.cancel()
        validationJob = viewModelScope.launch {
            delay(300) // Wait 300ms before validating
            val error = validateOldPasscode(newValue)
            mutableStateFlow.update {
                it.copy(oldPasscodeError = error)
            }
        }
    }

    private fun onNewPasscodeChange(newValue: String) {
        mutableStateFlow.update {
            it.copy(newPasscode = newValue)
        }

        validationJob?.cancel()
        validationJob = viewModelScope.launch {
            delay(300)
            val newError = validateNewPasscode(newValue)
            val confirmError = if (state.confirmPasscode.isNotEmpty()) {
                validateConfirmPasscode(state.confirmPasscode, newValue)
            } else {
                null
            }

            mutableStateFlow.update {
                it.copy(
                    newPasscodeError = newError,
                    confirmPasscodeError = confirmError,
                )
            }
        }
    }

    private fun onConfirmPasscodeChange(newValue: String) {
        mutableStateFlow.update {
            it.copy(confirmPasscode = newValue)
        }

        validationJob?.cancel()
        validationJob = viewModelScope.launch {
            delay(300)
            val error = validateConfirmPasscode(newValue, state.newPasscode)
            mutableStateFlow.update {
                it.copy(confirmPasscodeError = error)
            }
        }
    }

    private fun validateSubmitClick() {
        val oldPasscodeError = validateOldPasscode(state.oldPasscode)
        val newPasscodeError = validateNewPasscode(state.newPasscode)
        val confirmPasscodeError = validateConfirmPasscode(state.confirmPasscode, state.newPasscode)

        mutableStateFlow.update {
            it.copy(
                oldPasscodeError = oldPasscodeError,
                newPasscodeError = newPasscodeError,
                confirmPasscodeError = confirmPasscodeError,
            )
        }

        if (oldPasscodeError == null && newPasscodeError == null && confirmPasscodeError == null) {
            handleSubmitClick()
        }
    }

    private fun handleSubmitClick() {
        mutableStateFlow.update {
            it.copy(dialogState = PasscodeState.DialogState.Loading)
        }
        viewModelScope.launch {
            userPreferencesRepository.setPasscode(
                state.newPasscode,
            )
            trySendAction(
                PasscodeAction.Internal.UpdatePasscodeResult(
                    Res.string.feature_settings_passcode_updated_successfully,
                ),
            )
        }
    }

    private fun handleUpdatePasscodeResult(action: PasscodeAction.Internal.UpdatePasscodeResult) {
        mutableStateFlow.update {
            it.copy(dialogState = PasscodeState.DialogState.Shown(action.result))
        }
        clearSensitiveData()
    }

    private fun clearSensitiveData() {
        mutableStateFlow.update {
            it.copy(
                oldPasscode = "",
                newPasscode = "",
                confirmPasscode = "",
                oldPasscodeError = null,
                newPasscodeError = null,
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        clearSensitiveData()
        validationJob?.cancel()
    }
}

internal data class PasscodeState(
    internal val currentPasscode: String = "",

    val oldPasscode: String = "",
    val newPasscode: String = "",
    val confirmPasscode: String = "",

    val oldPasscodeError: StringResource? = null,
    val newPasscodeError: StringResource? = null,
    val confirmPasscodeError: StringResource? = null,

    val isOldPasscodeVisible: Boolean = false,
    val isNewPasscodeVisible: Boolean = false,
    val isConfirmPasscodeVisible: Boolean = false,
    val dialogState: DialogState? = null,
) {
    sealed interface DialogState {
        data object Loading : DialogState
        data class Shown(val message: StringResource) : DialogState
    }
}

internal sealed interface PasscodeEvent {
    data object OnNavigateBack : PasscodeEvent
    data object OnNavigateToPasscodeScreen : PasscodeEvent
}

internal sealed interface PasscodeAction {
    data class OnOldPasscodeChange(val oldPasscode: String) : PasscodeAction
    data class OnNewPasscodeChange(val newPasscode: String) : PasscodeAction
    data class OnConfirmPasscodeChange(val confirmPasscode: String) : PasscodeAction

    data object NewPasscodeVisibleClick : PasscodeAction
    data object OldPasscodeVisibleClick : PasscodeAction
    data object ConfirmPasscodeVisibleClick : PasscodeAction

    data object SubmitClick : PasscodeAction

    data object NavigateBackClick : PasscodeAction
    data object DismissDialog : PasscodeAction
    data object NavigateToPasscodeScreen : PasscodeAction

    sealed interface Internal : PasscodeAction {
        data class UpdatePasscodeResult(val result: StringResource) : Internal
        data class CurrentPasscodeReceived(val passcode: String) : Internal
    }
}
