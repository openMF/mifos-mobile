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

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import org.mifos.mobile.core.data.repository.ReviewLoanApplicationRepository
import org.mifos.mobile.core.model.entity.payload.LoansPayload
import org.mifos.mobile.core.model.enums.LoanState
import org.mifos.mobile.core.network.DataManager
import javax.inject.Inject

class ReviewLoanApplicationRepositoryImpl @Inject constructor(
    private val dataManager: DataManager,
) : ReviewLoanApplicationRepository {

    override suspend fun submitLoan(
        loanState: LoanState,
        loansPayload: LoansPayload,
        loanId: Long,
    ): Flow<ResponseBody> {
        return flow {
            emit(
                if (loanState == org.mifos.mobile.core.model.enums.LoanState.CREATE) {
                    dataManager.createLoansAccount(loansPayload)
                } else {
                    dataManager.updateLoanAccount(loanId, loansPayload)
                },
            )
        }
    }
}
