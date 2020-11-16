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
import org.mifos.mobile.presenters.GuarantorDetailPresenter
import org.mifos.mobile.ui.views.GuarantorDetailView
import org.mifos.mobile.util.RxSchedulersOverrideRule

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class GuarantorDetailPresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var view: GuarantorDetailView? = null

    @Mock
    var responseBody: ResponseBody? = null
    private var presenter: GuarantorDetailPresenter? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        presenter = GuarantorDetailPresenter(context!!, dataManager!!)
        presenter?.attachView(view)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        presenter?.detachView()
    }

    @Test
    @Throws(IOException::class)
    fun testDeleteGuarantor() {
        val loanId: Long = 1
        val guarantorId: Long = 1
        Mockito.`when`(dataManager?.deleteGuarantor(loanId, guarantorId))
                .thenReturn(Observable.just(responseBody))
        presenter?.deleteGuarantor(loanId, guarantorId)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.guarantorDeletedSuccessfully(responseBody?.string())
    }

    @Test
    @Throws(IOException::class)
    fun testDeleteGuarantorOnError() {
        val loanId: Long = 1
        val guarantorId: Long = 1
        val exception = Exception("ExceptionMessage")
        Mockito.`when`(dataManager?.deleteGuarantor(loanId, guarantorId))
                .thenReturn(Observable.error(exception))
        presenter?.deleteGuarantor(loanId, guarantorId)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showError(exception.message)
    }
}