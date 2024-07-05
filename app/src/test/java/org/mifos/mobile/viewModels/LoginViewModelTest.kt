package org.mifos.mobile.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.mifos.mobile.core.data.repositories.ClientRepository
import org.mifos.mobile.core.data.repositories.UserAuthRepository
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.User
import org.mifos.mobile.core.model.entity.client.Client
import org.mifos.mobile.feature.login.utils.LoginState
import org.mifos.mobile.feature.login.viewmodel.LoginViewModel
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.FakeRemoteDataSource
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var userAuthRepositoryImp: UserAuthRepository

    @Mock
    lateinit var clientRepositoryImp: ClientRepository
    
    private lateinit var mockUser: User
    private lateinit var loginViewModel: LoginViewModel
    private var emptyClientPage: Page<Client?>? = null
    private var clientPage: Page<Client?>? = null

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        loginViewModel = LoginViewModel(
            userAuthRepositoryImp,
            clientRepositoryImp
        )
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
    fun testLogin_SuccessfulLoginReceivedFromRepository_ReturnsLoginSuccess() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        Mockito.`when`(
            userAuthRepositoryImp.login(Mockito.anyString(), Mockito.anyString())
        ).thenReturn(flowOf(mockUser))
        loginViewModel.loginUiState.test {
            loginViewModel.login("username", "password")
            Assert.assertEquals(LoginState.Initial, awaitItem())
            Assert.assertEquals(LoginState.LoginSuccess, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        Dispatchers.resetMain()
    }

    @Test(expected = Exception::class)
    fun testLogin_UnsuccessfulLoginReceivedFromRepository_ReturnsError() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        Mockito.`when`(
            userAuthRepositoryImp.login(Mockito.anyString(), Mockito.anyString())
        ).thenThrow(Exception("Error occurred"))
        loginViewModel.loginUiState.test {
            loginViewModel.login("username", "password")
            Assert.assertEquals(LoginState.Initial, awaitItem())
            Assert.assertEquals(LoginState.Error, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        Dispatchers.resetMain()
    }

    @Test(expected = Exception::class)
    fun testLoadClient_UnsuccessfulLoadClientReceivedFromRepository_ReturnsError() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        Mockito.`when`(
            clientRepositoryImp.loadClient()
        ).thenThrow(Exception("Error occurred"))
        loginViewModel.loginUiState.test {
            loginViewModel.loadClient()
            Assert.assertEquals(LoginState.Initial, awaitItem())
            Assert.assertEquals(LoginState.Loading, awaitItem())
            Assert.assertEquals(LoginState.Error, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        Mockito.verify(clientRepositoryImp).clearPrefHelper()
        Mockito.verify(clientRepositoryImp).reInitializeService()
        Dispatchers.resetMain()
    }

    @Test(expected = Exception::class)
    fun testLoadClient_EmptyClientPageReceivedFromRepository_ReturnsError() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        Mockito.`when`(
            clientRepositoryImp.loadClient()
        ).thenThrow(Exception("Error occurred"))
        loginViewModel.loginUiState.test {
            loginViewModel.loadClient()
            Assert.assertEquals(LoginState.Initial, awaitItem())
            Assert.assertEquals(LoginState.Loading, awaitItem())
            Assert.assertEquals(LoginState.Error, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        Dispatchers.resetMain()
    }

    @Test
    fun testLoadClient_NonEmptyClientPageReceivedFromRepository_ReturnsLoadClientSuccess() =
        runTest {
            Dispatchers.setMain(Dispatchers.Unconfined)
            val clientId = clientPage?.pageItems?.get(0)?.id?.toLong()
            val clientName = clientPage?.pageItems?.get(0)?.displayName
            val client = mock(Client::class.java).apply {
                Mockito.`when`(id).thenReturn(clientId?.toInt())
                Mockito.`when`(displayName).thenReturn(clientName)
            }
            val clientPage1 = Page<Client?>().apply {
                pageItems = listOf(client)
            }
            Mockito.`when`(
                clientRepositoryImp.loadClient()
            ).thenReturn(
                flow {
                  emit(clientPage1 as Page<Client>)
                }
            )
            loginViewModel.loginUiState.test {
                loginViewModel.loadClient()
                Assert.assertEquals(LoginState.Initial, awaitItem())
                Assert.assertEquals(LoginState.LoadClientSuccess(clientName), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            Dispatchers.resetMain()
        }

    @After
    fun tearDown() {
    
    }
}