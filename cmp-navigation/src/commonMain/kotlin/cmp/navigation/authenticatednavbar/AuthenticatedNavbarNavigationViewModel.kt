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

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.model.AppSettings
import org.mifos.mobile.core.ui.utils.BaseViewModel

internal class AuthenticatedNavbarNavigationViewModel(
    networkMonitor: NetworkMonitor,
) : BaseViewModel<Unit, AuthenticatedNavBarEvent, AuthenticatedNavBarAction>(
    initialState = Unit,
) {

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    override fun handleAction(action: AuthenticatedNavBarAction) {
        when (action) {
            AuthenticatedNavBarAction.HomeTabClick -> handleVaultTabClicked()

            is AuthenticatedNavBarAction.Internal -> handleInternalAction(action)

            AuthenticatedNavBarAction.ProfileTabClick -> handleProfileTabClicked()

            AuthenticatedNavBarAction.TransferTabClick -> handleTransferTabClicked()
        }
    }

    private fun handleVaultTabClicked() {
        sendEvent(AuthenticatedNavBarEvent.NavigateToHomeScreen)
    }

    private fun handleTransferTabClicked() {
        sendEvent(AuthenticatedNavBarEvent.NavigateToThirdPartyTransferScreen)
    }

    private fun handleProfileTabClicked() {
        sendEvent(AuthenticatedNavBarEvent.NavigateToUserProfileScreen)
    }

    private fun handleInternalAction(action: AuthenticatedNavBarAction.Internal) {
        when (action) {
            is AuthenticatedNavBarAction.Internal.UserStateUpdateReceive -> {
            }
        }
    }
}

internal sealed class AuthenticatedNavBarAction {

    // TODO: Add top level destinations here

    data object HomeTabClick : AuthenticatedNavBarAction()

    data object ProfileTabClick : AuthenticatedNavBarAction()

    data object TransferTabClick : AuthenticatedNavBarAction()

    sealed class Internal : AuthenticatedNavBarAction() {
        data class UserStateUpdateReceive(val appSettings: AppSettings?) : Internal()
    }
}

internal sealed class AuthenticatedNavBarEvent {

    abstract val tab: AuthenticatedNavBarTabItem

    data object NavigateToHomeScreen : AuthenticatedNavBarEvent() {
        override val tab: AuthenticatedNavBarTabItem = AuthenticatedNavBarTabItem.HomeTab
    }

    data object NavigateToThirdPartyTransferScreen : AuthenticatedNavBarEvent() {
        override val tab: AuthenticatedNavBarTabItem = AuthenticatedNavBarTabItem.TransferTab
    }

    data object NavigateToUserProfileScreen : AuthenticatedNavBarEvent() {
        override val tab: AuthenticatedNavBarTabItem = AuthenticatedNavBarTabItem.ProfileTab
    }
}
