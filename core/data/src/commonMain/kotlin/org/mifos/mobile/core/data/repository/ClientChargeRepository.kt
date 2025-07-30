/*
 * Copyright 2024 Mifos Initiative
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
import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.enums.ChargeType

interface ClientChargeRepository {

    fun getCharges(clientId: Long): Flow<DataState<Page<Charge>>>

    fun getLoanOrSavingsCharges(chargeType: ChargeType, chargeTypeId: Long): Flow<DataState<List<Charge>>>

    fun clientLocalCharges(): Flow<DataState<Page<Charge>>>

    suspend fun syncCharges(charges: Page<Charge>?): DataState<Page<Charge>?>
}
