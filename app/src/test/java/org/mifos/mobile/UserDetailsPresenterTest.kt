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
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.models.notification.NotificationRegisterPayload
import org.mifos.mobile.presenters.UserDetailsPresenter
import org.mifos.mobile.ui.views.UserDetailsView
import org.mifos.mobile.util.RxSchedulersOverrideRule

import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserDetailsPresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var view: UserDetailsView? = null

    @Mock
    var preferencesHelper: PreferencesHelper? = null

    @Mock
    var responseBody: ResponseBody? = null
    private var presenter: UserDetailsPresenter? = null
    private var client: Client? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        presenter = UserDetailsPresenter(context!!, dataManager!!, preferencesHelper!!)
        client = FakeRemoteDataSource.currentClient
        presenter?.attachView(view)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        presenter?.detachView()
    }

    @Test
    fun testGetUserDetails() {
        Mockito.`when`(dataManager?.currentClient)
                .thenReturn(Observable.just(client))
        presenter?.userDetails
        Mockito.verify(view)?.showUserDetails(client)
    }

    @Test
    fun testGetUserDetailsFails() {
        Mockito.`when`(dataManager?.currentClient)
                .thenReturn(Observable.error(Throwable()))
        presenter?.userDetails
        Mockito.verify(view)?.showError(context
                ?.getString(R.string.error_client_not_found))
    }

    @Test
    fun testRegisterNotification() {
        val token = "1"
        Mockito.`when`<Observable<ResponseBody?>?>(dataManager?.registerNotification(ArgumentMatchers.any(NotificationRegisterPayload::class.java)))
                .thenReturn(Observable.just(responseBody))
        presenter?.registerNotification(token)
        Mockito.verify(preferencesHelper)?.setSentTokenToServer(true)
        Mockito.verify(preferencesHelper)?.saveGcmToken(token)
    }
}