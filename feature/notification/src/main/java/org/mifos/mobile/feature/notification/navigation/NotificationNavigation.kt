/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.notification.navigation

const val NOTIFICATION_NAVIGATION_ROUTE_BASE = "notification_base_route"
const val NOTIFICATION_SCREEN_ROUTE = "notification_screen_route"

sealed class NotificationNavigation(val route: String) {
    data object NotificationBase : NotificationNavigation(route = NOTIFICATION_NAVIGATION_ROUTE_BASE)
    data object NotificationScreen : NotificationNavigation(route = NOTIFICATION_SCREEN_ROUTE)
}
