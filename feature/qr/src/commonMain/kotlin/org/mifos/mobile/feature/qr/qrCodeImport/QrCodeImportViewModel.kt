/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.qr.qrCodeImport

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import mifos_mobile.feature.qr.generated.resources.Res
import mifos_mobile.feature.qr.generated.resources.feature_qr_invalid
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.core.qr.decodeQrCode
import org.mifos.mobile.core.ui.utils.BaseViewModel

/**
 * `ViewModel` for the QR Code Import screen.
 *
 * This ViewModel handles the logic for importing a QR code from an image,
 * decoding its content, and navigating to the beneficiary application screen.
 * It uses a [BaseViewModel] to manage its state ([QrCodeImportState]), handle
 * actions ([QrCodeImportAction]), and emit events ([QrCodeImportEvent]).
 */
internal class QrCodeImportViewModel :
    BaseViewModel<QrCodeImportState, QrCodeImportEvent, QrCodeImportAction>(
        initialState = QrCodeImportState(
            dialogState = null,
        ),
    ) {

    /**
     * Handles incoming actions from the UI and dispatches them to the appropriate
     * business logic functions.
     *
     * @param action The [QrCodeImportAction] to be handled.
     */
    override fun handleAction(action: QrCodeImportAction) {
        when (action) {
            is QrCodeImportAction.Proceed -> decodeAndNavigate(action.bitmap)
            QrCodeImportAction.OnNavigate -> sendEvent(QrCodeImportEvent.Navigate)
            QrCodeImportAction.DismissDialog -> updateState { it.copy(dialogState = null) }
        }
    }

    /**
     * A helper function to update the mutable state flow.
     *
     * @param update A lambda function that takes the current state and returns a new state.
     */
    private fun updateState(update: (QrCodeImportState) -> QrCodeImportState) {
        mutableStateFlow.update(update)
    }

    /**
     * Displays an error dialog with a given message.
     *
     * @param message The [StringResource] for the error message to display.
     */
    private fun showError(message: StringResource) {
        updateState {
            it.copy(dialogState = QrCodeImportState.DialogState.Error(message))
        }
    }

    /**
     * Decodes the QR code from the given [ImageBitmap] and attempts to navigate.
     *
     * It first shows a loading dialog, then decodes the bitmap. If decoding is successful,
     * it validates that the content is a JSON string and tries to parse it into a
     * [Beneficiary] object. On success, it sends an event to open the beneficiary
     * application screen. On any failure (decoding, invalid format, or parsing error),
     * it shows an error dialog.
     *
     * @param bitmap The [ImageBitmap] containing the QR code to be decoded.
     */
    private fun decodeAndNavigate(bitmap: ImageBitmap) {
        updateState {
            it.copy(dialogState = QrCodeImportState.DialogState.Loading)
        }

        viewModelScope.launch {
            // Note: `decodeQrCode` is an assumed function that decodes the QR code from a bitmap.
            // This function is not provided in the original snippet but is necessary for the logic.
            val result = decodeQrCode(bitmap)?.trim()

            val isJsonWrapped = result?.startsWith("{") == true && result.endsWith("}")

            if (!isJsonWrapped) {
                showError(Res.string.feature_qr_invalid)
                return@launch
            }

            try {
                val beneficiary = Json.decodeFromString<Beneficiary>(result)
                sendEvent(
                    QrCodeImportEvent.OpenBeneficiaryApplication(
                        beneficiary,
                        BeneficiaryState.CREATE_QR,
                    ),
                )
            } catch (_: Exception) {
                showError(Res.string.feature_qr_invalid)
            }
        }
    }
}

/**
 * Represents the UI state for the QR Code Import screen.
 *
 * @property qrCodeResult The decoded string content of the QR code, or `null`.
 * @property dialogState The current state of any dialog to be shown on the screen,
 * or `null` if no dialog is visible.
 */
internal data class QrCodeImportState(
    val qrCodeResult: String? = null,
    val dialogState: DialogState?,
) {
    /**
     * A sealed interface representing the different types of dialogs that can be
     * shown on the QR Code Import screen.
     */
    sealed interface DialogState {
        /**
         * Represents a generic error dialog with a message.
         * @property message The [StringResource] for the error message.
         */
        data class Error(val message: StringResource) : DialogState

        /** Represents a loading dialog. */
        data object Loading : DialogState
    }
}

/**
 * A sealed interface representing one-time events that trigger UI side effects,
 * such as navigation.
 */
sealed interface QrCodeImportEvent {

    /** Event to trigger a generic navigation action. */
    data object Navigate : QrCodeImportEvent

    /**
     * Event to navigate to the beneficiary application screen.
     * @property beneficiary The parsed [Beneficiary] object from the QR code.
     * @property beneficiaryState The state to use for the beneficiary screen,
     * indicating it's from a QR code.
     */
    data class OpenBeneficiaryApplication(
        val beneficiary: Beneficiary,
        val beneficiaryState: BeneficiaryState,
    ) : QrCodeImportEvent
}

/**
 * A sealed interface representing user actions for the QR Code Import screen.
 */
sealed interface QrCodeImportAction {
    /** Action to navigate from the screen. */
    data object OnNavigate : QrCodeImportAction

    /**
     * Action to proceed with decoding the QR code from an image.
     * @property bitmap The [ImageBitmap] of the QR code.
     */
    data class Proceed(val bitmap: ImageBitmap) : QrCodeImportAction

    /** Action to dismiss any visible dialog. */
    data object DismissDialog : QrCodeImportAction
}
