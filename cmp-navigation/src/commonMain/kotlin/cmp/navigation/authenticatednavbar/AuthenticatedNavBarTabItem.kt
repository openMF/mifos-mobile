/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package cmp.navigation.authenticatednavbar

import androidx.compose.ui.graphics.vector.ImageVector
import cmp.navigation.utils.toObjectNavigationRoute
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.ui.navigation.NavigationItem
import org.mifos.mobile.feature.home.navigation.HomeRoute
import org.mifos.mobile.feature.settings.navigation.SettingsNavGraphRoute
import org.mifos.mobile.feature.third.party.transfer.navigation.ThirdPartyTransferNavGraphRoute
import org.mifos.mobile.navigation.generated.resources.Res
import org.mifos.mobile.navigation.generated.resources.home
import org.mifos.mobile.navigation.generated.resources.profile
import org.mifos.mobile.navigation.generated.resources.transfer

sealed class AuthenticatedNavBarTabItem : NavigationItem {

    data object HomeTab : AuthenticatedNavBarTabItem() {
        override val iconResSelected: ImageVector
            get() = MifosIcons.HomeTabFilled
        override val iconRes: ImageVector
            get() = MifosIcons.HomeTabFilled
        override val labelRes: StringResource
            get() = Res.string.home
        override val contentDescriptionRes: StringResource
            get() = Res.string.home
        override val graphRoute: String
            get() = HomeRoute.toObjectNavigationRoute()
        override val startDestinationRoute: String
            get() = HomeRoute.toObjectNavigationRoute()
        override val testTag: String
            get() = "HomeTab"
    }

    // TODO Add Top level destinations here

    data object TransferTab : AuthenticatedNavBarTabItem() {
        override val iconResSelected: ImageVector
            get() = MifosIcons.MoneyHand
        override val iconRes: ImageVector
            get() = MifosIcons.MoneyHand
        override val labelRes: StringResource
            get() = Res.string.transfer
        override val contentDescriptionRes: StringResource
            get() = Res.string.transfer
        override val graphRoute: String
            get() = ThirdPartyTransferNavGraphRoute.toObjectNavigationRoute()
        override val startDestinationRoute: String
            get() = ThirdPartyTransferNavGraphRoute.toObjectNavigationRoute()
        override val testTag: String
            get() = "TransferTab"
    }

    data object ProfileTab : AuthenticatedNavBarTabItem() {
        override val iconResSelected: ImageVector
            get() = MifosIcons.PersonTabFilled
        override val iconRes: ImageVector
            get() = MifosIcons.PersonTabFilled
        override val labelRes: StringResource
            get() = Res.string.profile
        override val contentDescriptionRes: StringResource
            get() = Res.string.profile
        override val graphRoute: String
            get() = SettingsNavGraphRoute.toObjectNavigationRoute()
        override val startDestinationRoute: String
            get() = SettingsNavGraphRoute.toObjectNavigationRoute()
        override val testTag: String
            get() = "ProfileTab"
    }
}
