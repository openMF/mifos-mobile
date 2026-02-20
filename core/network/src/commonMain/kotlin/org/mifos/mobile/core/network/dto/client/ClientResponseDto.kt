/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network.dto.client

import kotlinx.serialization.Serializable

@Serializable
data class ClientResponseDto(
    val id: Int = 0,

    val accountNo: String? = null,

    val status: StatusResponseDto? = null,

    val active: Boolean? = null,

    val activationDate: List<Int> = emptyList(),

    val dobDate: List<Int> = emptyList(),

    val firstname: String? = null,

    val middlename: String? = null,

    val lastname: String? = null,

    val displayName: String? = null,

    val fullname: String? = null,

    val officeId: Int? = null,

    val officeName: String? = null,

    val staffId: Int? = null,

    val staffName: String? = null,

    val timeline: TimelineResponseDto? = null,

    val imageId: Int = 0,

    val isImagePresent: Boolean = false,

    val externalId: String? = null,

    val mobileNo: String? = null,

    val clientClassification: ClientClassificationResponseDto? = null,

    val clientType: ClientTypeResponseDto? = null,

    val gender: GenderResponseDto? = null,

    val groups: List<GroupResponseDto> = emptyList(),
)
