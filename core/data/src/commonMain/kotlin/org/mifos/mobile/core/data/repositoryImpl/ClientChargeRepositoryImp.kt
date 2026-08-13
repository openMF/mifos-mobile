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

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.mapper.charge.toModel
import org.mifos.mobile.core.data.mapper.share.toShareChargeModel
import org.mifos.mobile.core.data.mapper.toPageModel
import org.mifos.mobile.core.data.repository.ClientChargeRepository
import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.core.network.DataManager
import kotlin.collections.map

class ClientChargeRepositoryImp(
    private val dataManager: DataManager,
//    private val chargeDao: ChargeDao,
    ioDispatcher: CoroutineDispatcher,
) : BaseRepository(ioDispatcher), ClientChargeRepository {

    override fun getCharges(clientId: Long): Flow<DataState<Page<Charge>>> {
        return dataManager.clientChargeApi.getClientChargeList(clientId)
            .map { response ->
                response.toPageModel { dto ->
                    dto.toModel()
                }
            }
            .asDataState()
    }

    override fun getLoanOrSavingsCharges(chargeType: ChargeType, chargeTypeId: Long): Flow<DataState<List<Charge>>> {
        return dataManager.clientChargeApi.getChargeList(chargeType.type, chargeTypeId)
            .map { response ->
                response.map { it.toModel() }
            }
            .asDataState()
    }

    override fun clientLocalCharges(): Flow<DataState<Page<Charge>>> {
//        return chargeDao.getAllLocalCharges().map { chargeList ->
//            Page(chargeList.size, chargeList.map { it.toCharge() })
//        }.flowOn(ioDispatcher)
        return flowOf(DataState.Success(Page(0, emptyList<Charge>())))
            .flowOn(ioDispatcher)
    }

    override suspend fun syncCharges(charges: Page<Charge>?): DataState<Page<Charge>?> = safeCall {
        val page = charges ?: Page(totalFilteredRecords = 0, pageItems = emptyList())
        page
    }

    override fun getShareAccountCharges(shareAccountId: Long): Flow<DataState<List<Charge>>> {
        return dataManager.shareAccountApi.getShareAccountDetails(shareAccountId)
            .map { response ->
                response.charges.map { it.toShareChargeModel() }
            }
            .asDataState()
    }
}
