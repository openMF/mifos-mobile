package org.mifos.mobile.repositories

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.payload.TransferPayload
import org.mifos.mobile.core.model.enums.TransferType
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response


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
    fun makeThirdPartyTransfer_successful() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val success = Mockito.mock(ResponseBody::class.java)
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

        Mockito.`when`(dataManager.makeThirdPartyTransfer(transferPayload))
            .thenReturn(success)

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
            org.mifos.mobile.core.model.enums.TransferType.TPT
        )
        result.test {
            Assert.assertEquals(success, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        Mockito.verify(dataManager).makeThirdPartyTransfer(transferPayload)
        Dispatchers.resetMain()
    }

    @Test
    fun makeSavingsTransfer_successful() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val success = Mockito.mock(ResponseBody::class.java)
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
            org.mifos.mobile.core.model.enums.TransferType.SELF
        )
        result.test {
            Assert.assertEquals(success, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        Mockito.verify(dataManager).makeTransfer(transferPayload)
        Dispatchers.resetMain()
    }

    @Test(expected = Exception::class)
    fun makeThirdPartyTransfer_unsuccessful() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
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
        Mockito.`when`(dataManager.makeThirdPartyTransfer(transferPayload))
            .thenThrow(Exception("Error occurred"))

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
            org.mifos.mobile.core.model.enums.TransferType.TPT
        )
        result.test {
            Assert.assertEquals(Throwable("Error occurred"), awaitError())
            cancelAndIgnoreRemainingEvents()
        }
        Mockito.verify(dataManager).makeThirdPartyTransfer(transferPayload)
        Dispatchers.resetMain()
    }

    @Test(expected = Exception::class)
    fun makeSavingsTransfer_unsuccessful() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
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
        Mockito.`when`(dataManager.makeTransfer(transferPayload)).
        thenThrow(Exception("Error occurred"))

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
            org.mifos.mobile.core.model.enums.TransferType.SELF
        )
        result.test {
            Assert.assertEquals(Throwable("Error occurred"), awaitError())
            cancelAndIgnoreRemainingEvents()
        }
        Mockito.verify(dataManager).makeTransfer(transferPayload)
        Dispatchers.resetMain()
    }
}