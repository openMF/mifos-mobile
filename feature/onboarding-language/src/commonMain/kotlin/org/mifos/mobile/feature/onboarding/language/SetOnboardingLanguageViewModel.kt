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

    override fun handleAction(action: OnboardingLanguageAction) {
        when (action) {
            is OnboardingLanguageAction.Internal.LoadLanguage -> handleLoadLanguage(action)
            is OnboardingLanguageAction.SetLanguage -> handleSetLanguage(action)
        }
    }

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

    private fun handleLoadLanguage(action: OnboardingLanguageAction.Internal.LoadLanguage) {
        mutableStateFlow.update {
            it.copy(currentLanguage = action.language)
        }
    }
}

internal data class OnboardingLanguageState(
    val currentLanguage: LanguageConfig,
)

internal sealed interface OnboardingLanguageEvent

internal sealed interface OnboardingLanguageAction {
    data class SetLanguage(val languageConfig: LanguageConfig) : OnboardingLanguageAction

    sealed interface Internal : OnboardingLanguageAction {
        data class LoadLanguage(val language: LanguageConfig) : Internal
    }
}
