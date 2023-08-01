package org.mifos.mobile.repositories

import io.reactivex.Observable
import junit.framework.Assert.assertEquals
import okhttp3.ResponseBody
import org.junit.Before
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

@RunWith(MockitoJUnitRunner::class)
class BeneficiaryRepositoryImpTest {

    @Mock
    lateinit var dataManager: DataManager

    private lateinit var beneficiaryRepositoryImp: BeneficiaryRepositoryImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        beneficiaryRepositoryImp = BeneficiaryRepositoryImp(dataManager)
    }

    @Test
    fun testBeneficiaryTemplate_Successful() {
        val success: Observable<BeneficiaryTemplate?> =
            Observable.just(mock(BeneficiaryTemplate::class.java))

        `when`(dataManager.beneficiaryTemplate).thenReturn(success)

        val result = beneficiaryRepositoryImp.beneficiaryTemplate()

        verify(dataManager).beneficiaryTemplate
        assertEquals(result, success)
    }

    @Test
    fun testBeneficiaryTemplate_Unsuccessful() {
        val error: Observable<BeneficiaryTemplate?> = Observable.error(Throwable("Error Response"))
        `when`(dataManager.beneficiaryTemplate).thenReturn(error)

        val result = beneficiaryRepositoryImp.beneficiaryTemplate()

        verify(dataManager).beneficiaryTemplate
        assertEquals(result, error)
    }

    @Test
    fun testCreateBeneficiary_Successful() {
        val success: Observable<ResponseBody?> =
            Observable.just(mock(ResponseBody::class.java))

        val beneficiaryPayload = mock(BeneficiaryPayload::class.java)

        `when`(dataManager.createBeneficiary(beneficiaryPayload)).thenReturn(success)

        val result = beneficiaryRepositoryImp.createBeneficiary(beneficiaryPayload)

        verify(dataManager).createBeneficiary(beneficiaryPayload)
        assertEquals(result, success)
    }

    @Test
    fun testCreateBeneficiary_Unsuccessful() {
        val error: Observable<ResponseBody?> =
            Observable.error(Throwable("Error Response"))

        val beneficiaryPayload = mock(BeneficiaryPayload::class.java)

        `when`(dataManager.createBeneficiary(beneficiaryPayload)).thenReturn(error)

        val result = beneficiaryRepositoryImp.createBeneficiary(beneficiaryPayload)

        verify(dataManager).createBeneficiary(beneficiaryPayload)
        assertEquals(result, error)
    }

    @Test
    fun testUpdateBeneficiary_Successful() {
        val success: Observable<ResponseBody?> =
            Observable.just(mock(ResponseBody::class.java))

        val beneficiaryUpdatePayload = mock(BeneficiaryUpdatePayload::class.java)

        `when`(dataManager.updateBeneficiary(123L, beneficiaryUpdatePayload)).thenReturn(success)

        val result = beneficiaryRepositoryImp.updateBeneficiary(123L, beneficiaryUpdatePayload)
        assertEquals(result, success)
    }

    @Test
    fun testUpdateBeneficiary_Unsuccessful() {
        val error: Observable<ResponseBody?> =
            Observable.error(Throwable("Error Response"))

        val beneficiaryUpdatePayload = mock(BeneficiaryUpdatePayload::class.java)

        `when`(dataManager.updateBeneficiary(123L, beneficiaryUpdatePayload)).thenReturn(error)

        val result = beneficiaryRepositoryImp.updateBeneficiary(123L, beneficiaryUpdatePayload)
        assertEquals(result, error)
    }


    @Test
    fun testDeleteBeneficiary_Successful() {
        val success: Observable<ResponseBody?> =
            Observable.just(mock(ResponseBody::class.java))

        `when`(dataManager.deleteBeneficiary(123L)).thenReturn(success)

        val result = beneficiaryRepositoryImp.deleteBeneficiary(123L)
        assertEquals(result, success)
    }

    @Test
    fun testDeleteBeneficiary_Unsuccessful() {
        val error: Observable<ResponseBody?> =
            Observable.error(Throwable("Error Response"))

        `when`(dataManager.deleteBeneficiary(123L)).thenReturn(error)

        val result = beneficiaryRepositoryImp.deleteBeneficiary(123L)
        assertEquals(result, error)
    }

    @Test
    fun testBeneficiaryList_Successful() {
        val success: Observable<List<Beneficiary?>?> =
            Observable.just(Beneficiary::class.java) as Observable<List<Beneficiary?>?>

        `when`(dataManager.beneficiaryList).thenReturn(success)

        val result = beneficiaryRepositoryImp.beneficiaryList()
        assertEquals(result, success)
    }

    @Test
    fun testBeneficiaryList_Unsuccessful() {
        val error: Observable<List<Beneficiary?>?> =
            Observable.error(Throwable("Error Response"))

        `when`(dataManager.beneficiaryList).thenReturn(error)

        val result = beneficiaryRepositoryImp.beneficiaryList()
        assertEquals(result, error)
    }
}