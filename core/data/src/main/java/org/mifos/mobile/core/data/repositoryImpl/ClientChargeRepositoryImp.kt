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
import org.mifos.mobile.core.common.network.Dispatcher
import org.mifos.mobile.core.common.network.MifosDispatchers
import org.mifos.mobile.core.data.repository.ClientChargeRepository
import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.core.network.DataManager
import javax.inject.Inject

class ClientChargeRepositoryImp @Inject constructor(
    private val dataManager: DataManager,
    @Dispatcher(MifosDispatchers.IO)
    private val ioDispatcher: CoroutineDispatcher,
) : ClientChargeRepository {

    override fun getCharges(chargeType: ChargeType, chargeTypeId: Long): Flow<List<Charge>> {
        return flow {
            emit(dataManager.getCharges(chargeType, chargeTypeId))
        }.flowOn(ioDispatcher)
    }
}
