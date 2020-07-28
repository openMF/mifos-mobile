package org.mifos.mobile.models.templates.loans

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 16/07/16.
 */

@Parcelize
data class LoanOfficerOptions(

        var id: Int? = null,

        var firstname: String,

        var lastname: String,

        var displayName: String,

        var mobileNo: String,

        var officeId: Int? = null,

        var officeName: String,

        @SerializedName("isLoanOfficer")
        var loanOfficer: Boolean? = null,

        @SerializedName("isActive")
        var active: Boolean? = null,

        var joiningDate: List<Int>

) : Parcelable