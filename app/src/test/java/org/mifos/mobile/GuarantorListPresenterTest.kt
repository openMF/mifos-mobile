package org.mifos.mobile

import android.content.Context

import io.reactivex.Observable

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.presenters.GuarantorListPresenter
import org.mifos.mobile.ui.views.GuarantorListView
import org.mifos.mobile.util.RxSchedulersOverrideRule

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class GuarantorListPresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var view: GuarantorListView? = null
    private var presenter: GuarantorListPresenter? = null
    private var guarantorPayloadList: List<GuarantorPayload?>? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        presenter = GuarantorListPresenter(context!!, dataManager!!)
        presenter?.attachView(view)
        guarantorPayloadList = FakeRemoteDataSource.guarantorsList
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        presenter?.detachView()
    }

    @Test
    @Throws(IOException::class)
    fun testGetGuarantorList() {
        val loanId: Long = 1
        Mockito.`when`(dataManager?.getGuarantorList(loanId))
                .thenReturn(Observable.just(guarantorPayloadList))
        presenter?.getGuarantorList(loanId)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showGuarantorListSuccessfully(guarantorPayloadList)
    }

    @Test
    @Throws(IOException::class)
    fun testGetGuarantorListOnError() {
        val loanId: Long = 1
        val exception = Exception("ExceptionMessage")
        Mockito.`when`(dataManager?.getGuarantorList(loanId))
                .thenReturn(Observable.error(exception))
        presenter?.getGuarantorList(loanId)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showError(exception.message)
    }
}