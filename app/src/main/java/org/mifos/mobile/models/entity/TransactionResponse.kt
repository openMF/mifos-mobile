package org.mifos.mobile.models.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionResponse(
        val clientRefId: String? = null,
        val completedTimestamp: String? = null,
        val transactionId: String? = null,
        val transferState: State? = null,
        val transferId: String? = null) : Parcelable {

    enum class State {
        @SerializedName("RECEIVED")
        RECEIVED,
        @SerializedName("PENDING")
        PENDING
    }
}