package org.mifos.mobile

import android.content.Context

import io.reactivex.Observable

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.notification.MifosNotification
import org.mifos.mobile.presenters.NotificationPresenter
import org.mifos.mobile.ui.views.NotificationView
import org.mifos.mobile.util.RxSchedulersOverrideRule

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

import java.util.*

@RunWith(MockitoJUnitRunner::class)
class NotificationPresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var view: NotificationView? = null
    private var presenter: NotificationPresenter? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        presenter = NotificationPresenter(dataManager!!, context!!)
        presenter?.attachView(view)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        presenter?.detachView()
    }

    @Test
    fun testLoadNotifications() {
        val list: List<MifosNotification?> = ArrayList()
        Mockito.`when`<Observable<List<MifosNotification?>?>?>(dataManager?.notifications)
                .thenReturn(Observable.just(list))
        presenter?.loadNotifications()
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showNotifications(list)
    }

    @Test
    fun testLoadNotificationsOnError() {
        Mockito.`when`(dataManager?.notifications)
                .thenReturn(Observable.error(Throwable()))
        presenter?.loadNotifications()
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showError(context
                ?.getString(R.string.notification))
    }
}