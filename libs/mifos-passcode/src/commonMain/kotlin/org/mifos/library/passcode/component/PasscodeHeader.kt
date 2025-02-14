/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.library.passcode.component

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.rememberTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mifos_mobile.libs.mifos_passcode.generated.resources.Res
import mifos_mobile.libs.mifos_passcode.generated.resources.library_mifos_passcode_confirm_passcode
import mifos_mobile.libs.mifos_passcode.generated.resources.library_mifos_passcode_create_passcode
import mifos_mobile.libs.mifos_passcode.generated.resources.library_mifos_passcode_enter_your_passcode
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.library.passcode.utility.Step

@Composable
internal fun PasscodeHeader(
    activeStep: Step,
    isPasscodeAlreadySet: Boolean,
    modifier: Modifier = Modifier,
) {
    val transitionState = remember { MutableTransitionState(activeStep) }
    transitionState.targetState = activeStep

    val transition: Transition<Step> = rememberTransition(
        transitionState = transitionState,
        label = "Headers Transition",
    )

    val offset = 200.0F
    val zeroOffset = Offset(x = 0.0F, y = 0.0F)
    val negativeOffset = Offset(x = -offset, y = 0.0F)
    val positiveOffset = Offset(x = offset, y = 0.0F)

    val xTransitionHeader1 by transition.animateOffset(label = "Transition Offset Header 1") {
        if (it == Step.Create) zeroOffset else negativeOffset
    }
    val xTransitionHeader2 by transition.animateOffset(label = "Transition Offset Header 2") {
        if (it == Step.Confirm) zeroOffset else positiveOffset
    }
    val alphaHeader1 by transition.animateFloat(label = "Transition Alpha Header 1") {
        if (it == Step.Create) 1.0F else 0.0F
    }
    val alphaHeader2 by transition.animateFloat(label = "Transition Alpha Header 2") {
        if (it == Step.Confirm) 1.0F else 0.0F
    }
    val scaleHeader1 by transition.animateFloat(label = "Transition Alpha Header 1") {
        if (it == Step.Create) 1.0F else 0.5F
    }
    val scaleHeader2 by transition.animateFloat(label = "Transition Alpha Header 2") {
        if (it == Step.Confirm) 1.0F else 0.5F
    }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            if (isPasscodeAlreadySet) {
                Text(
                    modifier = Modifier
                        .offset(x = xTransitionHeader1.x.dp)
                        .alpha(alpha = alphaHeader1)
                        .scale(scale = scaleHeader1),
                    text = stringResource(resource = Res.string.library_mifos_passcode_enter_your_passcode),
                    style = TextStyle(fontSize = 20.sp),
                )
            } else {
                if (activeStep == Step.Create) {
                    Text(
                        modifier = Modifier
                            .offset(x = xTransitionHeader1.x.dp)
                            .alpha(alpha = alphaHeader1)
                            .scale(scale = scaleHeader1),
                        text = stringResource(resource = Res.string.library_mifos_passcode_create_passcode),
                        style = TextStyle(fontSize = 20.sp),
                    )
                } else if (activeStep == Step.Confirm) {
                    Text(
                        modifier = Modifier
                            .offset(x = xTransitionHeader2.x.dp)
                            .alpha(alpha = alphaHeader2)
                            .scale(scale = scaleHeader2),
                        text = stringResource(resource = Res.string.library_mifos_passcode_confirm_passcode),
                        style = TextStyle(fontSize = 20.sp),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PasscodeHeaderPreview() {
    PasscodeHeader(activeStep = Step.Create, isPasscodeAlreadySet = true)
}
