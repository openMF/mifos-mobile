package org.mifos.mobile.models.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionResponse(
        @SerializedName("clientRefId")
        val clientRefId: String? = null,
        @SerializedName("completedTimestamp")
        val completedTimestamp: String? = null,
        @SerializedName("transactionId")
        val transactionId: String? = null,
        @SerializedName("transferState")
        val transferState: State? = null,
        @SerializedName("transferId")
        val transferId: String? = null): Parcelable {

    enum class State {
        @SerializedName("RECEIVED")
        RECEIVED,
        @SerializedName("PENDING")
        PENDING
    }
}
