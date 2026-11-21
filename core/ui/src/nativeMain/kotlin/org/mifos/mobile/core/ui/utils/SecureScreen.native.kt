/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.utils

import androidx.compose.runtime.Composable

@Composable
actual fun SecureScreen() {
    // iOS and other native platforms can implement screenshot protection if needed.
    // For now, this is a no-op as requested for Android FLAG_SECURE.
}
