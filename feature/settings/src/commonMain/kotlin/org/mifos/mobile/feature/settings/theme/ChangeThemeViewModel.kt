/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.theme

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.DarkThemeConfig
import org.mifos.mobile.core.ui.utils.BaseViewModel

internal class ChangeThemeViewModel(
    private val repository: UserPreferencesRepository,
) : BaseViewModel<ThemeState, ThemeEvent, ThemeAction>(
    ThemeState(DarkThemeConfig.FOLLOW_SYSTEM),
) {
    init {
        repository.observeDarkThemeConfig.onEach {
            ThemeAction.Internal.LoadTheme(it)
        }.launchIn(viewModelScope)
    }
    override fun handleAction(action: ThemeAction) {
        when (action) {
            is ThemeAction.SetTheme -> handleSetTheme(action)
            ThemeAction.NavigateBack -> {
                sendEvent(ThemeEvent.OnNavigateBack)
            }
            is ThemeAction.Internal.LoadTheme -> handleLoadTheme(action)
        }
    }
    private fun handleSetTheme(action: ThemeAction.SetTheme) {
        viewModelScope.launch {
            repository.setThemeConfig(action.theme)
            mutableStateFlow.update {
                it.copy(currentTheme = action.theme)
            }
            sendEvent(ThemeEvent.OnNavigateBack)
        }
    }
    private fun handleLoadTheme(action: ThemeAction.Internal.LoadTheme) {
        mutableStateFlow.update {
            it.copy(currentTheme = action.theme)
        }
    }
}
internal data class ThemeState(
    val currentTheme: DarkThemeConfig,
)
internal sealed interface ThemeEvent {
    data object OnNavigateBack : ThemeEvent
}
internal sealed interface ThemeAction {
    data class SetTheme(val theme: DarkThemeConfig) : ThemeAction
    data object NavigateBack : ThemeAction
    sealed interface Internal : ThemeAction {
        data class LoadTheme(val theme: DarkThemeConfig) : Internal
    }
}
