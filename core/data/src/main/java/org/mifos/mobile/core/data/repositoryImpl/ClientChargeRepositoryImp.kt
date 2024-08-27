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

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mifos.mobile.core.data.repository.ClientChargeRepository
import org.mifos.mobile.core.datastore.model.Charge
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.network.DataManager
import javax.inject.Inject

class ClientChargeRepositoryImp @Inject constructor(
    private val dataManager: DataManager,
) : ClientChargeRepository {

    override suspend fun getClientCharges(clientId: Long): Flow<Page<Charge>> {
        return flow {
            emit(dataManager.getClientCharges(clientId))
        }
    }

    override suspend fun getLoanCharges(loanId: Long): Flow<List<Charge>> {
        return flow {
            emit(dataManager.getLoanCharges(loanId))
        }
    }

    override suspend fun getSavingsCharges(savingsId: Long): Flow<List<Charge>> {
        return flow {
            emit(dataManager.getSavingsCharges(savingsId))
        }
    }

    override suspend fun clientLocalCharges(): Flow<Page<Charge?>> {
        return flow {
            emit(dataManager.clientLocalCharges())
        }
    }
}
