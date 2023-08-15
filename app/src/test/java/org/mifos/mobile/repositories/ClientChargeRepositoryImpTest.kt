package org.mifos.mobile.repositories

import CoroutineTestRule
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.Charge
import org.mifos.mobile.models.Page
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class ClientChargeRepositoryImpTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var dataManager: DataManager

    private lateinit var clientChargeRepositoryImp: ClientChargeRepositoryImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        clientChargeRepositoryImp = ClientChargeRepositoryImp(dataManager)
    }

    @Test
    fun testGetClientCharges_Successful() = runBlocking {
        val success: Response<Page<Charge?>?> =
            Mockito.mock(Response::class.java) as Response<Page<Charge?>?>

        `when`(dataManager.getClientCharges(123L)).thenReturn(success)

        val result = clientChargeRepositoryImp.getClientCharges(123L)
        assertEquals(result, success)
    }

    @Test
    fun testGetClientCharges_Unsuccessful() = runBlocking {
        val error: Response<Page<Charge?>?> =
            Response.error(404, ResponseBody.create(null, "error"))

        `when`(dataManager.getClientCharges(123L)).thenReturn(error)

        val result = clientChargeRepositoryImp.getClientCharges(123L)
        assertEquals(result, error)
    }

    @Test
    fun testGetLoanCharges_Successful() = runBlocking {
        val success: Response<List<Charge?>?> =
            Mockito.mock(Response::class.java) as Response<List<Charge?>?>

        `when`(dataManager.getLoanCharges(123L)).thenReturn(success)

        val result = clientChargeRepositoryImp.getLoanCharges(123L)
        assertEquals(result, success)
    }

    @Test
    fun testGetLoanCharges_Unsuccessful() = runBlocking {
        val error: Response<List<Charge?>?> =
            Response.error(404, ResponseBody.create(null, "error"))

        `when`(dataManager.getLoanCharges(123L)).thenReturn(error)

        val result = clientChargeRepositoryImp.getLoanCharges(123L)
        assertEquals(result, error)
    }

    @Test
    fun testGetSavingsCharges_Successful() = runBlocking {
        val success: Response<List<Charge?>?> =
            Mockito.mock(Response::class.java) as Response<List<Charge?>?>

        `when`(dataManager.getSavingsCharges(123L)).thenReturn(success)

        val result = clientChargeRepositoryImp.getSavingsCharges(123L)
        assertEquals(result, success)
    }

    @Test
    fun testGetSavingsCharges_Unsuccessful() = runBlocking {
        val error: Response<List<Charge?>?> =
            Response.error(404, ResponseBody.create(null, "error"))

        `when`(dataManager.getSavingsCharges(123L)).thenReturn(error)

        val result = clientChargeRepositoryImp.getSavingsCharges(123L)
        assertEquals(result, error)
    }

    @Test
    fun testClientLocalCharges_Successful() = runBlocking {
        val success: Response<Page<Charge?>?> =
            Mockito.mock(Response::class.java) as Response<Page<Charge?>?>

        `when`(dataManager.clientLocalCharges()).thenReturn(success)

        val result = clientChargeRepositoryImp.clientLocalCharges()
        assertEquals(result, success)
    }

    @Test
    fun testClientLocalCharges_Unsuccessful() = runBlocking {
        val error: Response<Page<Charge?>?> =
            Response.error(404, ResponseBody.create(null, "error"))

        `when`(dataManager.clientLocalCharges()).thenReturn(error)

        val result = clientChargeRepositoryImp.clientLocalCharges()
        assertEquals(result, error)
    }
}