package org.mifos.mobile.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.core.data.repositories.ClientRepository
import org.mifos.mobile.core.data.repositories.UserAuthRepository
import org.mifos.mobile.utils.LoginUiState
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userAuthRepositoryImp: UserAuthRepository,
    private val clientRepositoryImp: ClientRepository
) :
    ViewModel() {

    private var _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Initial)
    val loginUiState: StateFlow<LoginUiState> get() = _loginUiState

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
            userAuthRepositoryImp.login(username, password).catch {
                _loginUiState.value = LoginUiState.Error
            }.collect {
                clientRepositoryImp.saveAuthenticationTokenForSession(it)
                _loginUiState.value = LoginUiState.LoginSuccess
            }
        }
    }

    /**
     * This method fetches the Client, associated with current Access Token.
     */
    fun loadClient() {
        viewModelScope.launch {
            clientRepositoryImp.loadClient().catch {
                _loginUiState.value = LoginUiState.Error
                clientRepositoryImp.clearPrefHelper()
                clientRepositoryImp.reInitializeService()
            }.collect {
                if (it.pageItems.isNotEmpty()) {
                    val clientId = it.pageItems[0].id.toLong()
                    val clientName = it.pageItems[0].displayName
                    clientRepositoryImp.setClientId(clientId)
                    clientRepositoryImp.reInitializeService()
                    _loginUiState.value = LoginUiState.LoadClientSuccess(clientName)
                }
            }
        }
    }
}