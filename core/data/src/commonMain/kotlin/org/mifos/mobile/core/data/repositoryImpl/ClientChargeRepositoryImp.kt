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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.mapper.charge.toEntity
import org.mifos.mobile.core.data.mapper.charge.toModel
import org.mifos.mobile.core.data.mapper.share.toShareChargeModel
import org.mifos.mobile.core.data.repository.ClientChargeRepository
import org.mifos.mobile.core.data.util.asMifosDataStateFlow
import org.mifos.mobile.core.database.dao.ChargeDao
import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.core.network.DataManager
import org.mobilenativefoundation.store.store5.Store
import template.core.base.store.mapData
import template.core.base.store.streamData

class ClientChargeRepositoryImp(
    private val dataManager: DataManager,
    private val chargeStore: Store<Long, List<Charge>>,
    private val chargeDao: ChargeDao,
    private val ioDispatcher: CoroutineDispatcher,
) : ClientChargeRepository {

    override fun getCharges(clientId: Long): Flow<DataState<Page<Charge>>> {
        return chargeStore.streamData(clientId)
            .mapData { charges -> Page(charges.size, charges) }
            .asMifosDataStateFlow()
            .flowOn(ioDispatcher)
    }

    override fun getLoanOrSavingsCharges(chargeType: ChargeType, chargeTypeId: Long): Flow<DataState<List<Charge>>> {
        return dataManager.clientChargeApi.getChargeList(chargeType.type, chargeTypeId)
            .map<_, DataState<List<Charge>>> { response ->
                DataState.Success(
                    response.map { it.toModel() },
                )
            }
            .catch { exception -> emit(DataState.Error(exception)) }
            .flowOn(ioDispatcher)
    }

    override fun clientLocalCharges(): Flow<DataState<Page<Charge>>> {
        return chargeDao.getAllLocalCharges()
            .map { chargeList ->
                DataState.Success(Page(chargeList.size, chargeList.map { it.toModel() }))
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun syncCharges(charges: Page<Charge>?): DataState<Page<Charge>?> {
        return withContext(ioDispatcher) {
            charges?.pageItems?.let { items ->
                chargeDao.syncCharges(items.map { it.toEntity() })
            }
            DataState.Success(charges)
        }
    }

    override fun getShareAccountCharges(shareAccountId: Long): Flow<DataState<List<Charge>>> {
        return dataManager.shareAccountApi.getShareAccountDetails(shareAccountId)
            .map<_, DataState<List<Charge>>> { response ->
                DataState.Success(
                    response.charges.map { it.toShareChargeModel() },
                )
            }
            .catch { exception ->
                emit(DataState.Error(exception))
            }
            .flowOn(ioDispatcher)
    }
}
