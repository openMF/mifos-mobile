package org.mifos.mobile.feature.login.utils

sealed class LoginState {
    object Initial : LoginState()
    object LoginSuccess : LoginState()
    object Loading : LoginState()
    object Error : LoginState()
    data class LoadClientSuccess(val clientName: String?) : LoginState()
}