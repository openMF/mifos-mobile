package org.mifos.mobile.core.model.entity

import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.templates.account.AccountOptionsTemplate

/**
 * Created by dilpreet on 23/6/17.
 */

data class AccountOptionAndBeneficiary(
    val accountOptionsTemplate: AccountOptionsTemplate,
    val beneficiaryList: List<Beneficiary?>,
)
