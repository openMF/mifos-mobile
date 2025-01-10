/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.register

/**
 * Created by dilpreet on 31/7/17.
 */

data class RegisterPayload(

    val username: String? = null,

    val firstName: String? = null,

    val lastName: String? = null,

    val email: String? = null,

    val mobileNumber: String? = null,

    val accountNumber: String? = null,

    val password: String? = null,

    val authenticationMode: String? = null,
)
