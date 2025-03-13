/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.qr.qrCodeDisplay

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.qr.generated.resources.Res
import mifos_mobile.feature.qr.generated.resources.choose_option
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.model.IgnoredOnParcel
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.qr.generateQrCode
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.feature.qr.navigation.QR_ARGS

internal class QrCodeDisplayViewModel(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<QrCodeDisplayState, QrCodeDisplayEvent, QrCodeDisplayAction>(
    initialState = QrCodeDisplayState(
        dialogState = null,
        qrArgs = savedStateHandle.getStateFlow<String?>(
            key = QR_ARGS,
            initialValue = null,
        ).value,
    ),
) {

    init {
        viewModelScope.launch {
            val option = getString(Res.string.choose_option)
            updateState {
                it.copy(
                    option = option,
                )
            }
        }
        generateQrBitmap()
    }

    override fun handleAction(action: QrCodeDisplayAction) {
        when (action) {
            is QrCodeDisplayAction.GenerateQrCode -> generateQrBitmap()
            QrCodeDisplayAction.OnNavigate -> sendEvent(QrCodeDisplayEvent.Navigate)
            QrCodeDisplayAction.DismissDialog -> setDialogState(null)
            QrCodeDisplayAction.ShareQrCode -> state.qrBitmap?.let {
                share(it, state.option)
            }
        }
    }

    private fun updateState(update: (QrCodeDisplayState) -> QrCodeDisplayState) {
        mutableStateFlow.update(update)
    }

    private fun setDialogState(dialogState: QrCodeDisplayState.DialogState?) {
        updateState { it.copy(dialogState = dialogState) }
    }

    private fun generateQrBitmap() {
        setDialogState(
            dialogState = QrCodeDisplayState.DialogState.Loading,
        )

        viewModelScope.launch {
            try {
                val qrBitmap = state.qrArgs?.let { generateQrCode(str = it) }
                if (qrBitmap != null) {
                    setDialogState(null)
                    updateState {
                        it.copy(
                            qrBitmap = qrBitmap,
                        )
                    }
                } else {
                    setDialogState(
                        QrCodeDisplayState.DialogState.Error(
                            "Failed to generate QR Code",
                        ),
                    )
                }
            } catch (e: Exception) {
                setDialogState(
                    QrCodeDisplayState.DialogState.Error(
                        "Error generating QR Code",
                    ),
                )
            }
        }
    }
}

@Parcelize
data class QrCodeDisplayState(
    val option: String = "",
    val qrArgs: String? = null,
    @IgnoredOnParcel
    val qrBitmap: ImageBitmap? = null,
    val dialogState: DialogState?,
) : Parcelable {
    sealed interface DialogState : Parcelable {
        @Parcelize
        data class Error(val message: String) : DialogState

        @Parcelize
        data object Loading : DialogState
    }
}

sealed interface QrCodeDisplayEvent {
    data object Navigate : QrCodeDisplayEvent
    data class ShowToast(val message: String) : QrCodeDisplayEvent
}

sealed interface QrCodeDisplayAction {
    data object OnNavigate : QrCodeDisplayAction
    data class GenerateQrCode(val qrString: String) : QrCodeDisplayAction
    data object DismissDialog : QrCodeDisplayAction
    data object ShareQrCode : QrCodeDisplayAction
}
