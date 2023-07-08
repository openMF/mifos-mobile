package org.mifos.mobile.repositories

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.FakeRemoteDataSource
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.User
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.models.payload.LoginPayload
import org.mifos.mobile.models.register.RegisterPayload
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserAuthRepositoryImpTest {

    @Mock
    lateinit var dataManager: DataManager

    private lateinit var userAuthRepositoryImp: UserAuthRepository
    private lateinit var mockUser : User
    private var mockClientPage : Page<Client?>? = null

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        userAuthRepositoryImp = UserAuthRepositoryImp(dataManager)
        mockUser = FakeRemoteDataSource.user
        mockClientPage = FakeRemoteDataSource.clients
    }

    @Test
    fun testRegisterUser_SuccessResponseReceivedFromDataManager_ReturnSuccessfulRegistration() {
        val successResponse: Observable<ResponseBody?> =
            Observable.just(Mockito.mock(ResponseBody::class.java))
        val registerPayload = RegisterPayload().apply {
            this.accountNumber = "accountNumber"
            this.authenticationMode = "authenticationMode"
            this.email = "email"
            this.firstName = "firstName"
            this.lastName = "lastName"
            this.mobileNumber = "mobileNumber"
            this.password = "password"
            this.username = "username"
        }

        Mockito.`when`(dataManager.registerUser(registerPayload)).thenReturn(successResponse)

        val result = userAuthRepositoryImp.registerUser(
            registerPayload.accountNumber,
            registerPayload.authenticationMode,
            registerPayload.email,
            registerPayload.firstName,
            registerPayload.lastName,
            registerPayload.mobileNumber,
            registerPayload.password,
            registerPayload.username
        )

        Mockito.verify(dataManager).registerUser(registerPayload)
        Assert.assertEquals(result, successResponse)
    }

    @Test
    fun testRegisterUser_ErrorResponseReceivedFromDataManager_ReturnsUnsuccessfulRegistration() {
        val error: Observable<ResponseBody?> = Observable.error(Throwable("Registration Failed"))
        val registerPayload = RegisterPayload().apply {
            this.accountNumber = "accountNumber"
            this.authenticationMode = "authenticationMode"
            this.email = "email"
            this.firstName = "firstName"
            this.lastName = "lastName"
            this.mobileNumber = "mobileNumber"
            this.password = "password"
            this.username = "username"
        }

        Mockito.`when`(dataManager.registerUser(registerPayload)).thenReturn(error)

        val result = userAuthRepositoryImp.registerUser(
            registerPayload.accountNumber,
            registerPayload.authenticationMode,
            registerPayload.email,
            registerPayload.firstName,
            registerPayload.lastName,
            registerPayload.mobileNumber,
            registerPayload.password,
            registerPayload.username
        )

        Mockito.verify(dataManager).registerUser(registerPayload)
        Assert.assertEquals(result, error)
    }

    @Test
    fun testLogin_SuccessResponseReceivedFromDataManager_ReturnsUserSuccessfully() {
        val mockLoginPayload = LoginPayload().apply {
            this.username = "username"
            this.password = "password"
        }
        val successResponse : Observable<User?> = Observable.just(mockUser)
        Mockito.`when`(
            dataManager.login(mockLoginPayload)
        ).thenReturn(successResponse)

        val result = userAuthRepositoryImp.login("username", "password")

        Mockito.verify(dataManager).login(mockLoginPayload)
        Assert.assertEquals(result, successResponse)
    }

    @Test
    fun testLogin_ErrorResponseReceivedFromDataManager_ReturnsError() {
        val mockLoginPayload = LoginPayload().apply {
            this.username = "username"
            this.password = "password"
        }
        val errorResponse : Observable<User?> = Observable.error(Throwable("Login Failed"))
        Mockito.`when`(
            dataManager.login(mockLoginPayload)
        ).thenReturn(errorResponse)

        val result = userAuthRepositoryImp.login("username", "password")

        Mockito.verify(dataManager).login(mockLoginPayload)
        Assert.assertEquals(result, errorResponse)
    }

    @Test
    fun testLoadClient_SuccessResponseReceivedFromDataManager_ReturnsClientPageSuccessfully() {
        val successResponse : Observable<Page<Client?>?> = Observable.just(mockClientPage)
        Mockito.`when`(
            dataManager.clients
        ).thenReturn(successResponse)

        val result = userAuthRepositoryImp.loadClient()

        Mockito.verify(dataManager).clients
        Assert.assertEquals(result, successResponse)
    }

    @Test
    fun testLoadClient_ErrorResponseReceivedFromDataManager_ReturnsError() {
        val errorResponse : Observable<Page<Client?>?> = Observable.error(Throwable("Load Client Failed"))
        Mockito.`when`(
            dataManager.clients
        ).thenReturn(errorResponse)

        val result = userAuthRepositoryImp.loadClient()

        Mockito.verify(dataManager).clients
        Assert.assertEquals(result, errorResponse)
    }
}