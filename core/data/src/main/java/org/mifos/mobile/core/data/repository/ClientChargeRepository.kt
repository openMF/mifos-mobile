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
import org.mifos.mobile.core.datastore.model.Charge
import org.mifos.mobile.core.model.entity.Page

interface ClientChargeRepository {

    suspend fun getClientCharges(clientId: Long): Flow<Page<Charge>>
    suspend fun getLoanCharges(loanId: Long): Flow<List<Charge>>
    suspend fun getSavingsCharges(savingsId: Long): Flow<List<Charge>>
    suspend fun clientLocalCharges(): Flow<Page<Charge?>>
}
