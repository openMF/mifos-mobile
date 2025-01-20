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
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.model.entity.templates.account.AccountOptionsTemplate
import org.mifos.mobile.core.model.entity.templates.savings.SavingsAccountTemplate
import org.mifospay.core.common.DataState

interface SavingsAccountRepository {

    fun getSavingsWithAssociations(
        accountId: Long?,
        associationType: String?,
    ): Flow<DataState<SavingsWithAssociations>>

    fun getSavingAccountApplicationTemplate(clientId: Long?): Flow<DataState<SavingsAccountTemplate>>

    suspend fun submitSavingAccountApplication(payload: SavingsAccountApplicationPayload?): DataState<String>

    suspend fun updateSavingsAccount(
        accountId: Long?,
        payload: SavingsAccountUpdatePayload?,
    ): DataState<String>

    suspend fun submitWithdrawSavingsAccount(
        accountId: String?,
        payload: SavingsAccountWithdrawPayload?,
    ): DataState<String>

    fun accountTransferTemplate(accountId: Long?, accountType: Long?): Flow<DataState<AccountOptionsTemplate>>
}
