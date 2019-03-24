package org.mifos.mobile.models

import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate

/**
 * Created by dilpreet on 23/6/17.
 */

data class AccountOptionAndBeneficiary(
        val accountOptionsTemplate: AccountOptionsTemplate,
        val beneficiaryList: List<Beneficiary>)
