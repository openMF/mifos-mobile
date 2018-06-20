package org.mifos.mobilebanking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by michaelsosnick on 12/11/16.
 */

@Parcelize
data class Currency(
        var code: String? = null,
        var name: String? = null,
        var decimalPlaces: Int = 0,
        var displaySymbol: String? = null,
        var nameCode: String? = null,
        var displayLabel: String? = null


) : Parcelable