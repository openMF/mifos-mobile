package org.mifos.mobile.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.reactivex.Observable
import okhttp3.ResponseBody
import org.junit.*
import org.junit.runner.RunWith
import org.mifos.mobile.R
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.models.notification.NotificationRegisterPayload
import org.mifos.mobile.models.notification.NotificationUserDetail
import org.mifos.mobile.repositories.HomeRepositoryImp
import org.mifos.mobile.repositories.UserDetailRepositoryImp
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.UserDetailUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserDetailViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var homeRepositoryImp: HomeRepositoryImp

    @Mock
    lateinit var userDetailRepositoryImp: UserDetailRepositoryImp

    @Mock
    lateinit var preferencesHelper: PreferencesHelper

    @Mock
    lateinit var userDetailUiStateObserver: Observer<UserDetailUiState>

    private lateinit var userDetailViewModel: UserDetailViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        userDetailViewModel = UserDetailViewModel(userDetailRepositoryImp, homeRepositoryImp)
        userDetailViewModel.preferencesHelper = preferencesHelper
        userDetailViewModel.userDetailUiState.observeForever(userDetailUiStateObserver)
    }

    @Test
    fun userDetails_Success() {
        val client = mock(Client::class.java)
        `when`(homeRepositoryImp.currentClient()).thenReturn(Observable.just(client))
        userDetailViewModel.userDetails
        Assert.assertEquals(
            UserDetailUiState.ShowUserDetails(client),
            userDetailViewModel.userDetailUiState.value
        )
    }

    @Test
    fun userDetails_Unsuccessful() {
        `when`(homeRepositoryImp.currentClient()).thenReturn(Observable.error(Throwable()))
        userDetailViewModel.userDetails
        Assert.assertEquals(
            UserDetailUiState.ShowError(R.string.error_fetching_client),
            userDetailViewModel.userDetailUiState.value
        )
    }

    @Test
    fun testRegisterNotification_Successful() {
        val response: Observable<ResponseBody?> = Observable.just(mock(ResponseBody::class.java))
        val payload = NotificationRegisterPayload(preferencesHelper.clientId!!, "Token")

        `when`(userDetailRepositoryImp.registerNotification(payload)).thenReturn(response)

        userDetailViewModel.registerNotification("Token")

        verify(preferencesHelper).setSentTokenToServer(true)
        verify(preferencesHelper).saveGcmToken("Token")
    }

    @Test
    fun testRegisterNotification_Unsuccessful() {
        val error: Observable<ResponseBody?> = Observable.error(Throwable("Error Response"))
        val payload = NotificationRegisterPayload(preferencesHelper.clientId!!, "Token")

        `when`(userDetailRepositoryImp.registerNotification(payload)).thenReturn(error)
        `when`(preferencesHelper.clientId).thenReturn(123L)

        userDetailViewModel.registerNotification("Token")

        verify(preferencesHelper, never()).setSentTokenToServer(anyBoolean())
        verify(preferencesHelper, never()).saveGcmToken(anyString())
    }

    @Test
    fun testGetUserNotificationId_Success() {
        val response: Observable<NotificationUserDetail?> =
            Observable.just(mock(NotificationUserDetail::class.java))

        `when`(preferencesHelper.clientId).thenReturn(123L)
        `when`(userDetailRepositoryImp.getUserNotificationId(123L))
            .thenReturn(response)

        userDetailViewModel.getUserNotificationId(
            NotificationRegisterPayload(123L, "Token"),
            "Token"
        )
    }

    @Test
    fun testGetUserNotificationId_Unsuccessful() {
        val error: Observable<NotificationUserDetail?> =
            Observable.error(Throwable("Error Response"))

        `when`(preferencesHelper.clientId?.let { userDetailRepositoryImp.getUserNotificationId(it) }).thenReturn(
            error
        )
        `when`(preferencesHelper.clientId).thenReturn(123L)

        userDetailViewModel.getUserNotificationId(
            NotificationRegisterPayload(123L, "Token"),
            "Token"
        )
    }

    @Test
    fun testUpdateRegistrationNotification_Success() {
        val response: Observable<ResponseBody?> = Observable.just(mock(ResponseBody::class.java))
        val payload = NotificationRegisterPayload(preferencesHelper.clientId!!, "Token")

        `when`(
            userDetailRepositoryImp.updateRegisterNotification(
                123L,
                payload
            )
        )
            .thenReturn(response)

        preferencesHelper.clientId?.let { NotificationRegisterPayload(it, "Token") }?.let {
            userDetailViewModel.updateRegistrationNotification(
                123L,
                it,
                "Token"
            )
        }

        verify(preferencesHelper).setSentTokenToServer(true)
        verify(preferencesHelper).saveGcmToken("Token")
    }

    @Test
    fun testUpdateRegistrationNotification_Error() {
        val error: Observable<ResponseBody?> = Observable.error(Throwable("Error Response"))
        val payload = NotificationRegisterPayload(preferencesHelper.clientId!!, "Token")

        `when`(
            userDetailRepositoryImp.updateRegisterNotification(
                preferencesHelper.clientId!!,
                payload
            )
        )
            .thenReturn(error)

        userDetailViewModel.updateRegistrationNotification(
            123L,
            NotificationRegisterPayload(preferencesHelper.clientId!!, "Token"),
            "Token"
        )
    }

    @After
    fun tearDown() {
        userDetailViewModel.userDetailUiState.removeObserver(userDetailUiStateObserver)
    }
}