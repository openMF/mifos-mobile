package org.mifos.mobile.utils

sealed class RegistrationUiState {
    data class Error(val exception: Int) : RegistrationUiState()
    object Success : RegistrationUiState()
    object Loading : RegistrationUiState()
    object Initial: RegistrationUiState()
}
