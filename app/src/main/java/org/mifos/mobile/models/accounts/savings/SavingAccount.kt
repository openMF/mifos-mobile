package org.mifos.mobile.models.accounts.savings

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.mifos.mobile.models.accounts.Account
import org.mifos.mobile.models.client.DepositType

/**
 * @author Vishwajeet
 * @since 22/06/16
 */

@Parcelize
data class SavingAccount(

        var accountNo: String? = null,

        var productName: String? = null,

        var productId: Int? = null,

        var overdraftLimit: Long = 0,

        var minRequiredBalance: Long = 0,

        var accountBalance: Double = 0.toDouble(),

        var totalDeposits: Double = 0.toDouble(),

        var savingsProductName: String? = null,

        var clientName: String? = null,

        var savingsProductId: String? = null,

        var nominalAnnualInterestRate: Double = 0.toDouble(),

        var status: Status? = null,

        var currency: Currency? = null,

        var depositType: DepositType? = null,

        var lastActiveTransactionDate: List<Int>? = null,

        var timeLine: TimeLine? = null
) : Parcelable, Account() {
    fun isRecurring(): Boolean {
        return this.depositType != null && (this.depositType?.isRecurring() == true)
    }
}