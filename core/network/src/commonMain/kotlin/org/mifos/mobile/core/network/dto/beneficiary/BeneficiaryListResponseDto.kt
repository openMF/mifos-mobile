/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network.dto.beneficiary

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.common.TypeResponseDto

@Serializable
data class BeneficiaryListResponseDto(
    val id: Long? = null,

    val name: String? = null,

    val officeName: String? = null,

    val clientName: String? = null,

    val accountType: TypeResponseDto? = null,

    val accountNumber: String? = null,

    val transferLimit: Double? = null,
)
