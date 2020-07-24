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
import org.mifos.mobile.models.accounts.loan.LoanWithdraw
import org.mifos.mobile.presenters.base.BasePresenter
import org.mifos.mobile.ui.views.LoanAccountWithdrawView

import javax.inject.Inject

/**
 * Created by dilpreet on 7/6/17.
 */
class LoanAccountWithdrawPresenter @Inject constructor(
        private val dataManager: DataManager,
        @ApplicationContext context: Context?
) : BasePresenter<LoanAccountWithdrawView?>(context) {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    override fun attachView(mvpView: LoanAccountWithdrawView?) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    /**
     * Used for withdrawing a LoanAccount using the given `loanId` and notifies the view after
     * successful withdrawing of a LoanAccount. And in case of any error during withdrawing, it
     * notifies the view.
     *
     * @param loanId       Id of LoanAccount which you want to delete
     * @param loanWithdraw [LoanWithdraw] for the Withdrawing LoanAccount
     */
    fun withdrawLoanAccount(loanId: Long?, loanWithdraw: LoanWithdraw?) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager.withdrawLoanAccount(loanId, loanWithdraw)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showLoanAccountWithdrawError(
                                context.getString(R.string.error_loan_account_withdraw))
                    }

                    override fun onNext(responseBody: ResponseBody) {
                        mvpView?.hideProgress()
                        mvpView?.showLoanAccountWithdrawSuccess()
                    }
                })?.let { compositeDisposable.add(it) }
    }

}