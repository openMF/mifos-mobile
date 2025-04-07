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
import kotlinx.coroutines.withContext
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.ReviewLoanApplicationRepository
import org.mifos.mobile.core.data.util.extractErrorMessage
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
        return try {
            withContext(ioDispatcher) {
                if (loanState == LoanState.CREATE) {
                    val response = dataManager.loanAccountsListApi.createLoansAccount(loansPayload)
                    if (response.status.value != 200) {
                        val errorMessage = extractErrorMessage(response)
                        return@withContext DataState.Error(
                            Exception(errorMessage),
                            null,
                        )
                    }
                    DataState.Success("Loan Created successfully")
                } else {
                    val response = dataManager.loanAccountsListApi.updateLoanAccount(
                        loanId,
                        loansPayload,
                    )
                    if (response.status.value != 200) {
                        val errorMessage = extractErrorMessage(response)
                        return@withContext DataState.Error(
                            Exception(errorMessage),
                            null,
                        )
                    }
                    DataState.Success("Loan Updated Successfully")
                }
            }
        } catch (e: Exception) {
            DataState.Error(e, null)
        }
    }
}
