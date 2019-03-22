/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */

package org.mifos.mobilebanking.models.accounts.loan

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Timeline(
        @SerializedName("submittedOnDate")
        var submittedOnDate: List<Int>,

        @SerializedName("submittedByUsername")
        var submittedByUsername: String,

        @SerializedName("submittedByFirstname")
        var submittedByFirstname: String,

        @SerializedName("submittedByLastname")
        var submittedByLastname: String,

        @SerializedName("approvedOnDate")
        var approvedOnDate: List<Int>,

        @SerializedName("approvedByUsername")
        var approvedByUsername: String,

        @SerializedName("approvedByFirstname")
        var approvedByFirstname: String,

        @SerializedName("approvedByLastname")
        var approvedByLastname: String,

        @SerializedName("expectedDisbursementDate")
        var expectedDisbursementDate: List<Int>,

        @SerializedName("actualDisbursementDate")
        var actualDisbursementDate: List<Int>,

        @SerializedName("disbursedByUsername")
        var disbursedByUsername: String,

        @SerializedName("disbursedByFirstname")
        var disbursedByFirstname: String,

        @SerializedName("disbursedByLastname")
        var disbursedByLastname: String,

        @SerializedName("closedOnDate")
        var closedOnDate: List<Int>,

        @SerializedName("expectedMaturityDate")
        var expectedMaturityDate: List<Int>,

        @SerializedName("withdrawnOnDate")
        var withdrawnOnDate: List<Int>

) : Parcelable
