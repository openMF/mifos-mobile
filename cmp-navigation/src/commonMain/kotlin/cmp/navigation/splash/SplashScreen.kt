/*
 * Copyright 2025 Mobile Byte Sensei
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/mobilebytesensei/financiera-bienestar/blob/dev/LICENSE
 */
package cmp.navigation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
) {
    Surface(
        color = Color.White,
    ) {
        Box(modifier = modifier.fillMaxSize())
    }
}
