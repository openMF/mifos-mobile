/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.accounts.loan

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Suppress("ktlint:standard:property-naming")
@Serializable
@Parcelize
data class Timeline(
    val submittedOnDate: List<Int>? = null,

    val submittedByUsername: String?,

    val submittedByFirstname: String?,

    val submittedByLastname: String?,

    val approvedOnDate: List<Int>? = null,

    val approvedByUsername: String?,

    val approvedByFirstname: String?,

    val approvedByLastname: String?,

    val expectedDisbursementDate: List<Int>? = null,

    val actualDisbursementDate: List<Int>? = null,

    val disbursedByUsername: String?,

    val disbursedByFirstname: String?,

    val disbursedByLastname: String?,

    val closedOnDate: List<Int>? = null,

    val expectedMaturityDate: List<Int>? = null,

    val withdrawnOnDate: List<Int>? = null,

) : Parcelable
