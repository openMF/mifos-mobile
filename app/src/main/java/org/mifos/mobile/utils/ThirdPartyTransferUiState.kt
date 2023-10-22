package org.mifos.mobile.utils

import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate

sealed class ThirdPartyTransferUiState {
    object Initial : ThirdPartyTransferUiState()
    object Loading : ThirdPartyTransferUiState()
    data class Error(val message: Int) : ThirdPartyTransferUiState()
    data class ShowThirdPartyTransferTemplate(val accountOptionsTemplate: AccountOptionsTemplate?) :
        ThirdPartyTransferUiState()

    data class ShowBeneficiaryList(val beneficiaries: List<Beneficiary?>?) :
        ThirdPartyTransferUiState()
}
