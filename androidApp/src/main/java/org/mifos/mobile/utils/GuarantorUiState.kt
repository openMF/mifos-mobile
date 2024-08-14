package org.mifos.mobile.utils

import org.mifos.mobile.core.model.entity.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorPayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorTemplatePayload

sealed class GuarantorUiState {
    object Loading : GuarantorUiState()

    data class ShowError(val message: String?) : GuarantorUiState()

    data class GuarantorDeletedSuccessfully(val message: String?) : GuarantorUiState()

    data class GuarantorUpdatedSuccessfully(val message: String?) : GuarantorUiState()

    data class ShowGuarantorListSuccessfully(val payload: List<GuarantorPayload?>?) :
        GuarantorUiState()

    data class ShowGuarantorApplication(val template: GuarantorTemplatePayload?) :
        GuarantorUiState()

    data class ShowGuarantorUpdation(val template: GuarantorTemplatePayload?) : GuarantorUiState()

    data class SubmittedSuccessfully(
        val message: String?,
        val payload: GuarantorApplicationPayload?
    ) : GuarantorUiState()
}