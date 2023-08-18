package org.mifos.mobile.repositories

import okhttp3.ResponseBody
import org.mifos.mobile.models.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.models.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.models.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.models.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
import org.mifos.mobile.models.templates.savings.SavingsAccountTemplate
import retrofit2.Response

interface SavingsAccountRepository {

    suspend fun getSavingsWithAssociations(
        accountId: Long?,
        associationType: String?,
    ): Response<SavingsWithAssociations?>?

    suspend fun getSavingAccountApplicationTemplate(clientId: Long?): Response<SavingsAccountTemplate?>?

    suspend fun submitSavingAccountApplication(payload: SavingsAccountApplicationPayload?): Response<ResponseBody?>?

    suspend fun updateSavingsAccount(
        accountId: Long?,
        payload: SavingsAccountUpdatePayload?
    ): Response<ResponseBody?>?

    suspend fun submitWithdrawSavingsAccount(
        accountId: String?,
        payload: SavingsAccountWithdrawPayload?
    ): Response<ResponseBody?>?

    suspend fun loanAccountTransferTemplate(): Response<AccountOptionsTemplate?>?
}