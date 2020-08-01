package org.mifos.mobile.presenters

import android.content.Context

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.mifos.mobile.api.DataManager

import org.mifos.mobile.injection.ApplicationContext
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.presenters.base.BasePresenter
import org.mifos.mobile.ui.views.GuarantorListView

import javax.inject.Inject

/*
* Created by saksham on 24/July/2018
*/
class GuarantorListPresenter @Inject constructor(@ApplicationContext context: Context?, var dataManager: DataManager?) :
        BasePresenter<GuarantorListView?>(context) {

    var compositeDisposable: CompositeDisposable = CompositeDisposable()
    override fun attachView(mvpView: GuarantorListView?) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    fun getGuarantorList(loanId: Long) {
        mvpView?.showProgress()
        dataManager?.getGuarantorList(loanId)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<List<GuarantorPayload?>?>() {
                    override fun onNext(payload: List<GuarantorPayload?>) {
                        mvpView?.hideProgress()
                        mvpView?.showGuarantorListSuccessfully(payload)
                    }

                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showError(e.message)
                    }

                    override fun onComplete() {}
                })?.let { compositeDisposable.add(it) }
    }

}