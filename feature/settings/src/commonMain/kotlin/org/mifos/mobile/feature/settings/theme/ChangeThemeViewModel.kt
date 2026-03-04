/*
 * Copyright 2026 Mifos Initiative
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
import mifos_mobile.feature.settings.generated.resources.Res
import mifos_mobile.feature.settings.generated.resources.feature_settings_theme_based_on_time
import mifos_mobile.feature.settings.generated.resources.feature_settings_theme_dark
import mifos_mobile.feature.settings.generated.resources.feature_settings_theme_light
import mifos_mobile.feature.settings.generated.resources.feature_settings_theme_system
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.datastore.model.TimeBasedTheme
import org.mifos.mobile.core.model.MifosThemeConfig
import org.mifos.mobile.core.ui.utils.BaseViewModel

/**
 * ViewModel for managing the theme selection in the application.
 *
 * This ViewModel handles user interactions related to theme settings, such as selecting a new theme
 * and saving the preference. It observes changes to the user's theme preference from a
 * [UserPreferencesRepository] and updates the UI state accordingly.
 *
 * @param repository The repository for accessing and updating user theme preferences.
 */
internal class ChangeThemeViewModel(
    private val repository: UserPreferencesRepository,
) : BaseViewModel<ThemeState, ThemeEvent, ThemeAction>(
    ThemeState(MifosThemeConfig.FOLLOW_SYSTEM),
) {

    init {
        // Observe the user's dark theme configuration from the repository.
        repository.observeDarkThemeConfig
            .onEach { theme ->
                trySendAction(ThemeAction.Internal.LoadTheme(theme))
            }
            .launchIn(viewModelScope)

        repository.observeTimeBasedThemeConfig
            .onEach {
                trySendAction(ThemeAction.Internal.LoadTimeBasedTheme(it))
            }
            .launchIn(viewModelScope)
    }

    /**
     * Handles incoming actions from the UI.
     *
     * @param action The [ThemeAction] to be processed.
     */
    override fun handleAction(action: ThemeAction) {
        when (action) {
            is ThemeAction.SetTheme -> handleSetTheme()
            is ThemeAction.ThemeSelection -> handleThemeSelection(action.theme)
            ThemeAction.NavigateBack -> {
                sendEvent(ThemeEvent.OnNavigateBack)
            }
            is ThemeAction.Internal.LoadTheme -> handleLoadTheme(action)
            ThemeAction.HideTimeBasedDialog -> {
                mutableStateFlow.update {
                    it.copy(
                        showTimeBasedDialog = false,
                    )
                }
            }
            ThemeAction.ShowTimeBasedDialog -> {
                mutableStateFlow.update {
                    it.copy(
                        showTimeBasedDialog = true,
                    )
                }
            }
            is ThemeAction.UpdateTimeBasedTheme -> {
                viewModelScope.launch {
                    repository.updateTimeBasedTheme(action.theme)
                    repository.updateTheme(MifosThemeConfig.BASED_ON_TIME)

                    mutableStateFlow.update {
                        it.copy(
                            currentTheme = MifosThemeConfig.BASED_ON_TIME,
                            timeBasedTheme = action.theme,
                            showTimeBasedDialog = false,
                        )
                    }
                }
            }

            is ThemeAction.Internal.LoadTimeBasedTheme -> {
                handleLoadTimeBasedTheme(action)
            }
        }
    }

    /**
     * Updates the current theme selection in the UI state.
     *
     * This method does not persist the theme to the repository; it only updates the ViewModel's state.
     * The theme is persisted when [handleSetTheme] is called.
     *
     * @param theme The [MifosThemeConfig] selected by the user.
     */
    private fun handleThemeSelection(theme: MifosThemeConfig) {
        if (theme == MifosThemeConfig.BASED_ON_TIME) {
            mutableStateFlow.update {
                it.copy(
                    showTimeBasedDialog = true,
                    currentTheme = theme,
                )
            }
        } else {
            mutableStateFlow.update {
                it.copy(currentTheme = theme)
            }
        }
    }

    /**
     * Persists the currently selected theme to the [UserPreferencesRepository].
     *
     * This method launches a coroutine to save the theme preference and updates the UI state
     * to reflect the new theme.
     */
    private fun handleSetTheme() {
        viewModelScope.launch {
            repository.updateTheme(state.currentTheme)
            mutableStateFlow.update {
                it.copy(currentTheme = state.currentTheme)
            }

            sendEvent(ThemeEvent.OnNavigateBack)
        }
    }

    /**
     * Handles the loading of a theme from the repository.
     *
     * This method is triggered by the [repository.observeDarkThemeConfig] flow. It updates the
     * ViewModel's state with the loaded theme.
     *
     * @param action The [ThemeAction.Internal.LoadTheme] containing the theme from the repository.
     */
    private fun handleLoadTheme(action: ThemeAction.Internal.LoadTheme) {
        mutableStateFlow.update {
            it.copy(currentTheme = action.theme)
        }
    }

    private fun handleLoadTimeBasedTheme(action: ThemeAction.Internal.LoadTimeBasedTheme) {
        mutableStateFlow.update {
            it.copy(timeBasedTheme = action.theme)
        }
    }
}

/**
 * Represents the UI state for the theme selection screen.
 *
 * @property currentTheme The currently selected theme configuration.
 */
internal data class ThemeState(
    val currentTheme: MifosThemeConfig,
    val showTimeBasedDialog: Boolean = false,
    val timeBasedTheme: TimeBasedTheme = TimeBasedTheme(
        hourStart = 6,
        hourEnd = 18,
        timeStart = 0,
        timeEnd = 0,
    ),
) {
    /**
     * A list of all available theme options and their corresponding string resource IDs.
     */
    val themeOptions
        get() = listOf(
            MifosThemeConfig.FOLLOW_SYSTEM to Res.string.feature_settings_theme_system,
            MifosThemeConfig.DARK to Res.string.feature_settings_theme_dark,
            MifosThemeConfig.LIGHT to Res.string.feature_settings_theme_light,
            MifosThemeConfig.BASED_ON_TIME to Res.string.feature_settings_theme_based_on_time,
        )
}

/**
 * Represents events that can be sent from the ViewModel to the UI.
 */
internal sealed interface ThemeEvent {
    /**
     * An event indicating that the UI should navigate back.
     */
    data object OnNavigateBack : ThemeEvent
}

/**
 * Represents actions that can be sent from the UI to the ViewModel.
 */
internal sealed interface ThemeAction {
    /**
     * An action to set and save the currently selected theme.
     */
    data object SetTheme : ThemeAction

    /**
     * An action to update the theme selection in the UI state.
     *
     * @property theme The [MifosThemeConfig] selected by the user.
     */
    data class ThemeSelection(val theme: MifosThemeConfig) : ThemeAction

    /**
     * An action to trigger navigation back from the theme settings screen.
     */
    data object NavigateBack : ThemeAction

    data object ShowTimeBasedDialog : ThemeAction

    data object HideTimeBasedDialog : ThemeAction

    data class UpdateTimeBasedTheme(val theme: TimeBasedTheme) : ThemeAction

    /**
     * Actions that are internal to the ViewModel and should not be sent from the UI.
     */
    sealed interface Internal : ThemeAction {
        /**
         * An internal action to load a theme into the ViewModel's state.
         *
         * @property theme The [MifosThemeConfig] loaded from the repository.
         */
        data class LoadTheme(val theme: MifosThemeConfig) : Internal

        data class LoadTimeBasedTheme(val theme: TimeBasedTheme) : Internal
    }
}
