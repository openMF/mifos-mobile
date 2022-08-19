package org.mifos.mobile

import android.content.Context

import io.reactivex.Observable

import okhttp3.Credentials
import okhttp3.ResponseBody

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.mifos.mobile.api.BaseURL
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.models.UpdatePasswordPayload
import org.mifos.mobile.presenters.UpdatePasswordPresenter
import org.mifos.mobile.ui.views.UpdatePasswordView
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.MFErrorParser.errorMessage

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UpdatePasswordPresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var view: UpdatePasswordView? = null

    @Mock
    var preferencesHelper: PreferencesHelper? = null

    @Mock
    var responseBody: ResponseBody? = null
    private var passwordPayload: UpdatePasswordPayload? = null
    private var presenter: UpdatePasswordPresenter? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        Mockito.`when`(preferencesHelper?.baseUrl)
                .thenReturn(BaseURL.PROTOCOL_HTTPS + BaseURL.API_ENDPOINT)
        presenter = UpdatePasswordPresenter(context!!, dataManager!!, preferencesHelper!!)
        passwordPayload = FakeRemoteDataSource.updatePasswordPayload
        presenter?.attachView(view)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        presenter?.detachView()
    }

    @Test
    fun updateAccountPassword() {
        Mockito.`when`<Observable<ResponseBody?>?>(dataManager?.updateAccountPassword(passwordPayload))
                .thenReturn(Observable.just(responseBody))
        presenter?.updateAccountPassword(passwordPayload)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showPasswordUpdatedSuccessfully()
    }

    @Test
    fun updateAccountPasswordOnError() {
        val exception = Exception("message")
        Mockito.`when`(dataManager?.updateAccountPassword(passwordPayload))
                .thenReturn(Observable.error(exception))
        presenter?.updateAccountPassword(passwordPayload)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showError(errorMessage(exception))
    }

    @Test
    fun updateAuthenticationToken() {
        val password = "password"
        presenter?.updateAuthenticationToken(password)
        val authenticationToken = Credentials.basic(preferencesHelper?.userName!!, password)
        Mockito.verify(preferencesHelper)?.saveToken(authenticationToken)
    }
}