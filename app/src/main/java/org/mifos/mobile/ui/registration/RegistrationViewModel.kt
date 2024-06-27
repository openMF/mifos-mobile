package org.mifos.mobile.ui.registration

import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.core.data.repositories.UserAuthRepository
import org.mifos.mobile.utils.RegistrationUiState
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val userAuthRepositoryImp: UserAuthRepository) :
    ViewModel() {

    private val _registrationUiState =
        MutableStateFlow<RegistrationUiState>(RegistrationUiState.Initial)
    val registrationUiState: StateFlow<RegistrationUiState> get() = _registrationUiState

    private val _registrationVerificationUiState =
        MutableStateFlow<RegistrationUiState>(RegistrationUiState.Initial)
    val registrationVerificationUiState: StateFlow<RegistrationUiState> get() = _registrationVerificationUiState

    fun isInputFieldBlank(fieldText: String): Boolean {
        return fieldText.trim().isEmpty()
    }

    fun isPhoneNumberNotValid(fieldText: String): Boolean{
        return fieldText.trim().toIntOrNull() == null
    }

    fun isInputLengthInadequate(fieldText: String): Boolean {
        return fieldText.trim().length < 6
    }

    fun inputHasSpaces(fieldText: String): Boolean {
        return fieldText.trim().contains(" ")
    }

    fun hasLeadingTrailingSpaces(fieldText: String): Boolean {
        return fieldText.trim().length < fieldText.length
    }

    fun isEmailInvalid(emailText: String): Boolean {
        return !PatternsCompat.EMAIL_ADDRESS.matcher(emailText.trim()).matches()
    }

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
            _registrationUiState.value = RegistrationUiState.Loading
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