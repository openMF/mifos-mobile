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