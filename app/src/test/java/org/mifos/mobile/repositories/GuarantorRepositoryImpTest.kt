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
import org.mifos.mobile.models.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.models.guarantor.GuarantorTemplatePayload
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
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

        guarantorRepositoryImp.getGuarantorTemplate(123L).test {
            assertEquals(success, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun testGetGuarantorTemplate_Unsuccessful(): Unit = runBlocking {
        val error = RuntimeException("error")

        `when`(dataManager.getGuarantorTemplate(123L)).thenThrow(error)

        kotlin.runCatching {
            guarantorRepositoryImp.getGuarantorTemplate(123L)
        }.exceptionOrNull()
    }

    @Test
    fun testCreateGuarantor_Successful() = runBlocking {
        val success = mock(ResponseBody::class.java)
        val payload = mock(GuarantorApplicationPayload::class.java)

        `when`(dataManager.createGuarantor(123L, payload)).thenReturn(success)

        guarantorRepositoryImp.createGuarantor(123L, payload).test {
            assertEquals(success, awaitItem())
            awaitComplete()
        }

    }

    @Test
    fun testCreateGuarantor_Unsuccessful(): Unit = runBlocking {
        val error = RuntimeException("Error")
        val payload = mock(GuarantorApplicationPayload::class.java)

        `when`(dataManager.createGuarantor(123L, payload)).thenThrow(error)

        kotlin.runCatching {
            guarantorRepositoryImp.createGuarantor(123L, payload)
        }.exceptionOrNull()
    }

    @Test
    fun testUpdateGuarantor_Successful() = runBlocking {
        val success = mock(ResponseBody::class.java)
        val payload = mock(GuarantorApplicationPayload::class.java)

        `when`(dataManager.updateGuarantor(payload, 11L, 22L)).thenReturn(success)

        guarantorRepositoryImp.updateGuarantor(payload, 11L, 22L).test {
            assertEquals(success, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun testUpdateGuarantor_Unsuccessful(): Unit = runBlocking {
        val error = RuntimeException("Error")
        val payload = mock(GuarantorApplicationPayload::class.java)

        `when`(dataManager.updateGuarantor(payload, 11L, 22L)).thenThrow(error)

        kotlin.runCatching {
            guarantorRepositoryImp.updateGuarantor(payload, 11L, 22L)
        }.exceptionOrNull()
    }

    @Test
    fun testDeleteGuarantor_Successful() = runBlocking {
        val success = mock(ResponseBody::class.java)
        `when`(dataManager.deleteGuarantor(1L, 2L)).thenReturn(success)

        guarantorRepositoryImp.deleteGuarantor(1L, 2L).test {
            assertEquals(success, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun testDeleteGuarantor_Unsuccessful(): Unit = runBlocking {
        val error = RuntimeException("Error")

        `when`(dataManager.deleteGuarantor(1L, 2L)).thenThrow(error)

        kotlin.runCatching {
            guarantorRepositoryImp.deleteGuarantor(1L, 2L)
        }.exceptionOrNull()
    }

    @Test
    fun testGetGuarantorList_Successful() = runBlocking {
        val success = mock(GuarantorPayload::class.java)

        `when`(dataManager.getGuarantorList(123L)).thenReturn(listOf(success))

        guarantorRepositoryImp.getGuarantorList(123L).test {
            assertEquals(success, awaitItem()?.get(0))
            awaitComplete()
        }
    }

    @Test
    fun testGetGuarantorList_Unsuccessful(): Unit = runBlocking {
        val error = RuntimeException("Error")

        `when`(dataManager.getGuarantorList(123L)).thenThrow(error)

        kotlin.runCatching {
            guarantorRepositoryImp.getGuarantorList(123L)
        }.exceptionOrNull()
    }
}