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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.qr.generated.resources.Res
import mifos_mobile.feature.qr.generated.resources.choose_option
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.FileUtils.Companion.logger
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ShareUtils
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
//        generateQrBitmap()
    }

    override fun handleAction(action: QrCodeDisplayAction) {
        when (action) {
            QrCodeDisplayAction.OnNavigate -> sendEvent(QrCodeDisplayEvent.Navigate)
            QrCodeDisplayAction.DismissDialog -> setDialogState(null)
            is QrCodeDisplayAction.ShareQrCode -> {
                viewModelScope.launch {
                    logger.d { "Sharing QR Code: ${action.option}, size: ${action.qrBitmap.size} bytes" }
                    ShareUtils.shareImage(
                        action.option,
                        action.qrBitmap,
                    )
                }
            }
        }
    }

    private fun updateState(update: (QrCodeDisplayState) -> QrCodeDisplayState) {
        mutableStateFlow.update(update)
    }

    private fun setDialogState(dialogState: QrCodeDisplayState.DialogState?) {
        updateState { it.copy(dialogState = dialogState) }
    }
}

@Parcelize
data class QrCodeDisplayState(
    val option: String = "",
    val qrArgs: String? = null,
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
    data object DismissDialog : QrCodeDisplayAction
    data class ShareQrCode(
        val qrBitmap: ByteArray,
        val option: String,
    ) : QrCodeDisplayAction
}
