/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.templates.loans

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Rajan Maurya on 16/07/16.
 */

@Parcelize
data class LoanOfficerOptions(

    var id: Int? = null,

    var firstname: String? = null,

    var lastname: String? = null,

    var displayName: String? = null,

    var mobileNo: String? = null,

    var officeId: Int? = null,

    var officeName: String? = null,

    @SerializedName("isLoanOfficer")
    var loanOfficer: Boolean? = null,

    @SerializedName("isActive")
    var active: Boolean? = null,

    var joiningDate: List<Int>? = null,

) : Parcelable
