package org.mifos.mobile

import android.content.Context

import io.reactivex.Observable

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.Charge
import org.mifos.mobile.models.Page
import org.mifos.mobile.presenters.ClientChargePresenter
import org.mifos.mobile.ui.views.ClientChargeView
import org.mifos.mobile.util.RxSchedulersOverrideRule

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Chirag Gupta on 12/06/17.
 */
@RunWith(MockitoJUnitRunner::class)
class ClientChargePresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var view: ClientChargeView? = null
    private var presenter: ClientChargePresenter? = null
    private var charge: Page<Charge?>? = null
    private var savingsCharge: List<Charge?>? = null
    private var loanCharge: List<Charge?>? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        presenter = ClientChargePresenter(dataManager, context)
        presenter?.attachView(view)
        charge = FakeRemoteDataSource.charge
        savingsCharge = FakeRemoteDataSource.savingsCharge
        loanCharge = FakeRemoteDataSource.loanCharge
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        presenter?.detachView()
    }

    @Test
    fun testLoadClientCharges() {
        Mockito.`when`(dataManager?.getClientCharges(1)).thenReturn(Observable.just(charge))
        presenter?.loadClientCharges(1)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showClientCharges(charge?.pageItems)
        Mockito.verify(view, Mockito.never())?.showErrorFetchingClientCharges(context?.getString(R.string.error_client_charge_loading))
    }

    @Test
    fun testLoadClientChargesFails() {
        Mockito.`when`(dataManager?.getClientCharges(1)).thenReturn(Observable.error(RuntimeException()))
        presenter?.loadClientCharges(1)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view, Mockito.never())?.showClientCharges(charge?.pageItems)
        Mockito.verify(view)?.showErrorFetchingClientCharges(
                context?.getString(R.string.error_client_charge_loading))
    }

    @Test
    fun testLoadLoanAccountCharges() {
        Mockito.`when`(dataManager?.getLoanCharges(1)).thenReturn(Observable.just(loanCharge))
        presenter?.loadLoanAccountCharges(1)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showClientCharges(loanCharge)
        Mockito.verify(view, Mockito.never())?.showErrorFetchingClientCharges(
                context?.getString(R.string.error_client_charge_loading))
    }

    @Test
    fun testLoadLoanAccountChargesFails() {
        Mockito.`when`(dataManager?.getLoanCharges(1)).thenReturn(Observable.error(RuntimeException()))
        presenter?.loadLoanAccountCharges(1)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view, Mockito.never())?.showClientCharges(loanCharge)
        Mockito.verify(view)?.showErrorFetchingClientCharges(
                context?.getString(R.string.error_client_charge_loading))
    }

    @Test
    fun testLoadSavingsAccountCharges() {
        Mockito.`when`(dataManager?.getSavingsCharges(1)).thenReturn(Observable.just(savingsCharge))
        presenter?.loadSavingsAccountCharges(1)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showClientCharges(savingsCharge)
        Mockito.verify(view, Mockito.never())?.showErrorFetchingClientCharges(
                context?.getString(R.string.error_client_charge_loading))
    }

    @Test
    fun testLoadSavingsAccountChargesFails() {
        Mockito.`when`(dataManager?.getSavingsCharges(1)).thenReturn(Observable.error(RuntimeException()))
        presenter?.loadSavingsAccountCharges(1)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view, Mockito.never())?.showClientCharges(savingsCharge)
        Mockito.verify(view)?.showErrorFetchingClientCharges(
                context?.getString(R.string.error_client_charge_loading))
    }

    @Test
    fun testLoadClientLocalCharges() {
        Mockito.`when`<Observable<Page<Charge?>?>>(dataManager?.clientLocalCharges).thenReturn(Observable.just(charge))
        presenter?.loadClientLocalCharges()
        Mockito.verify(view)?.showClientCharges(charge?.pageItems)
        Mockito.verify(view, Mockito.never())?.showErrorFetchingClientCharges(
                context?.getString(R.string.error_client_charge_loading))
    }

    @Test
    fun testLoadClientLocalChargesFails() {
        Mockito.`when`(dataManager?.clientLocalCharges).thenReturn(Observable.error(RuntimeException()))
        presenter?.loadClientLocalCharges()
        Mockito.verify(view, Mockito.never())?.showClientCharges(charge?.pageItems)
        Mockito.verify(view)?.showErrorFetchingClientCharges(
                context?.getString(R.string.error_client_charge_loading))
    }
}