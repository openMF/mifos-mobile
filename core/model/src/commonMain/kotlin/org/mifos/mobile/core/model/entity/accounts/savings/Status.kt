/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.accounts.savings

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Serializable
@Parcelize
data class Status(
    val id: Int? = null,
    val code: String? = null,

    val value: String? = null,

    val submittedAndPendingApproval: Boolean? = null,

    val approved: Boolean? = null,

    val rejected: Boolean? = null,

    val withdrawnByApplicant: Boolean? = null,

    val active: Boolean? = null,

    val closed: Boolean? = null,

    val prematureClosed: Boolean? = null,

    internal val transferInProgress: Boolean? = null,

    internal val transferOnHold: Boolean? = null,

    val matured: Boolean? = null,

) : Parcelable
