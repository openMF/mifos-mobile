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
import org.mifos.mobile.presenters.BeneficiaryDetailPresenter
import org.mifos.mobile.ui.views.BeneficiaryDetailView
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by dilpreet on 24/7/17.
 */
@RunWith(MockitoJUnitRunner::class)
class BeneficiaryDetailPresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var view: BeneficiaryDetailView? = null

    @Mock
    var mockedResponseBody: ResponseBody? = null
    private var presenter: BeneficiaryDetailPresenter? = null
    private val beneficiaryId: Long = 1

    @Before
    fun setUp() {
        presenter = BeneficiaryDetailPresenter(dataManager!!, context)
        presenter?.attachView(view)
    }

    @After
    fun tearDown() {
        presenter?.detachView()
    }

    @Test
    fun testDeleteBeneficiary() {
        Mockito.`when`(dataManager?.deleteBeneficiary(beneficiaryId)).thenReturn(Observable.just(mockedResponseBody))
        presenter?.deleteBeneficiary(beneficiaryId)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showBeneficiaryDeletedSuccessfully()
        Mockito.verify(view, Mockito.never())?.showError(context?.getString(R.string.error_deleting_beneficiary))
    }

    @Test
    fun testDeleteBeneficiaryFails() {
        Mockito.`when`(dataManager?.deleteBeneficiary(beneficiaryId)).thenReturn(Observable.error(RuntimeException()))
        presenter?.deleteBeneficiary(beneficiaryId)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showError(context?.getString(R.string.error_deleting_beneficiary))
        Mockito.verify(view, Mockito.never())?.showBeneficiaryDeletedSuccessfully()
    }
}