/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.status

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.feature.status.navigation.StatusNavigationRoute

internal class StatusViewModel(
    savedStateHandle: SavedStateHandle,
    private val userPreferencesRepository: UserPreferencesRepository,
) : BaseViewModel<StatusState, StatusEvent, StatusAction>(
    initialState = StatusState(),
) {
    init {
        val nextRoute = savedStateHandle.toRoute<StatusNavigationRoute>()

        mutableStateFlow.update {
            it.copy(
                eventType = nextRoute.eventType,
                eventDestination = nextRoute.eventDestination,
                title = nextRoute.title,
                subtitle = nextRoute.subtitle,
                buttonText = nextRoute.buttonText,
            )
        }
    }

    override fun handleAction(action: StatusAction) {
        when (action) {
            is StatusAction.OnNextClick -> sendEvent(
                StatusEvent.NavigateNext(
                    state.eventDestination ?: "",
                ),
            )

            StatusAction.UnlockApp -> handleUnlockApp()
        }
    }

    private fun handleUnlockApp() {
        viewModelScope.launch {
            userPreferencesRepository.setIsUnlocked(true)
        }
    }
}

internal data class StatusState(

    val eventType: String? = "",
    val eventDestination: String? = "",
    val title: String? = "",
    val subtitle: String? = "",
    val buttonText: String? = "",
)

internal sealed interface StatusAction {

    data object OnNextClick : StatusAction

    data object UnlockApp : StatusAction
}

internal sealed interface StatusEvent {
    data class NavigateNext(val nextScreen: String) : StatusEvent
}
