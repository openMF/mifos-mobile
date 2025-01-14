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

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.PUT
import io.ktor.client.statement.HttpResponse
import org.mifos.mobile.core.model.entity.UpdatePasswordPayload
import org.mifos.mobile.core.network.utils.ApiEndPoints

interface UserDetailsService {
    @PUT(ApiEndPoints.USER)
    suspend fun updateAccountPassword(@Body payload: UpdatePasswordPayload?): HttpResponse
}
