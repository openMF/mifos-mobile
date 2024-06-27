package org.mifos.mobile.core.data.repositories

import org.mifos.mobile.core.network.DataManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import org.mifos.mobile.core.model.entity.payload.LoansPayload
import org.mifos.mobile.core.model.enums.LoanState
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
                if (loanState == org.mifos.mobile.core.model.enums.LoanState.CREATE) {
                    dataManager.createLoansAccount(loansPayload)
                } else {
                    dataManager.updateLoanAccount(loanId, loansPayload)
                }
            )
        }
    }

}