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
import org.mifos.mobile.presenters.LoanAccountsTransactionPresenter
import org.mifos.mobile.ui.views.LoanAccountsTransactionView
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.Constants

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by dilpreet on 27/6/17.
 */
@RunWith(MockitoJUnitRunner::class)
class LoanAccountsTransactionPresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var view: LoanAccountsTransactionView? = null
    private var presenter: LoanAccountsTransactionPresenter? = null
    private var loanWithTransaction: LoanWithAssociations? = null
    private var loanWithEmptyTransactions: LoanWithAssociations? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        presenter = LoanAccountsTransactionPresenter(dataManager!!, context)
        presenter?.attachView(view)
        loanWithTransaction = FakeRemoteDataSource.loanAccountWithTransaction
        loanWithEmptyTransactions = FakeRemoteDataSource.loanAccountWithEmptyTransaction
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        presenter?.detachView()
    }

    @Test
    @Throws(Exception::class)
    fun testLoadLoanAccountDetails() {
        Mockito.`when`(dataManager?.getLoanWithAssociations(Constants.TRANSACTIONS, 29)).thenReturn(Observable.just(loanWithTransaction))
        presenter?.loadLoanAccountDetails(29)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showLoanTransactions(loanWithTransaction)
        Mockito.verify(view, Mockito.never())?.showEmptyTransactions(null)
        Mockito.verify(view, Mockito.never())?.showErrorFetchingLoanAccountsDetail(context?.getString(R.string.error_loan_account_details_loading))
    }

    @Test
    @Throws(Exception::class)
    fun testLoadLoanAccountDetailsEmpty() {
        Mockito.`when`(dataManager?.getLoanWithAssociations(Constants.TRANSACTIONS, 29)).thenReturn(Observable.just(loanWithEmptyTransactions))
        presenter?.loadLoanAccountDetails(29)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showEmptyTransactions(loanWithEmptyTransactions)
        Mockito.verify(view, Mockito.never())?.showLoanTransactions(null)
        Mockito.verify(view, Mockito.never())?.showErrorFetchingLoanAccountsDetail(context?.getString(R.string.error_loan_account_details_loading))
    }

    @Test
    @Throws(Exception::class)
    fun testLoadLoanAccountDetailsFails() {
        Mockito.`when`(dataManager?.getLoanWithAssociations(Constants.TRANSACTIONS, 29)).thenReturn(Observable.error(RuntimeException()))
        presenter?.loadLoanAccountDetails(29)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showErrorFetchingLoanAccountsDetail(context?.getString(R.string.error_loan_account_details_loading))
        Mockito.verify(view, Mockito.never())?.showEmptyTransactions(null)
        Mockito.verify(view, Mockito.never())?.showLoanTransactions(null)
    }
}