package org.mifos.mobile.models.beneficiary

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.mifos.mobile.PartyIdInfo

/**
 * Created by dilpreet on 14/6/17.
 */

@Parcelize
data class ThirdPartyBeneficiary(

        @SerializedName("firstName")
        var firstName: String? = null,

        @SerializedName("lastName")
        var lastName: String? = null,

        @SerializedName("partyIdInfo")
        var partyIdInfo: PartyIdInfo? = null

) : Parcelable