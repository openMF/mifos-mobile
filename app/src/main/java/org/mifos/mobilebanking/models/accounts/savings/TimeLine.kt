package org.mifos.mobilebanking.models.accounts.savings

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Created by Rajan Maurya on 05/03/17.
 */

@Parcelize
data class TimeLine(
        @SerializedName("submittedOnDate")
        var submittedOnDate: List<Int> = ArrayList(),

        @SerializedName("submittedByUsername")
        var submittedByUsername: String,

        @SerializedName("submittedByFirstname")
        var submittedByFirstname: String,

        @SerializedName("submittedByLastname")
        var submittedByLastname: String,

        @SerializedName("approvedOnDate")
        var approvedOnDate: List<Int> = ArrayList(),

        @SerializedName("approvedByUsername")
        var approvedByUsername: String,

        @SerializedName("approvedByFirstname")
        var approvedByFirstname: String,

        @SerializedName("approvedByLastname")
        var approvedByLastname: String,

        @SerializedName("activatedOnDate")
        var activatedOnDate: List<Int>,

        @SerializedName("activatedByUsername")
        var activatedByUsername: String,

        @SerializedName("activatedByFirstname")
        var activatedByFirstname: String,

        @SerializedName("activatedByLastname")
        var activatedByLastname: String,

        @SerializedName("closedOnDate")
        var closedOnDate: List<Int>

) : Parcelable