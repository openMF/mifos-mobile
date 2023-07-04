package org.mifos.mobile.viewModels

import androidx.core.util.PatternsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.mifos.mobile.repositories.UserAuthRepository
import org.mifos.mobile.utils.RegistrationUiState
import org.mifos.mobile.utils.RegistrationVerificationUiState
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val userAuthRepositoryImp: UserAuthRepository) :
    ViewModel() {
    private val compositeDisposables: CompositeDisposable = CompositeDisposable()

    private val _registrationUiState = MutableLiveData<RegistrationUiState>()
    val registrationUiState: LiveData<RegistrationUiState> get() = _registrationUiState

    private val _registrationVerificationUiState =
        MutableLiveData<RegistrationVerificationUiState>()
    val registrationVerificationUiState: LiveData<RegistrationVerificationUiState> get() = _registrationVerificationUiState

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
        )?.observeOn(AndroidSchedulers.mainThread())?.subscribeOn(Schedulers.io())
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

    fun verifyUser(authenticationToken: String?, requestId: String?) {
        _registrationVerificationUiState.value = RegistrationVerificationUiState.Loading
        userAuthRepositoryImp.verifyUser(authenticationToken, requestId)?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    _registrationVerificationUiState.value =
                        RegistrationVerificationUiState.ErrorOnRegistrationVerification(e)
                }

                override fun onNext(responseBody: ResponseBody) {
                    _registrationVerificationUiState.value =
                        RegistrationVerificationUiState.RegistrationVerificationSuccessful
                }
            })?.let { compositeDisposables.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposables.clear()
    }
}