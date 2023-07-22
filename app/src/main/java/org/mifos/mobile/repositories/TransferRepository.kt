package org.mifos.mobile.repositories

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.mifos.mobile.ui.enums.TransferType

interface TransferRepository {

    fun makeTransfer(
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
    ): Observable<ResponseBody?>?
}