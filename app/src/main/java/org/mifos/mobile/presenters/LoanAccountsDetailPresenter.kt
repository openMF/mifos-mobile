package org.mifos.mobile.presenters

import android.content.Context

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import org.mifos.mobile.R
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.injection.ApplicationContext
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.presenters.base.BasePresenter
import org.mifos.mobile.ui.views.LoanAccountsDetailView
import org.mifos.mobile.utils.Constants

import javax.inject.Inject

/**
 * @author Vishwajeet
 * @since 19/08/16
 */
class LoanAccountsDetailPresenter @Inject constructor(
        private val dataManager: DataManager,
        @ApplicationContext context: Context?
) : BasePresenter<LoanAccountsDetailView?>(context) {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    override fun attachView(mvpView: LoanAccountsDetailView?) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    /**
     * Load details of a particular loan account from the server and notify the view
     * to display it. Notify the view, in case there is any error in fetching
     * the details from server.
     *
     * @param loanId Id of Loan Account
     */
    fun loadLoanAccountDetails(loanId: Long?) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager.getLoanWithAssociations(Constants.REPAYMENT_SCHEDULE,
                loanId)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<LoanWithAssociations?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showErrorFetchingLoanAccountsDetail(
                                context?.getString(R.string.loan_account_details))
                    }

                    override fun onNext(loanWithAssociations: LoanWithAssociations) {
                        mvpView?.hideProgress()
                        mvpView?.showLoanAccountsDetail(loanWithAssociations)
                    }
                })?.let {
                    compositeDisposable.add(it
                    )
                }
    }

}