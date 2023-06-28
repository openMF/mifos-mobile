package org.mifos.mobile.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.reactivex.Observable
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.repositories.UserAuthRepositoryImp
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.RegistrationUiState
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.lang.RuntimeException

@RunWith(MockitoJUnitRunner::class)
class RegistrationViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var userAuthRepositoryImp : UserAuthRepositoryImp

    @Mock
    lateinit var registrationUiStateObserver: Observer<RegistrationUiState>

    private lateinit var registrationViewModel: RegistrationViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        registrationViewModel = RegistrationViewModel(userAuthRepositoryImp)
        registrationViewModel.registrationUiState.observeForever(registrationUiStateObserver)
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
        val result = registrationViewModel.inputHasSpaces("test string")
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
        val result = registrationViewModel.isEmailInvalid("test@example.com")
        Assert.assertFalse(result)
    }

    @Test
    fun testIsEmailInvalid_WithInvalidEmailInput_ReturnsTrue() {
        val result = registrationViewModel.isEmailInvalid("testExample.com")
        Assert.assertTrue(result)
    }


    @Test
    fun testRegisterUser_SuccessfulRegistrationReceivedFromRepository_ReturnsRegistrationSuccessful() {
        val responseBody = Mockito.mock(ResponseBody::class.java)
        Mockito.`when`(
            userAuthRepositoryImp.registerUser(Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString(),)
                ).thenReturn(Observable.just(responseBody))

        registrationViewModel.registerUser("accountNumber", "authMode",
        "email", "firstName", "lastName", "mobileNumber", "password"
        , "userName")

        Mockito.verify(registrationUiStateObserver).onChanged(RegistrationUiState.Loading)
        Mockito.verify(registrationUiStateObserver).onChanged(RegistrationUiState.RegistrationSuccessful)
        Mockito.verifyNoMoreInteractions(registrationUiStateObserver)
    }

    @Test
    fun testRegisterUser_UnsuccessfulRegistrationReceivedFromRepository_ReturnsRegistrationUnsuccessful() {
        val error = RuntimeException("Registration Failed")
        Mockito.`when`(
            userAuthRepositoryImp.registerUser(
                Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())
        ).thenReturn(Observable.error(error))

        registrationViewModel.registerUser("accountNumber",
            "authMode", "email", "firstName", "lastName",
            "mobileNumber", "password", "username")

        Mockito.verify(registrationUiStateObserver).onChanged(RegistrationUiState.Loading)
        Mockito.verify(registrationUiStateObserver).onChanged(RegistrationUiState.ErrorOnRegistration(error))
        Mockito.verifyNoMoreInteractions(registrationUiStateObserver)
    }

    @After
    fun tearDown() {
        registrationViewModel.registrationUiState.removeObserver(registrationUiStateObserver)
    }
}