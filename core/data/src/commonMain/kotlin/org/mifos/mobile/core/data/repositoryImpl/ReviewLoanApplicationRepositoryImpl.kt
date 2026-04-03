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

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.mapper.payloads.toDto
import org.mifos.mobile.core.data.repository.ReviewLoanApplicationRepository
import org.mifos.mobile.core.data.util.toMifosExceptionSuspend
import org.mifos.mobile.core.model.entity.payload.LoansPayload
import org.mifos.mobile.core.model.enums.LoanState
import org.mifos.mobile.core.network.DataManager

class ReviewLoanApplicationRepositoryImpl(
    private val dataManager: DataManager,
    private val ioDispatcher: CoroutineDispatcher,
) : ReviewLoanApplicationRepository {

    override suspend fun submitLoan(
        loanState: LoanState,
        loansPayload: LoansPayload,
        loanId: Long,
    ): DataState<String> {
        return withContext(ioDispatcher) {
            try {
                when (loanState) {
                    LoanState.CREATE -> {
                        val response =
                            dataManager.loanAccountsListApi.createLoansAccount(loansPayload.toDto())
                        println("response $response")
                        return@withContext DataState.Success("Loan Created Successfully")
                    }
                    LoanState.UPDATE -> {
                        val response =
                            dataManager.loanAccountsListApi.updateLoanAccount(loanId, loansPayload.toDto())
                        println("response $response")
                        return@withContext DataState.Success("Loan Updated Successfully")
                    }
                }
            } catch (e: Exception) {
                DataState.Error(e.toMifosExceptionSuspend(), null)
            }
        }
    }
}
