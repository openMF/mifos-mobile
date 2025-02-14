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

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.mifos.library.passcode.theme.blueTint
import org.mifos.library.passcode.utility.Constants.STEPS_COUNT
import org.mifos.library.passcode.utility.Step

@Composable
internal fun PasscodeStepIndicator(
    activeStep: Step,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = 6.dp,
            alignment = Alignment.CenterHorizontally,
        ),
    ) {
        repeat(STEPS_COUNT) { step ->
            val isActiveStep = step <= activeStep.index
            val stepColor =
                animateColorAsState(if (isActiveStep) blueTint else Color.Gray, label = "")

            Box(
                modifier = Modifier
                    .size(
                        width = 72.dp,
                        height = 4.dp,
                    )
                    .background(
                        color = stepColor.value,
                        shape = MaterialTheme.shapes.medium,
                    ),
            )
        }
    }
}
