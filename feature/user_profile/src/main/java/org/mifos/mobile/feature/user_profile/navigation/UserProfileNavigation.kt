package org.mifos.mobile.feature.user_profile.navigation

const val USER_PROFILE_NAVIGATION_ROUTE_BASE = "user_profile_base_route"
const val USER_PROFILE_SCREEN_ROUTE = "user_profile_screen_route"

sealed class UserProfileNavigation(val route: String) {
    data object UserProfileBase : UserProfileNavigation(route = USER_PROFILE_NAVIGATION_ROUTE_BASE)
    data object UserProfileScreen : UserProfileNavigation(route = USER_PROFILE_SCREEN_ROUTE)
}

