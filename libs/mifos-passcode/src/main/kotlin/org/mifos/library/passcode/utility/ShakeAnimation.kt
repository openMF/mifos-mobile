/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.library.passcode.utility

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.keyframes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal object ShakeAnimation {

    fun CoroutineScope.performShakeAnimation(xShake: Animatable<Float, *>) {
        launch {
            xShake.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 280
                    0f at 0 using LinearOutSlowInEasing
                    20f at 80 using LinearOutSlowInEasing
                    20f at 120 using LinearOutSlowInEasing
                    10f at 160 using LinearOutSlowInEasing
                    10f at 200 using LinearOutSlowInEasing
                    5f at 240 using LinearOutSlowInEasing
                    0f at 280
                },
            )
        }
    }
}
