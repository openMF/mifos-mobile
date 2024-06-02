package org.mifos.mobile.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.models.accounts.loan.LoanWithdraw
import org.mifos.mobile.models.templates.loans.LoanTemplate
import javax.inject.Inject

class LoanRepositoryImp @Inject constructor(private val dataManager: DataManager) : LoanRepository {

    override fun getLoanWithAssociations(
        associationType: String?,
        loanId: Long?
    ): Flow<LoanWithAssociations?> {
        return flow {
            emit(dataManager.getLoanWithAssociations(associationType, loanId))
        }
    }

    override fun withdrawLoanAccount(
        loanId: Long?,
        loanWithdraw: LoanWithdraw?
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