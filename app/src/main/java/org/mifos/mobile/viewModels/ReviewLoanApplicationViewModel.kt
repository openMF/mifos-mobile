package org.mifos.mobile.viewModels

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import okhttp3.ResponseBody
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.payload.LoansPayload
import org.mifos.mobile.ui.enums.LoanState
import javax.inject.Inject

class ReviewLoanApplicationViewModel @Inject constructor(var dataManager: DataManager) : ViewModel() {

    private lateinit var loansPayload: LoansPayload
    private lateinit var loanState: LoanState
    private lateinit var loanName: String
    private lateinit var accountNo: String
    private var loanId: Long = 0

    fun insertData(loanState: LoanState, loansPayload: LoansPayload, loanName: String, accountNo: String) {
        this.loanState = loanState
        this.loansPayload = loansPayload
        this.loanName = loanName
        this.accountNo = accountNo
    }

    fun insertData(loanState: LoanState, loanId: Long, loansPayload: LoansPayload, loanName: String, accountNo: String) {
        this.loanState = loanState
        this.loanId = loanId
        this.loansPayload = loansPayload
        this.loanName = loanName
        this.accountNo = accountNo
    }

    fun getLoanProduct() = loansPayload.productName

    fun getLoanPurpose() = loansPayload.loanPurpose

    fun getPrincipal() = loansPayload.principal

    fun getCurrency() = loansPayload.currency

    fun getSubmissionDate() = loansPayload.submittedOnDate

    fun getDisbursementDate() = loansPayload.expectedDisbursementDate

    fun getLoanName() = loanName

    fun getAccountNo() = accountNo

    fun getLoanState() = loanState

    fun submitLoan(): Observable<ResponseBody> {
        loansPayload.productName = null
        loansPayload.loanPurpose = null
        loansPayload.currency = null
        if (loanState == LoanState.CREATE)
            return dataManager.createLoansAccount(loansPayload)
        else
            return dataManager.updateLoanAccount(loanId, loansPayload)
    }
}
