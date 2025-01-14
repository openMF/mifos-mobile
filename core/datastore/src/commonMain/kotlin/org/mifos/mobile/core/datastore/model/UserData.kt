/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val userId: Long,
    val userName: String,
    val clientId: Long,
    val isAuthenticated: Boolean,
    val base64EncodedAuthenticationKey: String,
) {
    companion object {
        val DEFAULT = UserData(
            userId = -1,
            userName = "",
            clientId = -1,
            isAuthenticated = false,
            base64EncodedAuthenticationKey = "",
        )
    }
}
