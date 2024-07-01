package org.mifos.mobile.ui.update_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.core.data.repositories.ClientRepository
import org.mifos.mobile.core.data.repositories.UserAuthRepository
import org.mifos.mobile.feature.registration.utils.RegistrationState
import javax.inject.Inject

@HiltViewModel
class UpdatePasswordViewModel @Inject constructor(
    private val userAuthRepositoryImp: UserAuthRepository,
    private val clientRepositoryImp: ClientRepository,
) : ViewModel() {

    private val _updatePasswordUiState = MutableStateFlow<RegistrationState>(RegistrationState.Initial)
    val updatePasswordUiState: StateFlow<RegistrationState> get() = _updatePasswordUiState

    fun updateAccountPassword(newPassword: String, confirmPassword: String) {
        viewModelScope.launch {
            _updatePasswordUiState.value = RegistrationState.Loading
            userAuthRepositoryImp.updateAccountPassword(newPassword, confirmPassword).catch {
                _updatePasswordUiState.value =
                    RegistrationState.Error(R.string.could_not_update_password_error)
            }.collect {
                _updatePasswordUiState.value = RegistrationState.Success
                clientRepositoryImp.updateAuthenticationToken(newPassword)
            }
        }
    }
}
