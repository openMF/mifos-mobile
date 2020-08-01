package org.mifos.mobile.presenters

import android.content.Context
import android.util.Log

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import okhttp3.ResponseBody

import org.mifos.mobile.api.DataManager
import org.mifos.mobile.injection.ApplicationContext
import org.mifos.mobile.presenters.base.BasePresenter
import org.mifos.mobile.ui.views.GuarantorDetailView

import java.io.IOException
import javax.inject.Inject

/*
* Created by saksham on 25/July/2018
*/
class GuarantorDetailPresenter @Inject constructor(
        @ApplicationContext context: Context?,
        var dataManager: DataManager?
) : BasePresenter<GuarantorDetailView?>(context) {
    var compositeDisposable: CompositeDisposable = CompositeDisposable()
    override fun attachView(mvpView: GuarantorDetailView?) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    fun deleteGuarantor(loanId: Long?, guarantorId: Long?) {
        mvpView?.showProgress()
        dataManager?.deleteGuarantor(loanId, guarantorId)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                    override fun onNext(responseBody: ResponseBody) {
                        mvpView?.hideProgress()
                        try {
                            mvpView?.guarantorDeletedSuccessfully(responseBody.string())
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
        val TAG: String? = GuarantorDetailPresenter::class.java.simpleName
    }

}