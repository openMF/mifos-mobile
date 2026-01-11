/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.mapper.auth

import org.mifos.mobile.core.model.entity.Role
import org.mifos.mobile.core.model.entity.User
import org.mifos.mobile.core.network.dto.auth.RoleDto
import org.mifos.mobile.core.network.dto.auth.UserDto

fun UserDto.toModel(): User =
    User(
        userId = userId,
        isAuthenticated = isAuthenticated,
        username = username,
        officeId = officeId,
        officeName = officeName,
        roles = ArrayList(roles.map { it.toModel() }),
        base64EncodedAuthenticationKey = base64EncodedAuthenticationKey,
        permissions = ArrayList(permissions),
        shouldRenewPassword = shouldRenewPassword,
        isTwoFactorAuthenticationRequired = isTwoFactorAuthenticationRequired,
        clients = ArrayList(clients),
    )

fun RoleDto.toModel(): Role =
    Role(
        id = id,
        name = name,
        description = description,
        disabled = disabled,
    )
