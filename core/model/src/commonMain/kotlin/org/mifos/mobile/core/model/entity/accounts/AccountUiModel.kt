package org.mifos.mobile.core.model.entity.accounts

import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.core.model.entity.accounts.share.ShareAccount

sealed interface AccountUiModel {
    val id: Long

    data class Loan(val data: LoanAccount) : AccountUiModel {
        override val id = data.id
    }

    data class Savings(val data: SavingAccount) : AccountUiModel {
        override val id = data.id
    }

    data class Share(val data: ShareAccount) : AccountUiModel {
        override val id = data.id
    }
}
