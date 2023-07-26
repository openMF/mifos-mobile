package org.mifos.mobile.repositories

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.models.accounts.loan.LoanWithdraw
import org.mifos.mobile.models.templates.loans.LoanTemplate

interface LoanRepository {

    suspend fun getLoanWithAssociations(
        associationType: String?,
        loanId: Long?
    ): Observable<LoanWithAssociations?>?

    suspend fun withdrawLoanAccount(
        loanId: Long?,
        loanWithdraw: LoanWithdraw?,
    ): Observable<ResponseBody?>?

    suspend fun template(): Observable<LoanTemplate?>?

    suspend fun getLoanTemplateByProduct(
        productId: Int?
    ): Observable<LoanTemplate?>?
}