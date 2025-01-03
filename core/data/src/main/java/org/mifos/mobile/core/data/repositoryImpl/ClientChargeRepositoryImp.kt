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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.mifos.mobile.core.common.network.Dispatcher
import org.mifos.mobile.core.common.network.MifosDispatchers
import org.mifos.mobile.core.data.model.toCharge
import org.mifos.mobile.core.data.model.toChargeEntity
import org.mifos.mobile.core.data.repository.ClientChargeRepository
import org.mifos.mobile.core.database.dao.ChargeDao
import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.network.DataManager
import javax.inject.Inject

class ClientChargeRepositoryImp @Inject constructor(
    private val dataManager: DataManager,
    private val chargeDao: ChargeDao,
    @Dispatcher(MifosDispatchers.IO)
    private val ioDispatcher: CoroutineDispatcher,
) : ClientChargeRepository {

    override fun getClientCharges(clientId: Long): Flow<Page<Charge>> {
        return flow {
            emit(dataManager.getClientCharges(clientId))
        }
    }

    override fun getLoanCharges(loanId: Long): Flow<List<Charge>> {
        return flow {
            emit(dataManager.getLoanCharges(loanId))
        }
    }

    override fun getSavingsCharges(savingsId: Long): Flow<List<Charge>> {
        return flow {
            emit(dataManager.getSavingsCharges(savingsId))
        }
    }

    override fun clientLocalCharges(): Flow<Page<Charge>> {
        return chargeDao.getAllLocalCharges().map { chargeList ->
            Page(chargeList.size, chargeList.map { it.toCharge() })
        }.flowOn(ioDispatcher)
    }

    override suspend fun syncCharges(charges: Page<Charge>?): Page<Charge>? {
        return withContext(ioDispatcher) {
            charges?.pageItems?.let {
                chargeDao.syncCharges(it.map { it.toChargeEntity() })
            }

            charges?.copy(pageItems = charges.pageItems)
        }
    }
}
