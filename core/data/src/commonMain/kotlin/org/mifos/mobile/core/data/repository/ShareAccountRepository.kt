/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.repository

import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.payload.ShareApplicationPayload
import org.mifos.mobile.core.model.entity.templates.shareProductDetails.ShareProductDetails
import org.mifos.mobile.core.model.entity.templates.shares.ShareProduct

interface ShareAccountRepository {

    fun getShareProducts(clientId: Long?): Flow<DataState<Page<ShareProduct>>>

    fun getShareProductById(productId: Long, clientId: Long?): Flow<DataState<ShareProductDetails>>

    suspend fun submitShareApplication(
        payload: ShareApplicationPayload?,
    ): DataState<String>
}
