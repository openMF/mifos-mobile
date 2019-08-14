package org.mifos.mobile
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PartyIdInfo(
    @SerializedName("partyIdType")
    var partyIdType: String? = null,

    @SerializedName("partyIdentifier")
    var partyIdentifier: String? = null,

    @SerializedName("partySubIdOrType")
    var partySubIdOrType: String? = null

) : Parcelable
