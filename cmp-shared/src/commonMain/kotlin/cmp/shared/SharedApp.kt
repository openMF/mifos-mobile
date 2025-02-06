/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package cmp.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cmp.navigation.ComposeApp

@Composable
fun SharedApp(
    modifier: Modifier = Modifier,
) {
    ComposeApp(modifier = modifier)
}
