/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.charge.chargeDetails

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import mifos_mobile.feature.client_charge.generated.resources.Res
import mifos_mobile.feature.client_charge.generated.resources.date
import mifos_mobile.feature.client_charge.generated.resources.due
import mifos_mobile.feature.client_charge.generated.resources.fee_title
import mifos_mobile.feature.client_charge.generated.resources.outstanding
import mifos_mobile.feature.client_charge.generated.resources.paid
import mifos_mobile.feature.client_charge.generated.resources.waived
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.ui.utils.BaseViewModel

/**
 * ChargeDetailsViewModel is a view model that handles the logic for the Charge Details Screen.
 *
 * @param savedStateHandle SavedStateHandle for the view model.
 */
internal class ChargeDetailsViewModel(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ChargeDetailsState, ChargeDetailsEvent, ChargeDetailsAction>(
    initialState = run {
        var route = savedStateHandle.toRoute<ChargesDetailsRoute>()
        val chargeDetailsMap = mapOf(
            Res.string.fee_title to route.title,
            Res.string.date to route.date,
            Res.string.due to route.due,
            Res.string.paid to route.paid,
            Res.string.waived to route.waived,
            Res.string.outstanding to route.outstanding,
        )
        ChargeDetailsState(
            details = chargeDetailsMap,
            isPaid = route.isPaid,
            refNo = route.refNo,
            paidOn = route.paidOn,
        )
    },
) {
    /**
     * Handles the actions for the Charge Details Screen.
     *
     * @param action The action to be handled.
     */
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

/**
 * ChargeDetailsState is a data class that represents the state of the Charge Details Screen.
 *
 * @param details Map of charge details.
 * @param isPaid Boolean indicating if the charge is paid.
 * @param refNo Reference number of the charge.
 * @param paidOn Date when the charge was paid.
 */
data class ChargeDetailsState(
    val details: Map<StringResource, String> = emptyMap(),
    val isPaid: Boolean = false,
    val refNo: String = "",
    val paidOn: String = "",
)

/**
 * ChargeDetailsEvent is a sealed interface that represents the events for the Charge Details Screen.
 *
 * @param NavigateBack Navigates back to the previous screen.
 */
sealed interface ChargeDetailsEvent {
    data object NavigateBack : ChargeDetailsEvent
}

/**
 * ChargeDetailsAction is a sealed interface that represents the actions for the Charge Details Screen.
 *
 * @param NavigateBack Navigates back to the previous screen.
 * @param PayOutStanding Pays the outstanding amount of the charge.
 */
sealed interface ChargeDetailsAction {
    data object NavigateBack : ChargeDetailsAction
    data object PayOutStanding : ChargeDetailsAction
}
