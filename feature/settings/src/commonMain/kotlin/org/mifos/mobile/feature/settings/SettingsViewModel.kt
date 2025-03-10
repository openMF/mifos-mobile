/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mifos_mobile.feature.settings.generated.resources.Res
import mifos_mobile.feature.settings.generated.resources.accounts
import mifos_mobile.feature.settings.generated.resources.change_account_password
import mifos_mobile.feature.settings.generated.resources.change_app_passcode
import mifos_mobile.feature.settings.generated.resources.change_app_theme
import mifos_mobile.feature.settings.generated.resources.change_passcode
import mifos_mobile.feature.settings.generated.resources.change_password
import mifos_mobile.feature.settings.generated.resources.choose_language
import mifos_mobile.feature.settings.generated.resources.ic_baseline_dark_mode_24
import mifos_mobile.feature.settings.generated.resources.ic_lock_black_24dp
import mifos_mobile.feature.settings.generated.resources.ic_passcode
import mifos_mobile.feature.settings.generated.resources.ic_translate
import mifos_mobile.feature.settings.generated.resources.ic_update
import mifos_mobile.feature.settings.generated.resources.language
import mifos_mobile.feature.settings.generated.resources.languages
import mifos_mobile.feature.settings.generated.resources.other
import mifos_mobile.feature.settings.generated.resources.pref_base_url_desc
import mifos_mobile.feature.settings.generated.resources.pref_base_url_title
import mifos_mobile.feature.settings.generated.resources.theme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getStringArray
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.datastore.model.AppTheme
import org.mifos.mobile.core.datastore.model.MifosAppLanguage

internal class SettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    var allLanguageList: Array<String>? = null

    init {
        viewModelScope.launch {
            allLanguageList = getStringArray(Res.array.languages).toTypedArray()
        }
    }

    val tenant: StateFlow<String?> = userPreferencesRepository
        .settingsInfo
        .map { it.tenant }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val baseUrl: StateFlow<String?> = userPreferencesRepository
        .settingsInfo
        .map { it.baseUrl }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val passcode: StateFlow<String?> = userPreferencesRepository
        .settingsInfo
        .map { it.passcode }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val theme: StateFlow<AppTheme> = userPreferencesRepository
        .appTheme
        .stateIn(viewModelScope, SharingStarted.Eagerly, AppTheme.SYSTEM)

    val language: StateFlow<MifosAppLanguage> = userPreferencesRepository
        .settingsInfo
        .map { it.language }
        .stateIn(viewModelScope, SharingStarted.Eagerly, MifosAppLanguage.ENGLISH)

    fun tryUpdatingEndpoint(selectedBaseUrl: String, selectedTenant: String): Boolean {
        if (baseUrl.value != selectedBaseUrl || tenant.value != selectedTenant) {
            viewModelScope.launch {
                userPreferencesRepository.updateSettings(
                    userPreferencesRepository.settingsInfo.first().copy(
                        baseUrl = selectedBaseUrl,
                        tenant = selectedTenant,
                    ),
                )
            }
            return true
        }
        return false
    }

    fun updateLanguage(language: MifosAppLanguage): Boolean {
        viewModelScope.launch {
            val updatedSettings = userPreferencesRepository.settingsInfo.first().copy(language = language)
            userPreferencesRepository.updateSettings(updatedSettings)
        }
        return language.code != "system"
    }

    fun updateTheme(theme: AppTheme) {
        viewModelScope.launch {
            userPreferencesRepository.updateTheme(theme)
        }
    }
}

internal enum class SettingsCardItem(
    val title: StringResource,
    val details: StringResource,
    val icon: DrawableResource,
    val subclassOf: StringResource,
    val firstItemInSubclass: Boolean = false,
    val showDividerInBottom: Boolean = false,
) {
    PASSWORD(
        title = Res.string.change_password,
        details = Res.string.change_account_password,
        icon = Res.drawable.ic_lock_black_24dp,
        firstItemInSubclass = true,
        subclassOf = Res.string.accounts,
    ),
    PASSCODE(
        title = Res.string.change_passcode,
        details = Res.string.change_app_passcode,
        icon = Res.drawable.ic_passcode,
        showDividerInBottom = true,
        subclassOf = Res.string.accounts,
    ),
    LANGUAGE(
        title = Res.string.language,
        details = Res.string.choose_language,
        icon = Res.drawable.ic_translate,
        firstItemInSubclass = true,
        subclassOf = Res.string.other,
    ),
    THEME(
        title = Res.string.theme,
        details = Res.string.change_app_theme,
        icon = Res.drawable.ic_baseline_dark_mode_24,
        subclassOf = Res.string.other,
    ),
    ENDPOINT(
        title = Res.string.pref_base_url_title,
        details = Res.string.pref_base_url_desc,
        icon = Res.drawable.ic_update,
        subclassOf = Res.string.other,
    ),
}
