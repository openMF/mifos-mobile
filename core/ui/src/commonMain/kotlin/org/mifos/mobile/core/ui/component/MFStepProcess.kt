/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.DevicePreview

@Composable
fun MFStepProcess(
    stepNumber: String,
    activateColor: Color,
    deactivateColor: Color,
    modifier: Modifier = Modifier,
    isLastStep: Boolean = false,
    processState: StepProcessState = StepProcessState.INACTIVE,
    content: @Composable (Modifier) -> Unit,
) {
    var barHeight by remember { mutableStateOf(0.dp) }
    val localDensity = LocalDensity.current

    Row(modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Column(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        color = if (processState == StepProcessState.INACTIVE) {
                            deactivateColor
                        } else {
                            activateColor
                        },
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = if (processState == StepProcessState.COMPLETED) "✔" else stepNumber,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
            if (!isLastStep) {
                Box(
                    modifier = Modifier
                        .height(barHeight)
                        .width(4.dp)
                        .background(
                            color = if (processState == StepProcessState.INACTIVE) {
                                deactivateColor
                            } else {
                                activateColor
                            },
                        ),
                )
            }
        }
        content(
            Modifier
                .padding(start = 10.dp, end = 6.dp)
                .onGloballyPositioned { barHeight = with(localDensity) { it.size.height.toDp() } },
        )
    }
}

enum class StepProcessState {
    COMPLETED,
    ACTIVE,
    INACTIVE,
}

fun getStepState(targetStep: Int, currentStep: Int): StepProcessState {
    return when {
        currentStep == targetStep -> StepProcessState.ACTIVE
        currentStep > targetStep -> StepProcessState.COMPLETED
        else -> StepProcessState.INACTIVE
    }
}

@DevicePreview
@Composable
fun Preview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        MFStepProcess(
            stepNumber = "1",
            activateColor = Color.Red,
            deactivateColor = Color.Gray,
            modifier = modifier,
            isLastStep = false,
            processState = StepProcessState.ACTIVE,
        ) {
            Text(text = "Step 1")
        }
    }
}
