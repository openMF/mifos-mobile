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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mifos_mobile.feature.update_password.generated.resources.Res
import mifos_mobile.feature.update_password.generated.resources.confirm_password_error_validation_blank
import mifos_mobile.feature.update_password.generated.resources.confirm_password_error_validation_minimum_chars
import mifos_mobile.feature.update_password.generated.resources.could_not_update_password_error
import mifos_mobile.feature.update_password.generated.resources.error_password_not_match
import mifos_mobile.feature.update_password.generated.resources.new_password_error_validation_blank
import mifos_mobile.feature.update_password.generated.resources.new_password_error_validation_minimum_chars
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.UserAuthRepository

internal class UpdatePasswordViewModel(
    private val userAuthRepositoryImp: UserAuthRepository,
//    private val clientRepositoryImp: ClientRepository,
) : ViewModel() {

    private val _updatePasswordUiState =
        MutableStateFlow<UpdatePasswordUiState>(UpdatePasswordUiState.Initial)
    val updatePasswordUiState = _updatePasswordUiState.asStateFlow()

    private fun updateAccountPassword(newPassword: String, confirmPassword: String) {
        viewModelScope.launch {
            when (userAuthRepositoryImp.updateAccountPassword(newPassword, confirmPassword)) {
                is DataState.Error<*> ->
                    _updatePasswordUiState.value =
                        UpdatePasswordUiState.Error(Res.string.could_not_update_password_error)

                DataState.Loading -> _updatePasswordUiState.value = UpdatePasswordUiState.Loading

                is DataState.Success<*> -> {
                    _updatePasswordUiState.value = UpdatePasswordUiState.Success
                    // TODO missing method in clientRepositoryImp
//                    clientRepositoryImp.updateAuthenticationToken(newPassword)}
                }
            }
        }
    }

    fun validateAndUpdatePassword(
        params: PasswordValidationParams,
    ) {
        with(params) {
            val newPasswordErrorContent = getPasswordError(newPassword, PasswordType.NEW)
            val confirmPasswordErrorContent = getPasswordError(confirmPassword, PasswordType.CONFIRM)

            setNewPasswordErrorContent(newPasswordErrorContent)
            setConfirmPasswordErrorContent(confirmPasswordErrorContent)

            when {
                newPasswordErrorContent == null && confirmPasswordErrorContent == null -> {
                    if (newPassword == confirmPassword) {
                        updateAccountPassword(newPassword, confirmPassword)
                    } else {
                        setPasswordDoesNotMatchOnBothError(Res.string.error_password_not_match)
                    }
                }

                newPasswordErrorContent == null && confirmPasswordErrorContent != null -> {
                    setConfirmPasswordError(true)
                }

                newPasswordErrorContent != null && confirmPasswordErrorContent == null -> {
                    setNewPasswordError(true)
                }

                else -> {
                    setNewPasswordError(true)
                    setConfirmPasswordError(true)
                }
            }
        }
    }

    private enum class PasswordType {
        NEW,
        CONFIRM,
    }

    private fun getPasswordError(
        password: String,
        type: PasswordType,
    ): StringResource? {
        return when {
            password.isEmpty() -> when (type) {
                PasswordType.NEW -> Res.string.new_password_error_validation_blank
                PasswordType.CONFIRM -> Res.string.confirm_password_error_validation_blank
            }

            password.length < 6 -> when (type) {
                PasswordType.NEW -> Res.string.new_password_error_validation_minimum_chars
                PasswordType.CONFIRM -> Res.string.confirm_password_error_validation_minimum_chars
            }

            else -> null
        }
    }
}

internal sealed class UpdatePasswordUiState {
    data class Error(val exception: StringResource) : UpdatePasswordUiState()
    data object Success : UpdatePasswordUiState()
    data object Loading : UpdatePasswordUiState()
    data object Initial : UpdatePasswordUiState()
}
