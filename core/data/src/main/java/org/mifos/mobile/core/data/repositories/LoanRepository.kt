package org.mifos.mobile.core.data.repositories

import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithdraw
import org.mifos.mobile.core.model.entity.templates.loans.LoanTemplate

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