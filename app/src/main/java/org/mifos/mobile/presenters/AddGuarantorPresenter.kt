package org.mifos.mobile.presenters

import android.content.Context
import android.util.Log

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import okhttp3.ResponseBody

import org.mifos.mobile.api.DataManager
import org.mifos.mobile.injection.ActivityContext
import org.mifos.mobile.models.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.models.guarantor.GuarantorTemplatePayload
import org.mifos.mobile.presenters.base.BasePresenter
import org.mifos.mobile.ui.enums.GuarantorState
import org.mifos.mobile.ui.views.AddGuarantorView

import java.io.IOException
import javax.inject.Inject

/*
* Created by saksham on 23/July/2018
*/
class AddGuarantorPresenter @Inject constructor(@ActivityContext context: Context?, var dataManager: DataManager?) :
        BasePresenter<AddGuarantorView?>(context) {

    var compositeDisposable: CompositeDisposable = CompositeDisposable()
    override fun attachView(mvpView: AddGuarantorView?) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    fun getGuarantorTemplate(state: GuarantorState?, loanId: Long?) {
        mvpView?.showProgress()
        dataManager?.getGuarantorTemplate(loanId)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<GuarantorTemplatePayload?>() {
                    override fun onNext(payload: GuarantorTemplatePayload) {
                        mvpView?.hideProgress()
                        if (state === GuarantorState.CREATE) {
                            mvpView?.showGuarantorApplication(payload)
                        } else if (state === GuarantorState.UPDATE) {
                            mvpView?.showGuarantorUpdation(payload)
                        }
                    }

                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                    }

                    override fun onComplete() {}
                })?.let { compositeDisposable.add(it) }
    }

    fun createGuarantor(loanId: Long?, payload: GuarantorApplicationPayload?) {
        mvpView?.showProgress()
        dataManager?.createGuarantor(loanId, payload)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                    override fun onNext(responseBody: ResponseBody) {
                        mvpView?.hideProgress()
                        try {
                            mvpView?.submittedSuccessfully(responseBody.string(), payload)
                        } catch (e: IOException) {
                            Log.d(TAG, e.message)
                        }
                    }

                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                    }

                    override fun onComplete() {}
                })?.let { compositeDisposable.add(it) }
    }

    fun updateGuarantor(
            payload: GuarantorApplicationPayload?, loanId: Long?,
            guarantorId: Long?
    ) {
        mvpView?.showProgress()
        dataManager?.updateGuarantor(payload, loanId, guarantorId)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                    override fun onNext(responseBody: ResponseBody) {
                        mvpView?.hideProgress()
                        try {
                            mvpView?.updatedSuccessfully(responseBody.string())
                        } catch (e: IOException) {
                            Log.d(TAG, e.message)
                        }
                    }

                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showError(e.message)
                    }

                    override fun onComplete() {}
                })?.let { compositeDisposable.add(it) }
    }

    companion object {
        val TAG: String? = AddGuarantorPresenter::class.java.simpleName
    }

}