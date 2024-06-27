package org.mifos.mobile.core.model.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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
    var displayLabel: String? = null,

) : Parcelable
