/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package cmp.navigation.rootnav

import androidx.lifecycle.viewModelScope
import cmp.navigation.rootnav.RootNavAction.Internal.UserStateUpdateReceive
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.mifos.mobile.core.data.repository.UserDataRepository
import org.mifos.mobile.core.datastore.model.AppSettings
import org.mifos.mobile.core.model.AuthState
import org.mifos.mobile.core.ui.utils.BaseViewModel

class RootNavViewModel(
    userDataRepository: UserDataRepository,
) : BaseViewModel<RootNavState, Unit, RootNavAction>(
    initialState = RootNavState.Splash,
) {

    init {
        combine(
            userDataRepository.authState,
            userDataRepository.settingsState,
        ) { authState, settingsData ->
            UserStateUpdateReceive(
                authState = authState,
                settingsData = settingsData,
            )
        }.onEach(::handleAction)
            .launchIn(viewModelScope)
    }

    override fun handleAction(action: RootNavAction) {
        when (action) {
            is UserStateUpdateReceive -> handleUserStateUpdateReceive(action)
        }
    }

    private fun handleUserStateUpdateReceive(
        action: UserStateUpdateReceive,
    ) {
        val settingsData = action.settingsData
        println("User data in view model $settingsData")
        val updatedRootNavState = when {
            settingsData.firstTimeState -> RootNavState.SetLanguage

            !settingsData.isAuthenticated -> RootNavState.Auth

//            settingsData.passcode.isEmpty() -> RootNavState.UserLocked
//          TODO: Passcode library needs to update so that it updates passcode in datastore
            settingsData.passcode.isEmpty() -> {
                RootNavState.UserUnlocked(settingsData.userId)
            }

            settingsData.isUnlocked -> {
                RootNavState.UserUnlocked(settingsData.userId)
            }

            else -> RootNavState.UserLocked
        }
        mutableStateFlow.update { updatedRootNavState }
    }
}

sealed class RootNavState {
    data object Auth : RootNavState()

    data object SetLanguage : RootNavState()

    data object Splash : RootNavState()

    data object UserLocked : RootNavState()

    data class UserUnlocked(
        val activeUserId: String,
    ) : RootNavState()
}

sealed class RootNavAction {

    sealed class Internal {

        data class UserStateUpdateReceive(
            val authState: AuthState,
            val settingsData: AppSettings,
        ) : RootNavAction()
    }
}
