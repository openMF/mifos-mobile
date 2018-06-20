package org.mifos.mobilebanking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by michaelsosnick on 12/11/16.
 */

@Parcelize
data class ChargeTimeType(
        var id: Int = 0,
        var code: String? = null,
        var value: String? = null
) : Parcelable