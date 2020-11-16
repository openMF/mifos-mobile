package org.mifos.mobile.models.client

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.mifos.mobile.api.ApiEndPoints

@Parcelize
data class DepositType(
        var id: Int? = null,
        var code: String? = null,

        var value: String? = null
) : Parcelable {

    fun isRecurring(): Boolean {
        return ServerTypes.RECURRING.id == this.id
    }

    fun endpoint(): String {
        return ServerTypes.fromId(id!!).endpoint
    }

    fun serverType(): ServerTypes {
        return ServerTypes.fromId(id!!)
    }


    enum class ServerTypes constructor(val id: Int?, val code: String? = null, val endpoint: String) {
        SAVINGS(100, "depositAccountType.savingsDeposit", ApiEndPoints.SAVINGS_ACCOUNTS),
        FIXED(200, "depositAccountType.fixedDeposit", ApiEndPoints.SAVINGS_ACCOUNTS),
        RECURRING(300, "depositAccountType.recurringDeposit", ApiEndPoints.RECURRING_ACCOUNTS);


        companion object {

            fun fromId(id: Int): ServerTypes {
                for (type in values()) {
                    if (type.id == id) {
                        return type
                    }
                }
                return SAVINGS
            }
        }
    }
}