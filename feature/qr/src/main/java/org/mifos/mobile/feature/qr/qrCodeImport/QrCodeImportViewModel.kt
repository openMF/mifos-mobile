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

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.zxing.BinaryBitmap
import com.google.zxing.NotFoundException
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.Result
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.multi.qrcode.QRCodeMultiReader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mifos.mobile.feature.qr.R
import org.mifos.mobile.feature.qr.qrCodeImport.QrCodeImportUiState.Initial
import javax.inject.Inject

@HiltViewModel
internal class QrCodeImportViewModel @Inject constructor() : ViewModel() {

    private val _qrCodeImportUiState = MutableStateFlow<QrCodeImportUiState>(Initial)
    val qrCodeImportUiState: StateFlow<QrCodeImportUiState> = _qrCodeImportUiState

    fun getDecodedResult(bitmap: Bitmap) {
        _qrCodeImportUiState.value = QrCodeImportUiState.Loading

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                decodeQrCode(bitmap)
            }

            if (result != null) {
                _qrCodeImportUiState.value = QrCodeImportUiState.HandleDecodedResult(result)
            } else {
                _qrCodeImportUiState.value =
                    QrCodeImportUiState.ShowError(R.string.error_reading_qr)
            }
        }
    }

    private fun decodeQrCode(bitmap: Bitmap): Result? {
        val intArray = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(intArray, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        val source = RGBLuminanceSource(bitmap.width, bitmap.height, intArray)
        val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

        return try {
            val reader = QRCodeMultiReader()
            reader.decode(binaryBitmap)
        } catch (e: NotFoundException) {
            null
        }
    }
}

internal sealed class QrCodeImportUiState {
    data object Initial : QrCodeImportUiState()
    data object Loading : QrCodeImportUiState()
    data class ShowError(val message: Int) : QrCodeImportUiState()
    data class HandleDecodedResult(val result: Result) : QrCodeImportUiState()
}
