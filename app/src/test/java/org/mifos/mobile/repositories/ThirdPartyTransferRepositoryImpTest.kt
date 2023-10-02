package org.mifos.mobile.repositories

import CoroutineTestRule
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
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
@ExperimentalCoroutinesApi
class ThirdPartyTransferRepositoryImpTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

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
        val response: Response<AccountOptionsTemplate?> =
            Response.success(Mockito.mock(AccountOptionsTemplate::class.java))

        `when`(dataManager.thirdPartyTransferTemplate()).thenReturn(response)

        val result = transferRepositoryImp.thirdPartyTransferTemplate()

        assertEquals(result, response)
    }

    @Test
    fun testThirdPartyTransferTemplate_Unsuccessful() = runBlocking {
        val error: Response<AccountOptionsTemplate?> =
            Response.error(404, "error".toResponseBody(null))
        `when`(dataManager.thirdPartyTransferTemplate()).thenReturn(error)

        val result = transferRepositoryImp.thirdPartyTransferTemplate()

        assertEquals(result, error)
    }

    @Test
    fun testBeneficiaryList_Successful() = runBlocking {
        val list1 = Mockito.mock(Beneficiary::class.java)
        val list2 = Mockito.mock(Beneficiary::class.java)

        val response: Response<List<Beneficiary?>?> = Response.success(listOf(list1, list2))

        `when`(dataManager.beneficiaryList()).thenReturn(response)

        val result = transferRepositoryImp.beneficiaryList()
        assertEquals(result, response)
    }

    @Test
    fun testBeneficiaryList_Unsuccessful() = runBlocking {
        val error: Response<List<Beneficiary?>?> =
            Response.error(404, "error".toResponseBody(null))

        `when`(dataManager.beneficiaryList()).thenReturn(error)

        val result = transferRepositoryImp.beneficiaryList()

        assertEquals(result, error)
    }
}