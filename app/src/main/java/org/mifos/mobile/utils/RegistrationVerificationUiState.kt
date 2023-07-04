package org.mifos.mobile.utils

sealed class RegistrationVerificationUiState {
    object Loading : RegistrationVerificationUiState()
    object RegistrationVerificationSuccessful : RegistrationVerificationUiState()
    data class ErrorOnRegistrationVerification(val exception: Throwable) :
        RegistrationVerificationUiState()
}
