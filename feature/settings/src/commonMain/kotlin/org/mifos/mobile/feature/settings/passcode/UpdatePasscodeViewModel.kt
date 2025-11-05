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

/**
 * ViewModel for the "Update Passcode" screen.
 *
 * This class manages the state and business logic for changing the user's passcode.
 * It handles input validation, interaction with the [UserPreferencesRepository] to
 * fetch the current passcode and set the new one, and communicates with the UI
 * through a UDF (Unidirectional Data Flow) pattern.
 *
 * @param userPreferencesRepository The repository for accessing user preferences,
 *   including the stored passcode.
 */
internal class UpdatePasscodeViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
) :
    BaseViewModel<PasscodeState, PasscodeEvent, PasscodeAction>(
        initialState = PasscodeState(),
    ) {
    init {
        // Observes the current passcode from the repository and updates the state.
        viewModelScope.launch {
            userPreferencesRepository.passcode.collect {
                trySendAction(PasscodeAction.Internal.CurrentPasscodeReceived(it))
            }
        }
    }

    /**
     * Handles all incoming actions from the UI, delegating them to the appropriate
     * business logic methods.
     *
     * @param action The [PasscodeAction] to be processed.
     */
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

    /**
     * Validates the old passcode against several criteria.
     * @param passcode The passcode string to validate.
     * @return A [StringResource] with an error message, or null if validation passes.
     */
    private fun validateOldPasscode(passcode: String): StringResource? = when {
        passcode.isEmpty() -> Res.string.feature_settings_passcode_empty_error
        passcode.length != 4 -> Res.string.feature_settings_passcode_length_error
        passcode.any { it.isLetter() } -> Res.string.feature_settings_passcode_invalid_error
        passcode != state.currentPasscode -> Res.string.feature_settings_passcode_old_passcode_error
        else -> null
    }

    /**
     * Validates the new passcode against several criteria.
     * @param passcode The passcode string to validate.
     * @return A [StringResource] with an error message, or null if validation passes.
     */
    private fun validateNewPasscode(passcode: String): StringResource? = when {
        passcode.isEmpty() -> Res.string.feature_settings_passcode_empty_error
        passcode.length != 4 -> Res.string.feature_settings_passcode_length_error
        passcode.any { it.isLetter() } -> Res.string.feature_settings_passcode_invalid_error
        else -> null
    }

    /**
     * Validates the confirmed passcode against the new passcode and other criteria.
     * @param confirmPasscode The confirmed passcode string.
     * @param newPasscode The new passcode string to match against.
     * @return A [StringResource] with an error message, or null if validation passes.
     */
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

    /**
     * Handles changes to the old passcode input field with debouncing to avoid
     * excessive validation.
     * @param newValue The new string value from the input field.
     */
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

    /**
     * Handles changes to the new passcode input field with debouncing and also re-validates
     * the confirmation field.
     * @param newValue The new string value from the input field.
     */
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

    /**
     * Handles changes to the confirm passcode input field with debouncing.
     * @param newValue The new string value from the input field.
     */
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

    /**
     * Validates all fields immediately, typically triggered by the submit button click.
     * If all validations pass, it proceeds to [handleSubmitClick].
     */
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

    /**
     * Sets the dialog state to loading and initiates the process of saving the new passcode
     * to the repository.
     */
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

    /**
     * Handles the result of the passcode update operation, showing a success dialog
     * and clearing sensitive data from the state.
     * @param action The result action containing the success message.
     */
    private fun handleUpdatePasscodeResult(action: PasscodeAction.Internal.UpdatePasscodeResult) {
        mutableStateFlow.update {
            it.copy(dialogState = PasscodeState.DialogState.Shown(action.result))
        }
        clearSensitiveData()
    }

    /**
     * Clears all passcode input fields and their associated errors from the state.
     */
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

    /**
     * Overridden to ensure sensitive data and background jobs are cleared when the
     * ViewModel is destroyed.
     */
    override fun onCleared() {
        super.onCleared()
        clearSensitiveData()
        validationJob?.cancel()
    }
}

/**
 * Represents the UI state for the "Update Passcode" screen.
 *
 * @property currentPasscode The user's current passcode fetched from preferences.
 * @property oldPasscode The value entered in the "Old Passcode" field.
 * @property newPasscode The value entered in the "New Passcode" field.
 * @property confirmPasscode The value entered in the "Confirm Passcode" field.
 * @property oldPasscodeError An optional error message for the old passcode field.
 * @property newPasscodeError An optional error message for the new passcode field.
 * @property confirmPasscodeError An optional error message for the confirm passcode field.
 * @property isOldPasscodeVisible Toggles the visibility of the old passcode.
 * @property isNewPasscodeVisible Toggles the visibility of the new passcode.
 * @property isConfirmPasscodeVisible Toggles the visibility of the confirm passcode.
 * @property dialogState The current state of the dialog (loading, shown, or hidden).
 */
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
    /**
     * Represents the state of any dialogs shown on the screen, such as a loading
     * indicator or a success message.
     */
    sealed interface DialogState {
        /** The dialog is showing a loading indicator. */
        data object Loading : DialogState

        /** The dialog is showing a message.
         * @param message The string resource to display.
         */
        data class Shown(val message: StringResource) : DialogState
    }
}

/**
 * A sealed interface representing one-time events that trigger UI side effects,
 * such as navigation.
 */
internal sealed interface PasscodeEvent {
    /** Event to navigate back from the current screen. */
    data object OnNavigateBack : PasscodeEvent

    /** Event to navigate to the main passcode screen after a successful update. */
    data object OnNavigateToPasscodeScreen : PasscodeEvent
}

/**
 * A sealed interface representing all possible user actions or internal events that
 * the ViewModel can handle.
 */
internal sealed interface PasscodeAction {
    /** Action triggered when the old passcode input changes. */
    data class OnOldPasscodeChange(val oldPasscode: String) : PasscodeAction

    /** Action triggered when the new passcode input changes. */
    data class OnNewPasscodeChange(val newPasscode: String) : PasscodeAction

    /** Action triggered when the confirm passcode input changes. */
    data class OnConfirmPasscodeChange(val confirmPasscode: String) : PasscodeAction

    /** Action to toggle the visibility of the new passcode. */
    data object NewPasscodeVisibleClick : PasscodeAction

    /** Action to toggle the visibility of the old passcode. */
    data object OldPasscodeVisibleClick : PasscodeAction

    /** Action to toggle the visibility of the confirm passcode. */
    data object ConfirmPasscodeVisibleClick : PasscodeAction

    /** Action triggered when the user clicks the submit button. */
    data object SubmitClick : PasscodeAction

    /** Action triggered when the user clicks the back navigation button. */
    data object NavigateBackClick : PasscodeAction

    /** Action to dismiss the current dialog. */
    data object DismissDialog : PasscodeAction

    /** Action to navigate to the passcode screen. */
    data object NavigateToPasscodeScreen : PasscodeAction

    /**
     * A sealed interface for internal actions not directly triggered by the UI.
     */
    sealed interface Internal : PasscodeAction {
        /** Internal action representing the result of the update operation. */
        data class UpdatePasscodeResult(val result: StringResource) : Internal

        /** Internal action triggered when the current passcode is fetched from the repository. */
        data class CurrentPasscodeReceived(val passcode: String) : Internal
    }
}
