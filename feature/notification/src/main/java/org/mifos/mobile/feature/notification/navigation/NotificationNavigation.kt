package org.mifos.mobile.feature.notification.navigation

const val NOTIFICATION_NAVIGATION_ROUTE_BASE = "notification_base_route"
const val NOTIFICATION_SCREEN_ROUTE = "notification_screen_route"

sealed class NotificationNavigation(val route: String) {
    data object NotificationBase : NotificationNavigation(route = NOTIFICATION_NAVIGATION_ROUTE_BASE)
    data object NotificationScreen : NotificationNavigation(route = NOTIFICATION_SCREEN_ROUTE)
}
