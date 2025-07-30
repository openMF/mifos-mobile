/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */

@file:Suppress("MatchingDeclarationName")

package org.mifos.mobile.feature.charge.chargeDetails

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.ui.composableWithStayTransitions

@Serializable
data class ChargesDetailsRoute(
    val title: String = "",
    val date: String = "",
    val due: String = "",
    val paid: String = "",
    val waived: String = "",
    val outstanding: String = ",",
    val refNo: String = "",
    val paidOn: String = "",
    val isPaid: Boolean = false,
)

fun NavGraphBuilder.chargesDetailsDestination(onNavigateBack: () -> Unit) {
    composableWithStayTransitions<ChargesDetailsRoute> {
        ChargeDetailScreen(
            onNavigateBack = onNavigateBack,
        )
    }
}

// TODO: last charge paid On , needed that.
// TODO: Add reference No instead of chargeId
fun NavController.navigateToChargesDetailsScreen(charge: Charge) {
    this.navigate(
        ChargesDetailsRoute(
            title = charge.name ?: "",
            isPaid = charge.paid,
            waived = charge.amountWaived.toString(),
            outstanding = charge.amountOutstanding.toString(),
            date = DateHelper.getDateAsString(charge.dueDate.mapNotNull { it }),
            due = charge.amount.toString(),
            paid = charge.amountPaid.toString(),
            refNo = charge.chargeId.toString(),
            paidOn = "",
        ),
    )
}
