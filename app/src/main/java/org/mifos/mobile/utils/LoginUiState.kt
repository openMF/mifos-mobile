package org.mifos.mobile.utils

sealed class LoginUiState {
    data class DynamicError(val errorMessage: String) : LoginUiState()
    object ServerDownError : LoginUiState()
    object DuringLoginError : LoginUiState()
    object LoginSuccess : LoginUiState()
    object Loading : LoginUiState()
    object UnauthorisedClientError : LoginUiState()
    object FetchingClientError : LoginUiState()
    object ClientNotFoundError : LoginUiState()
    data class LoadClientSuccess(val clientName: String?) : LoginUiState()
}