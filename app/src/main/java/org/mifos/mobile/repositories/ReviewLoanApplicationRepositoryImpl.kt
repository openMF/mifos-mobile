package org.mifos.mobile.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.payload.LoansPayload
import org.mifos.mobile.ui.enums.LoanState
import javax.inject.Inject

class ReviewLoanApplicationRepositoryImpl @Inject constructor(private val dataManager: DataManager) :
    ReviewLoanApplicationRepository {

    override suspend fun submitLoan(
        loanState: LoanState,
        loansPayload: LoansPayload,
        loanId: Long
    ): Flow<ResponseBody> {
        return flow {
            emit(
                if (loanState == LoanState.CREATE) {
                    dataManager.createLoansAccount(loansPayload)
                } else {
                    dataManager.updateLoanAccount(loanId, loansPayload)
                }
            )
        }
    }

}