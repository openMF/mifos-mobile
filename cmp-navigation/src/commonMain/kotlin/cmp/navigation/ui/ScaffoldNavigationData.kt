
package cmp.navigation.ui

import kotlinx.collections.immutable.ImmutableList
import org.mifos.mobile.core.ui.navigation.NavigationItem

data class ScaffoldNavigationData(
    val onNavigationClick: (NavigationItem) -> Unit,
    val navigationItems: ImmutableList<NavigationItem>,
    val selectedNavigationItem: NavigationItem?,
    val shouldShowNavigation: Boolean,
)
