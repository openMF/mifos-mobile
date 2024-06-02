package org.mifos.mobile.repositories

import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import org.mifos.mobile.models.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.models.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.models.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.models.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
import org.mifos.mobile.models.templates.savings.SavingsAccountTemplate

interface SavingsAccountRepository {

    suspend fun getSavingsWithAssociations(
        accountId: Long?,
        associationType: String?,
    ): Flow<SavingsWithAssociations>

    suspend fun getSavingAccountApplicationTemplate(clientId: Long?): Flow<SavingsAccountTemplate>

    suspend fun submitSavingAccountApplication(payload: SavingsAccountApplicationPayload?): Flow<ResponseBody>

    suspend fun updateSavingsAccount(
        accountId: Long?,
        payload: SavingsAccountUpdatePayload?
    ): Flow<ResponseBody>

    suspend fun submitWithdrawSavingsAccount(
        accountId: String?,
        payload: SavingsAccountWithdrawPayload?
    ): Flow<ResponseBody>

    fun accountTransferTemplate(accountId: Long?, accountType: Long?): Flow<AccountOptionsTemplate>
}