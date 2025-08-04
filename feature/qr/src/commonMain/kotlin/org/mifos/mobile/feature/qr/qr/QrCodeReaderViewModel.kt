/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.qr.qr

import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import mifos_mobile.feature.qr.generated.resources.Res
import mifos_mobile.feature.qr.generated.resources.feature_qr_error
import mifos_mobile.feature.qr.generated.resources.feature_qr_invalid
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.core.ui.utils.BaseViewModel

/**
 * `ViewModel` for the QR Code Reader screen.
 *
 * This ViewModel handles the logic for scanning a QR code, parsing the data, and
 * navigating to the appropriate screen based on the content. It follows a
 * Unidirectional Data Flow (UDF) pattern, using a [BaseViewModel] to manage its state
 * ([QrCodeReaderState]), handle actions ([QrCodeReaderAction]), and emit events
 * ([QrCodeReaderEvent]).
 */
internal class QrCodeReaderViewModel :
    BaseViewModel<QrCodeReaderState, QrCodeReaderEvent, QrCodeReaderAction>(
        initialState = QrCodeReaderState(
            dialogState = null,
        ),
    ) {

    /**
     * Handles incoming actions from the UI and dispatches them to the appropriate
     * business logic functions.
     *
     * @param action The [QrCodeReaderAction] to be handled.
     */
    override fun handleAction(action: QrCodeReaderAction) {
        when (action) {
            is QrCodeReaderAction.ScanQrCode -> {
                getQrCodeResult(action.data)
            }

            QrCodeReaderAction.OnNavigate -> sendEvent(QrCodeReaderEvent.Navigate)

            QrCodeReaderAction.OnNavigateToUpload -> sendEvent(QrCodeReaderEvent.NavigateToUploadQr)

            QrCodeReaderAction.OnDismiss -> dismissDialog()
        }
    }

    /**
     * A helper function to update the mutable state flow.
     *
     * @param update A lambda function that takes the current state and returns a new state.
     */
    private fun updateState(update: (QrCodeReaderState) -> QrCodeReaderState) {
        mutableStateFlow.update(update)
    }

    /**
     * Displays an error dialog with a given message.
     *
     * @param message The [StringResource] for the error message to display.
     */
    private fun showError(message: StringResource) {
        updateState {
            it.copy(dialogState = QrCodeReaderState.DialogState.Error(message))
        }
    }

    /**
     * Dismisses any currently visible dialog by setting the dialog state to null.
     */
    private fun dismissDialog() {
        updateState {
            it.copy(dialogState = null)
        }
    }

    /**
     * Processes the scanned QR code data.
     *
     * It first checks if the data is a valid JSON string. If so, it attempts to
     * parse it into a [Beneficiary] object. On success, it sends a navigation event.
     * On failure (invalid format or parsing error), it displays an error message.
     *
     * @param data The raw string data from the scanned QR code.
     * @return `true` if the QR code was successfully parsed and a navigation event was sent,
     * `false` otherwise.
     */
    private fun getQrCodeResult(data: String): Boolean {
        val trimmedData = data.trim()

        if (!trimmedData.startsWith("{") || !trimmedData.endsWith("}")) {
            showError(Res.string.feature_qr_invalid)
            return false
        }

        return try {
            val beneficiary = parseBeneficiaryFromJson(trimmedData)
            if (beneficiary != null) {
                sendEvent(
                    QrCodeReaderEvent.NavigateToBeneficiary(
                        beneficiary,
                        BeneficiaryState.CREATE_QR,
                    ),
                )
                true
            } else {
                showError(Res.string.feature_qr_invalid)
                false
            }
        } catch (_: Exception) {
            showError(Res.string.feature_qr_error)
            false
        }
    }

    /**
     * Parses a JSON string into a [Beneficiary] object using a JSON deserializer.
     *
     * @param jsonString The JSON string to parse.
     * @return A [Beneficiary] object if parsing is successful, otherwise `null`.
     */
    private fun parseBeneficiaryFromJson(jsonString: String): Beneficiary? {
        return try {
            Json.decodeFromString<Beneficiary>(jsonString)
        } catch (e: Exception) {
            null
        }
    }
}

/**
 * Represents the UI state for the QR Code Reader screen.
 *
 * @property dialogState The current state of any dialog to be shown on the screen,
 * or `null` if no dialog is visible.
 */
internal data class QrCodeReaderState(
    val dialogState: DialogState?,
) {
    /**
     * A sealed interface representing the different types of dialogs that can be
     * shown on the QR Code Reader screen.
     */
    sealed interface DialogState {
        /**
         * Represents a generic error dialog with a message.
         * @property message The [StringResource] for the error message.
         */
        data class Error(val message: StringResource) : DialogState
    }
}

/**
 * A sealed interface representing user actions for the QR Code Reader screen.
 */
sealed interface QrCodeReaderAction {
    /**
     * Action triggered when a QR code is successfully scanned.
     * @property data The raw string data from the scanned QR code.
     */
    data class ScanQrCode(val data: String) : QrCodeReaderAction

    /** Action to navigate upload qr from the screen. */
    data object OnNavigateToUpload : QrCodeReaderAction

    /** Action to navigate from the screen. */
    data object OnNavigate : QrCodeReaderAction

    /** Action to dismiss a dialog. */
    data object OnDismiss : QrCodeReaderAction
}

/**
 * A sealed interface representing one-time events that trigger UI side effects,
 * such as navigation.
 */
sealed interface QrCodeReaderEvent {
    /** Event to trigger a generic navigation action. */
    data object Navigate : QrCodeReaderEvent

    /** Event to trigger a qr import navigation action. */
    data object NavigateToUploadQr : QrCodeReaderEvent

    /**
     * Event to navigate to the beneficiary details screen.
     * @property beneficiary The parsed [Beneficiary] object from the QR code.
     * @property beneficiaryState The state to use for the beneficiary screen,
     * defaults to [BeneficiaryState.CREATE_QR].
     */
    data class NavigateToBeneficiary(
        val beneficiary: Beneficiary,
        val beneficiaryState: BeneficiaryState = BeneficiaryState.CREATE_QR,
    ) : QrCodeReaderEvent
}
