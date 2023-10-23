package org.mifos.mobile.utils

import com.google.zxing.Result

sealed class QrCodeUiState {
    object Initial : QrCodeUiState()
    object Loading : QrCodeUiState()
    data class ShowError(val message: Int) : QrCodeUiState()
    data class HandleDecodedResult(val result: Result?) : QrCodeUiState()
}
