package org.mifos.mobile.repositories

import CoroutineTestRule
import app.cash.turbine.test
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
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
    fun testThirdPartyTransferTemplate_Successful() = runTest {
        val response= Mockito.mock(AccountOptionsTemplate::class.java)

        `when`(dataManager.thirdPartyTransferTemplate()).thenReturn(response)

        val result = transferRepositoryImp.thirdPartyTransferTemplate()
        result.test {
            assertEquals(response, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = Exception::class)
    fun testThirdPartyTransferTemplate_Unsuccessful() = runTest {
        `when`(dataManager.thirdPartyTransferTemplate())
            .thenThrow(Exception("Error"))
        val result = transferRepositoryImp.thirdPartyTransferTemplate()
        result.test {
            assertEquals(Throwable("Error"), awaitError())
            cancelAndIgnoreRemainingEvents()
        }
    }

}