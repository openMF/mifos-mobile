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
import org.mifos.mobile.models.beneficiary.BeneficiaryPayload
import org.mifos.mobile.models.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.models.templates.beneficiary.BeneficiaryTemplate
import org.mifos.mobile.presenters.BeneficiaryApplicationPresenter
import org.mifos.mobile.ui.views.BeneficiaryApplicationView
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by dilpreet on 12/7/17.
 */
@RunWith(MockitoJUnitRunner::class)
class BeneficiaryApplicationPresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var view: BeneficiaryApplicationView? = null

    @Mock
    var mockedResponseBody: ResponseBody? = null
    private var updatePayload: BeneficiaryUpdatePayload? = null
    private var payload: BeneficiaryPayload? = null
    private var presenter: BeneficiaryApplicationPresenter? = null
    private var beneficiaryTemplate: BeneficiaryTemplate? = null

    @Before
    fun setUp() {
        presenter = BeneficiaryApplicationPresenter(dataManager!!, context)
        presenter?.attachView(view)
        beneficiaryTemplate = FakeRemoteDataSource.beneficiaryTemplate
        payload = FakeRemoteDataSource.beneficiaryPayload()
        updatePayload = FakeRemoteDataSource.beneficiaryUpdatePayload()
    }

    @After
    fun tearDown() {
        presenter?.detachView()
    }

    @Test
    fun testShowBeneficiaryTemplate() {
        Mockito.`when`(dataManager?.beneficiaryTemplate).thenReturn(Observable.just(beneficiaryTemplate))
        presenter?.loadBeneficiaryTemplate()
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showBeneficiaryTemplate(beneficiaryTemplate)
        Mockito.verify(view, Mockito.never())?.showError(context
                ?.getString(R.string.error_fetching_beneficiary_template))
    }

    @Test
    fun testShowBeneficiaryTemplateFails() {
        Mockito.`when`(dataManager?.beneficiaryTemplate).thenReturn(Observable.error(RuntimeException()))
        presenter?.loadBeneficiaryTemplate()
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showError(context
                ?.getString(R.string.error_fetching_beneficiary_template))
        Mockito.verify(view, Mockito.never())?.showBeneficiaryTemplate(null)
    }

    @Test
    fun testCreateBeneficiary() {
        Mockito.`when`(dataManager?.createBeneficiary(payload)).thenReturn(Observable.just(mockedResponseBody))
        presenter?.createBeneficiary(payload)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showBeneficiaryCreatedSuccessfully()
        Mockito.verify(view, Mockito.never())?.showError(context
                ?.getString(R.string.error_creating_beneficiary))
    }

    @Test
    fun testCreateBeneficiaryFails() {
        Mockito.`when`(dataManager?.createBeneficiary(payload)).thenReturn(Observable.error(RuntimeException()))
        presenter?.createBeneficiary(payload)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view, Mockito.never())?.showBeneficiaryCreatedSuccessfully()
        Mockito.verify(view)?.showError(context
                ?.getString(R.string.error_creating_beneficiary))
    }

    @Test
    fun testUpdateBeneficiary() {
        Mockito.`when`(dataManager?.updateBeneficiary(1, updatePayload)).thenReturn(Observable.just(mockedResponseBody))
        presenter?.updateBeneficiary(1, updatePayload)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showBeneficiaryUpdatedSuccessfully()
        Mockito.verify(view, Mockito.never())?.showError(context
                ?.getString(R.string.error_updating_beneficiary))
    }

    @Test
    fun testUpdateBeneficiaryFails() {
        Mockito.`when`(dataManager?.updateBeneficiary(1, updatePayload)).thenReturn(Observable.error(RuntimeException()))
        presenter?.updateBeneficiary(1, updatePayload)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showError(context
                ?.getString(R.string.error_updating_beneficiary))
        Mockito.verify(view, Mockito.never())?.showBeneficiaryUpdatedSuccessfully()
    }
}