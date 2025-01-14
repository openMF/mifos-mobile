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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Serializable
@Parcelize
data class LoanOfficerOptions(

    val id: Int? = null,

    val firstname: String? = null,

    val lastname: String? = null,

    val displayName: String? = null,

    val mobileNo: String? = null,

    val officeId: Int? = null,

    val officeName: String? = null,

    @SerialName("isLoanOfficer")
    val loanOfficer: Boolean? = null,

    @SerialName("isActive")
    val active: Boolean? = null,

    val joiningDate: List<Int>? = null,

) : Parcelable
