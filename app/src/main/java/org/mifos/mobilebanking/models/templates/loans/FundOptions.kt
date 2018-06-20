package org.mifos.mobilebanking.models.templates.loans

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 16/07/16.
 */

@Parcelize
data class FundOptions(
        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("name")
        var name: String
) : Parcelable