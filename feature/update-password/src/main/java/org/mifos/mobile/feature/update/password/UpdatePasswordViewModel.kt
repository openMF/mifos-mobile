/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.update.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.core.data.repository.ClientRepository
import org.mifos.mobile.core.data.repository.UserAuthRepository
import org.mifos.mobile.feature.update.password.UpdatePasswordUiState.Initial
import javax.inject.Inject

@HiltViewModel
internal class UpdatePasswordViewModel @Inject constructor(
    private val userAuthRepositoryImp: UserAuthRepository,
    private val clientRepositoryImp: ClientRepository,
) : ViewModel() {

    private val _updatePasswordUiState = MutableStateFlow<UpdatePasswordUiState>(Initial)
    val updatePasswordUiState = _updatePasswordUiState.asStateFlow()

    fun updateAccountPassword(newPassword: String, confirmPassword: String) {
        viewModelScope.launch {
            _updatePasswordUiState.value = UpdatePasswordUiState.Loading
            userAuthRepositoryImp.updateAccountPassword(newPassword, confirmPassword).catch {
                _updatePasswordUiState.value =
                    UpdatePasswordUiState.Error(R.string.could_not_update_password_error)
            }.collect {
                _updatePasswordUiState.value = UpdatePasswordUiState.Success
                clientRepositoryImp.updateAuthenticationToken(newPassword)
            }
        }
    }
}

internal sealed class UpdatePasswordUiState {
    data class Error(val exception: Int) : UpdatePasswordUiState()
    data object Success : UpdatePasswordUiState()
    data object Loading : UpdatePasswordUiState()
    data object Initial : UpdatePasswordUiState()
}
