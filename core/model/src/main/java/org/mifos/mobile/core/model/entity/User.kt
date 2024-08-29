/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity

/**
 * @author Vishwajeet
 * @since 12/06/16
 */

data class User(

    var userId: Long = 0,
    var isAuthenticated: Boolean = false,
    var username: String? = null,
    var base64EncodedAuthenticationKey: String? = null,
    var permissions: List<String> = ArrayList(),
)
