package org.mifos.mobile.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


enum class StepProcessState {
    COMPLETED,
    ACTIVE,
    INACTIVE
}

@Composable
fun MFStepProcess(
    stepNumber: String,
    processState: StepProcessState = StepProcessState.INACTIVE,
    activateColor: Color,
    deactivateColor: Color,
    isLastStep: Boolean = false,
    processContent: @Composable (Modifier) -> Unit
) {
    var barHeight by remember { mutableStateOf(0.dp) }
    val localDensity = LocalDensity.current

    Row {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Column(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color = if (processState == StepProcessState.INACTIVE) deactivateColor else activateColor),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (processState == StepProcessState.COMPLETED) "✔" else stepNumber,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            if (!isLastStep) {
                Box(
                    modifier = Modifier
                        .height(barHeight)
                        .width(4.dp)
                        .background(color = if (processState == StepProcessState.INACTIVE) deactivateColor else activateColor),
                ) {}
            }
        }
        processContent(
            Modifier
                .padding(start = 10.dp, end = 6.dp)
                .onGloballyPositioned { barHeight = with(localDensity) { it.size.height.toDp() } }
        )
    }
}

fun getStepState(targetStep: Int, currentStep: Int): StepProcessState {
    return when {
        currentStep == targetStep -> StepProcessState.ACTIVE
        currentStep > targetStep -> StepProcessState.COMPLETED
        else -> StepProcessState.INACTIVE
    }
}