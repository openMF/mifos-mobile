package org.mifos.mobile.repositories

import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import org.mifos.mobile.models.payload.LoansPayload
import org.mifos.mobile.ui.enums.LoanState

interface ReviewLoanApplicationRepository {

    suspend fun submitLoan(
        loanState: LoanState,
        loansPayload: LoansPayload,
        loanId: Long
    ): Flow<ResponseBody>

}