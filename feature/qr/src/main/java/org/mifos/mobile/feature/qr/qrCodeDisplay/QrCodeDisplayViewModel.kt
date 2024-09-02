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

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import org.mifos.mobile.core.qr.QrCodeGenerator
import org.mifos.mobile.feature.qr.navigation.QR_ARGS
import javax.inject.Inject

@HiltViewModel
internal class QrCodeDisplayViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val qrString = savedStateHandle.getStateFlow<String?>(key = QR_ARGS, initialValue = null)

    val qrCodeDisplayUiState = qrString
        .flatMapLatest { qrString ->
            flow {
                val qrBitmap = QrCodeGenerator.encodeAsBitmap(str = qrString)
                if (qrBitmap == null) {
                    emit(QrCodeDisplayUiState.Error())
                } else {
                    emit(QrCodeDisplayUiState.Success(qrBitmap = qrBitmap))
                }
            }
        }.catch {
            emit(QrCodeDisplayUiState.Error(errorMessage = it.message))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(300),
            initialValue = QrCodeDisplayUiState.Loading,
        )
}

internal sealed class QrCodeDisplayUiState {
    data object Loading : QrCodeDisplayUiState()
    data class Success(val qrBitmap: Bitmap) : QrCodeDisplayUiState()
    data class Error(val errorMessage: String? = null) : QrCodeDisplayUiState()
}
