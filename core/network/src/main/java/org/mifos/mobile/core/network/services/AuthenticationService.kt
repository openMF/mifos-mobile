/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network.services

import org.mifos.mobile.core.common.ApiEndPoints
import org.mifos.mobile.core.model.entity.User
import org.mifos.mobile.core.model.entity.payload.LoginPayload
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationService {

    @POST(ApiEndPoints.AUTHENTICATION)
    suspend fun authenticate(@Body loginPayload: LoginPayload): User
}
