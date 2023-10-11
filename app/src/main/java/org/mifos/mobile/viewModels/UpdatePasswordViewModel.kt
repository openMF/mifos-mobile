package org.mifos.mobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
            response?.code()?.compareTo(400)?.let {
                if (it >= 0) {
                    _updatePasswordUiState.value = RegistrationUiState.Error("")
                }
            }
            try {
                if (response?.isSuccessful == true) {
                    _updatePasswordUiState.value = RegistrationUiState.Success
                    clientRepositoryImp.updateAuthenticationToken(newPassword)
                } else {
                    _updatePasswordUiState.value =
                        response?.errorBody()?.string()?.let { RegistrationUiState.Error(it) }
                }
            } catch (e: Throwable) {
                _updatePasswordUiState.value =
                    response?.errorBody()?.string()?.let { RegistrationUiState.Error(it) }
            }
        }
    }

}