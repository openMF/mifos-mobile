package org.mifos.mobile.repositories

import okhttp3.ResponseBody
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.models.accounts.loan.LoanWithdraw
import org.mifos.mobile.models.templates.loans.LoanTemplate
import retrofit2.Response
import javax.inject.Inject

class LoanRepositoryImp @Inject constructor(private val dataManager: DataManager) : LoanRepository {

    override suspend fun getLoanWithAssociations(
        associationType: String?,
        loanId: Long?
    ): Response<LoanWithAssociations?>? {
        return dataManager.getLoanWithAssociations(associationType, loanId)
    }

    override suspend fun withdrawLoanAccount(
        loanId: Long?,
        loanWithdraw: LoanWithdraw?
    ): Response<ResponseBody?>? {
        return dataManager.withdrawLoanAccount(loanId, loanWithdraw)
    }

    override suspend fun template(): Response<LoanTemplate?>? {
        return dataManager.loanTemplate()
    }

    override suspend fun getLoanTemplateByProduct(productId: Int?): Response<LoanTemplate?>? {
        return dataManager.getLoanTemplateByProduct(productId)
    }
}