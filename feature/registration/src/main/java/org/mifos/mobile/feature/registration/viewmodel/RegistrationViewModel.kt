package org.mifos.mobile.feature.registration.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.feature.registration.R
import org.mifos.mobile.core.data.repositories.UserAuthRepository
import org.mifos.mobile.feature.registration.utils.RegistrationState
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val userAuthRepositoryImp: UserAuthRepository) :
    ViewModel() {

    private val _registrationUiState =
        MutableStateFlow<RegistrationState>(RegistrationState.Initial)
    val registrationUiState: StateFlow<RegistrationState> get() = _registrationUiState

    private val _registrationVerificationUiState =
        MutableStateFlow<RegistrationState>(RegistrationState.Initial)
    val registrationVerificationUiState: StateFlow<RegistrationState> get() = _registrationVerificationUiState

    fun registerUser(
        accountNumber: String,
        authenticationMode: String,
        email: String,
        firstName: String,
        lastName: String,
        mobileNumber: String,
        password: String,
        username: String
    ) {
        viewModelScope.launch {
            _registrationUiState.value = RegistrationState.Loading
            userAuthRepositoryImp.registerUser(
                accountNumber,
                authenticationMode,
                email,
                firstName,
                lastName,
                mobileNumber,
                password,
                username
            ).catch {
                _registrationUiState.value =
                    RegistrationState.Error(R.string.could_not_register_user_error)
            }.collect {
                _registrationUiState.value = RegistrationState.Success
            }
        }
    }

    fun verifyUser(authenticationToken: String?, requestId: String?) {
        viewModelScope.launch {
            _registrationVerificationUiState.value = RegistrationState.Loading
            userAuthRepositoryImp.verifyUser(authenticationToken, requestId).catch {
                _registrationVerificationUiState.value =
                    RegistrationState.Error(R.string.could_not_register_user_error)
            }.collect {
                _registrationVerificationUiState.value =
                    RegistrationState.Success
            }
        }
    }
}