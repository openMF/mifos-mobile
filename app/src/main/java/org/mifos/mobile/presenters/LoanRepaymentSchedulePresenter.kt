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
import org.mifos.mobile.ui.views.LoanRepaymentScheduleMvpView
import org.mifos.mobile.utils.Constants

import javax.inject.Inject

/**
 * Created by Rajan Maurya on 03/03/17.
 */
class LoanRepaymentSchedulePresenter @Inject constructor(
        @ApplicationContext context: Context?,
        private val dataManager: DataManager
) : BasePresenter<LoanRepaymentScheduleMvpView?>(context) {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    override fun attachView(mvpView: LoanRepaymentScheduleMvpView?) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    /**
     * Load details of a particular loan account with its Repayment Schedule as
     * [LoanWithAssociations] from the server and notify the view to display it. Notify the
     * view, in case there is any error in fetching the details from server.
     * @param loanId Id of Loan Account
     */
    fun loanLoanWithAssociations(loanId: Long?) {
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
                        mvpView?.showError(context
                                .getString(R.string.repayment_schedule))
                    }

                    override fun onNext(loanWithAssociations: LoanWithAssociations) {
                        mvpView?.hideProgress()
                        if (loanWithAssociations.repaymentSchedule?.periods?.isNotEmpty() == true) {
                            mvpView?.showLoanRepaymentSchedule(loanWithAssociations)
                        } else {
                            mvpView?.showEmptyRepaymentsSchedule(loanWithAssociations)
                        }
                    }
                })?.let {
                    compositeDisposable.add(it
                    )
                }
    }

}