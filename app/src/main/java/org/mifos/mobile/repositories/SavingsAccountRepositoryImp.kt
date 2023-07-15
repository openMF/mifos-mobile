package org.mifos.mobile.repositories

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.models.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.models.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.models.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
import org.mifos.mobile.models.templates.savings.SavingsAccountTemplate
import javax.inject.Inject

class SavingsAccountRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    SavingsAccountRepository {

    override fun getSavingsWithAssociations(
        accountId: Long?,
        associationType: String?
    ): Observable<SavingsWithAssociations?>? {
        return dataManager.getSavingsWithAssociations(accountId, associationType)
    }

    override fun getSavingAccountApplicationTemplate(clientId: Long?): Observable<SavingsAccountTemplate?>? {
        return dataManager.getSavingAccountApplicationTemplate(clientId)
    }

    override fun submitSavingAccountApplication(payload: SavingsAccountApplicationPayload?): Observable<ResponseBody?>? {
        return dataManager.submitSavingAccountApplication(payload)
    }

    override fun updateSavingsAccount(accountId: Long?, payload: SavingsAccountUpdatePayload?): Observable<ResponseBody?>? {
        return dataManager.updateSavingsAccount(accountId, payload)
    }

    override fun submitWithdrawSavingsAccount(
        accountId: String?,
        payload: SavingsAccountWithdrawPayload?
    ): Observable<ResponseBody?>? {
        return dataManager.submitWithdrawSavingsAccount(accountId, payload)
    }

    override fun loanAccountTransferTemplate(): Observable<AccountOptionsTemplate?>? {
        return dataManager.accountTransferTemplate
    }
}