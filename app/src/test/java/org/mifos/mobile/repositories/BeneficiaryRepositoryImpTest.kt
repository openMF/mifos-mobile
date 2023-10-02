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
import org.mifos.mobile.models.beneficiary.BeneficiaryPayload
import org.mifos.mobile.models.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.models.templates.beneficiary.BeneficiaryTemplate
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class BeneficiaryRepositoryImpTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var dataManager: DataManager

    private lateinit var beneficiaryRepositoryImp: BeneficiaryRepositoryImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        beneficiaryRepositoryImp = BeneficiaryRepositoryImp(dataManager)
    }

    @Test
    fun testBeneficiaryTemplate_Successful() = runBlocking {
        val success: Response<BeneficiaryTemplate?> =
            Response.success(mock(BeneficiaryTemplate::class.java))

        `when`(dataManager.beneficiaryTemplate()).thenReturn(success)

        val result = beneficiaryRepositoryImp.beneficiaryTemplate()

        verify(dataManager).beneficiaryTemplate()
        assertEquals(result, success)
    }

    @Test
    fun testBeneficiaryTemplate_Unsuccessful() = runBlocking {
        val error: Response<BeneficiaryTemplate?> =
            Response.error(404, "error".toResponseBody(null))
        `when`(dataManager.beneficiaryTemplate()).thenReturn(error)

        val result = beneficiaryRepositoryImp.beneficiaryTemplate()

        verify(dataManager).beneficiaryTemplate()
        assertEquals(result, error)
    }

    @Test
    fun testCreateBeneficiary_Successful() = runBlocking {
        val success: Response<ResponseBody?> =
            Response.success(mock(ResponseBody::class.java))

        val beneficiaryPayload = mock(BeneficiaryPayload::class.java)

        `when`(dataManager.createBeneficiary(beneficiaryPayload)).thenReturn(success)

        val result = beneficiaryRepositoryImp.createBeneficiary(beneficiaryPayload)

        verify(dataManager).createBeneficiary(beneficiaryPayload)
        assertEquals(result, success)
    }

    @Test
    fun testCreateBeneficiary_Unsuccessful() = runBlocking {
        val error: Response<ResponseBody?> =
            Response.error(404, "error".toResponseBody(null))
        val beneficiaryPayload = mock(BeneficiaryPayload::class.java)

        `when`(dataManager.createBeneficiary(beneficiaryPayload)).thenReturn(error)

        val result = beneficiaryRepositoryImp.createBeneficiary(beneficiaryPayload)

        verify(dataManager).createBeneficiary(beneficiaryPayload)
        assertEquals(result, error)
    }

    @Test
    fun testUpdateBeneficiary_Successful() = runBlocking {
        val success: Response<ResponseBody?> =
            Response.success(mock(ResponseBody::class.java))

        val beneficiaryUpdatePayload = mock(BeneficiaryUpdatePayload::class.java)

        `when`(dataManager.updateBeneficiary(123L, beneficiaryUpdatePayload)).thenReturn(success)

        val result = beneficiaryRepositoryImp.updateBeneficiary(123L, beneficiaryUpdatePayload)
        assertEquals(result, success)
    }

    @Test
    fun testUpdateBeneficiary_Unsuccessful() = runBlocking {
        val error: Response<ResponseBody?> =
            Response.error(404, "error".toResponseBody(null))

        val beneficiaryUpdatePayload = mock(BeneficiaryUpdatePayload::class.java)

        `when`(dataManager.updateBeneficiary(123L, beneficiaryUpdatePayload)).thenReturn(error)

        val result = beneficiaryRepositoryImp.updateBeneficiary(123L, beneficiaryUpdatePayload)
        assertEquals(result, error)
    }


    @Test
    fun testDeleteBeneficiary_Successful() = runBlocking {
        val success: Response<ResponseBody?> =
            Response.success(mock(ResponseBody::class.java))

        `when`(dataManager.deleteBeneficiary(123L)).thenReturn(success)

        val result = beneficiaryRepositoryImp.deleteBeneficiary(123L)
        assertEquals(result, success)
    }

    @Test
    fun testDeleteBeneficiary_Unsuccessful() = runBlocking {
        val error: Response<ResponseBody?> =
            Response.error(404, "error".toResponseBody(null))

        `when`(dataManager.deleteBeneficiary(123L)).thenReturn(error)

        val result = beneficiaryRepositoryImp.deleteBeneficiary(123L)
        assertEquals(result, error)
    }

    @Test
    fun testBeneficiaryList_Successful() = runBlocking {
        val success: Response<List<Beneficiary?>?> =
            Response.success(Beneficiary::class.java) as Response<List<Beneficiary?>?>

        `when`(dataManager.beneficiaryList()).thenReturn(success)

        val result = beneficiaryRepositoryImp.beneficiaryList()
        assertEquals(result, success)
    }

    @Test
    fun testBeneficiaryList_Unsuccessful() = runBlocking {
        val error: Response<List<Beneficiary?>?> =
            Response.error(404, "error".toResponseBody(null))

        `when`(dataManager.beneficiaryList()).thenReturn(error)

        val result = beneficiaryRepositoryImp.beneficiaryList()
        assertEquals(result, error)
    }
}