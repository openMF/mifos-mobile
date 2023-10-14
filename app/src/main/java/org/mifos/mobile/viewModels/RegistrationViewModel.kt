package org.mifos.mobile.viewModels

import androidx.core.util.PatternsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.repositories.UserAuthRepository
import org.mifos.mobile.utils.RegistrationUiState
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val userAuthRepositoryImp: UserAuthRepository) :
    ViewModel() {

    private val _registrationUiState = MutableLiveData<RegistrationUiState>()
    val registrationUiState: LiveData<RegistrationUiState> get() = _registrationUiState

    private val _registrationVerificationUiState =
        MutableLiveData<RegistrationUiState>()
    val registrationVerificationUiState: LiveData<RegistrationUiState> get() = _registrationVerificationUiState

    fun isInputFieldBlank(fieldText: String): Boolean {
        return fieldText.trim().isEmpty()
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
            val response = userAuthRepositoryImp.registerUser(
                accountNumber,
                authenticationMode,
                email,
                firstName,
                lastName,
                mobileNumber,
                password,
                username
            )
            if (response?.isSuccessful == true) {
                _registrationUiState.value = RegistrationUiState.Success
            } else {
                _registrationUiState.value = RegistrationUiState.Error(R.string.could_not_register_user_error)
            }
        }
    }

    fun verifyUser(authenticationToken: String?, requestId: String?) {
        viewModelScope.launch {
            _registrationVerificationUiState.value = RegistrationUiState.Loading
            val response = userAuthRepositoryImp.verifyUser(authenticationToken, requestId)
            if (response?.isSuccessful == true) {
                _registrationVerificationUiState.value =
                    RegistrationUiState.Success
            } else {
                _registrationVerificationUiState.value = RegistrationUiState.Error(R.string.could_not_register_user_error)
            }
        }
    }
}