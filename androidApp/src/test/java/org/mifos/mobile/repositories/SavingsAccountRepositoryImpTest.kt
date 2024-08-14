package org.mifos.mobile.repositories

import CoroutineTestRule
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.models.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.models.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.models.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
import org.mifos.mobile.models.templates.savings.SavingsAccountTemplate
import org.mifos.mobile.core.common.Constants
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class SavingsAccountRepositoryImpTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var dataManager: DataManager

    private lateinit var savingsAccountRepositoryImp: SavingsAccountRepository
    private val mockAccountId = 1L
    private val mockAssociationType = org.mifos.mobile.core.common.Constants.TRANSACTIONS
    private val mockClientId = 1L

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        savingsAccountRepositoryImp = SavingsAccountRepositoryImp(dataManager)
    }

    @Test
    fun testGetSavingsWithAssociations_SuccessResponseReceivedFromDataManager_ReturnsSuccess() =
        runTest {
            val mockSavingsWithAssociations = mock(SavingsWithAssociations::class.java)
            Mockito.`when`(
                dataManager.getSavingsWithAssociations(mockAccountId, mockAssociationType)
            ).thenReturn(mockSavingsWithAssociations)

            val result = savingsAccountRepositoryImp.getSavingsWithAssociations(
                mockAccountId,
                mockAssociationType
            )
            result.test {
                Assert.assertEquals(mockSavingsWithAssociations, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            Mockito.verify(dataManager)
                .getSavingsWithAssociations(mockAccountId, mockAssociationType)
        }

    @Test(expected = Exception::class)
    fun testGetSavingsWithAssociations_ErrorResponseReceivedFromDataManager_ReturnsError() =
        runTest {
            Mockito.`when`(
                dataManager.getSavingsWithAssociations(mockAccountId, mockAssociationType)
            ).thenThrow(Exception("Error occurred"))

            val result = savingsAccountRepositoryImp.getSavingsWithAssociations(
                mockAccountId,
                mockAssociationType
            )
            result.test {
                assertEquals(Throwable("Error occurred"), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            Mockito.verify(dataManager)
                .getSavingsWithAssociations(mockAccountId, mockAssociationType)

        }

    @Test
    fun testGetSavingsAccountApplicationTemplate_SuccessResponseFromDataManager_ReturnsSuccess() =
        runTest {
            val mockSavingsAccountTemplate = mock(SavingsAccountTemplate::class.java)
            Mockito.`when`(
                dataManager.getSavingAccountApplicationTemplate(mockClientId)
            ).thenReturn(mockSavingsAccountTemplate)

            val result =
                savingsAccountRepositoryImp.getSavingAccountApplicationTemplate(mockClientId)
            result.test {
                assertEquals(mockSavingsAccountTemplate, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            Mockito.verify(dataManager).getSavingAccountApplicationTemplate(mockClientId)
        }

    @Test(expected = Exception::class)
    fun testGetSavingsAccountApplicationTemplate_ErrorResponseFromDataManager_ReturnsError() =
        runTest {
            Mockito.`when`(
                dataManager.getSavingAccountApplicationTemplate(mockClientId)
            ).thenThrow(Exception("Error occurred"))

            val result =
                savingsAccountRepositoryImp.getSavingAccountApplicationTemplate(mockClientId)
            result.test {
                assertEquals(Throwable("Error occurred"), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            Mockito.verify(dataManager).getSavingAccountApplicationTemplate(mockClientId)
        }

    @Test
    fun testSubmitSavingAccountApplication_SuccessResponseFromDataManager_ReturnsSuccess() =
        runTest {
            val mockSavingsAccountApplicationPayload =
                Mockito.mock(SavingsAccountApplicationPayload::class.java)
            val responseBody = mock(ResponseBody::class.java)
            Mockito.`when`(
                dataManager.submitSavingAccountApplication(mockSavingsAccountApplicationPayload)
            ).thenReturn(responseBody)

            val result = savingsAccountRepositoryImp.submitSavingAccountApplication(
                mockSavingsAccountApplicationPayload
            )
            result.test {
                assertEquals(responseBody, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            Mockito.verify(dataManager)
                .submitSavingAccountApplication(mockSavingsAccountApplicationPayload)
        }

    @Test(expected = Exception::class)
    fun testSubmitSavingAccountApplication_ErrorResponseFromDataManager_ReturnsError() =
        runTest {
            val mockSavingsAccountApplicationPayload =
                Mockito.mock(SavingsAccountApplicationPayload::class.java)
            Mockito.`when`(
                dataManager.submitSavingAccountApplication(mockSavingsAccountApplicationPayload)
            ).thenThrow(Exception("Error occurred"))

            val result = savingsAccountRepositoryImp.submitSavingAccountApplication(
                mockSavingsAccountApplicationPayload
            )
            result.test {
                assertEquals(Throwable("Error occurred"), awaitItem())
                cancelAndIgnoreRemainingEvents()

            }
            Mockito.verify(dataManager)
                .submitSavingAccountApplication(mockSavingsAccountApplicationPayload)

        }

    @Test
    fun testUpdateSavingsAccount_SuccessResponseFromDataManager_ReturnsSuccess() = runTest {
        val mockSavingsAccountUpdatePayload = Mockito.mock(SavingsAccountUpdatePayload::class.java)
        val responseBody = mock(ResponseBody::class.java)
        Mockito.`when`(
            dataManager.updateSavingsAccount(mockAccountId, mockSavingsAccountUpdatePayload)
        ).thenReturn(responseBody)

        val result = savingsAccountRepositoryImp.updateSavingsAccount(
            mockAccountId,
            mockSavingsAccountUpdatePayload
        )
        result.test {
            assertEquals(responseBody, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        Mockito.verify(dataManager)
            .updateSavingsAccount(mockAccountId, mockSavingsAccountUpdatePayload)
    }

    @Test(expected = Exception::class)
    fun testUpdateSavingsAccount_ErrorResponseFromDataManager_ReturnsError() = runTest {
        val mockSavingsAccountUpdatePayload = Mockito.mock(SavingsAccountUpdatePayload::class.java)
        Mockito.`when`(
            dataManager.updateSavingsAccount(mockAccountId, mockSavingsAccountUpdatePayload)
        ).thenThrow(Exception("Error occurred"))

        val result = savingsAccountRepositoryImp.updateSavingsAccount(
            mockAccountId,
            mockSavingsAccountUpdatePayload
        )
        result.test {
            assertEquals(Throwable("Error occurred"), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        Mockito.verify(dataManager)
            .updateSavingsAccount(mockAccountId, mockSavingsAccountUpdatePayload)

    }

    @Test
    fun testSubmitWithdrawSavingsAccount_SuccessResponseFromDataManager_ReturnsSuccess() =
        runTest {
            val mockAccountId = "1"
            val mockSavingsAccountWithdrawPayload =
                Mockito.mock(SavingsAccountWithdrawPayload::class.java)
            val responseBody = mock(ResponseBody::class.java)
            Mockito.`when`(
                dataManager.submitWithdrawSavingsAccount(
                    mockAccountId,
                    mockSavingsAccountWithdrawPayload
                )
            ).thenReturn(responseBody)

            val result = savingsAccountRepositoryImp.submitWithdrawSavingsAccount(
                mockAccountId,
                mockSavingsAccountWithdrawPayload
            )
            result.test {
                assertEquals(responseBody, awaitItem())
                cancelAndIgnoreRemainingEvents()

            }
            Mockito.verify(dataManager)
                .submitWithdrawSavingsAccount(mockAccountId, mockSavingsAccountWithdrawPayload)
        }

    @Test(expected = Exception::class)
    fun testSubmitWithdrawSavingsAccount_ErrorResponseFromDataManager_ReturnsError() = runTest {
        val mockAccountId = "1"
        val mockSavingsAccountWithdrawPayload =
            Mockito.mock(SavingsAccountWithdrawPayload::class.java)
       Mockito.`when`(
            dataManager.submitWithdrawSavingsAccount(
                mockAccountId,
                mockSavingsAccountWithdrawPayload
            )
        ).thenThrow(Exception("Error occurred"))

        val result = savingsAccountRepositoryImp.submitWithdrawSavingsAccount(
            mockAccountId,
            mockSavingsAccountWithdrawPayload
        )
        result.test {
            assertEquals(Throwable("Error occurred"), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        Mockito.verify(dataManager)
            .submitWithdrawSavingsAccount(mockAccountId, mockSavingsAccountWithdrawPayload)

    }

    @Test
    fun testLoanAccountTransferTemplate_SuccessResponseFromDataManager_ReturnsSuccess() =
        runTest {
            val responseBody=mock(AccountOptionsTemplate::class.java)
            Mockito.`when`(
                dataManager.accountTransferTemplate()
            ).thenReturn(responseBody)

            val result = savingsAccountRepositoryImp.loanAccountTransferTemplate()
            result.test {
                assertEquals(responseBody, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            Mockito.verify(dataManager).accountTransferTemplate()
        }

    @Test(expected = Exception::class)
    fun testLoanAccountTransferTemplate_ErrorResponseFromDataManager_ReturnsError() = runTest {
       Mockito.`when`(
            dataManager.accountTransferTemplate()
        ).thenThrow(Exception("Error occurred"))

        val result = savingsAccountRepositoryImp.loanAccountTransferTemplate()
        result.test {
            assertEquals(Throwable("Error occurred"), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        Mockito.verify(dataManager).accountTransferTemplate()

    }
}