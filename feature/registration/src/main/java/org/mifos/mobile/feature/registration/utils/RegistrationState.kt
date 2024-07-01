package org.mifos.mobile.feature.registration.utils

sealed class RegistrationState {
    data class Error(val exception: Int) : RegistrationState()
    object Success : RegistrationState()
    object Loading : RegistrationState()
    object Initial: RegistrationState()
}
