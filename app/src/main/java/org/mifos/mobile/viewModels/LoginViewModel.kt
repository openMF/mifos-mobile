package org.mifos.mobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.mifos.mobile.repositories.ClientRepository
import org.mifos.mobile.repositories.UserAuthRepository
import org.mifos.mobile.utils.LoginUiState
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userAuthRepositoryImp: UserAuthRepository,
    private val clientRepositoryImp: ClientRepository
) :
    ViewModel() {

    private var _loginUiState = MutableLiveData<LoginUiState>()
    val loginUiState: LiveData<LoginUiState> get() = _loginUiState

    fun isFieldEmpty(fieldText: String): Boolean {
        return fieldText.isEmpty()
    }

    fun isUsernameLengthInadequate(username: String): Boolean {
        return username.length < 5
    }

    fun isPasswordLengthInadequate(password: String): Boolean {
        return password.length < 6
    }

    fun usernameHasSpaces(username: String): Boolean {
        return username.trim().contains(" ")
    }

    /**
     * This method attempts to authenticate the user from
     * the server and then persist the authentication data if we successfully
     * authenticate the credentials or notify about any errors.
     */
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginUiState.value = LoginUiState.Loading
            val response = userAuthRepositoryImp.login(username, password)
            if (response?.isSuccessful == true) {
                response.body()?.let { clientRepositoryImp.saveAuthenticationTokenForSession(it) }
                _loginUiState.value = LoginUiState.LoginSuccess
            } else {
                _loginUiState.value = LoginUiState.Error
            }
        }
    }

    /**
     * This method fetches the Client, associated with current Access Token.
     */
    fun loadClient() {
        viewModelScope.launch {
            val response = clientRepositoryImp.loadClient()
            if (response?.isSuccessful == true) {
                if (response.body()?.pageItems?.isNotEmpty() == true) {
                    val clientId = response.body()!!.pageItems[0]?.id?.toLong()
                    val clientName = response.body()!!.pageItems[0]?.displayName
                    clientRepositoryImp.setClientId(clientId)
                    _loginUiState.value = LoginUiState.LoadClientSuccess(clientName)
                } else {
                    _loginUiState.value = LoginUiState.Error
                }
            } else {
                _loginUiState.value = LoginUiState.Error
                clientRepositoryImp.clearPrefHelper()
            }
        }
    }
}