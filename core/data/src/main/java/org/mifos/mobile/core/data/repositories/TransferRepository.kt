package org.mifos.mobile.core.data.repositories

import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import org.mifos.mobile.core.model.enums.TransferType
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
    ): Flow<ResponseBody>
}