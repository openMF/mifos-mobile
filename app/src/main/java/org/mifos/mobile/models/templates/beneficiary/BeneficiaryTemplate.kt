package org.mifos.mobile.models.templates.beneficiary

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by dilpreet on 14/6/17.
 */

@Parcelize
data class BeneficiaryTemplate(
    var accountTypeOptions: List<AccountTypeOption>? = null,
) : Parcelable
