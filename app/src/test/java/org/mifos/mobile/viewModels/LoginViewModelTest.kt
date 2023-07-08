package org.mifos.mobile.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.reactivex.Observable

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.FakeRemoteDataSource
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.User
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.repositories.UserAuthRepository
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.LoginUiState
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.lang.RuntimeException

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var userAuthRepositoryImp : UserAuthRepository

    @Mock
    lateinit var loginUiStateObserver : Observer<LoginUiState>

    private lateinit var mockUser : User
    private lateinit var loginViewModel : LoginViewModel
    private var emptyClientPage : Page<Client?>? = null
    private var clientPage : Page<Client?>? = null

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        loginViewModel = LoginViewModel(userAuthRepositoryImp)
        loginViewModel.loginUiState.observeForever(loginUiStateObserver)
        mockUser = FakeRemoteDataSource.user
        emptyClientPage = FakeRemoteDataSource.noClients
        clientPage = FakeRemoteDataSource.clients
    }

    @Test
    fun testIsFieldEmpty_WithNonEmptyStringInput_ReturnsFalse() {
        val result = loginViewModel.isFieldEmpty("nonEmptyTestString")
        Assert.assertFalse(result)
    }

    @Test
    fun testIsFieldEmpty_WithEmptyStringInput_ReturnsTrue() {
        val result = loginViewModel.isFieldEmpty("")
        Assert.assertTrue(result)
    }

    @Test
    fun testIsUsernameLengthInadequate_WithAdequateLengthInput_ReturnsFalse() {
        val result = loginViewModel.isUsernameLengthInadequate("username123")
        Assert.assertFalse(result)
    }

    @Test
    fun testIsUsernameLengthInadequate_WithInadequateLengthInput_ReturnsTrue() {
        val result = loginViewModel.isUsernameLengthInadequate("user")
        Assert.assertTrue(result)
    }

    @Test
    fun testIsPasswordLengthInadequate_WithAdequateLengthInput_ReturnsFalse() {
        val result = loginViewModel.isUsernameLengthInadequate("password123")
        Assert.assertFalse(result)
    }

    @Test
    fun testIsPasswordLengthInadequate_WithInadequateLengthInput_ReturnsTrue() {
        val result = loginViewModel.isUsernameLengthInadequate("pass")
        Assert.assertTrue(result)
    }

    @Test
    fun testUsernameHasSpaces_WithSpacesInput_ReturnsTrue() {
        val result = loginViewModel.usernameHasSpaces("username withSpace")
        Assert.assertTrue(result)
    }

    @Test
    fun testUsernameHasSpaces_WithNoSpacesInput_ReturnsFalse() {
        val result = loginViewModel.usernameHasSpaces("usernameNoSpaces")
        Assert.assertFalse(result)
    }

    @Test
    fun testLogin_SuccessfulLoginReceivedFromRepository_ReturnsLoginSuccess() {
        Mockito.`when`(
            userAuthRepositoryImp.login(Mockito.anyString(), Mockito.anyString())
        ).thenReturn(Observable.just(mockUser))

        loginViewModel.login("username", "password")

        Mockito.verify(loginUiStateObserver).onChanged(LoginUiState.Loading)
        Mockito.verify(userAuthRepositoryImp).saveAuthenticationTokenForSession(mockUser)
        Mockito.verify(loginUiStateObserver).onChanged(LoginUiState.LoginSuccess)
        Mockito.verifyNoMoreInteractions(loginUiStateObserver)
    }

    @Test
    fun testLogin_UnsuccessfulLoginReceivedFromRepository_ReturnsError() {
        val error = RuntimeException("Login Failed")
        Mockito.`when`(
            userAuthRepositoryImp.login(Mockito.anyString(), Mockito.anyString())
        ).thenReturn(Observable.error(error))

        loginViewModel.login("username", "password")

        Mockito.verify(loginUiStateObserver).onChanged(LoginUiState.Loading)
        Mockito.verify(loginUiStateObserver).onChanged(LoginUiState.Error)
        Mockito.verifyNoMoreInteractions(loginUiStateObserver)
    }

    @Test
    fun testLoadClient_UnsuccessfulLoadClientReceivedFromRepository_ReturnsError() {
        val error = RuntimeException("Load Client Failed")
        Mockito.`when`(
            userAuthRepositoryImp.loadClient()
        ).thenReturn(Observable.error(error))

        loginViewModel.loadClient()

        Mockito.verify(loginUiStateObserver).onChanged(LoginUiState.Error)
        Mockito.verify(userAuthRepositoryImp).clearPrefHelper()
        Mockito.verify(userAuthRepositoryImp).reInitializeService()
        Mockito.verifyNoMoreInteractions(loginUiStateObserver)
    }

    @Test
    fun testLoadClient_EmptyClientPageReceivedFromRepository_ReturnsError() {
        Mockito.`when`(
            userAuthRepositoryImp.loadClient()
        ).thenReturn(Observable.just(emptyClientPage))

        loginViewModel.loadClient()

        Mockito.verify(loginUiStateObserver).onChanged(LoginUiState.Error)
        Mockito.verifyNoMoreInteractions(loginUiStateObserver)
    }

    @Test
    fun testLoadClient_NonEmptyClientPageReceivedFromRepository_ReturnsLoadClientSuccess() {
        val clientId = clientPage?.pageItems?.get(0)?.id?.toLong()
        val clientName = clientPage?.pageItems?.get(0)?.displayName
        Mockito.`when`(
            userAuthRepositoryImp.loadClient()
        ).thenReturn(Observable.just(clientPage))

        loginViewModel.loadClient()

        Mockito.verify(userAuthRepositoryImp).setClientId(clientId)
        Mockito.verify(userAuthRepositoryImp).reInitializeService()
        Mockito.verify(loginUiStateObserver).onChanged(LoginUiState.LoadClientSuccess(clientName))
        Mockito.verifyNoMoreInteractions(loginUiStateObserver)
    }

    @After
    fun tearDown() {
        loginViewModel.loginUiState.removeObserver(loginUiStateObserver)
    }
}