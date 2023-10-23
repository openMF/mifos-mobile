package org.mifos.mobile.utils

sealed class TransferUiState {
    object Initial : TransferUiState()
    object Loading : TransferUiState()
    object TransferSuccess : TransferUiState()
    data class Error(val errorMessage: Throwable) : TransferUiState()
}

