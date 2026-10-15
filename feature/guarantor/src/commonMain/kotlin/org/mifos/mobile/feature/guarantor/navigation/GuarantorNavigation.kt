/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.guarantor.navigation

import org.mifos.mobile.core.common.Constants.INDEX
import org.mifos.mobile.core.common.Constants.LOAN_ID
import org.mifos.mobile.feature.guarantor.navigation.GuarantorRoute.GUARANTOR_ADD_SCREEN_ROUTE
import org.mifos.mobile.feature.guarantor.navigation.GuarantorRoute.GUARANTOR_DETAIL_SCREEN_ROUTE
import org.mifos.mobile.feature.guarantor.navigation.GuarantorRoute.GUARANTOR_LIST_SCREEN_ROUTE
import org.mifos.mobile.feature.guarantor.navigation.GuarantorRoute.GUARANTOR_NAVIGATION_ROUTE_BASE

/**
 * Sealed class representing the possible navigation destinations within the Guarantor feature.
 *
 * This class defines all the navigation routes for the guarantor management flow,
 * including list, detail, and add screens with their respective argument requirements.
 */
sealed class GuarantorNavigation(val route: String) {

    /**
     * Represents the base route for the Guarantor feature.
     * Serves as the entry point for the guarantor navigation graph.
     */
    data object GuarantorScreenBase : GuarantorNavigation(
        route = "$GUARANTOR_NAVIGATION_ROUTE_BASE/{$LOAN_ID}",
    ) {
        fun passArguments(loanId: String) = "$GUARANTOR_NAVIGATION_ROUTE_BASE/$loanId"
    }

    /**
     * Represents the guarantor list screen route.
     * Displays all guarantors for a specific loan.
     */
    data object GuarantorList : GuarantorNavigation(
        route = "$GUARANTOR_LIST_SCREEN_ROUTE/{$LOAN_ID}",
    ) {
        fun passArguments(loanId: String) = "$GUARANTOR_LIST_SCREEN_ROUTE/$loanId"
    }

    /**
     * Represents the guarantor detail screen route.
     * Shows detailed information about a specific guarantor.
     */
    data object GuarantorDetails : GuarantorNavigation(
        route = "$GUARANTOR_DETAIL_SCREEN_ROUTE/{$LOAN_ID}/{$INDEX}",
    ) {
        fun passArguments(index: Int, loanId: Long) = "$GUARANTOR_DETAIL_SCREEN_ROUTE/$loanId/$index"
    }

    /**
     * Represents the add/edit guarantor screen route.
     * Used for both adding new guarantors and editing existing ones.
     */
    data object GuarantorAdd : GuarantorNavigation(
        route = "$GUARANTOR_ADD_SCREEN_ROUTE/{$LOAN_ID}/{$INDEX}",
    ) {
        fun passArguments(index: Int, loanId: Long) = "$GUARANTOR_ADD_SCREEN_ROUTE/$loanId/$index"
    }
}

/**
 * Object containing the route constants for the Guarantor feature navigation.
 *
 * These constants define the base routes used throughout the guarantor navigation graph.
 */
object GuarantorRoute {
    const val GUARANTOR_NAVIGATION_ROUTE_BASE = "guarantor_route"
    const val GUARANTOR_LIST_SCREEN_ROUTE = "guarantor_list_screen_route"
    const val GUARANTOR_DETAIL_SCREEN_ROUTE = "guarantor_detail_screen_route"
    const val GUARANTOR_ADD_SCREEN_ROUTE = "guarantor_add_screen_route"
}
