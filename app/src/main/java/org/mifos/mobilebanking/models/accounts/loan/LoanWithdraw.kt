package org.mifos.mobilebanking.models.accounts.loan

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by dilpreet on 7/6/17.
 */

@Parcelize
data class LoanWithdraw(
        @SerializedName("withdrawnOnDate")
        var withdrawnOnDate: String? = null,

        @SerializedName("note")
        var note: String? = null,

        internal var dateFormat : String = "dd MMMM yyyy",
        internal var locale : String = "en"
) : Parcelable