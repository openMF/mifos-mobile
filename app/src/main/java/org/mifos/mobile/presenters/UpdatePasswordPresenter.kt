package org.mifos.mobile.presenters

import android.content.Context

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import okhttp3.Credentials
import okhttp3.ResponseBody

import org.mifos.mobile.api.BaseApiManager.Companion.createService
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.injection.ApplicationContext
import org.mifos.mobile.models.UpdatePasswordPayload
import org.mifos.mobile.presenters.base.BasePresenter
import org.mifos.mobile.ui.views.UpdatePasswordView
import org.mifos.mobile.utils.MFErrorParser.errorMessage

import javax.inject.Inject

/*
* Created by saksham on 13/July/2018
*/
class UpdatePasswordPresenter @Inject constructor(
        @ApplicationContext context: Context?, private val dataManager: DataManager?,
        private val preferencesHelper: PreferencesHelper?
) : BasePresenter<UpdatePasswordView?>(context) {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun attachView(mvpView: UpdatePasswordView?) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    fun updateAccountPassword(payload: UpdatePasswordPayload?) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager?.updateAccountPassword(payload)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                    override fun onNext(responseBody: ResponseBody) {
                        mvpView?.hideProgress()
                        mvpView?.showPasswordUpdatedSuccessfully()
                        updateAuthenticationToken(payload?.password)
                    }

                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showError(errorMessage(e))
                    }

                    override fun onComplete() {}
                })?.let { compositeDisposable.add(it) }
    }

    fun updateAuthenticationToken(password: String?) {
        val authenticationToken = Credentials.basic(preferencesHelper?.userName, password)
        preferencesHelper?.saveToken(authenticationToken)
        createService(preferencesHelper?.baseUrl,
                preferencesHelper?.tenant,
                preferencesHelper?.token)
    }

}