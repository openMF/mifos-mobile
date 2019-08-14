package org.mifos.mobile.models.paymentHub

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.mifos.mobile.PartyIdInfo

@Parcelize
data class TransactingEntity(
        @SerializedName("partyIdInfo")
        val partyIdInfo: PartyIdInfo,

        @SerializedName("merchantClassificationCode")
        val merchantClassificationCode: String? = null,

        @SerializedName("name")
        val name: String

) : Parcelable