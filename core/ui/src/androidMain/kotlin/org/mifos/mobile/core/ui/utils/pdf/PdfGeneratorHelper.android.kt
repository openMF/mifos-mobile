/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.utils.pdf

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Android-specific helper to create PdfGenerator with Context.
 */
@Composable
actual fun rememberPdfGenerator(): PdfGenerator {
    val context = LocalContext.current
    return remember(context) {
        PdfGenerator().apply {
            setContext(context)
        }
    }
}
