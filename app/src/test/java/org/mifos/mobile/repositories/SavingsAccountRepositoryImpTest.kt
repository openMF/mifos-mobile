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
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.models.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.models.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.models.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
import org.mifos.mobile.models.templates.savings.SavingsAccountTemplate
import org.mifos.mobile.utils.Constants
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class SavingsAccountRepositoryImpTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var dataManager: DataManager

    private lateinit var savingsAccountRepositoryImp: SavingsAccountRepository
    private val mockAccountId = 1L
    private val mockAssociationType = Constants.TRANSACTIONS
    private val mockClientId = 1L

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        savingsAccountRepositoryImp = SavingsAccountRepositoryImp(dataManager)
    }

    @Test
    fun testGetSavingsWithAssociations_SuccessResponseReceivedFromDataManager_ReturnsSuccess() =
        runBlocking {
            val mockSavingsWithAssociations: Response<SavingsWithAssociations?> =
                Response.success(Mockito.mock(SavingsWithAssociations::class.java))
            Mockito.`when`(
                dataManager.getSavingsWithAssociations(mockAccountId, mockAssociationType)
            ).thenReturn(mockSavingsWithAssociations)

            val result = savingsAccountRepositoryImp.getSavingsWithAssociations(
                mockAccountId,
                mockAssociationType
            )

            Mockito.verify(dataManager)
                .getSavingsWithAssociations(mockAccountId, mockAssociationType)
            Assert.assertEquals(result, mockSavingsWithAssociations)
        }

    @Test
    fun testGetSavingsWithAssociations_ErrorResponseReceivedFromDataManager_ReturnsError() =
        runBlocking {
            val errorResponse: Response<SavingsWithAssociations?> =
                Response.error(404, ResponseBody.create(null, "error"))
            Mockito.`when`(
                dataManager.getSavingsWithAssociations(mockAccountId, mockAssociationType)
            ).thenReturn(errorResponse)

            val result = savingsAccountRepositoryImp.getSavingsWithAssociations(
                mockAccountId,
                mockAssociationType
            )

            Mockito.verify(dataManager)
                .getSavingsWithAssociations(mockAccountId, mockAssociationType)
            Assert.assertEquals(result, errorResponse)
        }

    @Test
    fun testGetSavingsAccountApplicationTemplate_SuccessResponseFromDataManager_ReturnsSuccess() =
        runBlocking {
            val mockSavingsAccountTemplate: Response<SavingsAccountTemplate?> =
                Response.success(Mockito.mock(SavingsAccountTemplate::class.java))
            Mockito.`when`(
                dataManager.getSavingAccountApplicationTemplate(mockClientId)
            ).thenReturn(mockSavingsAccountTemplate)

            val result =
                savingsAccountRepositoryImp.getSavingAccountApplicationTemplate(mockClientId)

            Mockito.verify(dataManager).getSavingAccountApplicationTemplate(mockClientId)
            Assert.assertEquals(result, mockSavingsAccountTemplate)
        }

    @Test
    fun testGetSavingsAccountApplicationTemplate_ErrorResponseFromDataManager_ReturnsError() =
        runBlocking {
            val errorResponse: Response<SavingsAccountTemplate?> =
                Response.error(404, ResponseBody.create(null, "error"))
            Mockito.`when`(
                dataManager.getSavingAccountApplicationTemplate(mockClientId)
            ).thenReturn(errorResponse)

            val result =
                savingsAccountRepositoryImp.getSavingAccountApplicationTemplate(mockClientId)

            Mockito.verify(dataManager).getSavingAccountApplicationTemplate(mockClientId)
            Assert.assertEquals(result, errorResponse)
        }

    @Test
    fun testSubmitSavingAccountApplication_SuccessResponseFromDataManager_ReturnsSuccess() =
        runBlocking {
            val mockSavingsAccountApplicationPayload =
                Mockito.mock(SavingsAccountApplicationPayload::class.java)
            val responseBody: Response<ResponseBody?> =
                Response.success(Mockito.mock(ResponseBody::class.java))
            Mockito.`when`(
                dataManager.submitSavingAccountApplication(mockSavingsAccountApplicationPayload)
            ).thenReturn(responseBody)

            val result = savingsAccountRepositoryImp.submitSavingAccountApplication(
                mockSavingsAccountApplicationPayload
            )

            Mockito.verify(dataManager)
                .submitSavingAccountApplication(mockSavingsAccountApplicationPayload)
            Assert.assertEquals(result, responseBody)
        }

    @Test
    fun testSubmitSavingAccountApplication_ErrorResponseFromDataManager_ReturnsError() =
        runBlocking {
            val mockSavingsAccountApplicationPayload =
                Mockito.mock(SavingsAccountApplicationPayload::class.java)
            val errorResponse: Response<ResponseBody?> =
                Response.error(404, ResponseBody.create(null, "error"))
            Mockito.`when`(
                dataManager.submitSavingAccountApplication(mockSavingsAccountApplicationPayload)
            ).thenReturn(errorResponse)

            val result = savingsAccountRepositoryImp.submitSavingAccountApplication(
                mockSavingsAccountApplicationPayload
            )

            Mockito.verify(dataManager)
                .submitSavingAccountApplication(mockSavingsAccountApplicationPayload)
            Assert.assertEquals(result, errorResponse)
        }

    @Test
    fun testUpdateSavingsAccount_SuccessResponseFromDataManager_ReturnsSuccess() = runBlocking {
        val mockSavingsAccountUpdatePayload = Mockito.mock(SavingsAccountUpdatePayload::class.java)
        val responseBody: Response<ResponseBody?> =
            Response.success(Mockito.mock(ResponseBody::class.java))
        Mockito.`when`(
            dataManager.updateSavingsAccount(mockAccountId, mockSavingsAccountUpdatePayload)
        ).thenReturn(responseBody)

        val result = savingsAccountRepositoryImp.updateSavingsAccount(
            mockAccountId,
            mockSavingsAccountUpdatePayload
        )

        Mockito.verify(dataManager)
            .updateSavingsAccount(mockAccountId, mockSavingsAccountUpdatePayload)
        Assert.assertEquals(result, responseBody)
    }

    @Test
    fun testUpdateSavingsAccount_ErrorResponseFromDataManager_ReturnsError() = runBlocking {
        val mockSavingsAccountUpdatePayload = Mockito.mock(SavingsAccountUpdatePayload::class.java)
        val errorResponse: Response<ResponseBody?> =
            Response.error(404, ResponseBody.create(null, "error"))
        Mockito.`when`(
            dataManager.updateSavingsAccount(mockAccountId, mockSavingsAccountUpdatePayload)
        ).thenReturn(errorResponse)

        val result = savingsAccountRepositoryImp.updateSavingsAccount(
            mockAccountId,
            mockSavingsAccountUpdatePayload
        )

        Mockito.verify(dataManager)
            .updateSavingsAccount(mockAccountId, mockSavingsAccountUpdatePayload)
        Assert.assertEquals(result, errorResponse)
    }

    @Test
    fun testSubmitWithdrawSavingsAccount_SuccessResponseFromDataManager_ReturnsSuccess() =
        runBlocking {
            val mockAccountId = "1"
            val mockSavingsAccountWithdrawPayload =
                Mockito.mock(SavingsAccountWithdrawPayload::class.java)
            val responseBody: Response<ResponseBody?> =
                Response.success(Mockito.mock(ResponseBody::class.java))
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

            Mockito.verify(dataManager)
                .submitWithdrawSavingsAccount(mockAccountId, mockSavingsAccountWithdrawPayload)
            Assert.assertEquals(result, responseBody)
        }

    @Test
    fun testSubmitWithdrawSavingsAccount_ErrorResponseFromDataManager_ReturnsError() = runBlocking {
        val mockAccountId = "1"
        val mockSavingsAccountWithdrawPayload =
            Mockito.mock(SavingsAccountWithdrawPayload::class.java)
        val errorResponse: Response<ResponseBody?> =
            Response.error(404, ResponseBody.create(null, "error"))
        Mockito.`when`(
            dataManager.submitWithdrawSavingsAccount(
                mockAccountId,
                mockSavingsAccountWithdrawPayload
            )
        ).thenReturn(errorResponse)

        val result = savingsAccountRepositoryImp.submitWithdrawSavingsAccount(
            mockAccountId,
            mockSavingsAccountWithdrawPayload
        )

        Mockito.verify(dataManager)
            .submitWithdrawSavingsAccount(mockAccountId, mockSavingsAccountWithdrawPayload)
        Assert.assertEquals(result, errorResponse)
    }

    @Test
    fun testLoanAccountTransferTemplate_SuccessResponseFromDataManager_ReturnsSuccess() =
        runBlocking {
            val responseBody: Response<AccountOptionsTemplate?> =
                Response.success(Mockito.mock(AccountOptionsTemplate::class.java))
            Mockito.`when`(
                dataManager.accountTransferTemplate()
            ).thenReturn(responseBody)

            val result = savingsAccountRepositoryImp.loanAccountTransferTemplate()

            Mockito.verify(dataManager).accountTransferTemplate()
            Assert.assertEquals(result, responseBody)
        }

    @Test
    fun testLoanAccountTransferTemplate_ErrorResponseFromDataManager_ReturnsError() = runBlocking {
        val errorResponse: Response<AccountOptionsTemplate?> =
            Response.error(404, ResponseBody.create(null, "error"))
        Mockito.`when`(
            dataManager.accountTransferTemplate()
        ).thenReturn(errorResponse)

        val result = savingsAccountRepositoryImp.loanAccountTransferTemplate()

        Mockito.verify(dataManager).accountTransferTemplate()
        Assert.assertEquals(result, errorResponse)
    }
}