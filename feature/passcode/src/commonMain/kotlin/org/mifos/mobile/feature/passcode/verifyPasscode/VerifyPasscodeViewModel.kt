/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.passcode.verifyPasscode

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.ui.utils.AuthResult
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ResultNavigator

/**
 * ViewModel responsible for verifying the user's passcode.
 *
 * Features:
 * - Collects stored passcode from [UserPreferencesRepository]
 * - Handles user input (digits, backspace, and continue)
 * - Validates input and emits success event using [ResultNavigator]
 *
 * @property userPreferencesRepository Repository for retrieving stored passcode
 * @property navigator Result navigator used to emit [AuthResult] upon success
 */
internal class VerifyPasscodeViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val navigator: ResultNavigator,
) : BaseViewModel<VerifyPasscodeState, VerifyPasscodeEvent, VerifyPasscodeAction>(
    initialState = VerifyPasscodeState(),
) {

    init {
        // Collect the stored passcode from preferences and update the state
        viewModelScope.launch {
            userPreferencesRepository.passcode.collect { passcode ->
                mutableStateFlow.update {
                    it.copy(storedPasscode = passcode)
                }
            }
        }
    }

    private var passcodeBuilder: StringBuilder = StringBuilder()

    /**
     * Handles UI actions like digit press, backspace, and continue click.
     *
     * @param action Action performed by user on the passcode screen
     */
    override fun handleAction(action: VerifyPasscodeAction) {
        when (action) {
            VerifyPasscodeAction.OnContinueClick -> validateAndUpdate()

            is VerifyPasscodeAction.OnDigitClick -> handleDigitClick(action.digit)

            VerifyPasscodeAction.OnBackspaceClick -> handleBackspaceClick()
        }
    }

    /**
     * Handles backspace action by removing the last entered digit.
     */
    private fun handleBackspaceClick() {
        if (passcodeBuilder.isNotEmpty()) {
            passcodeBuilder.deleteAt(passcodeBuilder.length - 1)
            updatePasscodeState()
        }
    }

    /**
     * Handles digit input by appending to the passcode if within limit.
     *
     * @param digit A single digit entered by the user.
     */
    private fun handleDigitClick(digit: String) {
        if (passcodeBuilder.length < stateFlow.value.maxDigits) {
            passcodeBuilder.append(digit)
            updatePasscodeState()
        }
        validateAndUpdate()
    }

    /**
     * Validates the passcode:
     * - If correct: emits [AuthResult(true)] and triggers [VerifyPasscodeEvent.PasscodeAccepted]
     * - If incorrect: resets the passcode state and shows error.
     */
    private fun validateAndUpdate() {
        if (passcodeBuilder.length == stateFlow.value.maxDigits && passcodeBuilder.all { char -> char.isDigit() }) {
            val confirm = passcodeBuilder.toString()

            if (confirm == state.storedPasscode) {
                viewModelScope.launch {
                    navigator.emit(AuthResult(true))
                    sendEvent(VerifyPasscodeEvent.PasscodeAccepted)
                }
            } else {
                passcodeBuilder.clear()
                mutableStateFlow.update {
                    it.copy(
                        passcode = "",
                        filledDots = 0,
                        passcodeError = true,
                    )
                }
            }
        }
    }

    /**
     * Updates the state with current input:
     * - Sets the passcode string
     * - Updates filled dot count
     * - Clears error state
     */
    private fun updatePasscodeState() {
        mutableStateFlow.update {
            it.copy(
                passcode = passcodeBuilder.toString(),
                filledDots = passcodeBuilder.length,
                passcodeError = false,
            )
        }
    }
}

/**
 * UI state for the passcode verification screen.
 *
 * @property storedPasscode The actual passcode stored in preferences
 * @property maxDigits The number of digits required for verification
 * @property filledDots Number of digits entered by the user
 * @property passcode The current passcode entered by the user
 * @property passcodeError Whether to show an error state for incorrect input
 */
internal data class VerifyPasscodeState(
    internal val storedPasscode: String = "",
    val maxDigits: Int = 4,
    val filledDots: Int = 0,
    val passcode: String = "",
    val passcodeError: Boolean = false,
)

/**
 * Events emitted from the ViewModel to notify the UI of one-time effects.
 */
internal sealed interface VerifyPasscodeEvent {

    /**
     * Event sent when the passcode entered matches the stored passcode.
     * The UI should navigate forward.
     */
    data object PasscodeAccepted : VerifyPasscodeEvent
}

/**
 * Actions representing user interactions on the passcode screen.
 */
internal sealed interface VerifyPasscodeAction {

    /**
     * User tapped the continue/submit button to validate the passcode.
     */
    data object OnContinueClick : VerifyPasscodeAction

    /**
     * User tapped backspace to remove the last digit.
     */
    data object OnBackspaceClick : VerifyPasscodeAction

    /**
     * User tapped a digit key on the passcode keypad.
     *
     * @property digit The digit pressed by the user (0–9)
     */
    data class OnDigitClick(val digit: String) : VerifyPasscodeAction
}
