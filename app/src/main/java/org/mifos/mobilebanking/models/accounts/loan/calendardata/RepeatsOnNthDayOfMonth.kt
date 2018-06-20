package org.mifos.mobilebanking.models.accounts.loan.calendardata

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 04/03/17.
 */

@Parcelize
data class RepeatsOnNthDayOfMonth(
        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("code")
        var code: String,

        @SerializedName("value")
        var value: String
) : Parcelable