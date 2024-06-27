package org.mifos.mobile.core.model.entity.client

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.core.model.entity.accounts.share.ShareAccount


@Parcelize
data class ClientAccounts(
    var loanAccounts: List<LoanAccount> = ArrayList(),
    var savingsAccounts: List<SavingAccount>? = ArrayList(),

    var shareAccounts: List<ShareAccount> = ArrayList(),

    ) : Parcelable {

    fun recurringSavingsAccounts(): List<SavingAccount> {
        return getSavingsAccounts(true)
    }

    fun nonRecurringSavingsAccounts(): List<SavingAccount> {
        return getSavingsAccounts(false)
    }

    private fun getSavingsAccounts(wantRecurring: Boolean): List<SavingAccount> {
        val result = ArrayList<SavingAccount>()
        if (this.savingsAccounts != null) {
            for (account in savingsAccounts!!) {
                if (account.isRecurring() == wantRecurring) {
                    result.add(account)
                }
            }
        }
        return result
    }
}
