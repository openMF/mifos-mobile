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
import mifos_mobile.feature.qr.generated.resources.error_reading_qr
import mifos_mobile.feature.qr.generated.resources.invalid_qr
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.core.qr.decodeQrCode
import org.mifos.mobile.core.ui.utils.BaseViewModel

internal class QrCodeImportViewModel :
    BaseViewModel<QrCodeImportState, QrCodeImportEvent, QrCodeImportAction>(
        initialState = QrCodeImportState(
            dialogState = null,
        ),
    ) {

    override fun handleAction(action: QrCodeImportAction) {
        when (action) {
            is QrCodeImportAction.Proceed -> getDecodedResult(action.bitmap)
            QrCodeImportAction.OnNavigate -> sendEvent(QrCodeImportEvent.Navigate)
            QrCodeImportAction.DismissDialog -> updateState { it.copy(dialogState = null) }
        }
    }

    private fun updateState(update: (QrCodeImportState) -> QrCodeImportState) {
        mutableStateFlow.update(update)
    }

    private fun getDecodedResult(bitmap: ImageBitmap) {
        updateState {
            it.copy(dialogState = QrCodeImportState.DialogState.Loading)
        }

        viewModelScope.launch {
            val errorMsg = getString(Res.string.error_reading_qr)
            val invalidQr = getString(Res.string.invalid_qr)
            val result = decodeQrCode(bitmap)
            if (result != null) {
                val trimmedResult = result.trim().removeSurrounding("{", "}")
                try {
                    if (trimmedResult.startsWith("{") && trimmedResult.endsWith("}")) {
                        val beneficiary = Json.decodeFromString<Beneficiary>(trimmedResult)
                        sendEvent(
                            QrCodeImportEvent.OpenBeneficiaryApplication(
                                beneficiary,
                                BeneficiaryState.CREATE_QR,
                            ),
                        )
                    } else {
                        updateState {
                            it.copy(dialogState = QrCodeImportState.DialogState.Error(invalidQr))
                        }
                    }
                } catch (e: Exception) {
                    updateState {
                        it.copy(dialogState = QrCodeImportState.DialogState.Error(errorMsg))
                    }
                }
            } else {
                updateState {
                    it.copy(dialogState = QrCodeImportState.DialogState.Error(errorMsg))
                }
            }
        }
    }
}

@Parcelize
data class QrCodeImportState(
    val qrCodeResult: String? = null,
    val dialogState: DialogState?,
) : Parcelable {
    sealed interface DialogState : Parcelable {
        @Parcelize
        data class Error(val message: String) : DialogState

        @Parcelize
        data object Loading : DialogState
    }
}

sealed interface QrCodeImportEvent {
    data class ShowToast(val message: String) : QrCodeImportEvent
    data object Navigate : QrCodeImportEvent
    data class OpenBeneficiaryApplication(
        val beneficiary: Beneficiary,
        val beneficiaryState: BeneficiaryState,
    ) : QrCodeImportEvent
}

sealed interface QrCodeImportAction {
    data object OnNavigate : QrCodeImportAction
    data class Proceed(val bitmap: ImageBitmap) : QrCodeImportAction
    data object DismissDialog : QrCodeImportAction
}
