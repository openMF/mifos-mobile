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
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.register.RegisterPayload
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(var dataManager : DataManager?) : ViewModel() {
    private val compositeDisposables: CompositeDisposable = CompositeDisposable()

    private var registrationResultSuccess = MutableLiveData<Boolean>()
    val readRegistrationResultSuccess : LiveData<Boolean> get() = registrationResultSuccess
    private var exceptionOnRegistration : Throwable? = null
    val readExceptionOnRegistration : Throwable get() = exceptionOnRegistration!!

    fun isInputFieldBlank(fieldText : String) : Boolean {
        return fieldText.trim { it <= ' '}.isEmpty()
    }

    fun isInputLengthInadequate(fieldText : String) : Boolean {
        return fieldText.trim { it <= ' '}.length < 6
    }

    fun inputHasSpaces(fieldText: String) : Boolean {
        return fieldText.trim { it <= ' '}.contains(" ")
    }

    fun hasLeadingTrailingSpaces(fieldText: String) : Boolean {
        return fieldText.trim { it <= ' ' }.length < fieldText.length
    }

    fun isEmailInvalid(emailText : String) : Boolean {
        return !Patterns.EMAIL_ADDRESS.matcher(emailText.trim { it <= ' ' }).matches()
    }

    fun registerUser(registerPayload: RegisterPayload?) {
        dataManager?.registerUser(registerPayload)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    exceptionOnRegistration = e
                    registrationResultSuccess.value = false
                }

                override fun onNext(responseBody: ResponseBody) {
                    registrationResultSuccess.value = true
                }
            })?.let { compositeDisposables.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposables.clear()
    }
}