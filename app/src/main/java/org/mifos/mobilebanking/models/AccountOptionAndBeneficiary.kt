package org.mifos.mobilebanking.models

import org.mifos.mobilebanking.models.beneficiary.Beneficiary
import org.mifos.mobilebanking.models.templates.account.AccountOptionsTemplate

/**
 * Created by dilpreet on 23/6/17.
 */

data class AccountOptionAndBeneficiary(
        val accountOptionsTemplate: AccountOptionsTemplate,
        val beneficiaryList: List<Beneficiary>)
