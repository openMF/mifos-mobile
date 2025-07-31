/*
 * Copyright 2024 Mifos Initiative
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.ClientChargeRepository
import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.core.network.DataManager

class ClientChargeRepositoryImp(
    private val dataManager: DataManager,
//    private val chargeDao: ChargeDao,
    private val ioDispatcher: CoroutineDispatcher,
) : ClientChargeRepository {

    override fun getCharges(clientId: Long): Flow<DataState<Page<Charge>>> {
        return dataManager.clientChargeApi.getClientChargeList(clientId)
            .map { response -> DataState.Success(response) }
            .catch { exception -> DataState.Error(exception, exception.message) }
            .flowOn(ioDispatcher)
    }

    override fun getLoanOrSavingsCharges(chargeType: ChargeType, chargeTypeId: Long): Flow<DataState<List<Charge>>> {
        return dataManager.clientChargeApi.getChargeList(chargeType.type, chargeTypeId)
            .map { response -> DataState.Success(response) }
            .catch { exception -> DataState.Error(exception, exception.message) }
            .flowOn(ioDispatcher)
    }

    override fun clientLocalCharges(): Flow<DataState<Page<Charge>>> {
//        return chargeDao.getAllLocalCharges().map { chargeList ->
//            Page(chargeList.size, chargeList.map { it.toCharge() })
//        }.flowOn(ioDispatcher)
        return flowOf(DataState.Success(Page(0, emptyList<Charge>())))
            .flowOn(ioDispatcher)
    }

    override suspend fun syncCharges(charges: Page<Charge>?): DataState<Page<Charge>?> {
        return withContext(ioDispatcher) {
//            charges?.pageItems?.let {
//                chargeDao.syncCharges(it.map { it.toChargeEntity() })
//            }
//
//            charges?.copy(pageItems = charges.pageItems)
            val result = charges?.copy(pageItems = charges.pageItems) ?: Page(0, emptyList())
            DataState.Success(result)
        }
    }
}
