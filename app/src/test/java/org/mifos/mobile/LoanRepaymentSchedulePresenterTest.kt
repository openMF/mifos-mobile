package org.mifos.mobile

import android.content.Context

import io.reactivex.Observable

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.presenters.LoanRepaymentSchedulePresenter
import org.mifos.mobile.ui.views.LoanRepaymentScheduleMvpView
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.Constants

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by dilpreet on 27/6/17.
 */
@RunWith(MockitoJUnitRunner::class)
class LoanRepaymentSchedulePresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var view: LoanRepaymentScheduleMvpView? = null
    private var presenter: LoanRepaymentSchedulePresenter? = null
    private var loanWithRepaymentSchedule: LoanWithAssociations? = null
    private var loanWithEmptyRepaymentSchedule: LoanWithAssociations? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        presenter = LoanRepaymentSchedulePresenter(context, dataManager!!)
        presenter?.attachView(view)
        loanWithRepaymentSchedule = FakeRemoteDataSource.loanAccountRepaymentSchedule
        loanWithEmptyRepaymentSchedule = FakeRemoteDataSource.loanAccountEmptyRepaymentSchedule
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        presenter?.detachView()
    }

    @Test
    @Throws(Exception::class)
    fun testLoadLoanAccountDetails() {
        Mockito.`when`(dataManager?.getLoanWithAssociations(Constants.REPAYMENT_SCHEDULE, 29)).thenReturn(Observable.just(loanWithRepaymentSchedule))
        presenter?.loanLoanWithAssociations(29)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showLoanRepaymentSchedule(loanWithRepaymentSchedule)
        Mockito.verify(view, Mockito.never())?.showEmptyRepaymentsSchedule(null)
        Mockito.verify(view, Mockito.never())?.showError(context?.getString(R.string.error_fetching_repayment_schedule))
    }

    @Test
    @Throws(Exception::class)
    fun testLoadLoanAccountDetailsEmpty() {
        Mockito.`when`(dataManager?.getLoanWithAssociations(Constants.REPAYMENT_SCHEDULE, 29)).thenReturn(Observable.just(loanWithEmptyRepaymentSchedule))
        presenter?.loanLoanWithAssociations(29)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showEmptyRepaymentsSchedule(loanWithEmptyRepaymentSchedule)
        Mockito.verify(view, Mockito.never())?.showLoanRepaymentSchedule(null)
        Mockito.verify(view, Mockito.never())?.showError(context?.getString(R.string.error_fetching_repayment_schedule))
    }

    @Test
    @Throws(Exception::class)
    fun testLoadLoanAccountDetailsFails() {
        Mockito.`when`(dataManager?.getLoanWithAssociations(Constants.REPAYMENT_SCHEDULE, 29)).thenReturn(Observable.error(RuntimeException()))
        presenter?.loanLoanWithAssociations(29)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showError(context?.getString(R.string.error_fetching_repayment_schedule))
        Mockito.verify(view, Mockito.never())?.showEmptyRepaymentsSchedule(null)
        Mockito.verify(view, Mockito.never())?.showLoanRepaymentSchedule(null)
    }
}