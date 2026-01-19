/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.ui

import androidx.compose.runtime.Composable

/**
 * Reports to the composition system that content is considered drawn when the specified condition is true.
 * Platform-specific implementation that affects rendering optimizations.
 *
 * @param block Lambda that returns true when content should be considered drawn
 */
@Composable
expect fun ReportDrawnWhen(block: () -> Boolean)
