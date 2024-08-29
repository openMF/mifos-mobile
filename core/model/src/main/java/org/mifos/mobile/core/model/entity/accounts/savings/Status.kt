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

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Status(
    var id: Int? = null,
    var code: String? = null,

    var value: String? = null,

    var submittedAndPendingApproval: Boolean? = null,

    var approved: Boolean? = null,

    var rejected: Boolean? = null,

    var withdrawnByApplicant: Boolean? = null,

    var active: Boolean? = null,

    var closed: Boolean? = null,

    var prematureClosed: Boolean? = null,

    internal var transferInProgress: Boolean? = null,

    internal var transferOnHold: Boolean? = null,

    var matured: Boolean? = null,

) : Parcelable
