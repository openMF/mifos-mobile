/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network.dto.templates.savings

import kotlinx.serialization.Serializable

@Serializable
data class FieldOfficerOptionsResponseDto(
    val id: Int,
    val firstname: String? = null,
    val lastname: String? = null,
    val displayName: String? = null,
    val officeId: Int? = null,
    val officeName: String? = null,
    val isLoanOfficer: Boolean? = null,
    val isActive: Boolean? = null,
)
