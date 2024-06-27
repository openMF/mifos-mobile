package org.mifos.mobile.core.model.entity.beneficiary

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by dilpreet on 16/6/17.
 */

@Parcelize
data class BeneficiaryPayload(
    internal var locale: String = "en_GB",

    var name: String? = null,

    var accountNumber: String? = null,

    var accountType: Int? = 0,

    var transferLimit: Float? = 0f,

    var officeName: String? = null,
) : Parcelable
