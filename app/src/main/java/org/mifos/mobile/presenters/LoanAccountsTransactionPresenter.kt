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
import org.mifos.mobile.ui.views.LoanAccountsTransactionView
import org.mifos.mobile.utils.Constants

import javax.inject.Inject

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/ /**
 * Created by dilpreet on 4/3/17.
 */
class LoanAccountsTransactionPresenter @Inject constructor(
        private val dataManager: DataManager,
        @ApplicationContext context: Context?
) : BasePresenter<LoanAccountsTransactionView?>(context) {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    override fun attachView(mvpView: LoanAccountsTransactionView?) {
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
        dataManager.getLoanWithAssociations(Constants.TRANSACTIONS, loanId)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<LoanWithAssociations?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showErrorFetchingLoanAccountsDetail(
                                context?.getString(R.string.loan_transaction_details))
                    }

                    override fun onNext(loanWithAssociations: LoanWithAssociations) {
                        mvpView?.hideProgress()
                        if (loanWithAssociations.transactions != null &&
                                loanWithAssociations.transactions?.isNotEmpty() == true) {
                            mvpView?.showLoanTransactions(loanWithAssociations)
                        } else {
                            mvpView?.showEmptyTransactions(loanWithAssociations)
                        }
                    }
                })?.let {
                    compositeDisposable.add(it
                    )
                }
    }

}