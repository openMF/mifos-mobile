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
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class ThirdPartyTransferRepositoryImpTest {

    @Mock
    lateinit var dataManager: DataManager

    lateinit var transferRepositoryImp: ThirdPartyTransferRepositoryImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        transferRepositoryImp = ThirdPartyTransferRepositoryImp(dataManager)
    }

    @Test
    fun testThirdPartyTransferTemplate_Successful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val response: Response<AccountOptionsTemplate?> =
            Response.success(Mockito.mock(AccountOptionsTemplate::class.java))

        `when`(dataManager.thirdPartyTransferTemplate()).thenReturn(response)

        val result = transferRepositoryImp.thirdPartyTransferTemplate()

        assertEquals(result, response)
        Dispatchers.resetMain()
    }

    @Test
    fun testThirdPartyTransferTemplate_Unsuccessful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val error: Response<AccountOptionsTemplate?> =
            Response.error(404, ResponseBody.create(null, "error"))
        `when`(dataManager.thirdPartyTransferTemplate()).thenReturn(error)

        val result = transferRepositoryImp.thirdPartyTransferTemplate()

        assertEquals(result, error)
        Dispatchers.resetMain()
    }

    @Test
    fun testBeneficiaryList_Successful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val list1 = Mockito.mock(Beneficiary::class.java)
        val list2 = Mockito.mock(Beneficiary::class.java)

        val response: Response<List<Beneficiary?>?> = Response.success(listOf(list1, list2))

        `when`(dataManager.beneficiaryList()).thenReturn(response)

        val result = transferRepositoryImp.beneficiaryList()
        assertEquals(result, response)
        Dispatchers.resetMain()
    }

    @Test
    fun testBeneficiaryList_Unsuccessful() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val error: Response<List<Beneficiary?>?> =
            Response.error(404, ResponseBody.create(null, "error"))

        `when`(dataManager.beneficiaryList()).thenReturn(error)

        val result = transferRepositoryImp.beneficiaryList()

        assertEquals(result, error)
        Dispatchers.resetMain()
    }
}