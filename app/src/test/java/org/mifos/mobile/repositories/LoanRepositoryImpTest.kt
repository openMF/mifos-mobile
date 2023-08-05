package org.mifos.mobile.repositories

import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.models.accounts.loan.LoanWithdraw
import org.mifos.mobile.models.templates.loans.LoanTemplate
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class LoanRepositoryImpTest {
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
    fun testGetLoanWithAssociations_Successful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val success: Response<LoanWithAssociations?> =
            Response.success(Mockito.mock(LoanWithAssociations::class.java))

        `when`(
            dataManager.getLoanWithAssociations(
                Mockito.anyString(),
                Mockito.anyLong()
            )
        ).thenReturn(success)

        val result = loanRepositoryImp.getLoanWithAssociations(
            "associationType",
            1
        )
        verify(dataManager).getLoanWithAssociations(Mockito.anyString(), Mockito.anyLong())
        assertEquals(result, success)
        Dispatchers.resetMain()
    }

    @Test
    fun testGetLoanWithAssociations_Unsuccessful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val error: Response<LoanWithAssociations?> =
            Response.error(404, ResponseBody.create(null, "error"))
        `when`(
            dataManager.getLoanWithAssociations(
                Mockito.anyString(),
                Mockito.anyLong()
            )
        ).thenReturn(error)

        val result = loanRepositoryImp.getLoanWithAssociations(
            "associationType",
            1
        )

        verify(dataManager).getLoanWithAssociations(Mockito.anyString(), Mockito.anyLong())
        assertEquals(result, error)
        Dispatchers.resetMain()
    }

    @Test
    fun testWithdrawLoanAccount_Successful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val success: Response<ResponseBody?> =
            Response.success(Mockito.mock(ResponseBody::class.java))

        `when`(dataManager.withdrawLoanAccount(1, loanWithdraw)).thenReturn(success)

        val result = loanRepositoryImp.withdrawLoanAccount(1, loanWithdraw)
        verify(dataManager).withdrawLoanAccount(1, loanWithdraw)
        assertEquals(result, success)
    }

    @Test
    fun testWithdrawLoanAccount_Unsuccessful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val error: Response<ResponseBody?> =
            Response.error(404, ResponseBody.create(null, "error"))
        `when`(dataManager.withdrawLoanAccount(1, loanWithdraw)).thenReturn(error)

        val result = loanRepositoryImp.withdrawLoanAccount(1, loanWithdraw)
        verify(dataManager).withdrawLoanAccount(1, loanWithdraw)
        assertEquals(result, error)
        Dispatchers.resetMain()
    }

    @Test
    fun testTemplate_Successful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val success: Response<LoanTemplate?> =
            Response.success(Mockito.mock(LoanTemplate::class.java))
        `when`(dataManager.loanTemplate()).thenReturn(success)

        val result = loanRepositoryImp.template()
        verify(dataManager).loanTemplate()
        assertEquals(result, success)
        Dispatchers.resetMain()
    }

    @Test
    fun testTemplate_Unsuccessful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val error: Response<LoanTemplate?> =
            Response.error(404, ResponseBody.create(null, "error"))
        `when`(dataManager.loanTemplate()).thenReturn(error)

        val result = loanRepositoryImp.template()
        verify(dataManager).loanTemplate()
        assertEquals(result, error)
        Dispatchers.resetMain()
    }

    @Test
    fun testGetLoanTemplateByProduct_Successful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val success: Response<LoanTemplate?> =
            Response.success(Mockito.mock(LoanTemplate::class.java))
        `when`(dataManager.getLoanTemplateByProduct(1)).thenReturn(success)

        val result = loanRepositoryImp.getLoanTemplateByProduct(1)
        verify(dataManager).getLoanTemplateByProduct(1)
        assertEquals(result, success)
        Dispatchers.resetMain()
    }

    @Test
    fun testGetLoanTemplateByProduct_Unsuccessful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val error: Response<LoanTemplate?> =
            Response.error(404, ResponseBody.create(null, "error"))
        `when`(dataManager.getLoanTemplateByProduct(1)).thenReturn(error)

        val result = loanRepositoryImp.getLoanTemplateByProduct(1)
        verify(dataManager).getLoanTemplateByProduct(1)
        assertEquals(result, error)
        Dispatchers.resetMain()
    }
}