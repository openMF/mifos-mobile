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
import org.mifos.mobile.repositories.ClientRepository
import org.mifos.mobile.repositories.UserAuthRepository
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.UiState
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import kotlin.RuntimeException

@RunWith(MockitoJUnitRunner::class)
class UpdatePasswordViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var userAuthRepositoryImp: UserAuthRepository

    @Mock
    lateinit var clientRepositoryImp: ClientRepository

    @Mock
    private lateinit var updatePasswordUiStateObserver: Observer<UiState>

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
    fun testUpdateAccountPassword_SuccessReceivedFromRepository_ReturnsSuccess() {
        val responseBody = Mockito.mock(ResponseBody::class.java)
        Mockito.`when`(
            userAuthRepositoryImp.updateAccountPassword(Mockito.anyString(), Mockito.anyString())
        ).thenReturn(Observable.just(responseBody))

        updatePasswordViewModel.updateAccountPassword("newPassword", "newPassword")
        Mockito.verify(updatePasswordUiStateObserver).onChanged(UiState.Loading)
        Mockito.verify(updatePasswordUiStateObserver).onChanged(UiState.Success)
        Mockito.verify(clientRepositoryImp).updateAuthenticationToken("newPassword")
        Mockito.verifyNoMoreInteractions(updatePasswordUiStateObserver)
    }

    @Test
    fun testUpdateAccountPassword_ErrorReceivedFromRepository_ReturnsError() {
        val error = RuntimeException("fail")
        Mockito.`when`(
            userAuthRepositoryImp.updateAccountPassword(Mockito.anyString(), Mockito.anyString())
        ).thenReturn(Observable.error(error))

        updatePasswordViewModel.updateAccountPassword("newPassword", "newPassword")

        Mockito.verify(updatePasswordUiStateObserver).onChanged(UiState.Loading)
        Mockito.verify(updatePasswordUiStateObserver).onChanged(UiState.Error(error))
        Mockito.verifyNoMoreInteractions(updatePasswordUiStateObserver)
    }


    @After
    fun tearDown() {
        updatePasswordViewModel.updatePasswordUiState.removeObserver(updatePasswordUiStateObserver)
    }
}