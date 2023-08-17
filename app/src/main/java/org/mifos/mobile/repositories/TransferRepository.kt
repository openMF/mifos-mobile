package org.mifos.mobile.repositories

import okhttp3.ResponseBody
import org.mifos.mobile.ui.enums.TransferType
import retrofit2.Response

interface TransferRepository {

    suspend fun makeTransfer(
        fromOfficeId: Int?,
        fromClientId: Long?,
        fromAccountType: Int?,
        fromAccountId: Int?,
        toOfficeId: Int?,
        toClientId: Long?,
        toAccountType: Int?,
        toAccountId: Int?,
        transferDate: String?,
        transferAmount: Double?,
        transferDescription: String?,
        dateFormat: String = "dd MMMM yyyy",
        locale: String = "en",
        fromAccountNumber: String?,
        toAccountNumber: String?,
        transferType: TransferType?
    ): Response<ResponseBody?>?
}