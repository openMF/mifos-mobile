package org.mifos.mobile.repositories

import CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.FakeRemoteDataSource
import org.mifos.mobile.FakeRemoteDataSource.userVerify
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.User
import org.mifos.mobile.models.payload.LoginPayload
import org.mifos.mobile.models.register.RegisterPayload
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class UserAuthRepositoryImpTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var dataManager: DataManager

    private lateinit var userAuthRepositoryImp: UserAuthRepository
    private lateinit var mockUser: User


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        userAuthRepositoryImp = UserAuthRepositoryImp(dataManager)
        mockUser = FakeRemoteDataSource.user
    }

    @Test
    fun testRegisterUser_SuccessResponseReceivedFromDataManager_ReturnSuccessfulRegistration() =
        runBlocking {
            val successResponse: Response<ResponseBody?> =
                Response.success(Mockito.mock(ResponseBody::class.java))
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
    fun testRegisterUser_ErrorResponseReceivedFromDataManager_ReturnsUnsuccessfulRegistration() =
        runBlocking {
            val error: Response<ResponseBody?> =
                Response.error(404, ResponseBody.create(null, "error"))
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
    fun testLogin_SuccessResponseReceivedFromDataManager_ReturnsUserSuccessfully() = runBlocking {
        val mockLoginPayload = LoginPayload().apply {
            this.username = "username"
            this.password = "password"
        }
        val successResponse: Response<User?> = Response.success(mockUser)
        Mockito.`when`(
            dataManager.login(mockLoginPayload)
        ).thenReturn(successResponse)

        val result = userAuthRepositoryImp.login("username", "password")

        Mockito.verify(dataManager).login(mockLoginPayload)
        Assert.assertEquals(result, successResponse)
    }

    @Test
    fun testLogin_ErrorResponseReceivedFromDataManager_ReturnsError() = runBlocking {
        val mockLoginPayload = LoginPayload().apply {
            this.username = "username"
            this.password = "password"
        }
        val errorResponse: Response<User?> = Response.error(404, ResponseBody.create(null, "error"))
        Mockito.`when`(
            dataManager.login(mockLoginPayload)
        ).thenReturn(errorResponse)

        val result = userAuthRepositoryImp.login("username", "password")

        Mockito.verify(dataManager).login(mockLoginPayload)
        Assert.assertEquals(result, errorResponse)
    }

    @Test
    fun testVerifyUser_SuccessResponseReceivedFromDataManager_ReturnsSuccessfulRegistrationVerification() =
        runBlocking {
            val successResponse: Response<ResponseBody?> =
                Response.success(Mockito.mock(ResponseBody::class.java))
            Mockito.`when`(
                dataManager.verifyUser(userVerify)
            ).thenReturn(successResponse)

            val result =
                userAuthRepositoryImp.verifyUser(
                    userVerify.authenticationToken,
                    userVerify.requestId
                )

            Mockito.verify(dataManager).verifyUser(userVerify)
            Assert.assertEquals(result, successResponse)
        }

    @Test
    fun testVerifyUser_ErrorResponseReceivedFromDataManager_ReturnsUnsuccessfulRegistrationVerification() =
        runBlocking {
            val errorResponse: Response<ResponseBody?> =
                Response.error(404, ResponseBody.create(null, "error"))
            Mockito.`when`(
                dataManager.verifyUser(userVerify)
            ).thenReturn(errorResponse)

            val result =
                userAuthRepositoryImp.verifyUser(
                    userVerify.authenticationToken,
                    userVerify.requestId
                )
            Mockito.verify(dataManager).verifyUser(userVerify)
            Assert.assertEquals(result, errorResponse)
        }
}