/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.mifos.mobile.feature.settings.faq.FaqViewModel
import org.mifos.mobile.feature.settings.language.LanguageViewModel
import org.mifos.mobile.feature.settings.passcode.UpdatePasscodeViewModel
import org.mifos.mobile.feature.settings.password.ChangePasswordViewModel
import org.mifos.mobile.feature.settings.settings.SettingsViewModel
import org.mifos.mobile.feature.settings.theme.ChangeThemeViewModel

/**
 * Koin module for providing dependencies related to the settings feature.
 * This module declares all the ViewModels used across the settings screens,
 * making them available for dependency injection.
 */
val SettingsModule = module {
    viewModelOf(::SettingsViewModel)
    viewModelOf(::UpdatePasscodeViewModel)
    viewModelOf(::ChangePasswordViewModel)
    viewModelOf(::UpdatePasscodeViewModel)
    viewModelOf(::FaqViewModel)
    viewModelOf(::LanguageViewModel)
    viewModelOf(::ChangeThemeViewModel)
}
