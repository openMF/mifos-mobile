package org.mifos.mobile.presenters

import android.content.Context

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import org.mifos.mobile.R
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.injection.ApplicationContext
import org.mifos.mobile.models.accounts.loan.LoanAccount
import org.mifos.mobile.models.accounts.savings.SavingAccount
import org.mifos.mobile.models.client.ClientAccounts
import org.mifos.mobile.presenters.base.BasePresenter
import org.mifos.mobile.ui.views.AccountOverviewMvpView

import javax.inject.Inject

/**
 * @author Rajan Maurya
 * On 16/10/17.
 */
class AccountOverviewPresenter @Inject constructor(
        @ApplicationContext context: Context,
        private val dataManager: DataManager
) : BasePresenter<AccountOverviewMvpView?>(context) {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    override fun attachView(mvpView: AccountOverviewMvpView?) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    /**
     * Fetches Client account details as [ClientAccounts] from the server and notifies the
     * view to display the [List] of [LoanAccount] and [SavingAccount]. And in
     * case of any error during fetching the required details it notifies the view.
     */
    fun loadClientAccountDetails() {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager.clientAccounts
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<ClientAccounts?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showError(context?.getString(R.string.error_fetching_accounts))
                    }

                    override fun onNext(clientAccounts: ClientAccounts) {
                        mvpView?.hideProgress()
                        mvpView?.showTotalLoanSavings(
                                getLoanAccountDetails(clientAccounts.loanAccounts),
                                getSavingAccountDetails(clientAccounts.savingsAccounts))
                    }
                })?.let {
                    compositeDisposable.add(it
                    )
                }
    }

    /**
     * Returns total Loan balance
     *
     * @param loanAccountList [List] of [LoanAccount] associated with the client
     * @return Returns `totalAmount` which is calculated by adding all [LoanAccount]
     * balance.
     */
    fun getLoanAccountDetails(loanAccountList: List<LoanAccount>?): Double {
        var totalAmount = 0.0
        if (loanAccountList != null)
            for ((_, _, _, _, _, _, _, _, _, _, _, _, _, _, loanBalance) in loanAccountList) {
                totalAmount += loanBalance
            }
        return totalAmount
    }

    /**
     * Returns total Savings balance
     *
     * @param savingAccountList [List] of [SavingAccount] associated with the client
     * @return Returns `totalAmount` which is calculated by adding all [SavingAccount]
     * balance.
     */
    fun getSavingAccountDetails(savingAccountList: List<SavingAccount>?): Double {
        var totalAmount = 0.0
        for ((_, _, _, _, _, accountBalance) in savingAccountList!!) {
            totalAmount += accountBalance
        }
        return totalAmount
    }

}