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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.auth.generated.resources.Res
import mifos_mobile.feature.auth.generated.resources.feature_sign_in_password_error
import mifos_mobile.feature.auth.generated.resources.feature_sign_in_username_error
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.UserAuthRepository
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.datastore.model.UserData
import org.mifos.mobile.core.model.entity.User
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ScreenUiState

class LoginViewModel(
    private val userAuthRepositoryImpl: UserAuthRepository,
    private val userPreferencesRepositoryImpl: UserPreferencesRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<LoginState, LoginEvent, LoginAction>(
    initialState = LoginState(uiState = ScreenUiState.Success),
) {

    private var loginJob: Job? = null

    init {
        savedStateHandle.get<String>("username")?.let {
            trySendAction(LoginAction.UsernameChanged(it))
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
                        isError = false,
                        username = action.username,
                        userNameError = null,
                    )
                }
            }

            is LoginAction.PasswordChanged -> {
                updateState {
                    it.copy(
                        isError = false,
                        password = action.password,
                        passwordError = null,
                    )
                }
            }

            is LoginAction.TogglePasswordVisibility -> {
                updateState { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }

            is LoginAction.LoginClicked -> loginUser(state.username, state.password)

            is LoginAction.Internal.ReceiveLoginResult -> handleLoginResult(action)

            is LoginAction.SignupClicked -> sendEvent(LoginEvent.NavigateToSignup)

            is LoginAction.NavigateToForgotPassword -> sendEvent(LoginEvent.NavigateToForgotPassword)

            is LoginAction.ErrorDialogDismiss -> {
                updateState { it.copy(dialogState = null) }
            }
        }
    }

    private fun handleLoginResult(action: LoginAction.Internal.ReceiveLoginResult) {
        when (action.loginResult) {
            is DataState.Error -> {
                updateState {
                    it.copy(
                        isError = true,
                        uiState = ScreenUiState.Success,
                        showOverlay = false,
                        dialogState = LoginState.DialogState.Error(action.loginResult.message),
                        userNameError = Res.string.feature_sign_in_username_error,
                        passwordError = Res.string.feature_sign_in_password_error,
                    )
                }
            }

            is DataState.Loading -> {
                updateState { it.copy(showOverlay = true) }
            }

            is DataState.Success -> {
                updateState { it.copy(showOverlay = false) }
                val user = action.loginResult.data
                val userData = UserData(
                    userId = user.userId,
                    userName = user.username.orEmpty(),
                    clientId = if (user.clients.isNotEmpty()) {
                        user.clients[0]
                    } else {
                        user.userId
                    },
                    isAuthenticated = user.isAuthenticated,
                    base64EncodedAuthenticationKey = user.base64EncodedAuthenticationKey.orEmpty(),
                    officeName = user.officeName.orEmpty(),
                    password = state.password,
                )
                viewModelScope.launch {
                    userPreferencesRepositoryImpl.updateUser(userData)
                    userPreferencesRepositoryImpl.setIsAuthenticated(true)
                }
                sendEvent(LoginEvent.NavigateToPasscode)
            }
        }
    }

    private fun loginUser(
        username: String,
        password: String,
    ) {
        loginJob?.cancel()

        updateState { it.copy(showOverlay = true) }

        loginJob = viewModelScope.launch {
            delay(300)

            val result = userAuthRepositoryImpl.login(username, password)
            sendAction(LoginAction.Internal.ReceiveLoginResult(result))
        }
    }
}

data class LoginState(
    val username: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val clientName: String = "",
    val isError: Boolean = false,
    val userNameError: StringResource? = null,
    val passwordError: StringResource? = null,
    val dialogState: DialogState? = null,
    val uiState: ScreenUiState?,
    val showOverlay: Boolean = false,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
    }

    val isLoginButtonEnabled: Boolean
        get() = username.isNotEmpty() && password.length >= 8
}

sealed interface LoginEvent {
    data object NavigateToSignup : LoginEvent
    data object NavigateToPasscode : LoginEvent
    data object NavigateToForgotPassword : LoginEvent
    data class ShowToast(val message: String) : LoginEvent
}

sealed interface LoginAction {
    data class UsernameChanged(val username: String) : LoginAction
    data class PasswordChanged(val password: String) : LoginAction
    data object TogglePasswordVisibility : LoginAction
    data object ErrorDialogDismiss : LoginAction
    data object LoginClicked : LoginAction
    data object SignupClicked : LoginAction
    data object NavigateToForgotPassword : LoginAction

    sealed class Internal : LoginAction {
        data class ReceiveLoginResult(
            val loginResult: DataState<User>,
        ) : Internal()
    }
}
