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
class SavingAccountsDetailViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var savingsAccountRepositoryImp : SavingsAccountRepository

    @Mock
    lateinit var savingAccountsDetailUiStateObserver : Observer<SavingsAccountUiState>

    private lateinit var savingAccountsDetailViewModel : SavingAccountsDetailViewModel
    private val mockAccountId = 1L
    private val mockAssociationType = Constants.TRANSACTIONS
    private val mockSavingsWithAssociations = FakeRemoteDataSource.savingsWithAssociations

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        savingAccountsDetailViewModel = SavingAccountsDetailViewModel(savingsAccountRepositoryImp)
        savingAccountsDetailViewModel.savingAccountsDetailUiState.observeForever(savingAccountsDetailUiStateObserver)
    }

    @Test
    fun testLoadSavingsWithAssociations_SuccessResponseFromRepository_ReturnsSuccessLoadingSavingsWithAssociations() {
        Mockito.`when`(
            savingsAccountRepositoryImp.getSavingsWithAssociations(mockAccountId, mockAssociationType)
        ).thenReturn(Observable.just(mockSavingsWithAssociations))

        savingAccountsDetailViewModel.loadSavingsWithAssociations(mockAccountId)

        Mockito.verify(savingAccountsDetailUiStateObserver).onChanged(SavingsAccountUiState.Loading)
        Mockito.verify(savingAccountsDetailUiStateObserver).onChanged(SavingsAccountUiState.SuccessLoadingSavingsWithAssociations(mockSavingsWithAssociations))
        Mockito.verifyNoMoreInteractions(savingAccountsDetailUiStateObserver)
    }

    @Test
    fun testLoadSavingsWithAssociations_ErrorResponseFromRepository_ReturnsError() {
        val errorResponse = RuntimeException("Error Response")
        Mockito.`when`(
            savingsAccountRepositoryImp.getSavingsWithAssociations(mockAccountId, mockAssociationType)
        ).thenReturn(Observable.error(errorResponse))

        savingAccountsDetailViewModel.loadSavingsWithAssociations(mockAccountId)

        Mockito.verify(savingAccountsDetailUiStateObserver).onChanged(SavingsAccountUiState.Loading)
        Mockito.verify(savingAccountsDetailUiStateObserver).onChanged(SavingsAccountUiState.Error)
        Mockito.verifyNoMoreInteractions(savingAccountsDetailUiStateObserver)
    }

    @After
    fun tearDown() {
        savingAccountsDetailViewModel.savingAccountsDetailUiState.removeObserver(savingAccountsDetailUiStateObserver)
    }
}