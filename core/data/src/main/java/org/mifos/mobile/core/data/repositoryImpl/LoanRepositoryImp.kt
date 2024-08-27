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
import org.mifos.mobile.core.data.repository.LoanRepository
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithdraw
import org.mifos.mobile.core.model.entity.templates.loans.LoanTemplate
import org.mifos.mobile.core.network.DataManager
import javax.inject.Inject

class LoanRepositoryImp @Inject constructor(
    private val dataManager: DataManager,
) : LoanRepository {

    override fun getLoanWithAssociations(
        associationType: String?,
        loanId: Long?,
    ): Flow<LoanWithAssociations?> {
        return flow {
            emit(dataManager.getLoanWithAssociations(associationType, loanId))
        }
    }

    override fun withdrawLoanAccount(
        loanId: Long?,
        loanWithdraw: LoanWithdraw?,
    ): Flow<ResponseBody?>? {
        return flow {
            emit(dataManager.withdrawLoanAccount(loanId, loanWithdraw))
        }
    }

    override fun template(): Flow<LoanTemplate?> {
        return flow {
            emit(dataManager.loanTemplate())
        }
    }

    override fun getLoanTemplateByProduct(productId: Int?): Flow<LoanTemplate?> {
        return flow {
            emit(dataManager.getLoanTemplateByProduct(productId))
        }
    }
}
