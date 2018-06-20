package org.mifos.mobilebanking.models.templates.account

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

import java.util.ArrayList

/**
 * Created by Rajan Maurya on 10/03/17.
 */

@Parcelize
data class AccountOptionsTemplate(
        @SerializedName("fromAccountOptions")
        var fromAccountOptions: List<AccountOption> = ArrayList(),

        @SerializedName("toAccountOptions")
        var toAccountOptions: List<AccountOption> = ArrayList()
) : Parcelable