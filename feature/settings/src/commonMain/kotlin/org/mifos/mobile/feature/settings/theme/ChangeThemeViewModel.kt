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

/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.settings.generated.resources.Res
import mifos_mobile.feature.settings.generated.resources.feature_settings_theme_dark
import mifos_mobile.feature.settings.generated.resources.feature_settings_theme_light
import mifos_mobile.feature.settings.generated.resources.feature_settings_theme_system
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.MifosThemeConfig
import org.mifos.mobile.core.ui.utils.BaseViewModel

internal class ChangeThemeViewModel(
    private val repository: UserPreferencesRepository,
) : BaseViewModel<ThemeState, ThemeEvent, ThemeAction>(
    ThemeState(MifosThemeConfig.FOLLOW_SYSTEM),
) {
    init {
        repository.observeDarkThemeConfig
            .onEach { theme ->
                trySendAction(ThemeAction.Internal.LoadTheme(theme))
            }
            .launchIn(viewModelScope)
    }

    override fun handleAction(action: ThemeAction) {
        when (action) {
            is ThemeAction.SetTheme -> handleSetTheme()

            is ThemeAction.ThemeSelection -> handleThemeSelection(action.theme)

            ThemeAction.NavigateBack -> {
                sendEvent(ThemeEvent.OnNavigateBack)
            }
            is ThemeAction.Internal.LoadTheme -> handleLoadTheme(action)
        }
    }

    private fun handleThemeSelection(theme: MifosThemeConfig) {
        mutableStateFlow.update {
            it.copy(
                currentTheme = theme,
            )
        }
    }

    private fun handleSetTheme() {
        viewModelScope.launch {
            println("in viewModel ${state.currentTheme}")
            repository.updateTheme(state.currentTheme)
            mutableStateFlow.update {
                it.copy(currentTheme = state.currentTheme)
            }
//            sendEvent(ThemeEvent.OnNavigateBack)
        }
    }

    private fun handleLoadTheme(action: ThemeAction.Internal.LoadTheme) {
        mutableStateFlow.update {
            it.copy(currentTheme = action.theme)
        }
    }
}

internal data class ThemeState(
    val currentTheme: MifosThemeConfig,
) {
    val themeOptions
        get() = listOf(
            MifosThemeConfig.FOLLOW_SYSTEM to Res.string.feature_settings_theme_system,
            MifosThemeConfig.DARK to Res.string.feature_settings_theme_dark,
            MifosThemeConfig.LIGHT to Res.string.feature_settings_theme_light,
        )
}

internal sealed interface ThemeEvent {
    data object OnNavigateBack : ThemeEvent
}

internal sealed interface ThemeAction {
    data object SetTheme : ThemeAction

    data class ThemeSelection(val theme: MifosThemeConfig) : ThemeAction
    data object NavigateBack : ThemeAction
    sealed interface Internal : ThemeAction {
        data class LoadTheme(val theme: MifosThemeConfig) : Internal
    }
}
