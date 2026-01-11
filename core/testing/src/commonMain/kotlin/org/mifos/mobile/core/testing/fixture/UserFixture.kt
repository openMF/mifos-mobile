/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.testing.fixture

import org.mifos.mobile.core.model.entity.Role
import org.mifos.mobile.core.model.entity.User

/**
 * Test fixtures for [User] entity.
 *
 * Usage:
 * ```kotlin
 * val testUser = UserFixture.createDefault()
 * val adminUser = UserFixture.createAdmin()
 * val customUser = UserFixture.create(username = "custom", userId = 100L)
 * ```
 */
object UserFixture {

    fun createDefault(): User = User(
        userId = 1L,
        isAuthenticated = true,
        username = "testuser",
        officeId = 1L,
        officeName = "Test Office",
        roles = arrayListOf(createDefaultRole()),
        base64EncodedAuthenticationKey = "test-auth-key-base64",
        permissions = arrayListOf("READ_ACCOUNT", "WRITE_ACCOUNT"),
        shouldRenewPassword = false,
        isTwoFactorAuthenticationRequired = false,
        clients = arrayListOf(1L),
    )

    fun createAdmin(): User = User(
        userId = 1L,
        isAuthenticated = true,
        username = "admin",
        officeId = 1L,
        officeName = "Head Office",
        roles = arrayListOf(createAdminRole()),
        base64EncodedAuthenticationKey = "admin-auth-key-base64",
        permissions = arrayListOf("ALL_FUNCTIONS"),
        shouldRenewPassword = false,
        isTwoFactorAuthenticationRequired = false,
        clients = arrayListOf(1L),
    )

    fun createUnauthenticated(): User = User(
        userId = 0L,
        isAuthenticated = false,
        username = null,
        base64EncodedAuthenticationKey = null,
    )

    fun createRequiresPasswordRenewal(): User = createDefault().copy(
        shouldRenewPassword = true,
    )

    fun createRequires2FA(): User = createDefault().copy(
        isTwoFactorAuthenticationRequired = true,
    )

    fun create(
        userId: Long = 1L,
        isAuthenticated: Boolean = true,
        username: String = "testuser",
        officeId: Long = 1L,
        officeName: String = "Test Office",
        roles: ArrayList<Role> = arrayListOf(createDefaultRole()),
        base64EncodedAuthenticationKey: String = "test-auth-key",
        permissions: ArrayList<String> = arrayListOf(),
        shouldRenewPassword: Boolean = false,
        isTwoFactorAuthenticationRequired: Boolean = false,
        clients: ArrayList<Long> = arrayListOf(1L),
    ): User = User(
        userId = userId,
        isAuthenticated = isAuthenticated,
        username = username,
        officeId = officeId,
        officeName = officeName,
        roles = roles,
        base64EncodedAuthenticationKey = base64EncodedAuthenticationKey,
        permissions = permissions,
        shouldRenewPassword = shouldRenewPassword,
        isTwoFactorAuthenticationRequired = isTwoFactorAuthenticationRequired,
        clients = clients,
    )

    private fun createDefaultRole(): Role = Role(
        id = 1L,
        name = "Self Service User",
        description = "Default self-service user role",
        disabled = false,
    )

    private fun createAdminRole(): Role = Role(
        id = 1L,
        name = "Super User",
        description = "Administrator role with all permissions",
        disabled = false,
    )
}
