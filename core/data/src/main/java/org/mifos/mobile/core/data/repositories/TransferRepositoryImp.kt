package org.mifos.mobile.core.data.repositories

import org.mifos.mobile.core.network.DataManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import org.mifos.mobile.core.model.entity.payload.TransferPayload
import org.mifos.mobile.core.model.enums.TransferType
import javax.inject.Inject

class TransferRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    TransferRepository {
    override suspend fun makeTransfer(
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
        dateFormat: String,
        locale: String,
        fromAccountNumber: String?,
        toAccountNumber: String?,
        transferType: TransferType?
    ): Flow<ResponseBody> {
        val transferPayload = TransferPayload().apply {
            this.fromOfficeId = fromOfficeId
            this.fromClientId = fromClientId
            this.fromAccountType = fromAccountType
            this.fromAccountId = fromAccountId
            this.toOfficeId = toOfficeId
            this.toClientId = toClientId
            this.toAccountType = toAccountType
            this.toAccountId = toAccountId
            this.transferDate = transferDate
            this.transferAmount = transferAmount
            this.transferDescription = transferDescription
            this.dateFormat = dateFormat
            this.locale = locale
            this.fromAccountNumber = fromAccountNumber
            this.toAccountNumber = toAccountNumber
        }
        return flow {
            emit(
                when (transferType) {
                    TransferType.SELF -> dataManager.makeTransfer(transferPayload)
                    else -> dataManager.makeThirdPartyTransfer(transferPayload)
                }
            )
        }

    }
}