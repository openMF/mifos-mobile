package org.mifos.mobile.repositories

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.FakeRemoteDataSource.userVerify
import org.mifos.mobile.api.DataManager
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

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        userAuthRepositoryImp = UserAuthRepositoryImp(dataManager)
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
    fun testVerifyUser_SuccessResponseReceivedFromDataManager_ReturnsSuccessfulRegistrationVerification() {
        val successResponse: Observable<ResponseBody?> =
            Observable.just(Mockito.mock(ResponseBody::class.java))
        Mockito.`when`(
            dataManager.verifyUser(userVerify)
        ).thenReturn(successResponse)

        val result =
            userAuthRepositoryImp.verifyUser(userVerify.authenticationToken, userVerify.requestId)

        Mockito.verify(dataManager).verifyUser(userVerify)
        Assert.assertEquals(result, successResponse)
    }

    @Test
    fun testVerifyUser_ErrorResponseReceivedFromDataManager_ReturnsUnsuccessfulRegistrationVerification() {
        val errorResponse: Observable<ResponseBody?> =
            Observable.error(Throwable("RegistrationVerification failed"))
        Mockito.`when`(
            dataManager.verifyUser(userVerify)
        ).thenReturn(errorResponse)

        val result =
            userAuthRepositoryImp.verifyUser(userVerify.authenticationToken, userVerify.requestId)
        Mockito.verify(dataManager).verifyUser(userVerify)
        Assert.assertEquals(result, errorResponse)

    }
}