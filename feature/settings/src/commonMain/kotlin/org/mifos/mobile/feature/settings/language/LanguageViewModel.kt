/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.language

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.LanguageConfig
import org.mifos.mobile.core.ui.utils.BaseViewModel

/**
 * `ViewModel` for the Language Selection screen.
 *
 * This ViewModel handles the logic for managing the app's language settings. It
 * observes the current language from the user preferences, allows the user to
 * select a new language, and saves the new setting. It follows a
 * Unidirectional Data Flow (UDF) pattern, using a [BaseViewModel] to manage its state
 * ([LanguageState]), handle actions ([LanguageAction]), and emit events
 * ([LanguageEvent]).
 *
 * @param userPreferencesRepositoryImpl Repository for accessing and modifying user preferences,
 * specifically the language setting.
 */
internal class LanguageViewModel(
    private val userPreferencesRepositoryImpl: UserPreferencesRepository,
) : BaseViewModel<LanguageState, LanguageEvent, LanguageAction>(
    LanguageState(
        currentLanguage = LanguageConfig.DEFAULT,
        selectedLanguage = LanguageConfig.DEFAULT,
    ),
) {
    init {
        // Observe changes to the language setting from the repository and
        // update the ViewModel's state accordingly.
        userPreferencesRepositoryImpl.observeLanguage.map {
            LanguageAction.Internal.LoadLanguage(it)
        }.onEach(::trySendAction)
            .launchIn(viewModelScope)
    }

    /**
     * Handles incoming actions from the UI and dispatches them to the appropriate
     * business logic functions.
     *
     * @param action The [LanguageAction] to be handled.
     */
    override fun handleAction(action: LanguageAction) {
        when (action) {
            is LanguageAction.OnNavigateBack -> sendEvent(LanguageEvent.NavigateBack)
            is LanguageAction.LanguageSelected -> handleLanguageSelection(action.language)
            is LanguageAction.Internal.LoadLanguage -> handleLoadLanguage(action)
            is LanguageAction.SetLanguage -> handleSetLanguage(action)
        }
    }

    /**
     * Updates the selected language in the state when the user makes a selection.
     *
     * @param language The [LanguageConfig] selected by the user.
     */
    private fun handleLanguageSelection(language: LanguageConfig) {
        viewModelScope.launch {
            mutableStateFlow.update {
                it.copy(
                    selectedLanguage = language,
                )
            }
        }
    }

    /**
     * Saves the selected language to the user preferences repository and
     * then navigates back.
     *
     * @param action The [LanguageAction.SetLanguage] containing the language to be saved.
     */
    private fun handleSetLanguage(action: LanguageAction.SetLanguage) {
        viewModelScope.launch {
            userPreferencesRepositoryImpl.setLanguage(action.languageConfig)
            sendEvent(LanguageEvent.NavigateBack)
        }
    }

    /**
     * Handles the initial loading of the current language from the repository.
     *
     * @param action The [LanguageAction.Internal.LoadLanguage] containing the language from preferences.
     */
    private fun handleLoadLanguage(action: LanguageAction.Internal.LoadLanguage) {
        mutableStateFlow.update {
            it.copy(
                currentLanguage = action.language,
                selectedLanguage = action.language,
            )
        }
    }
}

/**
 * Represents the UI state for the Language Selection screen.
 *
 * @property selectedLanguage The language currently selected by the user, but not yet saved.
 * @property currentLanguage The language currently saved and active in the user preferences.
 */
internal data class LanguageState(
    val selectedLanguage: LanguageConfig,
    val currentLanguage: LanguageConfig,
)

/**
 * A sealed interface representing one-time events that trigger UI side effects.
 */
internal sealed interface LanguageEvent {
    /** Event to navigate back from the screen. */
    data object NavigateBack : LanguageEvent
}

/**
 * A sealed interface representing user actions or internal events that the
 * ViewModel needs to handle for the Language Selection screen.
 */
internal sealed interface LanguageAction {
    /** User action to navigate back from the screen. */
    data object OnNavigateBack : LanguageAction

    /**
     * User action when a language item is selected from the list.
     * @property language The [LanguageConfig] that was selected.
     */
    data class LanguageSelected(val language: LanguageConfig) : LanguageAction

    /**
     * User action to set and save the currently selected language.
     * @property languageConfig The [LanguageConfig] to be set.
     */
    data class SetLanguage(val languageConfig: LanguageConfig) : LanguageAction

    /**
     * A sealed interface for internal actions, which are not triggered directly by the UI.
     */
    sealed interface Internal : LanguageAction {
        /**
         * An internal action to handle the initial loading of the language setting.
         * @property language The [LanguageConfig] loaded from user preferences.
         */
        data class LoadLanguage(val language: LanguageConfig) : Internal
    }
}
