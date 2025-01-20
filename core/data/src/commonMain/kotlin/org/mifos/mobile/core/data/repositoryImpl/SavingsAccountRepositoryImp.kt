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
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.model.entity.templates.account.AccountOptionsTemplate
import org.mifos.mobile.core.model.entity.templates.savings.SavingsAccountTemplate
import org.mifos.mobile.core.network.DataManager
import org.mifospay.core.common.DataState
import org.mifospay.core.common.asDataStateFlow

class SavingsAccountRepositoryImp(
    private val dataManager: DataManager,
    private val ioDispatcher: CoroutineDispatcher,
) : SavingsAccountRepository {

    override fun getSavingsWithAssociations(
        accountId: Long?,
        associationType: String?,
    ): Flow<DataState<SavingsWithAssociations>> {
        return dataManager.savingAccountsListApi.getSavingsWithAssociations(
            accountId!!,
            associationType,
        )
            .asDataStateFlow().flowOn(ioDispatcher)
    }

    override fun getSavingAccountApplicationTemplate(
        clientId: Long?,
    ): Flow<DataState<SavingsAccountTemplate>> {
        return dataManager.savingAccountsListApi.getSavingsAccountApplicationTemplate(clientId)
            .asDataStateFlow().flowOn(ioDispatcher)
    }

    override suspend fun submitSavingAccountApplication(
        payload: SavingsAccountApplicationPayload?,
    ): DataState<String> {
        return try {
            withContext(ioDispatcher) {
                dataManager.savingAccountsListApi.submitSavingAccountApplication(payload)
            }
            DataState.Success("Submitted successfully")
        } catch (e: Exception) {
            DataState.Error(e, null)
        }
    }

    override suspend fun updateSavingsAccount(
        accountId: Long?,
        payload: SavingsAccountUpdatePayload?,
    ): DataState<String> {
        return try {
            withContext(ioDispatcher) {
                dataManager.savingAccountsListApi.updateSavingsAccountUpdate(accountId!!, payload)
            }
            DataState.Success("Updated successfully")
        } catch (e: Exception) {
            DataState.Error(e, null)
        }
    }

    override suspend fun submitWithdrawSavingsAccount(
        accountId: String?,
        payload: SavingsAccountWithdrawPayload?,
    ): DataState<String> {
        return try {
            withContext(ioDispatcher) {
                dataManager.savingAccountsListApi.submitWithdrawSavingsAccount(accountId!!, payload)
            }
            DataState.Success("Submitted successfully")
        } catch (e: Exception) {
            DataState.Error(e, null)
        }
    }

    override fun accountTransferTemplate(
        accountId: Long?,
        accountType: Long?,
    ): Flow<DataState<AccountOptionsTemplate>> {
        return dataManager.savingAccountsListApi.accountTransferTemplate(accountId!!, accountType)
            .asDataStateFlow().flowOn(ioDispatcher)
    }
}
