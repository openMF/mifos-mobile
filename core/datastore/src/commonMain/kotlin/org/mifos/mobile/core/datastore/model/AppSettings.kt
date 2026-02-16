/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.datastore.model

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.LanguageConfig
import org.mifos.mobile.core.model.MifosThemeConfig

@Serializable
data class AppSettings(
    val userId: String,
    val tenant: String,
    val baseUrl: String,
    val passcode: String,
    val appTheme: MifosThemeConfig,
    val sentTokenToServer: Boolean = false,
    val gcmToken: String? = null,
    val useDynamicColor: Boolean,
    val isAuthenticated: Boolean,
    val isUnlocked: Boolean,
    val language: LanguageConfig,
    val showOnboarding: Boolean,
    val firstTimeState: Boolean,
    val timeBasedTheme: TimeBasedTheme,
    val selectedServices: Set<String> = emptySet(),
) {
    companion object {
        val DEFAULT = AppSettings(
            userId = "",
            tenant = "default",
            baseUrl = "https://tt.mifos.community/",
            appTheme = MifosThemeConfig.FOLLOW_SYSTEM,
            sentTokenToServer = false,
            gcmToken = null,
            language = LanguageConfig.DEFAULT,
            showOnboarding = true,
            firstTimeState = true,
            useDynamicColor = false,
            isAuthenticated = false,
            passcode = "",
            isUnlocked = false,
            timeBasedTheme = TimeBasedTheme(hourStart = 6, hourEnd = 0, timeStart = 17, timeEnd = 59),
        )
    }
}
