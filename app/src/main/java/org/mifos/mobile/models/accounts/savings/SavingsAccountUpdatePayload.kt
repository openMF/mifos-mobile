package org.mifos.mobile.models.accounts.savings

/*
 * Created by saksham on 03/July/2018
 */

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SavingsAccountUpdatePayload(

        var clientId: Long? = 0,

        var productId: Long? = 0

) : Parcelable
