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
import org.mifos.mobile.models.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.models.guarantor.GuarantorTemplatePayload
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class GuarantorRepositoryImpTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var dataManager: DataManager

    private lateinit var guarantorRepositoryImp: GuarantorRepositoryImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        guarantorRepositoryImp = GuarantorRepositoryImp(dataManager)
    }

    @Test
    fun testGetGuarantorTemplate_Successful() = runBlocking {
        val success = mock(GuarantorTemplatePayload::class.java)

        `when`(dataManager.getGuarantorTemplate(123L)).thenReturn(success)

        val result = guarantorRepositoryImp.getGuarantorTemplate(123L)

        verify(dataManager).getGuarantorTemplate(123L)
        assertEquals(result, success)
    }

    @Test
    fun testGetGuarantorTemplate_Unsuccessful() = runBlocking {
        val error = IOException("error")

        `when`(dataManager.getGuarantorTemplate(123L)).thenThrow(error)

        val result = guarantorRepositoryImp.getGuarantorTemplate(123L)

        verify(dataManager).getGuarantorTemplate(123L)
        assertEquals(result, error)
    }

    @Test
    fun testCreateGuarantor_Successful() = runBlocking {
        val success = mock(ResponseBody::class.java)
        val payload = mock(GuarantorApplicationPayload::class.java)

        `when`(dataManager.createGuarantor(123L, payload)).thenReturn(success)

        val result = guarantorRepositoryImp.createGuarantor(123L, payload)

        verify(dataManager).createGuarantor(123L, payload)
        assertEquals(result, success)
    }

    @Test
    fun testCreateGuarantor_Unsuccessful() = runBlocking {
        val error = IOException("Error")
        val payload = mock(GuarantorApplicationPayload::class.java)

        `when`(dataManager.createGuarantor(123L, payload)).thenThrow(error)

        val result = guarantorRepositoryImp.createGuarantor(123L, payload)

        verify(dataManager).createGuarantor(123L, payload)
        assertEquals(result, error)
    }

    @Test
    fun testUpdateGuarantor_Successful() = runBlocking {
        val success = mock(ResponseBody::class.java)
        val payload = mock(GuarantorApplicationPayload::class.java)

        `when`(dataManager.updateGuarantor(payload, 11L, 22L)).thenReturn(success)

        val result = guarantorRepositoryImp.updateGuarantor(payload, 11L, 22L)

        verify(dataManager).updateGuarantor(payload, 11L, 22L)
        assertEquals(result, success)
    }

    @Test
    fun testUpdateGuarantor_Unsuccessful() = runBlocking {
        val error = IOException("Error")
        val payload = mock(GuarantorApplicationPayload::class.java)

        `when`(dataManager.updateGuarantor(payload, 11L, 22L)).thenThrow(error)

        val result = guarantorRepositoryImp.updateGuarantor(payload, 11L, 22L)

        verify(dataManager).updateGuarantor(payload, 11L, 22L)
        assertEquals(result, error)
    }

    @Test
    fun testDeleteGuarantor_Successful() = runBlocking {
        val success = mock(ResponseBody::class.java)
        `when`(dataManager.deleteGuarantor(1L, 2L)).thenReturn(success)

        val result = guarantorRepositoryImp.deleteGuarantor(1L, 2L)

        verify(dataManager).deleteGuarantor(1L, 2L)
        assertEquals(result, success)
    }

    @Test
    fun testDeleteGuarantor_Unsuccessful() = runBlocking {
        val error = IOException("Error")

        `when`(dataManager.deleteGuarantor(1L, 2L)).thenThrow(error)

        val result = guarantorRepositoryImp.deleteGuarantor(1L, 2L)

        verify(dataManager).deleteGuarantor(1L, 2L)
        assertEquals(result, error)
    }

    @Test
    fun testGetGuarantorList_Successful() = runBlocking {
        val success = mock(GuarantorPayload::class.java) as List<GuarantorPayload>

        `when`(dataManager.getGuarantorList(123L)).thenReturn(success)

        val result = guarantorRepositoryImp.getGuarantorList(123L)

        verify(dataManager).getGuarantorList(123L)
        assertEquals(result, success)
    }

    @Test
    fun testGetGuarantorList_Unsuccessful() = runBlocking {
        val error = IOException("Error")

        `when`(dataManager.getGuarantorList(123L)).thenThrow(error)

        val result = guarantorRepositoryImp.getGuarantorList(123L)

        verify(dataManager).getGuarantorList(123L)
        assertEquals(result, error)
    }
}