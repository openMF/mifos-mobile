package org.mifos.mobile.viewModels

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.mifos.mobile.RegistrationRepository
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.register.RegisterPayload
import org.mifos.mobile.utils.RegistrationUiState
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(var dataManager : DataManager?) : ViewModel() {
    private val compositeDisposables: CompositeDisposable = CompositeDisposable()

    private val registrationRepository = RegistrationRepository(dataManager)
    private val _registrationUiState = MutableLiveData<RegistrationUiState>()
    val registrationUiState : LiveData<RegistrationUiState> get() = _registrationUiState

    fun isInputFieldBlank(fieldText : String) : Boolean {
        return fieldText.trim().isEmpty()
    }

    fun isInputLengthInadequate(fieldText : String) : Boolean {
        return fieldText.trim().length < 6
    }

    fun inputHasSpaces(fieldText: String) : Boolean {
        return fieldText.trim().contains(" ")
    }

    fun hasLeadingTrailingSpaces(fieldText: String) : Boolean {
        return fieldText.trim().length < fieldText.length
    }

    fun isEmailInvalid(emailText : String) : Boolean {
        return !Patterns.EMAIL_ADDRESS.matcher(emailText.trim()).matches()
    }

    fun createRegisterPayload(
        accountNumber: String,
        authenticationMode: String,
        email: String,
        firstName: String,
        lastName: String,
        mobileNumber: String,
        password: String,
        username: String
    ) : RegisterPayload {
        return registrationRepository.createRegisterPayload(accountNumber, authenticationMode, email, firstName,
        lastName, mobileNumber, password, username)
    }

    fun registerUser(registerPayload: RegisterPayload?) {
        _registrationUiState.value = RegistrationUiState.Loading
        registrationRepository.registerUser(registerPayload)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    _registrationUiState.value = RegistrationUiState.ErrorOnRegistration(e)
                }

                override fun onNext(responseBody: ResponseBody) {
                    _registrationUiState.value = RegistrationUiState.RegistrationSuccessful
                }
            })?.let { compositeDisposables.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposables.clear()
    }
}