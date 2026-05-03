/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.platform.context

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

actual abstract class AppContext private constructor() {
    companion object {
        val INSTANCE = object : AppContext() {}
    }
}

actual val LocalContext: ProvidableCompositionLocal<AppContext>
    get() = staticCompositionLocalOf { AppContext.INSTANCE }

actual val AppContext.activity: Any
    @Composable
    get() = AppContext.INSTANCE
