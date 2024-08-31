/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.auth.registration.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.core.data.repository.UserAuthRepository
import org.mifos.mobile.feature.auth.R
import javax.inject.Inject

@HiltViewModel
internal class RegistrationViewModel @Inject constructor(
    private val userAuthRepositoryImp: UserAuthRepository,
) : ViewModel() {

    private val _registrationUiState =
        MutableStateFlow<RegistrationUiState>(RegistrationUiState.Initial)
    val registrationUiState: StateFlow<RegistrationUiState> get() = _registrationUiState

    private val _registrationVerificationUiState =
        MutableStateFlow<RegistrationUiState>(RegistrationUiState.Initial)
    val registrationVerificationUiState: StateFlow<RegistrationUiState> get() = _registrationVerificationUiState

    fun registerUser(
        accountNumber: String,
        authenticationMode: String,
        email: String,
        firstName: String,
        lastName: String,
        countryCode: String,
        mobileNumber: String,
        password: String,
        username: String,
    ) {
        viewModelScope.launch {
            _registrationUiState.value = RegistrationUiState.Loading
            userAuthRepositoryImp.registerUser(
                accountNumber = accountNumber,
                authenticationMode = authenticationMode,
                email = email,
                firstName = firstName,
                lastName = lastName,
                mobileNumber = countryCode + mobileNumber,
                password = password,
                username = username,
            ).catch {
                _registrationUiState.value =
                    RegistrationUiState.Error(R.string.could_not_register_user_error)
            }.collect {
                _registrationUiState.value = RegistrationUiState.Success
            }
        }
    }

    fun verifyUser(authenticationToken: String?, requestId: String?) {
        viewModelScope.launch {
            _registrationVerificationUiState.value = RegistrationUiState.Loading
            userAuthRepositoryImp.verifyUser(authenticationToken, requestId).catch {
                _registrationVerificationUiState.value =
                    RegistrationUiState.Error(R.string.could_not_register_user_error)
            }.collect {
                _registrationVerificationUiState.value =
                    RegistrationUiState.Success
            }
        }
    }
}

internal sealed class RegistrationUiState {
    data class Error(val exception: Int) : RegistrationUiState()
    data object Success : RegistrationUiState()
    data object Loading : RegistrationUiState()
    data object Initial : RegistrationUiState()
}
