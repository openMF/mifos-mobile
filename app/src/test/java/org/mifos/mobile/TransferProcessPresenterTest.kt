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
import org.mifos.mobile.models.payload.TransferPayload
import org.mifos.mobile.presenters.TransferProcessPresenter
import org.mifos.mobile.ui.views.TransferProcessView
import org.mifos.mobile.util.RxSchedulersOverrideRule

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by dilpreet on 24/7/17.
 */
@RunWith(MockitoJUnitRunner::class)
class TransferProcessPresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var view: TransferProcessView? = null

    @Mock
    var mockedResponseBody: ResponseBody? = null
    private var presenter: TransferProcessPresenter? = null
    private var transferPayload: TransferPayload? = null

    @Before
    fun setUp() {
        presenter = TransferProcessPresenter(dataManager!!, context!!)
        presenter?.attachView(view)
        transferPayload = FakeRemoteDataSource.transferPayload
    }

    @After
    fun tearDown() {
        presenter?.detachView()
    }

    @Test
    fun testMakeSavingsTransfer() {
        Mockito.`when`(dataManager?.makeTransfer(transferPayload)).thenReturn(Observable.just(mockedResponseBody))
        presenter?.makeSavingsTransfer(transferPayload)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showTransferredSuccessfully()
    }

    @Test
    fun testMakeSavingsTransferFails() {
        Mockito.`when`(dataManager?.makeTransfer(transferPayload)).thenReturn(Observable.error(RuntimeException()))
        presenter?.makeSavingsTransfer(transferPayload)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view, Mockito.never())?.showTransferredSuccessfully()
    }

    @Test
    fun testMakeTPTTransfer() {
        Mockito.`when`<Observable<ResponseBody?>?>(dataManager?.makeThirdPartyTransfer(transferPayload)).thenReturn(Observable.just(mockedResponseBody))
        presenter?.makeTPTTransfer(transferPayload)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showTransferredSuccessfully()
    }

    @Test
    fun testMakeTPTTransferFails() {
        Mockito.`when`(dataManager?.makeThirdPartyTransfer(transferPayload)).thenReturn(Observable.error(RuntimeException()))
        presenter?.makeTPTTransfer(transferPayload)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view, Mockito.never())?.showTransferredSuccessfully()
    }
}