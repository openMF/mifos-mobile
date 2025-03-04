/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.auth.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.ClientRepository
import org.mifos.mobile.core.data.repository.UserAuthRepository
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.datastore.model.UserData
import org.mifos.mobile.core.model.IgnoredOnParcel
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.User
import org.mifos.mobile.core.ui.utils.BaseViewModel

class LoginViewModel(
    private val userAuthRepositoryImpl: UserAuthRepository,
    private val clientRepositoryImpl: ClientRepository,
    private val userPreferencesRepositoryImpl: UserPreferencesRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<LoginState, LoginEvent, LoginAction>(
    initialState = LoginState(dialogState = null),
) {
    init {
        savedStateHandle.get<String>("username")?.let {
            trySendAction(LoginAction.UsernameChanged(it))
        }

        viewModelScope.launch {
            loadClient()
        }
    }

    private fun updateState(update: (LoginState) -> LoginState) {
        mutableStateFlow.update(update)
    }

    override fun handleAction(action: LoginAction) {
        when (action) {
            is LoginAction.UsernameChanged -> {
                updateState {
                    it.copy(
                        username = action.username,
                        isLoginButtonEnabled = action.username.isNotEmpty() && it.password.isNotEmpty(),
                    )
                }
            }
            is LoginAction.PasswordChanged -> {
                updateState {
                    it.copy(
                        password = action.password,
                        isLoginButtonEnabled = it.username.isNotEmpty() && it.password.isNotEmpty(),
                    )
                }
            }
            is LoginAction.TogglePasswordVisibility -> {
                updateState { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }
            is LoginAction.LoginClicked -> {
                loginUser(state.username, state.password)
            }
            is LoginAction.Internal.ReceiveLoginResult -> {
                handleLoginResult(action)
            }
            is LoginAction.SignupClicked -> {
                sendEvent(LoginEvent.NavigateToSignup)
            }
            is LoginAction.ErrorDialogDismiss -> updateState { it.copy(dialogState = null) }
        }
    }

    private fun handleLoginResult(action: LoginAction.Internal.ReceiveLoginResult) {
        when (action.loginResult) {
            is DataState.Error -> {
                val message = action.loginResult.exception.message ?: "Error logging in"

                mutableStateFlow.update {
                    it.copy(dialogState = LoginState.DialogState.Error(message))
                }
            }

            is DataState.Loading -> {
                mutableStateFlow.update {
                    it.copy(dialogState = LoginState.DialogState.Loading)
                }
            }

            is DataState.Success -> {
                mutableStateFlow.update {
                    it.copy(dialogState = null)
                }
                val user = action.loginResult.data

                val userData = user.toUserData()

                viewModelScope.launch {
                    userPreferencesRepositoryImpl.updateUser(userData)
                }
                sendEvent(LoginEvent.NavigateToPasscodeScreen)
            }
        }
    }

    private fun User.toUserData(): UserData {
        return UserData(
            userId = this.userId,
            userName = this.username ?: "",
            clientId = this.userId,
            isAuthenticated = this.isAuthenticated,
            base64EncodedAuthenticationKey = this.base64EncodedAuthenticationKey ?: "",
        )
    }

    private fun loginUser(
        username: String,
        password: String,
    ) {
        mutableStateFlow.update {
            it.copy(dialogState = LoginState.DialogState.Loading)
        }

        viewModelScope.launch {
            val result = userAuthRepositoryImpl.login(username, password)
            sendAction(LoginAction.Internal.ReceiveLoginResult(result))
        }
    }

    /**
     * This method fetches the Client, associated with current Access Token.
     */
    private fun loadClient() {
        viewModelScope.launch {
            try {
                val client = clientRepositoryImpl.loadClient().firstOrNull()
                if (client != null && client.data?.pageItems?.isEmpty() != false) {
                    val clientId = client.data?.pageItems?.get(0)?.id?.toLong()
                    val clientName = client.data?.pageItems?.get(0)?.displayName
                    userPreferencesRepositoryImpl.updateClientId(clientId)
                    mutableStateFlow.update { it.copy(clientName = clientName ?: "clientName") }
                }
            } catch (e: Exception) {
                mutableStateFlow.update {
                    it.copy(
                        dialogState = LoginState.DialogState.Error(
                            e.message ?: "Error loading client",
                        ),
                    )
                }
            }
        }
    }
}

@Parcelize
data class LoginState(
    val username: String = "",
    @IgnoredOnParcel
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val clientName: String = "",
    val dialogState: DialogState?,
    val isLoginButtonEnabled: Boolean = false,
) : Parcelable {
    sealed interface DialogState : Parcelable {
        @Parcelize
        data class Error(val message: String) : DialogState

        @Parcelize
        data object Loading : DialogState
    }
}

sealed interface LoginEvent {
    data object NavigateToSignup : LoginEvent
    data object NavigateToPasscodeScreen : LoginEvent
    data class ShowToast(val message: String) : LoginEvent
}

sealed interface LoginAction {
    data class UsernameChanged(val username: String) : LoginAction
    data class PasswordChanged(val password: String) : LoginAction
    data object TogglePasswordVisibility : LoginAction
    data object ErrorDialogDismiss : LoginAction
    data object LoginClicked : LoginAction
    data object SignupClicked : LoginAction

    sealed class Internal : LoginAction {
        data class ReceiveLoginResult(
            val loginResult: DataState<User>,
        ) : Internal()
    }
}
