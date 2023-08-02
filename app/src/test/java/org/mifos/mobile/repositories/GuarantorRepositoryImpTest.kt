package org.mifos.mobile.repositories

import io.reactivex.Observable
import junit.framework.Assert.assertEquals
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.models.guarantor.GuarantorTemplatePayload
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GuarantorRepositoryImpTest {

    @Mock
    private lateinit var dataManager: DataManager

    private lateinit var guarantorRepositoryImp: GuarantorRepositoryImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        guarantorRepositoryImp = GuarantorRepositoryImp(dataManager)
    }

    @Test
    fun testGetGuarantorTemplate_Successful() {
        val success: Observable<GuarantorTemplatePayload?> =
            Observable.just(mock(GuarantorTemplatePayload::class.java))

        `when`(dataManager.getGuarantorTemplate(123L)).thenReturn(success)

        val result = guarantorRepositoryImp.getGuarantorTemplate(123L)

        verify(dataManager).getGuarantorTemplate(123L)
        assertEquals(result, success)
    }

    @Test
    fun testGetGuarantorTemplate_Unsuccessful() {
        val error: Observable<GuarantorTemplatePayload?> =
            Observable.error(Throwable("Unable to get guarantor template"))

        `when`(dataManager.getGuarantorTemplate(123L)).thenReturn(error)

        val result = guarantorRepositoryImp.getGuarantorTemplate(123L)

        verify(dataManager).getGuarantorTemplate(123L)
        assertEquals(result, error)
    }

    @Test
    fun testCreateGuarantor_Successful() {
        val success: Observable<ResponseBody?> = Observable.just(mock(ResponseBody::class.java))
        val payload = mock(GuarantorApplicationPayload::class.java)

        `when`(dataManager.createGuarantor(123L, payload)).thenReturn(success)

        val result = guarantorRepositoryImp.createGuarantor(123L, payload)

        verify(dataManager).createGuarantor(123L, payload)
        assertEquals(result, success)
    }

    @Test
    fun testCreateGuarantor_Unsuccessful() {
        val error: Observable<ResponseBody?> =
            Observable.error(Throwable("Unable to create guarantor"))
        val payload = mock(GuarantorApplicationPayload::class.java)

        `when`(dataManager.createGuarantor(123L, payload)).thenReturn(error)

        val result = guarantorRepositoryImp.createGuarantor(123L, payload)

        verify(dataManager).createGuarantor(123L, payload)
        assertEquals(result, error)
    }

    @Test
    fun testUpdateGuarantor_Successful() {
        val success: Observable<ResponseBody?> = Observable.just(mock(ResponseBody::class.java))
        val payload = mock(GuarantorApplicationPayload::class.java)

        `when`(dataManager.updateGuarantor(payload, 11L, 22L)).thenReturn(success)

        val result = guarantorRepositoryImp.updateGuarantor(payload, 11L, 22L)

        verify(dataManager).updateGuarantor(payload, 11L, 22L)
        assertEquals(result, success)
    }

    @Test
    fun testUpdateGuarantor_Unsuccessful() {
        val error: Observable<ResponseBody?> =
            Observable.error(Throwable("Unable to update guarantor"))
        val payload = mock(GuarantorApplicationPayload::class.java)

        `when`(dataManager.updateGuarantor(payload, 11L, 22L)).thenReturn(error)

        val result = guarantorRepositoryImp.updateGuarantor(payload, 11L, 22L)

        verify(dataManager).updateGuarantor(payload, 11L, 22L)
        assertEquals(result, error)
    }

    @Test
    fun testDeleteGuarantor_Successful() {
        val success: Observable<ResponseBody?> = Observable.just(mock(ResponseBody::class.java))

        `when`(dataManager.deleteGuarantor(1L, 2L)).thenReturn(success)

        val result = guarantorRepositoryImp.deleteGuarantor(1L, 2L)

        verify(dataManager).deleteGuarantor(1L, 2L)
        assertEquals(result, success)
    }

    @Test
    fun testDeleteGuarantor_Unsuccessful() {
        val error: Observable<ResponseBody?> =
            Observable.error(Throwable("Unable to delete guarantor"))

        `when`(dataManager.deleteGuarantor(1L, 2L)).thenReturn(error)

        val result = guarantorRepositoryImp.deleteGuarantor(1L, 2L)

        verify(dataManager).deleteGuarantor(1L, 2L)
        assertEquals(result, error)
    }

    @Test
    fun testGetGuarantorList_Successful() {
        val success: Observable<List<GuarantorPayload?>?> =
            Observable.just(mock(GuarantorPayload::class.java)) as Observable<List<GuarantorPayload?>?>

        `when`(dataManager.getGuarantorList(123L)).thenReturn(success)

        val result = guarantorRepositoryImp.getGuarantorList(123L)

        verify(dataManager).getGuarantorList(123L)
        assertEquals(result, success)
    }

    @Test
    fun testGetGuarantorList_Unsuccessful() {
        val error: Observable<List<GuarantorPayload?>?> =
            Observable.error(Throwable("Unable to update guarantor"))

        `when`(dataManager.getGuarantorList(123L)).thenReturn(error)

        val result = guarantorRepositoryImp.getGuarantorList(123L)

        verify(dataManager).getGuarantorList(123L)
        assertEquals(result, error)
    }
}