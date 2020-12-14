package org.mifos.mobile.ui.transaction

import org.mifos.mobile.models.entity.Transaction
import org.mifos.mobile.models.entity.TransactionInfo
import org.mifos.mobile.ui.views.base.MVPView

interface TransactionContract {
    interface View : MVPView {

        fun showTransactionSuccessfully()

        fun showTransactionUnSuccessfully()

        fun showError(errorMessage: String)

    }

    interface Presenter {
        fun makeTransaction(transaction: Transaction)
    }
}