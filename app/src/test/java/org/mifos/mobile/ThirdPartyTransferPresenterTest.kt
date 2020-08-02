package org.mifos.mobile

import android.content.Context

import io.reactivex.Observable

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
import org.mifos.mobile.presenters.ThirdPartyTransferPresenter
import org.mifos.mobile.ui.views.ThirdPartyTransferView
import org.mifos.mobile.util.RxSchedulersOverrideRule

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by dilpreet on 24/7/17.
 */
@RunWith(MockitoJUnitRunner::class)
class ThirdPartyTransferPresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var view: ThirdPartyTransferView? = null
    private var accountOptionsTemplate: AccountOptionsTemplate? = null
    private var presenter: ThirdPartyTransferPresenter? = null
    private var beneficiaryList: List<Beneficiary?>? = null

    @Before
    fun setUp() {
        presenter = ThirdPartyTransferPresenter(dataManager!!, context!!)
        presenter?.attachView(view)
        accountOptionsTemplate = FakeRemoteDataSource.accountOptionsTemplate
        beneficiaryList = FakeRemoteDataSource.beneficiaries
    }

    @After
    fun tearDown() {
        presenter?.detachView()
    }

    @Test
    fun testTransferTemplate() {
        Mockito.`when`(dataManager?.thirdPartyTransferTemplate).thenReturn(Observable.just(accountOptionsTemplate))
        Mockito.`when`(dataManager?.beneficiaryList).thenReturn(Observable.just(beneficiaryList))
        presenter?.loadTransferTemplate()
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showThirdPartyTransferTemplate(accountOptionsTemplate)
        Mockito.verify(view)?.showBeneficiaryList(beneficiaryList)
        Mockito.verify(view, Mockito.never())?.showError(context?.getString(
                R.string.error_fetching_account_transfer_template))
    }

    @Test
    fun testTransferTemplateFails() {
        Mockito.`when`(dataManager?.thirdPartyTransferTemplate).thenReturn(Observable.error(RuntimeException()))
        Mockito.`when`(dataManager?.beneficiaryList).thenReturn(Observable.error(RuntimeException()))
        presenter?.loadTransferTemplate()
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showError(context?.getString(
                R.string.error_fetching_account_transfer_template))
        Mockito.verify(view, Mockito.never())?.showThirdPartyTransferTemplate(accountOptionsTemplate)
        Mockito.verify(view, Mockito.never())?.showBeneficiaryList(beneficiaryList)
    }
}