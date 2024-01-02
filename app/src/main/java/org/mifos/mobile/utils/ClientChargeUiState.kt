package org.mifos.mobile.utils

import org.mifos.mobile.models.Charge

sealed class ClientChargeUiState {
    object Initial : ClientChargeUiState()
    object Loading : ClientChargeUiState()
    data class ShowError(val message: Int) : ClientChargeUiState()
    data class ShowClientCharges(val charges: List<Charge?>) : ClientChargeUiState()
}
