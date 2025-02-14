/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.library.passcode.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mifos.library.passcode.data.PasscodeManager
import org.mifos.library.passcode.utility.Step
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.ui.utils.BaseViewModel

private const val KEY_STATE = "passcode_state"
private const val PASSCODE_LENGTH = 4

class PasscodeViewModel(
    private val passcodeRepository: PasscodeManager,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<PasscodeState, PasscodeEvent, PasscodeAction>(
    initialState = savedStateHandle[KEY_STATE] ?: PasscodeState(),
) {
    private var createPasscode: StringBuilder = StringBuilder()
    private var confirmPasscode: StringBuilder = StringBuilder()

    init {
        observePasscodeRepository()
    }

    private fun observePasscodeRepository() {
        viewModelScope.launch {
            passcodeRepository.hasPasscode.collect { hasPasscode ->
                mutableStateFlow.update {
                    it.copy(
                        hasPasscode = hasPasscode,
                        isPasscodeAlreadySet = hasPasscode,
                    )
                }
            }
        }
    }

    override fun handleAction(action: PasscodeAction) {
        when (action) {
            is PasscodeAction.EnterKey -> enterKey(action.key)
            is PasscodeAction.DeleteKey -> deleteKey()
            is PasscodeAction.DeleteAllKeys -> deleteAllKeys()
            is PasscodeAction.TogglePasscodeVisibility -> togglePasscodeVisibility()
            is PasscodeAction.Restart -> restart()
            is PasscodeAction.Internal.ProcessCompletedPasscode -> processCompletedPasscode()
        }
    }

    private fun enterKey(key: String) {
        if (state.filledDots >= PASSCODE_LENGTH) return

        val currentPasscode =
            if (state.activeStep == Step.Create) createPasscode else confirmPasscode
        currentPasscode.append(key)

        mutableStateFlow.update {
            it.copy(
                currentPasscodeInput = currentPasscode.toString(),
                filledDots = currentPasscode.length,
            )
        }

        if (state.filledDots == PASSCODE_LENGTH) {
            viewModelScope.launch {
                sendAction(PasscodeAction.Internal.ProcessCompletedPasscode)
            }
        }
    }

    private fun deleteKey() {
        val currentPasscode =
            if (state.activeStep == Step.Create) createPasscode else confirmPasscode
        if (currentPasscode.isNotEmpty()) {
            currentPasscode.deleteAt(currentPasscode.length - 1)
            mutableStateFlow.update {
                it.copy(
                    currentPasscodeInput = currentPasscode.toString(),
                    filledDots = currentPasscode.length,
                )
            }
        }
    }

    private fun deleteAllKeys() {
        if (state.activeStep == Step.Create) {
            createPasscode.clear()
        } else {
            confirmPasscode.clear()
        }
        mutableStateFlow.update {
            it.copy(
                currentPasscodeInput = "",
                filledDots = 0,
            )
        }
    }

    private fun togglePasscodeVisibility() {
        mutableStateFlow.update { it.copy(passcodeVisible = !it.passcodeVisible) }
    }

    private fun restart() {
        resetState()
    }

    private fun processCompletedPasscode() {
        viewModelScope.launch {
            when {
                state.isPasscodeAlreadySet -> validateExistingPasscode()
                state.activeStep == Step.Create -> moveToConfirmStep()
                else -> validateNewPasscode()
            }
        }
    }

    private suspend fun validateExistingPasscode() {
        val savedPasscode = passcodeRepository.getPasscode.first()
        if (savedPasscode == createPasscode.toString()) {
            sendEvent(PasscodeEvent.PasscodeConfirmed(createPasscode.toString()))
            createPasscode.clear()
        } else {
            sendEvent(PasscodeEvent.PasscodeRejected)
        }
        mutableStateFlow.update { it.copy(currentPasscodeInput = "") }
    }

    private fun moveToConfirmStep() {
        mutableStateFlow.update {
            it.copy(
                activeStep = Step.Confirm,
                filledDots = 0,
                currentPasscodeInput = "",
            )
        }
    }

    private suspend fun validateNewPasscode() {
        if (createPasscode.toString() == confirmPasscode.toString()) {
            passcodeRepository.savePasscode(confirmPasscode.toString())
            sendEvent(PasscodeEvent.PasscodeConfirmed(confirmPasscode.toString()))
            resetState()
        } else {
            sendEvent(PasscodeEvent.PasscodeRejected)
            resetState()
        }
    }

    private fun resetState() {
        mutableStateFlow.update {
            PasscodeState(
                hasPasscode = it.hasPasscode,
                isPasscodeAlreadySet = it.isPasscodeAlreadySet,
            )
        }
        createPasscode.clear()
        confirmPasscode.clear()
    }
}

@Parcelize
data class PasscodeState(
    val hasPasscode: Boolean = false,
    val activeStep: Step = Step.Create,
    val filledDots: Int = 0,
    val passcodeVisible: Boolean = false,
    val currentPasscodeInput: String = "",
    val isPasscodeAlreadySet: Boolean = false,
) : Parcelable

sealed class PasscodeEvent {
    data class PasscodeConfirmed(val passcode: String) : PasscodeEvent()
    data object PasscodeRejected : PasscodeEvent()
}

sealed class PasscodeAction {
    data class EnterKey(val key: String) : PasscodeAction()
    data object DeleteKey : PasscodeAction()
    data object DeleteAllKeys : PasscodeAction()
    data object TogglePasscodeVisibility : PasscodeAction()
    data object Restart : PasscodeAction()

    sealed class Internal : PasscodeAction() {
        data object ProcessCompletedPasscode : Internal()
    }
}
