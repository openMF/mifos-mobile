package org.mifos.mobile.ui.transaction

import org.mifos.mobile.models.entity.Transaction
import org.mifos.mobile.models.entity.TransactionInfo
import org.mifos.mobile.ui.views.base.MVPView

interface TransactionContract {
    interface View : MVPView {
        //TODO:edit this for access tokens and other user data
        fun showTransactionSuccessfully(transactionInfo: TransactionInfo)

        fun showTransactionUnSuccessfully(message: String?)

        fun showError(errorMessage: String)
    }
    interface Presenter {

        fun transaction(transaction: Transaction)
    }
}