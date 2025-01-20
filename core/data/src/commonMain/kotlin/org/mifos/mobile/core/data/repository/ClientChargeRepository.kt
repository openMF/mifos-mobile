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
import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.model.entity.Page
import org.mifospay.core.common.DataState

interface ClientChargeRepository {

    fun getClientCharges(clientId: Long): Flow<DataState<Page<Charge>>>

    fun getLoanCharges(loanId: Long): Flow<DataState<List<Charge>>>

    fun getSavingsCharges(savingsId: Long): Flow<DataState<List<Charge>>>

    fun clientLocalCharges(): Flow<DataState<Page<Charge>>>

    suspend fun syncCharges(charges: Page<Charge>?): DataState<Page<Charge>?>
}
