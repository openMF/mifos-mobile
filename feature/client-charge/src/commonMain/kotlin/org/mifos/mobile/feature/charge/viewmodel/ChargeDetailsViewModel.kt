/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.charge.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.client_charge.generated.resources.Res
import mifos_mobile.feature.client_charge.generated.resources.date
import mifos_mobile.feature.client_charge.generated.resources.due
import mifos_mobile.feature.client_charge.generated.resources.fee_title
import mifos_mobile.feature.client_charge.generated.resources.outstanding
import mifos_mobile.feature.client_charge.generated.resources.paid
import mifos_mobile.feature.client_charge.generated.resources.waived
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.feature.charge.navigation.ChargesDetailsRoute

internal class ChargeDetailsViewModel(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ChargeDetailsState, ChargeDetailsEvent, ChargeDetailsAction>(
    initialState = ChargeDetailsState(),
) {
    init {
        viewModelScope.launch {
            val title = savedStateHandle.toRoute<ChargesDetailsRoute>().title
            val date = savedStateHandle.toRoute<ChargesDetailsRoute>().date
            val due = savedStateHandle.toRoute<ChargesDetailsRoute>().due
            val paid = savedStateHandle.toRoute<ChargesDetailsRoute>().paid
            val waived = savedStateHandle.toRoute<ChargesDetailsRoute>().waived
            val outstanding = savedStateHandle.toRoute<ChargesDetailsRoute>().outstanding
            val refNo = savedStateHandle.toRoute<ChargesDetailsRoute>().refNo
            val paidOn = savedStateHandle.toRoute<ChargesDetailsRoute>().paidOn
            val isPaid = savedStateHandle.toRoute<ChargesDetailsRoute>().isPaid

            val chargeDetailsMap = mapOf(
                getString(Res.string.fee_title) to title,
                getString(Res.string.date) to date,
                getString(Res.string.due) to due,
                getString(Res.string.paid) to paid,
                getString(Res.string.waived) to waived,
                getString(Res.string.outstanding) to outstanding,
            )
            mutableStateFlow.update {
                it.copy(
                    details = chargeDetailsMap,
                    isPaid = isPaid,
                    refNo = refNo,
                    paidOn = paidOn,
                )
            }
        }
    }

    override fun handleAction(action: ChargeDetailsAction) {
        when (action) {
            ChargeDetailsAction.NavigateBack -> {
                sendEvent(ChargeDetailsEvent.NavigateBack)
            }

            ChargeDetailsAction.PayOutStanding -> {
                // TODO: flow is not there in current figma do accordingly later
            }
        }
    }
}

data class ChargeDetailsState(
    val details: Map<String, String> = emptyMap(),
    val isPaid: Boolean = false,
    val refNo: String = "",
    val paidOn: String = "",
)

sealed interface ChargeDetailsEvent {
    data object NavigateBack : ChargeDetailsEvent
}

sealed interface ChargeDetailsAction {
    data object NavigateBack : ChargeDetailsAction
    data object PayOutStanding : ChargeDetailsAction
}
