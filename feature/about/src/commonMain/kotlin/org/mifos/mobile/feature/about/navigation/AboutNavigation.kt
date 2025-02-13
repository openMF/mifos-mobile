/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.about.navigation

// Constants for Routes
const val ABOUT_US_NAVIGATION_ROUTE_BASE = "about_us_base_route"
const val ABOUT_US_SCREEN_ROUTE = "about_us_screen_route"
const val PRIVACY_POLICY_SCREEN_ROUTE = "privacy_policy_screen_route"

// Sealed class for Navigation Routes
sealed class AboutUsNavigation(val route: String) {
    data object AboutUsBase : AboutUsNavigation(route = ABOUT_US_NAVIGATION_ROUTE_BASE)
    data object AboutUsScreen : AboutUsNavigation(route = ABOUT_US_SCREEN_ROUTE)
    data object PrivacyPolicyScreen : AboutUsNavigation(route = PRIVACY_POLICY_SCREEN_ROUTE)
}
