package org.mifos.mobile

import android.content.Context

import io.reactivex.Observable

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.Transaction
import org.mifos.mobile.presenters.RecentTransactionsPresenter
import org.mifos.mobile.ui.views.RecentTransactionsView
import org.mifos.mobile.util.RxSchedulersOverrideRule

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by dilpreet on 24/7/17.
 */
@RunWith(MockitoJUnitRunner::class)
class RecentTransactionsPresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var view: RecentTransactionsView? = null
    private var presenter: RecentTransactionsPresenter? = null
    private var recentTransaction: Page<Transaction?>? = null
    private var offset = 0
    private val limit = 50

    @Before
    fun setUp() {
        presenter = RecentTransactionsPresenter(dataManager!!, context!!)
        presenter?.attachView(view)
        recentTransaction = FakeRemoteDataSource.transactions
    }

    @After
    fun tearDown() {
        presenter?.detachView()
    }

    @Test
    fun testLoadRecentTransactionsZeroOffset() {
        offset = 0
        Mockito.`when`(dataManager?.getRecentTransactions(offset, limit)).thenReturn(Observable.just(
                recentTransaction))
        presenter?.loadRecentTransactions(false, offset)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showRecentTransactions(recentTransaction?.pageItems)
        Mockito.verify(view, Mockito.never())?.showErrorFetchingRecentTransactions(context?.getString(R.string.error_recent_transactions_loading))
    }

    @Test
    fun testLoadRecentTransactionsWithOffset() {
        offset = 10
        Mockito.`when`(dataManager?.getRecentTransactions(offset, limit)).thenReturn(Observable.just(
                recentTransaction))
        presenter?.loadRecentTransactions(true, offset)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showLoadMoreRecentTransactions(recentTransaction?.pageItems)
        Mockito.verify(view, Mockito.never())?.showErrorFetchingRecentTransactions(context?.getString(R.string.error_recent_transactions_loading))
    }

    @Test
    fun testLoadRecentTransactionsWithZeroTransactions() {
        offset = 0
        Mockito.`when`(dataManager?.getRecentTransactions(offset, limit)).thenReturn(Observable.just(
                Page()))
        presenter?.loadRecentTransactions(false, offset)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showEmptyTransaction()
        Mockito.verify(view, Mockito.never())?.showErrorFetchingRecentTransactions(context?.getString(R.string.error_recent_transactions_loading))
    }

    @Test
    fun testLoadRecentTransactionsFails() {
        offset = 0
        Mockito.`when`(dataManager?.getRecentTransactions(offset, limit)).thenReturn(Observable.error(RuntimeException()))
        presenter?.loadRecentTransactions(false, offset)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showErrorFetchingRecentTransactions(context?.getString(R.string.error_recent_transactions_loading))
        Mockito.verify(view, Mockito.never())?.showEmptyTransaction()
    }
}