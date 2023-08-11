package org.mifos.mobile.repositories

import io.reactivex.Observable
import org.junit.Assert

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.client.ClientAccounts
import org.mifos.mobile.utils.Constants
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AccountsRepositoryImpTest {

    @Mock
    lateinit var dataManager: DataManager

    private lateinit var accountsRepositoryImp : AccountsRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        accountsRepositoryImp = AccountsRepositoryImp(dataManager)
    }

    @Test
    fun testLoadClientAccounts_SuccessResponseReceivedFromDataManager_ReturnsSuccess() {
        val mockClientAccounts : ClientAccounts? = Mockito.mock(ClientAccounts::class.java)
        val successResponse = Observable.just(mockClientAccounts)
        Mockito.`when`(
            dataManager.clientAccounts
        ).thenReturn(successResponse)

        val result = accountsRepositoryImp.loadClientAccounts()

        Mockito.verify(dataManager).clientAccounts
        Assert.assertEquals(result, successResponse)
    }

    @Test
    fun testLoadClientAccounts_ErrorResponseReceivedFromDataManager_ReturnsError() {
        val errorResponse : Observable<ClientAccounts?> = Observable.error(Throwable("loading client error"))
        Mockito.`when`(
            dataManager.clientAccounts
        ).thenReturn(errorResponse)

        val result = accountsRepositoryImp.loadClientAccounts()

        Mockito.verify(dataManager).clientAccounts
        Assert.assertEquals(result, errorResponse)
    }

    @Test
    fun testLoadAccounts_SuccessResponseReceivedFromDataManager_ReturnsSuccess() {
        val mockClientAccounts : ClientAccounts? = Mockito.mock(ClientAccounts::class.java)
        val successResponse = Observable.just(mockClientAccounts)
        val mockAccountType = Constants.SAVINGS_ACCOUNTS
        Mockito.`when`(
            dataManager.getAccounts(mockAccountType)
        ).thenReturn(successResponse)

        val result = accountsRepositoryImp.loadAccounts(mockAccountType)

        Mockito.verify(dataManager).getAccounts(mockAccountType)
        Assert.assertEquals(result, successResponse)
    }

    @Test
    fun testLoadAccounts_ErrorResponseReceivedFromDataManager_ReturnsError() {
        val errorResponse : Observable<ClientAccounts?> = Observable.error(Throwable("loading client error"))
        val mockAccountType = Constants.SAVINGS_ACCOUNTS
        Mockito.`when`(
            dataManager.getAccounts(mockAccountType)
        ).thenReturn(errorResponse)

        val result = accountsRepositoryImp.loadAccounts(mockAccountType)

        Mockito.verify(dataManager).getAccounts(mockAccountType)
        Assert.assertEquals(result, errorResponse)
    }

}