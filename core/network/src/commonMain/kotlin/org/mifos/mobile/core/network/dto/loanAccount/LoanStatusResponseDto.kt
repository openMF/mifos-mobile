/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network.dto.loanAccount

import kotlinx.serialization.Serializable

@Serializable
data class LoanStatusResponseDto(
    val id: Int? = null,

    val code: String? = null,

    val value: String? = null,

    val pendingApproval: Boolean? = null,

    val waitingForDisbursal: Boolean? = null,

    val active: Boolean? = null,

    val closedObligationsMet: Boolean? = null,

    val closedWrittenOff: Boolean? = null,

    val closedRescheduled: Boolean? = null,

    val closed: Boolean? = null,

    val overpaid: Boolean? = null,

)
