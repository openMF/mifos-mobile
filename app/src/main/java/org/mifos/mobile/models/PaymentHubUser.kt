package org.mifos.mobile.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.mifos.mobile.PartyIdInfo

/**
 * Created by dilpreet on 14/6/17.
 */

@Parcelize
data class PaymentHubUser(

        @SerializedName("firstName")
        var firstName: String? = null,

        @SerializedName("lastName")
        var lastName: String? = null,

        @SerializedName("fspName")
        var fspName: String? = null,

        @SerializedName("tenant")
        var tenant: String? = null,

        @SerializedName("partyIdInfo")
        var partyIdInfo: PartyIdInfo? = null

) : Parcelable