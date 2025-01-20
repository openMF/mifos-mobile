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
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithdraw
import org.mifos.mobile.core.model.entity.templates.loans.LoanTemplate
import org.mifospay.core.common.DataState

interface LoanRepository {

    fun getLoanWithAssociations(
        associationType: String?,
        loanId: Long?,
    ): Flow<DataState<LoanWithAssociations?>>

    suspend fun withdrawLoanAccount(
        loanId: Long?,
        loanWithdraw: LoanWithdraw?,
    ): DataState<String>

    fun template(clientId: Long?): Flow<DataState<LoanTemplate?>>

    fun getLoanTemplateByProduct(
        clientId: Long?,
        productId: Int?,
    ): Flow<DataState<LoanTemplate?>>
}
