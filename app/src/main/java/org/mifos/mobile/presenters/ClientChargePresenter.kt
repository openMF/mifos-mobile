package org.mifos.mobile.presenters

import android.content.Context

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import org.mifos.mobile.R
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.injection.ActivityContext
import org.mifos.mobile.models.Charge
import org.mifos.mobile.models.Page
import org.mifos.mobile.presenters.base.BasePresenter
import org.mifos.mobile.ui.views.ClientChargeView

import javax.inject.Inject

/**
 * @author Vishwajeet
 * @since 17/8/16.
 */
class ClientChargePresenter @Inject constructor(private val dataManager: DataManager?, @ActivityContext context: Context?) :
        BasePresenter<ClientChargeView?>(context) {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    override fun attachView(mvpView: ClientChargeView?) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    fun loadClientCharges(clientId: Long) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager?.getClientCharges(clientId)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<Page<Charge?>?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showErrorFetchingClientCharges(
                                context?.getString(R.string.client_charges))
                    }

                    override fun onNext(chargePage: Page<Charge?>) {
                        mvpView?.hideProgress()
                        mvpView?.showClientCharges(chargePage.pageItems)
                    }
                })?.let {
                    compositeDisposable.add(it
                    )
                }
    }

    fun loadLoanAccountCharges(loanId: Long) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager?.getLoanCharges(loanId)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<List<Charge?>?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showErrorFetchingClientCharges(
                                context?.getString(R.string.client_charges))
                    }

                    override fun onNext(chargeList: List<Charge?>) {
                        mvpView?.hideProgress()
                        mvpView?.showClientCharges(chargeList)
                    }
                })?.let {
                    compositeDisposable.add(it
                    )
                }
    }

    fun loadSavingsAccountCharges(savingsId: Long) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager?.getSavingsCharges(savingsId)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<List<Charge?>?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showErrorFetchingClientCharges(
                                context?.getString(R.string.client_charges))
                    }

                    override fun onNext(chargeList: List<Charge?>) {
                        mvpView?.hideProgress()
                        mvpView?.showClientCharges(chargeList)
                    }
                })?.let {
                    compositeDisposable.add(it
                    )
                }
    }

    fun loadClientLocalCharges() {
        checkViewAttached()
        dataManager?.clientLocalCharges
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<Page<Charge?>?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.showErrorFetchingClientCharges(
                                context?.getString(R.string.client_charges))
                    }

                    override fun onNext(chargePage: Page<Charge?>) {
                        mvpView?.showClientCharges(chargePage.pageItems)
                    }
                })?.let {
                    compositeDisposable.add(it
                    )
                }
    }

}