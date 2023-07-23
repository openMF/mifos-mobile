package org.mifos.mobile.repositories

import io.reactivex.Observable
import junit.framework.Assert.assertEquals
import org.junit.Before
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


@RunWith(MockitoJUnitRunner::class)
class ClientChargeRepositoryImpTest {
    @Mock
    lateinit var dataManager: DataManager

    private lateinit var clientChargeRepositoryImp: ClientChargeRepositoryImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        clientChargeRepositoryImp = ClientChargeRepositoryImp(dataManager)
    }

    @Test
    fun testGetClientCharges_Successful() {
        val success: Observable<Page<Charge?>?> =
            Mockito.mock(Observable::class.java) as Observable<Page<Charge?>?>

        `when`(dataManager.getClientCharges(123L)).thenReturn(success)

        val result = clientChargeRepositoryImp.getClientCharges(123L)
        assertEquals(result, success)
    }

    @Test
    fun testGetClientCharges_Unsuccessful() {
        val error: Observable<Page<Charge?>?> =
            Observable.error(Throwable("Failed to get client charge"))

        `when`(dataManager.getClientCharges(123L)).thenReturn(error)

        val result = clientChargeRepositoryImp.getClientCharges(123L)
        assertEquals(result, error)
    }

    @Test
    fun testGetLoanCharges_Successful() {
        val success: Observable<List<Charge?>?> =
            Mockito.mock(Observable::class.java) as Observable<List<Charge?>?>

        `when`(dataManager.getLoanCharges(123L)).thenReturn(success)

        val result = clientChargeRepositoryImp.getLoanCharges(123L)
        assertEquals(result, success)
    }

    @Test
    fun testGetLoanCharges_Unsuccessful() {
        val error: Observable<List<Charge?>?> =
            Observable.error(Throwable("Failed to get loan charges"))

        `when`(dataManager.getLoanCharges(123L)).thenReturn(error)

        val result = clientChargeRepositoryImp.getLoanCharges(123L)
        assertEquals(result, error)
    }

    @Test
    fun testGetSavingsCharges_Successful() {
        val success: Observable<List<Charge?>?> =
            Mockito.mock(Observable::class.java) as Observable<List<Charge?>?>

        `when`(dataManager.getSavingsCharges(123L)).thenReturn(success)

        val result = clientChargeRepositoryImp.getSavingsCharges(123L)
        assertEquals(result, success)
    }

    @Test
    fun testGetSavingsCharges_Unsuccessful() {
        val error: Observable<List<Charge?>?> =
            Observable.error(Throwable("Failed to get loan charges"))

        `when`(dataManager.getSavingsCharges(123L)).thenReturn(error)

        val result = clientChargeRepositoryImp.getSavingsCharges(123L)
        assertEquals(result, error)
    }

    @Test
    fun testClientLocalCharges_Successful() {
        val success: Observable<Page<Charge?>?> =
            Mockito.mock(Observable::class.java) as Observable<Page<Charge?>?>

        `when`(dataManager.clientLocalCharges).thenReturn(success)

        val result = clientChargeRepositoryImp.clientLocalCharges()
        assertEquals(result, success)
    }

    @Test
    fun testClientLocalCharges_Unsuccessful() {
        val error: Observable<Page<Charge?>?> =
            Observable.error(Throwable("Failed to get loan charges"))

        `when`(dataManager.clientLocalCharges).thenReturn(error)

        val result = clientChargeRepositoryImp.clientLocalCharges()
        assertEquals(result, error)
    }
}