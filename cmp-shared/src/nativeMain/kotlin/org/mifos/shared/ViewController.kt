/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.shared

import androidx.compose.ui.window.ComposeUIViewController
import cmp.shared.SharedApp
import cmp.shared.utils.initKoin

fun viewController() = ComposeUIViewController(
    configure = {
        initKoin()
    },
) {
    SharedApp()
}
