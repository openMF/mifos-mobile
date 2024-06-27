package org.mifos.mobile.core.data.repositories

import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.model.entity.templates.account.AccountOptionsTemplate
import org.mifos.mobile.core.model.entity.templates.savings.SavingsAccountTemplate

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