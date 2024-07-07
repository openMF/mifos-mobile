package org.mifos.mobile.feature.update_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.core.data.repositories.ClientRepository
import org.mifos.mobile.core.data.repositories.UserAuthRepository
import org.mifos.mobile.feature.update.password.R
import javax.inject.Inject

@HiltViewModel
class UpdatePasswordViewModel @Inject constructor(
    private val userAuthRepositoryImp: UserAuthRepository,
    private val clientRepositoryImp: ClientRepository,
) : ViewModel() {

    private val _updatePasswordUiState = MutableStateFlow<UpdatePasswordUiState>(UpdatePasswordUiState.Initial)
    val updatePasswordUiState: StateFlow<UpdatePasswordUiState> get() = _updatePasswordUiState

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


sealed class UpdatePasswordUiState {
    data class Error(val exception: Int) : UpdatePasswordUiState()
    data object Success : UpdatePasswordUiState()
    data object Loading : UpdatePasswordUiState()
    data object Initial: UpdatePasswordUiState()
}