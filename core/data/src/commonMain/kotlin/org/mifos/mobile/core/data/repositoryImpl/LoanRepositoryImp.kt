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
import org.mifos.mobile.core.data.repository.LoanRepository
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithdraw
import org.mifos.mobile.core.model.entity.templates.loans.LoanTemplate
import org.mifos.mobile.core.network.DataManager
import org.mifospay.core.common.DataState
import org.mifospay.core.common.asDataStateFlow

class LoanRepositoryImp(
    private val dataManager: DataManager,
    private val ioDispatcher: CoroutineDispatcher,
) : LoanRepository {

    override fun getLoanWithAssociations(
        associationType: String?,
        loanId: Long?,
    ): Flow<DataState<LoanWithAssociations?>> {
        return dataManager.loanAccountsListApi.getLoanWithAssociations(loanId!!, associationType)
            .asDataStateFlow().flowOn(ioDispatcher)
    }

    override suspend fun withdrawLoanAccount(
        loanId: Long?,
        loanWithdraw: LoanWithdraw?,
    ): DataState<String> {
        return try {
            withContext(ioDispatcher) {
                dataManager.loanAccountsListApi.withdrawLoanAccount(loanId!!, loanWithdraw)
            }
            DataState.Success("withdraw successful")
        } catch (e: Exception) {
            DataState.Error(e, null)
        }
    }

    override fun template(clientId: Long?): Flow<DataState<LoanTemplate?>> {
        return dataManager.loanAccountsListApi.getLoanTemplate(clientId = clientId)
            .asDataStateFlow().flowOn(ioDispatcher)
    }

    override fun getLoanTemplateByProduct(clientId: Long?, productId: Int?): Flow<DataState<LoanTemplate?>> {
        return dataManager.loanAccountsListApi.getLoanTemplateByProduct(clientId, productId)
            .asDataStateFlow().flowOn(ioDispatcher)
    }
}
