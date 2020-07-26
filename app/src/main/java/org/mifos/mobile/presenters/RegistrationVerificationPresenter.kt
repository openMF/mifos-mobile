package org.mifos.mobile.presenters

import android.content.Context

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import okhttp3.ResponseBody

import org.mifos.mobile.api.DataManager
import org.mifos.mobile.injection.ApplicationContext
import org.mifos.mobile.models.register.UserVerify
import org.mifos.mobile.presenters.base.BasePresenter
import org.mifos.mobile.ui.views.RegistrationVerificationView
import org.mifos.mobile.utils.MFErrorParser.errorMessage

import javax.inject.Inject

/**
 * Created by dilpreet on 31/7/17.
 */
class RegistrationVerificationPresenter @Inject constructor(
        private val dataManager: DataManager,
        @ApplicationContext context: Context
) : BasePresenter<RegistrationVerificationView?>(context) {

    private val compositeDisposables: CompositeDisposable = CompositeDisposable()
    override fun attachView(mvpView: RegistrationVerificationView?) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        compositeDisposables.clear()
    }

    fun verifyUser(userVerify: UserVerify?) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager.verifyUser(userVerify)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showError(errorMessage(e))
                    }

                    override fun onNext(responseBody: ResponseBody) {
                        mvpView?.hideProgress()
                        mvpView?.showVerifiedSuccessfully()
                    }
                })?.let { compositeDisposables.add(it) }
    }

}