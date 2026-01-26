/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.passcode

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.passcode.generated.resources.Res
import mifos_mobile.feature.passcode.generated.resources.feature_passcode_common_continue
import mifos_mobile.feature.passcode.generated.resources.feature_passcode_setup_successful
import mifos_mobile.feature.passcode.generated.resources.feature_passcode_setup_successful_msg
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.EventType
import org.mifos.mobile.core.ui.utils.BaseViewModel

internal class PasscodeViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
) : BaseViewModel<PasscodeState, PasscodeEvent, PasscodeAction>(
    initialState = PasscodeState(),
) {

    private var passcodeBuilder: StringBuilder = StringBuilder()

    override fun handleAction(action: PasscodeAction) {
        when (action) {
            PasscodeAction.OnContinueClick -> validateAndUpdate()

            is PasscodeAction.OnDigitClick -> handleDigitClick(action.digit)

            PasscodeAction.OnBackspaceClick -> handleBackspaceClick()
        }
    }

    private fun handleBackspaceClick() {
        if (passcodeBuilder.isNotEmpty()) {
            passcodeBuilder.deleteAt(passcodeBuilder.length - 1)
            updatePasscodeState()
        }
    }

    private fun handleDigitClick(digit: String) {
        if (passcodeBuilder.length < stateFlow.value.maxDigits) {
            passcodeBuilder.append(digit)
            updatePasscodeState()
        }
        validateAndUpdate()
    }

    private fun validateAndUpdate() {
        if (passcodeBuilder.length == stateFlow.value.maxDigits && passcodeBuilder.all { char -> char.isDigit() }) {
            when (state.mode) {
                PasscodeMode.Set -> {
                    val first = passcodeBuilder.toString()
                    passcodeBuilder.clear()
                    mutableStateFlow.update {
                        it.copy(
                            mode = PasscodeMode.Confirm,
                            firstPasscode = first,
                            passcode = "",
                            filledDots = 0,
                        )
                    }
                }

                PasscodeMode.Confirm -> {
                    val confirm = passcodeBuilder.toString()

                    if (confirm == state.firstPasscode) {
                        viewModelScope.launch {
                            userPreferencesRepository.setPasscode(confirm)
                            sendEvent(
                                PasscodeEvent.OnPasscodeConfirm(
                                    eventType = EventType.SUCCESS.name,
                                    eventDestination = Constants.UNLOCKED,
                                    title = getString(Res.string.feature_passcode_setup_successful),
                                    subtitle = getString(Res.string.feature_passcode_setup_successful_msg),
                                    buttonText = getString(Res.string.feature_passcode_common_continue),
                                ),
                            )
                        }
                    } else {
                        passcodeBuilder.clear()
                        mutableStateFlow.update {
                            it.copy(
                                mode = PasscodeMode.Set,
                                firstPasscode = "",
                                passcode = "",
                                filledDots = 0,
                                passcodeError = true,
                            )
                        }
                    }
                }
            }
        }
    }

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

internal data class PasscodeState(
    internal val storedPasscode: String = "",
    val maxDigits: Int = 4,
    val filledDots: Int = 0,
    val passcode: String = "",
    val mode: PasscodeMode = PasscodeMode.Set,
    val firstPasscode: String = "",
    val passcodeError: Boolean = false,
)

internal sealed interface PasscodeEvent {
    data class OnPasscodeConfirm(
        val eventType: String,
        val eventDestination: String,
        val title: String,
        val subtitle: String,
        val buttonText: String,
    ) : PasscodeEvent
}

internal sealed interface PasscodeAction {
    data object OnContinueClick : PasscodeAction
    data object OnBackspaceClick : PasscodeAction
    data class OnDigitClick(val digit: String) : PasscodeAction
}

enum class PasscodeMode {
    Set,
    Confirm,
}
