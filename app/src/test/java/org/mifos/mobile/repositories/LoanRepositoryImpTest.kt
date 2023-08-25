package org.mifos.mobile.repositories

import CoroutineTestRule
import app.cash.turbine.test
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.models.accounts.loan.LoanWithdraw
import org.mifos.mobile.models.templates.loans.LoanTemplate
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner.Silent::class)
@ExperimentalCoroutinesApi
class LoanRepositoryImpTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var dataManager: DataManager

    @Mock
    lateinit var loanWithdraw: LoanWithdraw

    private lateinit var loanRepositoryImp: LoanRepositoryImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        loanRepositoryImp = LoanRepositoryImp(dataManager)
    }

    @Test
    fun testGetLoanWithAssociations_Successful(): Unit = runBlocking {
        val success: LoanWithAssociations = Mockito.mock(LoanWithAssociations::class.java)

        `when`(
            dataManager.getLoanWithAssociations(
                Mockito.anyString(),
                Mockito.anyLong()
            )
        ).thenReturn(success)

        loanRepositoryImp.getLoanWithAssociations(
            "associationType",
            1
        )?.test {
            assertEquals(success, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun testGetLoanWithAssociations_Unsuccessful(): Unit = runBlocking {
        val error = RuntimeException("Network Error")
        `when`(
            dataManager.getLoanWithAssociations(
                Mockito.anyString(),
                Mockito.anyLong()
            )
        ).thenThrow(error)

        kotlin.runCatching {
            loanRepositoryImp.getLoanWithAssociations("associationType", 1)
        }.exceptionOrNull()
    }

    @Test
    fun testWithdrawLoanAccount_Successful(): Unit = runBlocking {
        val success: ResponseBody = Mockito.mock(ResponseBody::class.java)

        `when`(dataManager.withdrawLoanAccount(1, loanWithdraw)).thenReturn(success)

        loanRepositoryImp.withdrawLoanAccount(1, loanWithdraw)?.test {
            assertEquals(success, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun testWithdrawLoanAccount_Unsuccessful(): Unit = runBlocking {
        val error = RuntimeException("Network Error")
        `when`(dataManager.withdrawLoanAccount(1, loanWithdraw)).thenThrow(error)

        kotlin.runCatching {
            loanRepositoryImp.withdrawLoanAccount(1, loanWithdraw)
        }.exceptionOrNull()
    }

    @Test
    fun testTemplate_Successful(): Unit = runBlocking {
        val success: LoanTemplate = Mockito.mock(LoanTemplate::class.java)
        `when`(dataManager.loanTemplate()).thenReturn(success)

        loanRepositoryImp.template()?.test {
            assertEquals(success, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun testTemplate_Unsuccessful(): Unit = runBlocking {
        val error = RuntimeException("Network Error")
        `when`(dataManager.loanTemplate()).thenThrow(error)

        kotlin.runCatching {
            loanRepositoryImp.template()
        }.exceptionOrNull()
    }

    @Test
    fun testGetLoanTemplateByProduct_Successful(): Unit = runBlocking {
        val success: LoanTemplate = Mockito.mock(LoanTemplate::class.java)
        `when`(dataManager.getLoanTemplateByProduct(1)).thenReturn(success)

        loanRepositoryImp.getLoanTemplateByProduct(1)?.test {
            assertEquals(success, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun testGetLoanTemplateByProduct_Unsuccessful(): Unit = runBlocking {
        val error = RuntimeException("Network Error")
        `when`(dataManager.getLoanTemplateByProduct(1)).thenThrow(error)
        kotlin.runCatching {
            loanRepositoryImp.getLoanTemplateByProduct(1)
        }.exceptionOrNull()
    }
}