/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val userId: Long = 0,
    @SerialName("authenticated")
    val isAuthenticated: Boolean = false,
    val username: String? = null,
    val officeId: Long = 0,
    val officeName: String? = null,
    val roles: ArrayList<RoleDto> = arrayListOf(),
    val base64EncodedAuthenticationKey: String? = null,
    val permissions: ArrayList<String> = arrayListOf(),
    val shouldRenewPassword: Boolean = false,
    val isTwoFactorAuthenticationRequired: Boolean = false,
    val clients: ArrayList<Long> = arrayListOf(),
)

@Serializable
data class RoleDto(
    val id: Long = 0,
    val name: String? = null,
    val description: String? = null,
    val disabled: Boolean = false,
)
