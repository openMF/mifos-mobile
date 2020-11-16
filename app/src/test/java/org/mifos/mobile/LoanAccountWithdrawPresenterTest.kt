package org.mifos.mobile

import android.content.Context

import io.reactivex.Observable

import okhttp3.ResponseBody

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.accounts.loan.LoanWithdraw
import org.mifos.mobile.presenters.LoanAccountWithdrawPresenter
import org.mifos.mobile.ui.views.LoanAccountWithdrawView
import org.mifos.mobile.util.RxSchedulersOverrideRule

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by dilpreet on 24/7/17.
 */
@RunWith(MockitoJUnitRunner::class)
class LoanAccountWithdrawPresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var view: LoanAccountWithdrawView? = null

    @Mock
    var loanWithdraw: LoanWithdraw? = null

    @Mock
    var mockedResponseBody: ResponseBody? = null
    private var presenter: LoanAccountWithdrawPresenter? = null
    private val loanId: Long = 1

    @Before
    fun setUp() {
        presenter = LoanAccountWithdrawPresenter(dataManager!!, context)
        presenter?.attachView(view)
    }

    @After
    fun tearDown() {
        presenter?.detachView()
    }

    @Test
    fun testWithdrawLoanAccount() {
        Mockito.`when`(dataManager?.withdrawLoanAccount(loanId, loanWithdraw)).thenReturn(Observable.just(mockedResponseBody))
        presenter?.withdrawLoanAccount(loanId, loanWithdraw)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showLoanAccountWithdrawSuccess()
        Mockito.verify(view, Mockito.never())?.showLoanAccountWithdrawError(context?.getString(R.string.error_loan_account_withdraw))
    }

    @Test
    fun testWithdrawLoanAccountFail() {
        Mockito.`when`(dataManager?.withdrawLoanAccount(loanId, loanWithdraw)).thenReturn(Observable.error(RuntimeException()))
        presenter?.withdrawLoanAccount(loanId, loanWithdraw)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showLoanAccountWithdrawError(context?.getString(R.string.error_loan_account_withdraw))
        Mockito.verify(view, Mockito.never())?.showLoanAccountWithdrawSuccess()
    }
}