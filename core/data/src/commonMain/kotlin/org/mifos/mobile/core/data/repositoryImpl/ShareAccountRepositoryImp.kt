/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.repositoryImpl

import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.mapper.payloads.toDto
import org.mifos.mobile.core.data.mapper.share.toModel
import org.mifos.mobile.core.data.mapper.toPageModel
import org.mifos.mobile.core.data.repository.ShareAccountRepository
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.accounts.share.ShareAccountWithAssociations
import org.mifos.mobile.core.model.entity.payload.ShareApplicationPayload
import org.mifos.mobile.core.model.entity.templates.shareProductDetails.ShareProductDetails
import org.mifos.mobile.core.model.entity.templates.shares.ShareProduct
import org.mifos.mobile.core.network.DataManager

class ShareAccountRepositoryImp(
    private val dataManager: DataManager,
    ioDispatcher: CoroutineDispatcher,
) : BaseRepository(ioDispatcher), ShareAccountRepository {

    override fun getShareProducts(clientId: Long?): Flow<DataState<Page<ShareProduct>>> {
        return dataManager.shareAccountApi.getShareProducts(clientId)
            .map { response ->
                response.toPageModel { dto ->
                    dto.toModel()
                }
            }
            .asDataState()
    }

    override fun getShareProductById(
        productId: Long,
        clientId: Long?,
    ): Flow<DataState<ShareProductDetails>> {
        return dataManager.shareAccountApi.getShareProductById(productId, clientId)
            .map { it.toModel() }
            .asDataState()
    }

    override suspend fun submitShareApplication(
        payload: ShareApplicationPayload?,
    ): DataState<String> = safeCall {
        dataManager.shareAccountApi
            .submitShareApplication(payload?.toDto())
            .bodyAsText()
    }

    override fun getShareAccountDetails(accountId: Long): Flow<DataState<ShareAccountWithAssociations>> {
        return dataManager.shareAccountApi.getShareAccountDetails(accountId)
            .map { it.toModel() }
            .asDataState()
    }
}
