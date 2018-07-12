package org.mifos.mobilebanking.models.accounts.savings

/*
 * Created by saksham on 03/July/2018
 */

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SavingsAccountUpdatePayload(

        @SerializedName("clientId")
        var clientId: Long = 0,

        @SerializedName("productId")
        var productId: Long = 0

) : Parcelable
