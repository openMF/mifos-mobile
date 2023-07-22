package org.mifos.mobile.repositories

import io.reactivex.Observable
import junit.framework.Assert.assertEquals
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
    fun testThirdPartyTransferTemplate_Successful() {
        val response: Observable<AccountOptionsTemplate?> =
            Observable.just(Mockito.mock(AccountOptionsTemplate::class.java))

        `when`(dataManager.thirdPartyTransferTemplate).thenReturn(response)

        val result = transferRepositoryImp.thirdPartyTransferTemplate()

        assertEquals(result, response)
    }

    @Test
    fun testThirdPartyTransferTemplate_Unsuccessful() {
        val error: Observable<AccountOptionsTemplate?> =
            Observable.error(Throwable("Failed to load template"))
        `when`(dataManager.thirdPartyTransferTemplate).thenReturn(error)

        val result = transferRepositoryImp.thirdPartyTransferTemplate()

        assertEquals(result, error)
    }

    @Test
    fun testBeneficiaryList_Successful() {
        val list1 = Mockito.mock(Beneficiary::class.java)
        val list2 = Mockito.mock(Beneficiary::class.java)

        val response: Observable<List<Beneficiary?>?> = Observable.just(listOf(list1, list2))

        `when`(dataManager.beneficiaryList).thenReturn(response)

        val result = transferRepositoryImp.beneficiaryList()
        assertEquals(result, response)
    }

    @Test
    fun testBeneficiaryList_Unsuccessful() {
        val list1 = Mockito.mock(Beneficiary::class.java)
        val list2 = Mockito.mock(Beneficiary::class.java)

        val error: Observable<List<Beneficiary?>?> =
            Observable.error(Throwable("Failed to load beneficiary list"))

        `when`(dataManager.beneficiaryList).thenReturn(error)

        val result = transferRepositoryImp.beneficiaryList()

        assertEquals(result,error)
    }
}