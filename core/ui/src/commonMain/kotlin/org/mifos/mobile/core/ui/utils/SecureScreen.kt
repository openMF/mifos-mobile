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

/**
 * A composable that enables screenshot protection for the current screen.
 * On Android, this adds WindowManager.LayoutParams.FLAG_SECURE to the window.
 */
@Composable
expect fun SecureScreen()
