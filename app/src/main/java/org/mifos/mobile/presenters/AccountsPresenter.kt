package org.mifos.mobile.presenters

import android.content.Context

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Predicate
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import org.mifos.mobile.R
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.injection.ApplicationContext
import org.mifos.mobile.models.CheckboxStatus
import org.mifos.mobile.models.accounts.loan.LoanAccount
import org.mifos.mobile.models.accounts.savings.SavingAccount
import org.mifos.mobile.models.accounts.share.ShareAccount
import org.mifos.mobile.models.client.ClientAccounts
import org.mifos.mobile.presenters.base.BasePresenter
import org.mifos.mobile.ui.views.AccountsView
import org.mifos.mobile.utils.Constants
import java.util.*

import javax.inject.Inject

/**
 * Created by Rajan Maurya on 23/10/16.
 */
class AccountsPresenter @Inject constructor(@ApplicationContext context: Context, private val dataManager: DataManager) :
        BasePresenter<AccountsView?>(context) {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    override fun attachView(mvpView: AccountsView?) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    /**
     * Loads savings, loan and share accounts associated with the Client from the server
     * and notifies the view to display it. And in case of any error during fetching the required
     * details it notifies the view.
     */
    fun loadClientAccounts() {
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
                        mvpView?.showSavingsAccounts(clientAccounts.savingsAccounts)
                        mvpView?.showLoanAccounts(clientAccounts.loanAccounts)
                        mvpView?.showShareAccounts(clientAccounts.shareAccounts)
                    }
                })?.let {
                    compositeDisposable.add(it
                    )
                }
    }

    /**
     * Loads savings, loan or share account depending upon `accountType` provided from the
     * server and notifies the view to display it.And in case of any error during fetching the
     * required details it notifies the view.
     * @param accountType Type of account for which we need to fetch details
     */
    fun loadAccounts(accountType: String?) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager.getAccounts(accountType)
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
                        when (accountType) {
                            Constants.SAVINGS_ACCOUNTS -> mvpView?.showSavingsAccounts(
                                    clientAccounts.savingsAccounts)
                            Constants.LOAN_ACCOUNTS -> mvpView?.showLoanAccounts(clientAccounts.loanAccounts)
                            Constants.SHARE_ACCOUNTS -> mvpView?.showShareAccounts(clientAccounts.shareAccounts)
                        }
                    }
                })?.let {
                    compositeDisposable.add(it
                    )
                }
    }

    /**
     * Filters [List] of [SavingAccount]
     * @param accounts [List] of [SavingAccount]
     * @param input [String] which is used for filtering
     * @return Returns [List] of filtered [SavingAccount] according to the `input`
     * provided.
     */
    fun searchInSavingsList(
            accounts: List<SavingAccount?>?,
            input: String?
    ): List<SavingAccount?> {
        return Observable.fromIterable(accounts)
                .filter { (accountNo, productName) ->
                    input?.toLowerCase(Locale.ROOT)?.let { productName?.toLowerCase(Locale.ROOT)?.contains(it) } == true ||
                            input?.toLowerCase(Locale.ROOT)?.let { accountNo?.toLowerCase(Locale.ROOT)?.contains(it) } == true
                }.toList().blockingGet()
    }

    /**
     * Filters [List] of [LoanAccount]
     * @param accounts [List] of [LoanAccount]
     * @param input [String] which is used for filtering
     * @return Returns [List] of filtered [LoanAccount] according to the `input`
     * provided.
     */
    fun searchInLoanList(
            accounts: List<LoanAccount?>?,
            input: String?
    ): List<LoanAccount?>? {
        return Observable.fromIterable(accounts)
                .filter { (_, _, _, accountNo, productName) ->
                    input?.toLowerCase(Locale.ROOT)?.let { productName?.toLowerCase(Locale.ROOT)?.contains(it) } == true ||
                            input?.toLowerCase(Locale.ROOT)?.let { accountNo?.toLowerCase(Locale.ROOT)?.contains(it) } == true
                }.toList().blockingGet()
    }

    /**
     * Filters [List] of [ShareAccount]
     * @param accounts [List] of [ShareAccount]
     * @param input [String] which is used for filtering
     * @return Returns [List] of filtered [ShareAccount] according to the `input`
     * provided.
     */
    fun searchInSharesList(
            accounts: Collection<ShareAccount?>?,
            input: String?
    ): List<ShareAccount?>? {
        return Observable.fromIterable(accounts)
                .filter { (accountNo, _, _, _, productName) ->
                    input?.toLowerCase(Locale.ROOT)?.let { productName?.toLowerCase(Locale.ROOT)?.contains(it) } == true ||
                            input?.toLowerCase(Locale.ROOT)?.let { accountNo?.toLowerCase(Locale.ROOT)?.contains(it) } == true
                }.toList().blockingGet()
    }

    /**
     * Filters [List] of [CheckboxStatus]
     * @param statusModelList [List] of [CheckboxStatus]
     * @return Returns [List] of [CheckboxStatus] which have
     * `checkboxStatus.isChecked()` as true.
     */
    fun getCheckedStatus(statusModelList: List<CheckboxStatus?>?): List<CheckboxStatus?>? {
        return Observable.fromIterable(statusModelList)
                .filter { (_, _, isChecked) -> isChecked }.toList().blockingGet()
    }

    /**
     * Filters [List] of [SavingAccount] according to [CheckboxStatus]
     * @param accounts [List] of filtered [SavingAccount]
     * @param status Used for filtering the [List]
     * @return Returns [List] of filtered [SavingAccount] according to the
     * `status` provided.
     */
    fun getFilteredSavingsAccount(
            accounts: List<SavingAccount?>?,
            status: CheckboxStatus?
    ): Collection<SavingAccount?>? {
        return Observable.fromIterable(accounts)
                .filter(Predicate { (_, _, _, _, _, _, _, _, _, _, _, status1) ->
                    if (context?.getString(R.string.active)?.let { status?.status?.compareTo(it) } == 0 && status1?.active == true) {
                        return@Predicate true
                    } else if (context?.getString(R.string.approved)?.let { status?.status?.compareTo(it) } == 0 && status1?.approved == true) {
                        return@Predicate true
                    } else if (context?.getString(R.string.approval_pending)?.let { status?.status?.compareTo(it) } == 0 && status1?.submittedAndPendingApproval == true) {
                        return@Predicate true
                    } else if (context?.getString(R.string.matured)?.let { status?.status?.compareTo(it) } == 0 && status1?.matured == true) {
                        return@Predicate true
                    } else if (context?.getString(R.string.closed)?.let { status?.status?.compareTo(it) } == 0 && status1?.closed == true) {
                        return@Predicate true
                    }
                    false
                }).toList().blockingGet()
    }

    /**
     * Filters [List] of [LoanAccount] according to [CheckboxStatus]
     * @param accounts [List] of filtered [LoanAccount]
     * @param status Used for filtering the [List]
     * @return Returns [List] of filtered [LoanAccount] according to the
     * `status` provided.
     */
    fun getFilteredLoanAccount(
            accounts: List<LoanAccount?>?,
            status: CheckboxStatus?
    ): Collection<LoanAccount?>? {
        return Observable.fromIterable(accounts)
                .filter(Predicate { (_, _, _, _, _, _, _, _, _, _, _, status1, _, _, _, _, _, inArrears) ->
                    if (context?.getString(R.string.in_arrears)?.let { status?.status?.compareTo(it) }
                            == 0 && inArrears == true) {
                        return@Predicate true
                    } else if (context?.getString(R.string.active)?.let { status?.status?.compareTo(it) } == 0 && status1?.active == true) {
                        return@Predicate true
                    } else if (context?.getString(R.string.waiting_for_disburse)?.let { status?.status?.compareTo(it) } == 0 && status1?.waitingForDisbursal == true) {
                        return@Predicate true
                    } else if (context?.getString(R.string.approval_pending)?.let { status?.status?.compareTo(it) } == 0 && status1?.pendingApproval == true) {
                        return@Predicate true
                    } else if (context?.getString(R.string.overpaid)?.let { status?.status?.compareTo(it) } == 0 && status1?.overpaid == true) {
                        return@Predicate true
                    } else if (context?.getString(R.string.closed)?.let { status?.status?.compareTo(it) } == 0 && status1?.closed == true) {
                        return@Predicate true
                    } else if (context?.getString(R.string.withdrawn)?.let { status?.status?.compareTo(it) } == 0 && status1?.isLoanTypeWithdrawn() == true) {
                        return@Predicate true
                    }
                    false
                }).toList().blockingGet()
    }

    /**
     * Filters [List] of [ShareAccount] according to [CheckboxStatus]
     * @param accounts [List] of filtered [ShareAccount]
     * @param status Used for filtering the [List]
     * @return Returns [List] of filtered [ShareAccount] according to the
     * `status` provided.
     */
    fun getFilteredShareAccount(
            accounts: List<ShareAccount?>?,
            status: CheckboxStatus?
    ): Collection<ShareAccount?>? {
        return Observable.fromIterable(accounts)
                .filter(Predicate { (_, _, _, _, _, _, status1) ->
                    if (context?.getString(R.string.active)?.let { status?.status?.compareTo(it) } == 0 &&
                            status1?.active == true) {
                        return@Predicate true
                    } else if (context?.getString(R.string.approved)?.let { status?.status?.compareTo(it) } == 0 && status1?.approved == true) {
                        return@Predicate true
                    } else if (context?.getString(R.string.approval_pending)?.let { status?.status?.compareTo(it) } == 0 && status1?.submittedAndPendingApproval == true) {
                        return@Predicate true
                    } else if (context?.getString(R.string.closed)?.let { status?.status?.compareTo(it) } == 0 && status1?.closed == true) {
                        return@Predicate true
                    }
                    false
                }).toList().blockingGet()
    }

}