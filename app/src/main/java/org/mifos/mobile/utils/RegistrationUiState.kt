package org.mifos.mobile.utils

sealed class RegistrationUiState {
    data class Error(val exception: String) : RegistrationUiState()
    object Success : RegistrationUiState()
    object Loading : RegistrationUiState()
}