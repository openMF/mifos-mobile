package org.mifos.mobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.repositories.ClientRepository
import org.mifos.mobile.repositories.UserAuthRepository
import org.mifos.mobile.utils.RegistrationUiState
import javax.inject.Inject

@HiltViewModel
class UpdatePasswordViewModel @Inject constructor(
    private val userAuthRepositoryImp: UserAuthRepository,
    private val clientRepositoryImp: ClientRepository
) : ViewModel() {

    private val _updatePasswordUiState =
        MutableStateFlow<RegistrationUiState>(RegistrationUiState.Initial)
    val updatePasswordUiState: StateFlow<RegistrationUiState> get() = _updatePasswordUiState
    fun isInputFieldEmpty(fieldText: String): Boolean {
        return fieldText.isEmpty()
    }

    fun isInputLengthInadequate(fieldText: String): Boolean {
        return fieldText.length < 6
    }

    fun validatePasswordMatch(newPassword: String, confirmPassword: String): Boolean {
        return newPassword == confirmPassword
    }

    fun updateAccountPassword(newPassword: String, confirmPassword: String) {
        viewModelScope.launch {
            _updatePasswordUiState.value = RegistrationUiState.Loading
            userAuthRepositoryImp.updateAccountPassword(newPassword, confirmPassword).catch {
                _updatePasswordUiState.value =
                    RegistrationUiState.Error(R.string.could_not_update_password_error)
            }.collect {
                _updatePasswordUiState.value = RegistrationUiState.Success
                clientRepositoryImp.updateAuthenticationToken(newPassword)
            }
        }
    }
}