package org.mifos.mobile.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.Credentials
import okhttp3.ResponseBody
import org.mifos.mobile.api.BaseApiManager
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.models.UpdatePasswordPayload
import org.mifos.mobile.utils.MFErrorParser
import javax.inject.Inject

class UpdatePasswordViewModel @Inject constructor(
    private val dataManager: DataManager?,
    private val preferencesHelper: PreferencesHelper?
) : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val passwordUpdateStatus: MutableLiveData<Boolean> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun updateAccountPassword(payload: UpdatePasswordPayload?) {
        dataManager?.updateAccountPassword(payload)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                override fun onNext(responseBody: ResponseBody) {
                    passwordUpdateStatus.value = true
                    updateAuthenticationToken(payload?.password)
                }

                override fun onError(e: Throwable) {
                    passwordUpdateStatus.value = false
                    errorMessage.value = MFErrorParser.errorMessage(e)
                }

                override fun onComplete() {}
            })?.let { compositeDisposable.add(it) }
    }

    fun updateAuthenticationToken(password: String?) {
        val authenticationToken = Credentials.basic(preferencesHelper?.userName, password)
        preferencesHelper?.saveToken(authenticationToken)
        BaseApiManager.createService(
            preferencesHelper?.baseUrl,
            preferencesHelper?.tenant,
            preferencesHelper?.token,
        )
    }
}