package org.mifos.mobile

import android.content.Context
import io.reactivex.Observable

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.client.ClientAccounts
import org.mifos.mobile.presenters.AccountsPresenter
import org.mifos.mobile.ui.views.AccountsView
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.Constants

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by dilpreet on 26/6/17.
 */
@RunWith(MockitoJUnitRunner::class)
class AccountsPresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var accountsView: AccountsView? = null
    private var presenter: AccountsPresenter? = null
    private var accounts: ClientAccounts? = null
    private var savingsAccount: ClientAccounts? = null
    private var loanAccounts: ClientAccounts? = null
    private var shareAccounts: ClientAccounts? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        presenter = AccountsPresenter(context!!, dataManager!!)
        presenter?.attachView(accountsView)
        accounts = FakeRemoteDataSource.clientAccounts
        savingsAccount = FakeRemoteDataSource.clientSavingsAccount
        loanAccounts = FakeRemoteDataSource.clientLoanAccount
        shareAccounts = FakeRemoteDataSource.clientShareAccount
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        presenter?.detachView()
    }

    @Test
    fun testLoadClientAccounts() {
        Mockito.`when`(dataManager?.clientAccounts).thenReturn(Observable.just(accounts))
        presenter?.loadClientAccounts()
        Mockito.verify(accountsView)?.showProgress()
        Mockito.verify(accountsView)?.hideProgress()
        Mockito.verify(accountsView)?.showSavingsAccounts(accounts?.savingsAccounts)
        Mockito.verify(accountsView)?.showLoanAccounts(accounts?.loanAccounts)
        Mockito.verify(accountsView)?.showShareAccounts(accounts?.shareAccounts)
        Mockito.verify(accountsView, Mockito.never())?.showError(context?.getString(R.string.error_fetching_accounts))
    }

    @Test
    fun testLoadClientAccountsFail() {
        Mockito.`when`(dataManager?.clientAccounts).thenReturn(Observable.error(RuntimeException()))
        presenter?.loadClientAccounts()
        Mockito.verify(accountsView)?.showProgress()
        Mockito.verify(accountsView)?.hideProgress()
        Mockito.verify(accountsView)?.showError(context?.getString(R.string.error_fetching_accounts))
    }

    @Test
    fun testLoadClientSavingsAccount() {
        Mockito.`when`(dataManager?.getAccounts(Constants.SAVINGS_ACCOUNTS)).thenReturn(Observable.just(savingsAccount))
        presenter?.loadAccounts(Constants.SAVINGS_ACCOUNTS)
        Mockito.verify(accountsView)?.showProgress()
        Mockito.verify(accountsView)?.hideProgress()
        Mockito.verify(accountsView)?.showSavingsAccounts(savingsAccount?.savingsAccounts)
        Mockito.verify(accountsView, Mockito.never())?.showLoanAccounts(null)
        Mockito.verify(accountsView, Mockito.never())?.showShareAccounts(null)
        Mockito.verify(accountsView, Mockito.never())?.showError(context?.getString(R.string.error_fetching_accounts))
    }

    @Test
    fun testLoadClientLoanAccount() {
        Mockito.`when`(dataManager?.getAccounts(Constants.LOAN_ACCOUNTS)).thenReturn(Observable.just(loanAccounts))
        presenter?.loadAccounts(Constants.LOAN_ACCOUNTS)
        Mockito.verify(accountsView)?.showProgress()
        Mockito.verify(accountsView)?.hideProgress()
        Mockito.verify(accountsView)?.showLoanAccounts(loanAccounts?.loanAccounts)
        Mockito.verify(accountsView, Mockito.never())?.showSavingsAccounts(null)
        Mockito.verify(accountsView, Mockito.never())?.showShareAccounts(null)
        Mockito.verify(accountsView, Mockito.never())?.showError(context?.getString(R.string.error_fetching_accounts))
    }

    @Test
    fun testLoadClientShareAccount() {
        Mockito.`when`(dataManager?.getAccounts(Constants.SHARE_ACCOUNTS)).thenReturn(Observable.just(shareAccounts))
        presenter?.loadAccounts(Constants.SHARE_ACCOUNTS)
        Mockito.verify(accountsView)?.showProgress()
        Mockito.verify(accountsView)?.hideProgress()
        Mockito.verify(accountsView)?.showShareAccounts(shareAccounts?.shareAccounts)
        Mockito.verify(accountsView, Mockito.never())?.showLoanAccounts(null)
        Mockito.verify(accountsView, Mockito.never())?.showSavingsAccounts(null)
        Mockito.verify(accountsView, Mockito.never())?.showError(context?.getString(R.string.error_fetching_accounts))
    }
}