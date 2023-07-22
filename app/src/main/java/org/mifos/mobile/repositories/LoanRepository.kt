package org.mifos.mobile.repositories

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.models.accounts.loan.LoanWithdraw
import org.mifos.mobile.models.templates.loans.LoanTemplate

interface LoanRepository {

    fun getLoanWithAssociations(
        associationType: String?,
        loanId: Long?
    ): Observable<LoanWithAssociations?>?

    fun withdrawLoanAccount(
        loanId: Long?,
        loanWithdraw: LoanWithdraw?,
    ): Observable<ResponseBody?>?

    fun template(): Observable<LoanTemplate?>?

    fun getLoanTemplateByProduct(
        productId: Int?
    ): Observable<LoanTemplate?>?
}