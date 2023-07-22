package org.mifos.mobile.repositories

import io.reactivex.Observable
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
    ): Observable<LoanWithAssociations?>? {
        return dataManager.getLoanWithAssociations(associationType, loanId)
    }

    override fun withdrawLoanAccount(
        loanId: Long?,
        loanWithdraw: LoanWithdraw?
    ): Observable<ResponseBody?>? {
        return dataManager.withdrawLoanAccount(loanId, loanWithdraw)
    }

    override fun template(): Observable<LoanTemplate?>? {
        return dataManager.loanTemplate
    }

    override fun getLoanTemplateByProduct(productId: Int?): Observable<LoanTemplate?>? {
        return dataManager.getLoanTemplateByProduct(productId)
    }
}