package org.mifos.mobile.utils

sealed class RegistrationUiState {
    data class Error(val exception: Throwable) : RegistrationUiState()
    object Success : RegistrationUiState()
    object Loading : RegistrationUiState()
}