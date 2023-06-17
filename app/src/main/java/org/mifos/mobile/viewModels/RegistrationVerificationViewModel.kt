package org.mifos.mobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.register.UserVerify
import javax.inject.Inject

class RegistrationVerificationViewModel @Inject constructor(private val dataManager : DataManager?) : ViewModel() {

    private val compositeDisposables : CompositeDisposable = CompositeDisposable()

    private var verificationResultSuccess = MutableLiveData<Boolean>()
    val readVerificationResultSuccess : LiveData<Boolean> get() = verificationResultSuccess

    private var exceptionOnVerification : Throwable? = null
    val readExceptionOnVerification : Throwable? get() = exceptionOnVerification!!

    fun verifyUser(userVerify: UserVerify?) {
        dataManager?.verifyUser(userVerify)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    exceptionOnVerification = e
                    verificationResultSuccess.value = false
                }

                override fun onNext(responseBody: ResponseBody) {
                    verificationResultSuccess.value = true
                }
            })?.let { compositeDisposables.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposables.clear()
    }
}