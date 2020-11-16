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
import org.mifos.mobile.presenters.AccountOverviewPresenter
import org.mifos.mobile.ui.views.AccountOverviewMvpView
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Chirag Gupta on 12/06/17.
 */
@RunWith(MockitoJUnitRunner::class)
class AccountOverviewPresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var mvpView: AccountOverviewMvpView? = null
    private var presenter: AccountOverviewPresenter? = null
    private var accounts: ClientAccounts? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        presenter = AccountOverviewPresenter(context!!, dataManager!!)
        presenter?.attachView(mvpView)
        accounts = FakeRemoteDataSource.clientAccounts
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        presenter?.detachView()
    }

    @Test
    fun testLoadClientAccountDetails() {
        Mockito.`when`(dataManager?.clientAccounts).thenReturn(Observable.just(accounts))
        presenter?.loadClientAccountDetails()
        Mockito.verify(mvpView)?.showProgress()
        Mockito.verify(mvpView)?.hideProgress()
        Mockito.verify(mvpView)?.showTotalLoanSavings(
                presenter?.getLoanAccountDetails(accounts?.loanAccounts),
                presenter?.getSavingAccountDetails(accounts?.savingsAccounts))
        Mockito.verify(mvpView, Mockito.never())?.showError(context?.getString(R.string.error_fetching_accounts))
    }

    @Test
    fun testLoadClientAccountDetailsFail() {
        Mockito.`when`(dataManager?.clientAccounts).thenReturn(Observable.error(RuntimeException()))
        presenter?.loadClientAccountDetails()
        Mockito.verify(mvpView)?.showProgress()
        Mockito.verify(mvpView)?.hideProgress()
        presenter?.getLoanAccountDetails(accounts?.loanAccounts)?.let {
            Mockito.verify(mvpView, Mockito.never())?.showTotalLoanSavings(
                    it,
                    presenter?.getSavingAccountDetails(accounts?.savingsAccounts)!!)
        }
        Mockito.verify(mvpView)?.showError(context?.getString(R.string.error_fetching_accounts))
    }
}