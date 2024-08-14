package org.mifos.mobile.repositories

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.Credentials
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.mifos.mobile.core.data.utils.FakeRemoteDataSource
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.client.Client
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit

@RunWith(MockitoJUnitRunner::class)
class ClientRepositoryImpTest {

    @Mock
    lateinit var dataManager: DataManager

    @Mock
    lateinit var preferencesHelper: PreferencesHelper

    @Mock
    lateinit var retrofit: Retrofit

    @Mock
    lateinit var retrofitBuilder: Retrofit.Builder

    private var mockClientPage: Page<Client?>? = null
    private lateinit var clientRepositoryImp: ClientRepositoryImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        clientRepositoryImp = ClientRepositoryImp(
            dataManager,
            preferencesHelper,
            retrofit
        )
        mockClientPage = com.mifos.mobile.core.data.utils.FakeRemoteDataSource.clients
    }

    @Test
    fun testLoadClient_SuccessResponseReceivedFromDataManager_ReturnsClientPageSuccessfully() =
       runTest {
            Dispatchers.setMain(Dispatchers.Unconfined)
            val successResponse:Page<Client>
            = Page<Client>(5, List(5) {
                (mock(Client::class.java) as Client)
            })
            Mockito.`when`(
                dataManager.clients()
            ).thenReturn(successResponse)

            val resultFlow = clientRepositoryImp.loadClient()
            resultFlow.test {
                Assert.assertEquals(successResponse, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
             Mockito.verify(dataManager).clients()
            Dispatchers.resetMain()
        }

    @Test(expected = Exception::class)
    fun testLoadClient_ErrorResponseReceivedFromDataManager_ReturnsError() =runTest{
        Dispatchers.setMain(Dispatchers.Unconfined)
          Mockito.`when`(
            dataManager.clients()
        ).thenThrow(Exception("Error occurred"))

        val result = clientRepositoryImp.loadClient()
        result.test{
            assert(Throwable("Error occurred") == awaitError())
        }
        Mockito.verify(dataManager).clients()
        Dispatchers.resetMain()
    }

    @Test
    fun testUpdateAuthenticationToken() {
        val mockPassword = "testPassword"
        val mockUsername = "testUsername"
        `when`(preferencesHelper.userName).thenReturn(mockUsername)
        `when`(retrofit.newBuilder()).thenReturn(retrofitBuilder)
        `when`(retrofitBuilder.client(any())).thenReturn(retrofitBuilder)
        clientRepositoryImp.updateAuthenticationToken(mockPassword)
        val authenticationToken = Credentials.basic(preferencesHelper.userName!!, mockPassword)

        Mockito.verify(preferencesHelper).saveToken(authenticationToken)
    }
}