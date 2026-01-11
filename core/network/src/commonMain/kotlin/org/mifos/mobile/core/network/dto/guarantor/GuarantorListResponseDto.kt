/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network.dto.guarantor

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.common.TypeResponseDto

@Serializable
data class GuarantorListResponseDto(

    val id: Long? = 0,

    val city: String? = null,

    val lastname: String? = null,

    val guarantorType: TypeResponseDto? = null,

    val firstname: String? = null,

    val joinedDate: List<Int>? = null,

    val loanId: Long? = null,

    val status: Boolean? = true,
)
