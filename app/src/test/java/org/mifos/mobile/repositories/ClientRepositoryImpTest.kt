package org.mifos.mobile.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.Credentials
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.FakeRemoteDataSource
import org.mifos.mobile.api.BaseURL
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.client.Client
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class ClientRepositoryImpTest {

    @Mock
    lateinit var dataManager: DataManager

    @Mock
    lateinit var preferencesHelper: PreferencesHelper

    private var mockClientPage: Page<Client?>? = null
    private lateinit var clientRepositoryImp: ClientRepositoryImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        clientRepositoryImp = ClientRepositoryImp(dataManager, preferencesHelper)
        mockClientPage = FakeRemoteDataSource.clients
    }

    @Test
    fun testLoadClient_SuccessResponseReceivedFromDataManager_ReturnsClientPageSuccessfully() =
        runBlocking {
            Dispatchers.setMain(Dispatchers.Unconfined)
            val successResponse: Response<Page<Client?>?> = Response.success(mockClientPage)
            Mockito.`when`(
                dataManager.clients()
            ).thenReturn(successResponse)

            val result = clientRepositoryImp.loadClient()

            Mockito.verify(dataManager).clients()
            Assert.assertEquals(result, successResponse)
            Dispatchers.resetMain()
        }

    @Test
    fun testLoadClient_ErrorResponseReceivedFromDataManager_ReturnsError() = runBlocking{
        Dispatchers.setMain(Dispatchers.Unconfined)
        val errorResponse: Response<Page<Client?>?> =
            Response.error(404, ResponseBody.create(null,"error"))
        Mockito.`when`(
            dataManager.clients()
        ).thenReturn(errorResponse)

        val result = clientRepositoryImp.loadClient()

        Mockito.verify(dataManager).clients()
        Assert.assertEquals(result, errorResponse)
        Dispatchers.resetMain()
    }

    @Test
    fun testUpdateAuthenticationToken() {
        val mockPassword = "testPassword"
        val mockUsername = "testUsername"
        Mockito.`when`(preferencesHelper.userName).thenReturn(mockUsername)

        Mockito.`when`(preferencesHelper.baseUrl)
            .thenReturn(BaseURL.PROTOCOL_HTTPS + BaseURL.API_ENDPOINT)

        clientRepositoryImp.updateAuthenticationToken(mockPassword)
        val authenticationToken = Credentials.basic(preferencesHelper.userName!!, mockPassword)

        Mockito.verify(preferencesHelper).saveToken(authenticationToken)
    }
}