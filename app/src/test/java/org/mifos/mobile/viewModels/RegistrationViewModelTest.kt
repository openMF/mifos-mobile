package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.*
import org.junit.runner.RunWith
import org.mifos.mobile.core.data.repositories.UserAuthRepository
import org.mifos.mobile.feature.auth.registration.viewmodel.RegistrationViewModel
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.feature.registration.utils.RegistrationUiState
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

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

    private lateinit var registrationViewModel: RegistrationViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        registrationViewModel =
            RegistrationViewModel(
                userAuthRepositoryImp
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
        runTest {
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
            ).thenReturn(flowOf(responseBody))
            registrationViewModel.registrationUiState.test {
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
                assertEquals(RegistrationUiState.Initial, awaitItem())
                assertEquals(RegistrationUiState.Loading, awaitItem())
                assertEquals(RegistrationUiState.Success, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test(expected = Exception::class)
    fun testRegisterUser_UnsuccessfulRegistrationReceivedFromRepository_ReturnsRegistrationUnsuccessful() =
        runTest {
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
            ).thenThrow(Exception("Error occurred"))
            registrationViewModel.registrationUiState.test {
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
                assertEquals(RegistrationUiState.Initial, awaitItem())
                assertEquals(RegistrationUiState.Loading, awaitItem())
                assertEquals(RegistrationUiState.Error(0), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun testVerifyUser_SuccessfulRegistrationVerificationReceivedFromRepository_ReturnsRegistrationVerificationSuccessful() =
        runTest {
            Mockito.`when`(
                userAuthRepositoryImp.verifyUser(Mockito.anyString(), Mockito.anyString())
            ).thenReturn(flowOf(Mockito.mock(ResponseBody::class.java)))

            registrationViewModel.registrationVerificationUiState.test {
            registrationViewModel.verifyUser("authenticationToken", "requestId")
            assertEquals(RegistrationUiState.Initial,awaitItem())
            assertEquals(RegistrationUiState.Loading,awaitItem())
            assertEquals(RegistrationUiState.Success,awaitItem())
            cancelAndIgnoreRemainingEvents()
            }
        }

    @Test(expected = Exception::class)
    fun testVerifyUser_UnsuccessfulRegistrationVerificationReceivedFromRepository_ReturnsRegistrationVerificationUnsuccessful() =
        runTest {
            val errorResponse = Exception("Error occurred")
            Mockito.`when`(
                userAuthRepositoryImp.verifyUser(Mockito.anyString(), Mockito.anyString())
            ).thenThrow(errorResponse)
            registrationViewModel.registrationVerificationUiState.test{
                registrationViewModel.verifyUser("authenticationToken", "requestId")
                assertEquals(RegistrationUiState.Initial,awaitItem())
                assertEquals(RegistrationUiState.Loading,awaitItem())
                assertEquals(RegistrationUiState.Error(0),awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @After
    fun tearDown() {
       
    }
}