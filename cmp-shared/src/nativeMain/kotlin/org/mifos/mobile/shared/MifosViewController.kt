/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.shared

import androidx.compose.ui.window.ComposeUIViewController
import org.mifos.mobile.shared.di.initKoin

@Suppress("ktlint:standard:function-naming")
fun MifosViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    },
) {
    MifosMobileSharedApp()
}
