package org.mifos.mobile.repositories

import io.reactivex.Observable
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.FakeRemoteDataSource
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.client.Client
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ClientRepositoryImpTest {

    @Mock
    lateinit var dataManager: DataManager
    private var mockClientPage: Page<Client?>? = null
    private lateinit var clientRepositoryImp: ClientRepositoryImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        clientRepositoryImp = ClientRepositoryImp(dataManager)
        mockClientPage = FakeRemoteDataSource.clients
    }

    @Test
    fun testLoadClient_SuccessResponseReceivedFromDataManager_ReturnsClientPageSuccessfully() {
        val successResponse: Observable<Page<Client?>?> = Observable.just(mockClientPage)
        Mockito.`when`(
            dataManager.clients
        ).thenReturn(successResponse)

        val result = clientRepositoryImp.loadClient()

        Mockito.verify(dataManager).clients
        Assert.assertEquals(result, successResponse)
    }

    @Test
    fun testLoadClient_ErrorResponseReceivedFromDataManager_ReturnsError() {
        val errorResponse: Observable<Page<Client?>?> =
            Observable.error(Throwable("Load Client Failed"))
        Mockito.`when`(
            dataManager.clients
        ).thenReturn(errorResponse)

        val result = clientRepositoryImp.loadClient()

        Mockito.verify(dataManager).clients
        Assert.assertEquals(result, errorResponse)
    }
}