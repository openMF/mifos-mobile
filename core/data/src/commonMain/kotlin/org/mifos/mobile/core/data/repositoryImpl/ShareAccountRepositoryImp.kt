/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.repositoryImpl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.asDataStateFlow
import org.mifos.mobile.core.data.repository.ShareAccountRepository
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.templates.shareProduct.ShareDetails
import org.mifos.mobile.core.model.entity.templates.shares.ShareProduct
import org.mifos.mobile.core.network.DataManager

class ShareAccountRepositoryImp(
    private val dataManager: DataManager,
    private val ioDispatcher: CoroutineDispatcher,
) : ShareAccountRepository {

    override fun getShareProducts(clientId: Long?): Flow<DataState<Page<ShareProduct>>> {
        return dataManager.shareAccountApi.getShareProducts(clientId)
            .asDataStateFlow().flowOn(ioDispatcher)
    }

    override fun getShareProductById(
        productId: Long,
        clientId: String?,
    ): Flow<DataState<ShareDetails>> {
        return dataManager.shareAccountApi.getShareProductById(productId, clientId)
            .asDataStateFlow().flowOn(ioDispatcher)
    }
}
