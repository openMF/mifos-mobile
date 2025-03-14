/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.user.profile.navigation

const val USER_PROFILE_NAVIGATION_ROUTE_BASE = "user_profile_base_route"
const val USER_PROFILE_SCREEN_ROUTE = "user_profile_screen_route"

internal sealed class UserProfileNavigation(val route: String) {
    data object UserProfileBase : UserProfileNavigation(route = USER_PROFILE_NAVIGATION_ROUTE_BASE)
    data object UserProfileScreen : UserProfileNavigation(route = USER_PROFILE_SCREEN_ROUTE)
}
