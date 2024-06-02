package org.mifos.mobile.repositories

import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.models.accounts.loan.LoanWithdraw
import org.mifos.mobile.models.templates.loans.LoanTemplate

interface LoanRepository {

    fun getLoanWithAssociations(
        associationType: String?,
        loanId: Long?
    ): Flow<LoanWithAssociations?>

    fun withdrawLoanAccount(
        loanId: Long?,
        loanWithdraw: LoanWithdraw?,
    ): Flow<ResponseBody?>?

    fun template(): Flow<LoanTemplate?>

    fun getLoanTemplateByProduct(
        productId: Int?
    ): Flow<LoanTemplate?>
}