package org.mifos.mobile.repositories

import io.reactivex.Observable
import junit.framework.Assert.assertEquals
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.models.client.ClientAccounts
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeRepositoryImpTest {

    @Mock
    lateinit var dataManager: DataManager

    private lateinit var homeRepositoryImp: HomeRepositoryImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        homeRepositoryImp = HomeRepositoryImp(dataManager)
    }

    @Test
    fun testClientAccount_Successfully() {
        val successResponse: Observable<ClientAccounts?> =
            Observable.just(Mockito.mock(ClientAccounts::class.java))
        `when`(dataManager.clientAccounts).thenReturn(successResponse)
        val result = homeRepositoryImp.clientAccounts()
        assertEquals(result, successResponse)
    }

    @Test
    fun testClientAccount_Unsuccessfully() {
        val error: Observable<ClientAccounts?> =
            Observable.error(Throwable("Client Accounts Loading Failed"))
        `when`(dataManager.clientAccounts).thenReturn(error)
        val result = homeRepositoryImp.clientAccounts()
        assertEquals(result, error)
    }

    @Test
    fun testCurrentClient_Successfully() {
        val successResponse: Observable<Client?> = Observable.just(Mockito.mock(Client::class.java))
        `when`(dataManager.currentClient).thenReturn(successResponse)
        val result = homeRepositoryImp.currentClient()
        assertEquals(result, successResponse)
    }

    @Test
    fun testCurrentClient_Unsuccessfully() {
        val error: Observable<Client?> =
            Observable.error(Throwable("Current Client Loading Failed"))
        `when`(dataManager.currentClient).thenReturn(error)
        val result = homeRepositoryImp.currentClient()
        assertEquals(result, error)
    }

    @Test
    fun testClientImage_Successfully() {
        val successResponse: Observable<ResponseBody?> =
            Observable.just(Mockito.mock(ResponseBody::class.java))
        `when`(dataManager.clientImage).thenReturn(successResponse)
        val result = homeRepositoryImp.clientImage()
        assertEquals(result, successResponse)
    }

    @Test
    fun testClientImage_Unsuccessfully() {
        val error: Observable<ResponseBody?> =
            Observable.error(Throwable("Client Image Loading Failed"))
        `when`(dataManager.clientImage).thenReturn(error)
        val result = homeRepositoryImp.clientImage()
        assertEquals(result, error)
    }

    @Test
    fun unreadNotificationsCount_Successfully() {
        val successResponse: Observable<Int> =
            Observable.just(5)
        `when`(dataManager.unreadNotificationsCount).thenReturn(successResponse)
        val result = homeRepositoryImp.unreadNotificationsCount()
        assertEquals(result, successResponse)
    }

    @Test
    fun unreadNotificationsCount_Unsuccessfully() {
        val error: Observable<Int> = Observable.error(Throwable("Unable to Read Notifications"))
        `when`(dataManager.unreadNotificationsCount).thenReturn(error)
        val result = homeRepositoryImp.unreadNotificationsCount()
        assertEquals(result, error)
    }
}