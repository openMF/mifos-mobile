package org.mifos.mobile.repositories

import okhttp3.ResponseBody
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.models.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.models.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.models.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
import org.mifos.mobile.models.templates.savings.SavingsAccountTemplate
import retrofit2.Response
import javax.inject.Inject

class SavingsAccountRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    SavingsAccountRepository {

    override suspend fun getSavingsWithAssociations(
        accountId: Long?,
        associationType: String?
    ): Response<SavingsWithAssociations?>? {
        return dataManager.getSavingsWithAssociations(accountId, associationType)
    }

    override suspend fun getSavingAccountApplicationTemplate(clientId: Long?): Response<SavingsAccountTemplate?>? {
        return dataManager.getSavingAccountApplicationTemplate(clientId)
    }

    override suspend fun submitSavingAccountApplication(payload: SavingsAccountApplicationPayload?): Response<ResponseBody?>? {
        return dataManager.submitSavingAccountApplication(payload)
    }

    override suspend fun updateSavingsAccount(
        accountId: Long?,
        payload: SavingsAccountUpdatePayload?
    ): Response<ResponseBody?>? {
        return dataManager.updateSavingsAccount(accountId, payload)
    }

    override suspend fun submitWithdrawSavingsAccount(
        accountId: String?,
        payload: SavingsAccountWithdrawPayload?
    ): Response<ResponseBody?>? {
        return dataManager.submitWithdrawSavingsAccount(accountId, payload)
    }

    override suspend fun loanAccountTransferTemplate(): Response<AccountOptionsTemplate?>? {
        return dataManager.accountTransferTemplate()
    }
}