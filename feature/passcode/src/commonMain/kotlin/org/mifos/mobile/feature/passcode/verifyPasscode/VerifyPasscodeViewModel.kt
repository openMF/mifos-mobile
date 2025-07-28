package org.mifos.mobile.feature.passcode.verifyPasscode

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


internal class VerifyPasscodeViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
) : BaseViewModel<VerifyPasscodeState, VerifyPasscodeEvent, VerifyPasscodeAction>(
    initialState = VerifyPasscodeState()
) {

    init {
        viewModelScope.launch {
            userPreferencesRepository.passcode.collect { passcode ->
                mutableStateFlow.update {
                    it.copy(storedPasscode = passcode)
                }
            }
        }
    }

    private var passcodeBuilder: StringBuilder = StringBuilder()

    override fun handleAction(action: VerifyPasscodeAction) {
        when (action) {
            VerifyPasscodeAction.OnContinueClick -> validateAndUpdate()

            is VerifyPasscodeAction.OnDigitClick -> handleDigitClick(action.digit)

            VerifyPasscodeAction.OnBackspaceClick -> handleBackspaceClick()
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

            val confirm = passcodeBuilder.toString()

            if (confirm == state.storedPasscode) {
                viewModelScope.launch {
                    sendEvent(
                        VerifyPasscodeEvent.OnPasscodeConfirm(
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
                        passcode = "",
                        filledDots = 0,
                        passcodeError = true,
                    )
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

internal data class VerifyPasscodeState(
    internal val storedPasscode: String = "",
    val maxDigits: Int = 4,
    val filledDots: Int = 0,
    val passcode: String = "",
    val passcodeError: Boolean = false,
)

internal sealed interface VerifyPasscodeEvent {
    data class OnPasscodeConfirm(
        val eventType: String,
        val eventDestination: String,
        val title: String,
        val subtitle: String,
        val buttonText: String,
    ) : VerifyPasscodeEvent
}

internal sealed interface VerifyPasscodeAction {
    data object OnContinueClick : VerifyPasscodeAction
    data object OnBackspaceClick : VerifyPasscodeAction
    data class OnDigitClick(val digit: String) : VerifyPasscodeAction
}
