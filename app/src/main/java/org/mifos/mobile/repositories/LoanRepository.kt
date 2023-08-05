package org.mifos.mobile.repositories

import okhttp3.ResponseBody
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.models.accounts.loan.LoanWithdraw
import org.mifos.mobile.models.templates.loans.LoanTemplate
import retrofit2.Response

interface LoanRepository {

    suspend fun getLoanWithAssociations(
        associationType: String?,
        loanId: Long?
    ): Response<LoanWithAssociations?>?

    suspend fun withdrawLoanAccount(
        loanId: Long?,
        loanWithdraw: LoanWithdraw?,
    ): Response<ResponseBody?>?

    suspend fun template(): Response<LoanTemplate?>?

    suspend fun getLoanTemplateByProduct(
        productId: Int?
    ): Response<LoanTemplate?>?
}