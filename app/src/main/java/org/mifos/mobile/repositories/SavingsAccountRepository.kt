package org.mifos.mobile.repositories

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.mifos.mobile.models.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.models.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.models.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.models.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
import org.mifos.mobile.models.templates.savings.SavingsAccountTemplate

interface SavingsAccountRepository {

    fun getSavingsWithAssociations(
        accountId: Long?,
        associationType: String?,
    ): Observable<SavingsWithAssociations?>?

    fun getSavingAccountApplicationTemplate(clientId: Long?): Observable<SavingsAccountTemplate?>?

    fun submitSavingAccountApplication(payload: SavingsAccountApplicationPayload?): Observable<ResponseBody?>?

    fun updateSavingsAccount(
        accountId: Long?,
        payload: SavingsAccountUpdatePayload?
    ): Observable<ResponseBody?>?

    fun submitWithdrawSavingsAccount(
        accountId: String?,
        payload: SavingsAccountWithdrawPayload?
    ): Observable<ResponseBody?>?

    fun loanAccountTransferTemplate() : Observable<AccountOptionsTemplate?>?
}