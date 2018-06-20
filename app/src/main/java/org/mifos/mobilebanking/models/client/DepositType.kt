package org.mifos.mobilebanking.models.client

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

import org.mifos.mobilebanking.api.ApiEndPoints

@Parcelize
data class DepositType(
        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("code")
        var code: String,

        @SerializedName("value")
        var value: String
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


    enum class ServerTypes constructor(val id: Int?, val code: String, val endpoint: String) {
        SAVINGS(100, "depositAccountType.savingsDeposit", ApiEndPoints.SAVINGS_ACCOUNTS),
        FIXED(200, "depositAccountType.fixedDeposit", ApiEndPoints.SAVINGS_ACCOUNTS),
        RECURRING(300, "depositAccountType.recurringDeposit", ApiEndPoints.RECURRING_ACCOUNTS);


        companion object {

            fun fromId(id: Int): ServerTypes {
                for (type in ServerTypes.values()) {
                    if (type.id == id) {
                        return type
                    }
                }
                return SAVINGS
            }
        }
    }
}