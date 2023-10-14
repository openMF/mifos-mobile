package org.mifos.mobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _updatePasswordUiState = MutableLiveData<RegistrationUiState>()
    val updatePasswordUiState: LiveData<RegistrationUiState> get() = _updatePasswordUiState
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
            val response = userAuthRepositoryImp.updateAccountPassword(newPassword, confirmPassword)
            try {
                if (response?.isSuccessful == true) {
                    _updatePasswordUiState.value = RegistrationUiState.Success
                    clientRepositoryImp.updateAuthenticationToken(newPassword)
                } else {
                    _updatePasswordUiState.value = RegistrationUiState.Error(R.string.could_not_update_password_error)
                }
            } catch (e: Throwable) {
                _updatePasswordUiState.value = RegistrationUiState.Error(R.string.could_not_update_password_error)
            }
        }
    }
}