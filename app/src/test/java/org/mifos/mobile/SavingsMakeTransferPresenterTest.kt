package org.mifos.mobile

import android.content.Context

import io.reactivex.Observable

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
import org.mifos.mobile.presenters.SavingsMakeTransferPresenter
import org.mifos.mobile.ui.views.SavingsMakeTransferMvpView
import org.mifos.mobile.util.RxSchedulersOverrideRule

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by dilpreet on 24/7/17.
 */
@RunWith(MockitoJUnitRunner::class)
class SavingsMakeTransferPresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var view: SavingsMakeTransferMvpView? = null
    private var accountOptionsTemplate: AccountOptionsTemplate? = null
    private var presenter: SavingsMakeTransferPresenter? = null

    @Before
    fun setUp() {
        presenter = SavingsMakeTransferPresenter(dataManager!!, context)
        presenter?.attachView(view)
        accountOptionsTemplate = FakeRemoteDataSource.accountOptionsTemplate
    }

    @After
    fun tearDown() {
        presenter?.detachView()
    }

    @Test
    fun testLoanAccountTransferTemplate() {
        Mockito.`when`(dataManager?.accountTransferTemplate).thenReturn(Observable.just(accountOptionsTemplate))
        presenter?.loanAccountTransferTemplate()
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showSavingsAccountTemplate(accountOptionsTemplate)
        Mockito.verify(view, Mockito.never())?.showError(context?.getString(
                R.string.error_fetching_account_transfer_template))
    }

    @Test
    fun testLoanAccountTransferTemplateFails() {
        Mockito.`when`(dataManager?.accountTransferTemplate).thenReturn(Observable.error(RuntimeException()))
        presenter?.loanAccountTransferTemplate()
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showError(context?.getString(
                R.string.error_fetching_account_transfer_template))
        Mockito.verify(view, Mockito.never())?.showSavingsAccountTemplate(accountOptionsTemplate)
    }
}