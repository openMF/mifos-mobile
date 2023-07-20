package org.mifos.mobile.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.reactivex.Observable
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.FakeRemoteDataSource
import org.mifos.mobile.repositories.SavingsAccountRepository
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.SavingsAccountUiState
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.lang.RuntimeException

@RunWith(MockitoJUnitRunner::class)
class SavingAccountsTransactionViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var savingsAccountRepositoryImp : SavingsAccountRepository

    @Mock
    lateinit var savingAccountsTransactionUiStateObserver : Observer<SavingsAccountUiState>

    private lateinit var savingAccountsTransactionViewModel : SavingAccountsTransactionViewModel
    private val mockAccountId = 1L
    private val mockAssociationType = Constants.TRANSACTIONS
    private val mockSavingsWithAssociations = FakeRemoteDataSource.savingsWithAssociations

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        savingAccountsTransactionViewModel = SavingAccountsTransactionViewModel(savingsAccountRepositoryImp)
        savingAccountsTransactionViewModel.savingAccountsTransactionUiState.observeForever(savingAccountsTransactionUiStateObserver)
    }

    @Test
    fun testLoadSavingsWithAssociations_SuccessReceivedFromRepository_ReturnSuccessLoadingSavingsWithAssociations() {
        Mockito.`when`(
            savingsAccountRepositoryImp.getSavingsWithAssociations(mockAccountId, mockAssociationType)
        ).thenReturn(Observable.just(mockSavingsWithAssociations))

        savingAccountsTransactionViewModel.loadSavingsWithAssociations(mockAccountId)

        Mockito.verify(savingAccountsTransactionUiStateObserver).onChanged(SavingsAccountUiState.Loading)
        Mockito.verify(savingAccountsTransactionUiStateObserver).onChanged(SavingsAccountUiState.SuccessLoadingSavingsWithAssociations(mockSavingsWithAssociations))
        Mockito.verifyNoMoreInteractions(savingAccountsTransactionUiStateObserver)
    }

    @Test
    fun testLoadSavingsWithAssociations_ErrorResponseFromRepository_ReturnsError() {
        val errorResponse = RuntimeException("Error Response")
        Mockito.`when`(
            savingsAccountRepositoryImp.getSavingsWithAssociations(mockAccountId, mockAssociationType)
        ).thenReturn(Observable.error(errorResponse))

        savingAccountsTransactionViewModel.loadSavingsWithAssociations(mockAccountId)
        Mockito.verify(savingAccountsTransactionUiStateObserver).onChanged(SavingsAccountUiState.Loading)
        Mockito.verify(savingAccountsTransactionUiStateObserver).onChanged(SavingsAccountUiState.Error)
        Mockito.verifyNoMoreInteractions(savingAccountsTransactionUiStateObserver)
    }

    @After
    fun tearDown() {
        savingAccountsTransactionViewModel.savingAccountsTransactionUiState.removeObserver(savingAccountsTransactionUiStateObserver)
    }
}