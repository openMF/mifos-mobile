package org.mifos.mobile.utils

sealed class LoginUiState {
    object LoginSuccess : LoginUiState()
    object Loading : LoginUiState()
    object Error : LoginUiState()
    data class LoadClientSuccess(val clientName: String?) : LoginUiState()
}