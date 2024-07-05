package org.mifos.mobile.feature.client_charge.utils

import org.mifos.mobile.core.datastore.model.Charge

sealed class ClientChargeState {
    data object Loading : ClientChargeState()
    data class Error(val message: String?) : ClientChargeState()
    data class Success(val charges: List<Charge>) : ClientChargeState()
}
