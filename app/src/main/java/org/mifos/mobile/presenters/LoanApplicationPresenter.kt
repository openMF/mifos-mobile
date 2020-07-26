package org.mifos.mobile.presenters

import android.content.Context

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import org.mifos.mobile.R
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.injection.ApplicationContext
import org.mifos.mobile.models.templates.loans.LoanTemplate
import org.mifos.mobile.presenters.base.BasePresenter
import org.mifos.mobile.ui.enums.LoanState
import org.mifos.mobile.ui.views.LoanApplicationMvpView

import javax.inject.Inject

/**
 * Created by Rajan Maurya on 06/03/17.
 */
class LoanApplicationPresenter @Inject constructor(private val dataManager: DataManager, @ApplicationContext context: Context?) :
        BasePresenter<LoanApplicationMvpView?>(context) {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    override fun attachView(mvpView: LoanApplicationMvpView?) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    /**
     * Loads LoanApplicationTemplate from the server as [LoanTemplate] and notifies the view
     * depending upon the `loanState`. And in case of any error during fetching the required
     * details it notifies the view.
     *
     * @param loanState State of Loan i.e.  `LoanState.CREATE` or  `LoanState.UPDATE`
     */
    fun loadLoanApplicationTemplate(loanState: LoanState) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager.loanTemplate
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<LoanTemplate?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showError(context?.getString(R.string.error_fetching_template))
                    }

                    override fun onNext(loanTemplate: LoanTemplate) {
                        mvpView?.hideProgress()
                        if (loanState === LoanState.CREATE) {
                            mvpView?.showLoanTemplate(loanTemplate)
                        } else {
                            mvpView?.showUpdateLoanTemplate(loanTemplate)
                        }
                    }
                })?.let {
                    compositeDisposable.add(it
                    )
                }
    }

    /**
     * Loads LoanApplicationTemplate from the server as [LoanTemplate] and notifies the view
     * depending upon the `productId` and `loanState`. And in case of any error during
     * fetching the required details it notifies the view.
     *
     * @param productId ProductId required for Fetching loan template according to it.
     * @param loanState State of Loan i.e.  `LoanState.CREATE` or  `LoanState.UPDATE`
     */
    fun loadLoanApplicationTemplateByProduct(productId: Int?, loanState: LoanState?) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager.getLoanTemplateByProduct(productId)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<LoanTemplate?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showError(context?.getString(R.string.error_fetching_template))
                    }

                    override fun onNext(loanTemplate: LoanTemplate) {
                        mvpView?.hideProgress()
                        if (loanState === LoanState.CREATE) {
                            mvpView?.showLoanTemplateByProduct(loanTemplate)
                        } else {
                            mvpView?.showUpdateLoanTemplateByProduct(loanTemplate)
                        }
                    }
                })?.let {
                    compositeDisposable.add(it
                    )
                }
    }

}