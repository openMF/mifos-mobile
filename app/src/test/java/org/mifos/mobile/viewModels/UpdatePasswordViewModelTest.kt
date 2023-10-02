package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.*
import org.junit.runner.RunWith
import org.mifos.mobile.repositories.ClientRepository
import org.mifos.mobile.repositories.UserAuthRepository
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.RegistrationUiState
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class UpdatePasswordViewModelTest {

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
    lateinit var clientRepositoryImp: ClientRepository

    @Mock
    private lateinit var updatePasswordUiStateObserver: Observer<RegistrationUiState>

    private lateinit var updatePasswordViewModel: UpdatePasswordViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        updatePasswordViewModel =
            UpdatePasswordViewModel(userAuthRepositoryImp, clientRepositoryImp)
        updatePasswordViewModel.updatePasswordUiState.observeForever(updatePasswordUiStateObserver)
    }

    @Test
    fun testIsInputFieldEmpty_WithEmptyStringInput_ReturnsTrue() {
        val result = updatePasswordViewModel.isInputFieldEmpty("")
        Assert.assertTrue(result)
    }

    @Test
    fun testIsInputFieldEmpty_WithNonEmptyStringInput_ReturnsFalse() {
        val result = updatePasswordViewModel.isInputFieldEmpty("nonEmptyStringInput")
        Assert.assertFalse(result)
    }

    @Test
    fun testIsInputLengthInadequate_WithAdequateLengthInput_ReturnsFalse() {
        val result = updatePasswordViewModel.isInputLengthInadequate("Password123")
        Assert.assertFalse(result)
    }

    @Test
    fun testIsInputLengthInadequate_WithInadequateLengthInput_ReturnsTrue() {
        val result = updatePasswordViewModel.isInputLengthInadequate("")
        Assert.assertTrue(result)
    }

    @Test
    fun testValidatePasswordMatch_WithSamePasswords_ReturnsTrue() {
        val result = updatePasswordViewModel.validatePasswordMatch("password", "password")
        Assert.assertTrue(result)
    }

    @Test
    fun testValidatePasswordMatch_WithDifferentPasswords_ReturnsFalse() {
        val result = updatePasswordViewModel.validatePasswordMatch("password1", "password2")
        Assert.assertFalse(result)
    }

    @Test
    fun testUpdateAccountPassword_SuccessReceivedFromRepository_ReturnsSuccess() = runBlocking {
        val responseBody = Mockito.mock(ResponseBody::class.java)
        Mockito.`when`(
            userAuthRepositoryImp.updateAccountPassword(Mockito.anyString(), Mockito.anyString())
        ).thenReturn(Response.success(responseBody))

        updatePasswordViewModel.updateAccountPassword("newPassword", "newPassword")
        Mockito.verify(updatePasswordUiStateObserver).onChanged(RegistrationUiState.Loading)
        Mockito.verify(updatePasswordUiStateObserver).onChanged(RegistrationUiState.Success)
        Mockito.verify(clientRepositoryImp).updateAuthenticationToken("newPassword")
        Mockito.verifyNoMoreInteractions(updatePasswordUiStateObserver)
    }

    @Test
    fun testUpdateAccountPassword_ErrorReceivedFromRepository_ReturnsError() = runBlocking {
        Mockito.`when`(
            userAuthRepositoryImp.updateAccountPassword(Mockito.anyString(), Mockito.anyString())
        ).thenReturn(Response.error(404, "error".toResponseBody(null)))

        updatePasswordViewModel.updateAccountPassword("newPassword", "newPassword")

        Mockito.verify(updatePasswordUiStateObserver).onChanged(RegistrationUiState.Loading)
        Mockito.verify(updatePasswordUiStateObserver)
            .onChanged(Mockito.any(RegistrationUiState.Error::class.java))
        Mockito.verifyNoMoreInteractions(updatePasswordUiStateObserver)
    }

    @After
    fun tearDown() {
        updatePasswordViewModel.updatePasswordUiState.removeObserver(updatePasswordUiStateObserver)
    }
}