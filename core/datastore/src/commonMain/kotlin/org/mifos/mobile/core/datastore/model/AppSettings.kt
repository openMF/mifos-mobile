/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val tenant: String,
    val baseUrl: String,
    val passcode: String? = null,
    val appTheme: AppTheme = AppTheme.SYSTEM,
    val language: MifosAppLanguage,
) {
    companion object {
        val DEFAULT = AppSettings(
            tenant = "default",
            baseUrl = "https://tt.mifos.community/",
            appTheme = AppTheme.SYSTEM,
            language = MifosAppLanguage.SYSTEM_LANGUAGE,
        )
    }
}
