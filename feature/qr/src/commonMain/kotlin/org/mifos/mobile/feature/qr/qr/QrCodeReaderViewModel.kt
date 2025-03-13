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
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.core.ui.utils.BaseViewModel

class QrCodeReaderViewModel : BaseViewModel<QrCodeReaderState, QrCodeReaderEvent, QrCodeReaderAction>(
    initialState = QrCodeReaderState(
        dialogState = null,
    ),
) {

    override fun handleAction(action: QrCodeReaderAction) {
        when (action) {
            is QrCodeReaderAction.ScanQrCode -> {
                getQrCodeResult(action.data)
            }
            QrCodeReaderAction.OnNavigate -> sendEvent(QrCodeReaderEvent.Navigate)
            QrCodeReaderAction.OnDismiss -> setDialogState(null)
        }
    }

    private fun updateState(update: (QrCodeReaderState) -> QrCodeReaderState) {
        mutableStateFlow.update(update)
    }

    private fun setDialogState(dialogState: QrCodeReaderState.DialogState?) {
        updateState { it.copy(dialogState = dialogState) }
    }

    private fun getQrCodeResult(data: String): Boolean {
        val errorMsg = "Error reading QR code"
        val invalidQr = "Invalid QR code"
        return try {
            val trimmedData = data.trim()

            if (trimmedData.startsWith("{") && trimmedData.endsWith("}")) {
                val beneficiary = parseBeneficiaryFromJson(trimmedData)
                beneficiary?.let {
                    sendEvent(
                        QrCodeReaderEvent.NavigateToBeneficiary(
                            it,
                            BeneficiaryState.CREATE_QR,
                        ),
                    )
                }
                true
            } else {
                setDialogState(
                    QrCodeReaderState.DialogState.Error(
                        invalidQr,
                    ),
                )
                false
            }
        } catch (e: Exception) {
            QrCodeReaderState.DialogState.Error(
                errorMsg,
            )
            false
        }
    }

    private fun parseBeneficiaryFromJson(jsonString: String): Beneficiary? {
        return try {
            Json.decodeFromString<Beneficiary>(jsonString)
        } catch (e: Exception) {
            null
        }
    }
}

@Parcelize
data class QrCodeReaderState(
    val dialogState: DialogState?,
) : Parcelable {
    sealed interface DialogState : Parcelable {
        @Parcelize
        data class Error(val message: String) : DialogState
    }
}

sealed interface QrCodeReaderAction {
    data class ScanQrCode(val data: String) : QrCodeReaderAction
    data object OnNavigate : QrCodeReaderAction
    data object OnDismiss : QrCodeReaderAction
}

sealed interface QrCodeReaderEvent {
    data object Navigate : QrCodeReaderEvent
    data class NavigateToBeneficiary(
        val beneficiary: Beneficiary,
        val beneficiaryState: BeneficiaryState = BeneficiaryState.CREATE_QR,
    ) : QrCodeReaderEvent
}
