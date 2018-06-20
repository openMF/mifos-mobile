package org.mifos.mobilebanking.models.templates.loans

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 16/07/16.
 */

@Parcelize
data class LoanOfficerOptions(
        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("firstname")
        var firstname: String,

        @SerializedName("lastname")
        var lastname: String,

        @SerializedName("displayName")
        var displayName: String,

        @SerializedName("mobileNo")
        var mobileNo: String,

        @SerializedName("officeId")
        var officeId: Int? = null,

        @SerializedName("officeName")
        var officeName: String,

        @SerializedName("isLoanOfficer")
        var loanOfficer: Boolean? = null,

        @SerializedName("isActive")
        var active: Boolean? = null,

        @SerializedName("joiningDate")
        var joiningDate: List<Int>

) : Parcelable