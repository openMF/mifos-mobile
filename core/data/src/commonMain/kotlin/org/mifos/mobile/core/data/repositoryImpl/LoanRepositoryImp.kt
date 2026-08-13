/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.repositoryImpl

import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.mapper.loan.toModel
import org.mifos.mobile.core.data.mapper.payloads.toDto
import org.mifos.mobile.core.data.mapper.templates.toModel
import org.mifos.mobile.core.data.mapper.transactions.toModel
import org.mifos.mobile.core.data.repository.LoanRepository
import org.mifos.mobile.core.model.entity.TransactionDetails
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithdraw
import org.mifos.mobile.core.model.entity.templates.loans.LoanTemplate
import org.mifos.mobile.core.network.DataManager

class LoanRepositoryImp(
    private val dataManager: DataManager,
    ioDispatcher: CoroutineDispatcher,
) : BaseRepository(ioDispatcher), LoanRepository {

    override fun getLoanWithAssociations(
        associationType: String?,
        loanId: Long?,
    ): Flow<DataState<LoanWithAssociations?>> =
        dataManager.loanAccountsListApi.getLoanWithAssociations(loanId!!, associationType)
            .map { response -> response.toModel() }
            .asDataState()

    override fun getLoanTransactionDetails(
        loanId: Long,
        transactionId: Long,
    ): Flow<DataState<TransactionDetails>> {
        return dataManager.loanAccountsListApi
            .getLoanTransactionDetails(loanId, transactionId)
            .map { it.toModel() }
            .asDataState()
    }

    override suspend fun withdrawLoanAccount(
        loanId: Long?,
        loanWithdraw: LoanWithdraw?,
    ): DataState<String> = safeCall {
        dataManager.loanAccountsListApi
            .withdrawLoanAccount(loanId!!, loanWithdraw?.toDto())
            .bodyAsText()
    }

    override fun template(clientId: Long?): Flow<DataState<LoanTemplate?>> {
        return dataManager.loanAccountsListApi.getLoanTemplate(clientId = clientId)
            .map { it.toModel() }
            .asDataState()
    }

    override fun getLoanTemplateByProduct(clientId: Long?, productId: Int?): Flow<DataState<LoanTemplate?>> {
        return dataManager.loanAccountsListApi.getLoanTemplateByProduct(clientId, productId)
            .map { it.toModel() }
            .asDataState()
    }
}
