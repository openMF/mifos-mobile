/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.onboarding.language

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
 * ViewModel responsible for managing the onboarding language selection screen state and business logic.
 *
 * This ViewModel handles:
 * - Loading the current language preference
 * - Updating the selected language
 * - Managing the onboarding flow state
 *
 * @property repository [UserPreferencesRepository] for accessing and modifying user preferences
 */
internal class SetOnboardingLanguageViewModel(
    private val repository: UserPreferencesRepository,
) : BaseViewModel<OnboardingLanguageState, OnboardingLanguageEvent, OnboardingLanguageAction>(
    OnboardingLanguageState(LanguageConfig.DEFAULT),
) {
    init {
        repository.observeLanguage.map {
            OnboardingLanguageAction.Internal.LoadLanguage(it)
        }.onEach(::trySendAction)
            .launchIn(viewModelScope)
    }

    /**
     * Processes incoming actions and delegates to appropriate handlers.
     * @param action The action to process
     */
    override fun handleAction(action: OnboardingLanguageAction) {
        when (action) {
            is OnboardingLanguageAction.Internal.LoadLanguage -> handleLoadLanguage(action)
            is OnboardingLanguageAction.SetLanguage -> handleSetLanguage(action)
        }
    }

    /**
     * Updates the selected language and updates onboarding state.
     * 
     * This method will:
     * 1. Persist the new language preference
     * 2. Update the UI state
     * 3. Mark onboarding as complete
     *
     * @param action Contains the new language configuration
     * 
     * Example:
     * ```kotlin
     * // When user selects a language
     * viewModel.trySendAction(
     *     OnboardingLanguageAction.SetLanguage(selectedLanguage)
     * )
     * ```
     */
    private fun handleSetLanguage(action: OnboardingLanguageAction.SetLanguage) {
        viewModelScope.launch {
            repository.setLanguage(action.languageConfig)
            mutableStateFlow.update {
                it.copy(currentLanguage = action.languageConfig)
            }
            repository.setShowOnboarding(false)
            repository.setFirstTimeState(false)
        }
    }

    /**
     * Updates the current language in the state.
     * 
     * This is called internally when the language preference changes.
     *
     * @param action Contains the language to load
     * 
     * Example:
     * ```kotlin
     * // Internal usage - triggered by language preference changes
     * private fun onLanguagePreferenceChanged(newLanguage: LanguageConfig) {
     *     trySendAction(
     *         OnboardingLanguageAction.Internal.LoadLanguage(newLanguage)
     *     )
     * }
     * ```
     */
    private fun handleLoadLanguage(action: OnboardingLanguageAction.Internal.LoadLanguage) {
        mutableStateFlow.update {
            it.copy(currentLanguage = action.language)
        }
    }
}

/**
 * Represents the UI state for the language selection screen.
 * @property currentLanguage The currently selected language
 */
internal data class OnboardingLanguageState(
    val currentLanguage: LanguageConfig,
)

/**
 * Events that can be triggered from the UI.
 * Currently not used but available for future extensions.
 */
internal sealed interface OnboardingLanguageEvent

/**
 * Actions that can be processed by the ViewModel.
 */
internal sealed interface OnboardingLanguageAction {
    /**
     * Action to set a new language.
     * @property languageConfig The language configuration to set
     */
    data class SetLanguage(val languageConfig: LanguageConfig) : OnboardingLanguageAction

    /**
     * Internal actions used by the ViewModel for state management.
     */
    sealed interface Internal : OnboardingLanguageAction {
        /**
         * Action to load a language configuration.
         * @property language The language configuration to load
         */
        data class LoadLanguage(val language: LanguageConfig) : Internal
    }
}
