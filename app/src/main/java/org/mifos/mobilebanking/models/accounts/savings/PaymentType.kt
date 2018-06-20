package org.mifos.mobilebanking.models.accounts.savings

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 05/03/17.
 */

@Parcelize
data class PaymentType(
        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("name")
        var name: String
) : Parcelable