package org.mifos.mobile.core.data.repositories

import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import org.mifos.mobile.core.model.entity.payload.LoansPayload
import org.mifos.mobile.core.model.enums.LoanState

interface ReviewLoanApplicationRepository {

    suspend fun submitLoan(
        loanState: LoanState,
        loansPayload: LoansPayload,
        loanId: Long
    ): Flow<ResponseBody>

}