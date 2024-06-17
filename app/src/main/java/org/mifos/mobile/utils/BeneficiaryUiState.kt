package org.mifos.mobile.utils

import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.models.templates.beneficiary.BeneficiaryTemplate


sealed class BeneficiaryUiState {
    object Initial : BeneficiaryUiState()
    object Loading : BeneficiaryUiState()
    object CreatedSuccessfully : BeneficiaryUiState()
    object UpdatedSuccessfully : BeneficiaryUiState()
    object DeletedSuccessfully : BeneficiaryUiState()
    data class ShowError(val message: Int) : BeneficiaryUiState()
    data class SetVisibility(val visibility: Int) : BeneficiaryUiState()
    data class ShowBeneficiaryTemplate(val beneficiaryTemplate: BeneficiaryTemplate) :
        BeneficiaryUiState()

    data class ShowBeneficiaryList(val beneficiaries: List<Beneficiary?>) : BeneficiaryUiState()
}

