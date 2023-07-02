package org.mifos.mobile.utils

sealed class RegistrationUiState {
    data class ErrorOnRegistration(val exception: Throwable) : RegistrationUiState()
    object RegistrationSuccessful : RegistrationUiState()
    object Loading : RegistrationUiState()
}