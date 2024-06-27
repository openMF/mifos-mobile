package org.mifos.mobile.core.model.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by michaelsosnick on 12/11/16.
 */

@Parcelize
data class ChargeCalculationType(
    var id: Int = 0,
    var code: String? = null, // example "chargeCalculationType.flat"
    var value: String? = null,
) : Parcelable
