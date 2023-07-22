package org.mifos.mobile.repositories

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.payload.TransferPayload
import org.mifos.mobile.ui.enums.TransferType
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class TransferRepositoryImpTest {

    @Mock
    lateinit var dataManager: DataManager

    private lateinit var transferProcessImp: TransferRepositoryImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        transferProcessImp = TransferRepositoryImp(dataManager)
    }

    @Test
    fun makeThirdPartyTransfer_successful() {
        val success = Observable.just(Mockito.mock(ResponseBody::class.java))
        val transferPayload = TransferPayload().apply {
            this.fromOfficeId = 1
            this.fromClientId = 2
            this.fromAccountType = 3
            this.fromAccountId = 4
            this.toOfficeId = 5
            this.toClientId = 6
            this.toAccountType = 7
            this.toAccountId = 8
            this.transferDate = "06 July 2023"
            this.transferAmount = 100.0
            this.transferDescription = "Transfer"
            this.dateFormat = "dd MMMM yyyy"
            this.locale = "en"
            this.fromAccountNumber = "0000001"
            this.toAccountNumber = "0000002"
        }

        Mockito.`when`(dataManager.makeThirdPartyTransfer(transferPayload)).thenReturn(success)

        val result = transferProcessImp.makeTransfer(
            transferPayload.fromOfficeId,
            transferPayload.fromClientId,
            transferPayload.fromAccountType,
            transferPayload.fromAccountId,
            transferPayload.toOfficeId,
            transferPayload.toClientId,
            transferPayload.toAccountType,
            transferPayload.toAccountId,
            transferPayload.transferDate,
            transferPayload.transferAmount,
            transferPayload.transferDescription,
            transferPayload.dateFormat,
            transferPayload.locale,
            transferPayload.fromAccountNumber,
            transferPayload.toAccountNumber,
            TransferType.TPT
        )

        Mockito.verify(dataManager).makeThirdPartyTransfer(transferPayload)
        Assert.assertEquals(result,success)
    }

    @Test
    fun makeSavingsTransfer_successful() {
        val success = Observable.just(Mockito.mock(ResponseBody::class.java))
        val transferPayload = TransferPayload().apply {
            this.fromOfficeId = 1
            this.fromClientId = 2
            this.fromAccountType = 3
            this.fromAccountId = 4
            this.toOfficeId = 5
            this.toClientId = 6
            this.toAccountType = 7
            this.toAccountId = 8
            this.transferDate = "06 July 2023"
            this.transferAmount = 100.0
            this.transferDescription = "Transfer"
            this.dateFormat = "dd MMMM yyyy"
            this.locale = "en"
            this.fromAccountNumber = "0000001"
            this.toAccountNumber = "0000002"
        }

        Mockito.`when`(dataManager.makeTransfer(transferPayload)).thenReturn(success)

        val result = transferProcessImp.makeTransfer(
            transferPayload.fromOfficeId,
            transferPayload.fromClientId,
            transferPayload.fromAccountType,
            transferPayload.fromAccountId,
            transferPayload.toOfficeId,
            transferPayload.toClientId,
            transferPayload.toAccountType,
            transferPayload.toAccountId,
            transferPayload.transferDate,
            transferPayload.transferAmount,
            transferPayload.transferDescription,
            transferPayload.dateFormat,
            transferPayload.locale,
            transferPayload.fromAccountNumber,
            transferPayload.toAccountNumber,
            TransferType.SELF
        )

        Mockito.verify(dataManager).makeTransfer(transferPayload)
        Assert.assertEquals(result,success)
    }

    @Test
    fun makeThirdPartyTransfer_unsuccessful() {
        val error: Observable<ResponseBody?> = Observable.error(Throwable("Transfer Failed"))
        val transferPayload = TransferPayload().apply {
            this.fromOfficeId = 1
            this.fromClientId = 2
            this.fromAccountType = 3
            this.fromAccountId = 4
            this.toOfficeId = 5
            this.toClientId = 6
            this.toAccountType = 7
            this.toAccountId = 8
            this.transferDate = "06 July 2023"
            this.transferAmount = 100.0
            this.transferDescription = "Transfer"
            this.dateFormat = "dd MMMM yyyy"
            this.locale = "en"
            this.fromAccountNumber = "0000001"
            this.toAccountNumber = "0000002"
        }
        Mockito.`when`(dataManager.makeThirdPartyTransfer(transferPayload)).thenReturn(error)

        val result = transferProcessImp.makeTransfer(
            transferPayload.fromOfficeId,
            transferPayload.fromClientId,
            transferPayload.fromAccountType,
            transferPayload.fromAccountId,
            transferPayload.toOfficeId,
            transferPayload.toClientId,
            transferPayload.toAccountType,
            transferPayload.toAccountId,
            transferPayload.transferDate,
            transferPayload.transferAmount,
            transferPayload.transferDescription,
            transferPayload.dateFormat,
            transferPayload.locale,
            transferPayload.fromAccountNumber,
            transferPayload.toAccountNumber,
            TransferType.TPT
        )

        Mockito.verify(dataManager).makeThirdPartyTransfer(transferPayload)
        Assert.assertEquals(result,error)
    }

    @Test
    fun makeSavingsTransfer_unsuccessful() {
        val error: Observable<ResponseBody?> = Observable.error(Throwable("Transfer Failed"))
        val transferPayload = TransferPayload().apply {
            this.fromOfficeId = 1
            this.fromClientId = 2
            this.fromAccountType = 3
            this.fromAccountId = 4
            this.toOfficeId = 5
            this.toClientId = 6
            this.toAccountType = 7
            this.toAccountId = 8
            this.transferDate = "06 July 2023"
            this.transferAmount = 100.0
            this.transferDescription = "Transfer"
            this.dateFormat = "dd MMMM yyyy"
            this.locale = "en"
            this.fromAccountNumber = "0000001"
            this.toAccountNumber = "0000002"
        }
        Mockito.`when`(dataManager.makeTransfer(transferPayload)).thenReturn(error)

        val result = transferProcessImp.makeTransfer(
            transferPayload.fromOfficeId,
            transferPayload.fromClientId,
            transferPayload.fromAccountType,
            transferPayload.fromAccountId,
            transferPayload.toOfficeId,
            transferPayload.toClientId,
            transferPayload.toAccountType,
            transferPayload.toAccountId,
            transferPayload.transferDate,
            transferPayload.transferAmount,
            transferPayload.transferDescription,
            transferPayload.dateFormat,
            transferPayload.locale,
            transferPayload.fromAccountNumber,
            transferPayload.toAccountNumber,
            TransferType.SELF
        )

        Mockito.verify(dataManager).makeTransfer(transferPayload)
        Assert.assertEquals(result,error)
    }
}