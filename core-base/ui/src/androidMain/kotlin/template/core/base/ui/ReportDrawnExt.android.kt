/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package template.core.base.ui

import androidx.compose.runtime.Composable

@Composable
actual fun ReportDrawnWhen(block: () -> Boolean) {
    androidx.activity.compose.ReportDrawnWhen { block() }
}
