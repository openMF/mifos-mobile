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

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Status(
    var id: Int? = null,

    var code: String? = null,

    var value: String? = null,

    var pendingApproval: Boolean? = null,

    var waitingForDisbursal: Boolean? = null,

    var active: Boolean? = null,

    var closedObligationsMet: Boolean? = null,

    var closedWrittenOff: Boolean? = null,

    var closedRescheduled: Boolean? = null,

    var closed: Boolean? = null,

    var overpaid: Boolean? = null,

) : Parcelable {

    fun isLoanTypeWithdrawn(): Boolean {
        return !(
            this.active == true || this.closed == true || this.pendingApproval == true ||
                this.waitingForDisbursal == true || this.closedObligationsMet == true ||
                this.closedWrittenOff == true || this.closedRescheduled == true ||
                this.overpaid == true
            )
    }
}
