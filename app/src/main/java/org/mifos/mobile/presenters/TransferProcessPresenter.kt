package org.mifos.mobile.presenters

import android.content.Context

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import okhttp3.ResponseBody

import org.mifos.mobile.R
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.injection.ApplicationContext
import org.mifos.mobile.models.payload.TransferPayload
import org.mifos.mobile.presenters.base.BasePresenter
import org.mifos.mobile.ui.views.TransferProcessView
import org.mifos.mobile.utils.MFErrorParser.errorMessage

import javax.inject.Inject

/**
 * Created by dilpreet on 1/7/17.
 */
class TransferProcessPresenter @Inject constructor(
        val dataManager: DataManager,
        @ApplicationContext context: Context
) : BasePresenter<TransferProcessView?>(context) {

    var compositeDisposables: CompositeDisposable = CompositeDisposable()
    override fun attachView(mvpView: TransferProcessView?) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        compositeDisposables.clear()
    }

    /**
     * Used for making a Transfer with the help of `transferPayload` provided in function
     * parameter. It notifies the view after successful making a Transfer. And in case of any error
     * during transfer, it notifies the view.
     *
     * @param transferPayload Contains details about the Transfer
     */
    fun makeSavingsTransfer(transferPayload: TransferPayload?) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager.makeTransfer(transferPayload)
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
                        mvpView?.showTransferredSuccessfully()
                    }
                })?.let {
                    compositeDisposables.add(it
                    )
                }
    }

    /**
     * Used for making a Third Party Transfer with the help of `transferPayload` provided in
     * function parameter. It notifies the view after successful making a Third Party Transfer. And
     * in case of any error during transfer, it notifies the view.
     *
     * @param transferPayload Contains details about the Third Party Transfer
     */
    fun makeTPTTransfer(transferPayload: TransferPayload?) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager.makeThirdPartyTransfer(transferPayload)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showError(context?.getString(R.string.transfer_error))
                    }

                    override fun onNext(responseBody: ResponseBody) {
                        mvpView?.hideProgress()
                        mvpView?.showTransferredSuccessfully()
                    }
                })?.let {
                    compositeDisposables.add(it
                    )
                }
    }

}