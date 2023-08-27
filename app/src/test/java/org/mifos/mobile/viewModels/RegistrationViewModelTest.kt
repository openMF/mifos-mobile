package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.*
import org.junit.runner.RunWith
import org.mifos.mobile.repositories.UserAuthRepository
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.RegistrationUiState
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class RegistrationViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var userAuthRepositoryImp: UserAuthRepository

    @Mock
    lateinit var registrationUiStateObserver: Observer<RegistrationUiState>

    @Mock
    lateinit var registrationVerificationUiStateObserver: Observer<RegistrationUiState>

    private lateinit var registrationViewModel: RegistrationViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        registrationViewModel = RegistrationViewModel(userAuthRepositoryImp)
        registrationViewModel.registrationUiState.observeForever(registrationUiStateObserver)
        registrationViewModel.registrationVerificationUiState.observeForever(
            registrationVerificationUiStateObserver
        )
    }

    @Test
    fun testIsInputFieldBlank_WithNonEmptyStringInput_ReturnsFalse() {
        val result = registrationViewModel.isInputFieldBlank("nonEmptyTestString")
        Assert.assertFalse(result)
    }

    @Test
    fun testIsInputFieldBlank_WithEmptyStringInput_ReturnsTrue() {
        val result = registrationViewModel.isInputFieldBlank("")
        Assert.assertTrue(result)
    }

    @Test
    fun testIsInputLengthInadequate_WithAdequateLengthInput_ReturnsFalse() {
        val result = registrationViewModel.isInputLengthInadequate("Password123")
        Assert.assertFalse(result)
    }

    @Test
    fun testIsInputLengthInadequate_WithInadequateLengthInput_ReturnsTrue() {
        val result = registrationViewModel.isInputLengthInadequate("")
        Assert.assertTrue(result)
    }

    @Test
    fun testInputHasSpaces_WithSpacesInput_ReturnsTrue() {
        val result = registrationViewModel.inputHasSpaces("testUpdateAuthenticationToken string")
        Assert.assertTrue(result)
    }

    @Test
    fun testInputHasSpaces_WithNoSpacesInput_ReturnsFalse() {
        val result = registrationViewModel.inputHasSpaces("testString")
        Assert.assertFalse(result)
    }

    @Test
    fun testHasLeadingTrailingSpaces_WithLeadingTrailingSpacesInput_ReturnsTrue() {
        val result = registrationViewModel.hasLeadingTrailingSpaces("  Hello World  ")
        Assert.assertTrue(result)
    }

    @Test
    fun testHasLeadingTrailingSpaces_WithoutLeadingTrailingSpacesInput_ReturnsFalse() {
        val result = registrationViewModel.hasLeadingTrailingSpaces("Hello World")
        Assert.assertFalse(result)
    }

    @Test
    fun testIsEmailInvalid_WithValidEmailInput_ReturnsFalse() {
        val result =
            registrationViewModel.isEmailInvalid("testUpdateAuthenticationToken@example.com")
        Assert.assertFalse(result)
    }

    @Test
    fun testIsEmailInvalid_WithInvalidEmailInput_ReturnsTrue() {
        val result = registrationViewModel.isEmailInvalid("testExample.com")
        Assert.assertTrue(result)
    }


    @Test
    fun testRegisterUser_SuccessfulRegistrationReceivedFromRepository_ReturnsRegistrationSuccessful() =
        runBlocking {
            val responseBody = Mockito.mock(ResponseBody::class.java)
            Mockito.`when`(
                userAuthRepositoryImp.registerUser(
                    Mockito.anyString(),
                    Mockito.anyString(),
                    Mockito.anyString(),
                    Mockito.anyString(),
                    Mockito.anyString(),
                    Mockito.anyString(),
                    Mockito.anyString(),
                    Mockito.anyString(),
                )
            ).thenReturn(Response.success(responseBody))

            registrationViewModel.registerUser(
                "accountNumber",
                "authMode",
                "email",
                "firstName",
                "lastName",
                "mobileNumber",
                "password",
                "userName"
            )

            Mockito.verify(registrationUiStateObserver).onChanged(RegistrationUiState.Loading)
            Mockito.verify(registrationUiStateObserver).onChanged(RegistrationUiState.Success)
            Mockito.verifyNoMoreInteractions(registrationUiStateObserver)
        }

    @Test
    fun testRegisterUser_UnsuccessfulRegistrationReceivedFromRepository_ReturnsRegistrationUnsuccessful(): Unit =
        runBlocking {
            Mockito.`when`(
                userAuthRepositoryImp.registerUser(
                    Mockito.anyString(),
                    Mockito.anyString(),
                    Mockito.anyString(),
                    Mockito.anyString(),
                    Mockito.anyString(),
                    Mockito.anyString(),
                    Mockito.anyString(),
                    Mockito.anyString()
                )
            ).thenReturn(Response.error(404, ResponseBody.create(null, "error")))

            registrationViewModel.registerUser(
                "accountNumber",
                "authMode",
                "email",
                "firstName",
                "lastName",
                "mobileNumber",
                "password",
                "username"
            )

            Mockito.verify(registrationUiStateObserver).onChanged(RegistrationUiState.Loading)
            suspend {
                Mockito.verify(registrationUiStateObserver)
                    .onChanged((RegistrationUiState.Error("Error")))
            }
        }

    @Test
    fun testVerifyUser_SuccessfulRegistrationVerificationReceivedFromRepository_ReturnsRegistrationVerificationSuccessful() =
        runBlocking {
            Mockito.`when`(
                userAuthRepositoryImp.verifyUser(Mockito.anyString(), Mockito.anyString())
            ).thenReturn(Response.success(Mockito.mock(ResponseBody::class.java)))

            registrationViewModel.verifyUser("authenticationToken", "requestId")

            Mockito.verify(registrationVerificationUiStateObserver)
                .onChanged(RegistrationUiState.Loading)
            Mockito.verify(registrationVerificationUiStateObserver)
                .onChanged(RegistrationUiState.Success)
            Mockito.verifyNoMoreInteractions(registrationUiStateObserver)
        }

    @Test
    fun testVerifyUser_UnsuccessfulRegistrationVerificationReceivedFromRepository_ReturnsRegistrationVerificationUnsuccessful() =
        runBlocking {
            Mockito.`when`(
                userAuthRepositoryImp.verifyUser(Mockito.anyString(), Mockito.anyString())
            ).thenReturn(Response.error(404, ResponseBody.create(null, "error")))

            registrationViewModel.verifyUser("authenticationToken", "requestId")

            Mockito.verify(registrationVerificationUiStateObserver)
                .onChanged(RegistrationUiState.Loading)
            Mockito.verifyNoMoreInteractions(registrationUiStateObserver)
        }

    @After
    fun tearDown() {
        registrationViewModel.registrationUiState.removeObserver(registrationUiStateObserver)
        registrationViewModel.registrationVerificationUiState.removeObserver(
            registrationVerificationUiStateObserver
        )
    }
}