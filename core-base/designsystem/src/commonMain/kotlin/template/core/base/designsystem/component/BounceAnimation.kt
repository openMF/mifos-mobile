/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview
import template.core.base.designsystem.KptTheme

/**
 * A composable that briefly enlarges the content to create a bounce effect when triggered.
 *
 * @param targetValue The peak scale value during bounce.
 * @param durationMillis The duration of the bounce animation in milliseconds.
 * @param content A composable lambda that receives the animated scale value.
 *
 * @sample BounceAnimationPreview
 */
@Composable
fun BounceAnimation(
    targetValue: Float = 1.1f,
    durationMillis: Int = 100,
    content: @Composable (scale: Float) -> Unit,
) {
    var triggered by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (triggered) targetValue else 1f,
        animationSpec = tween(durationMillis, easing = FastOutSlowInEasing),
        finishedListener = { triggered = false },
        label = "bounce_animation",
    )

    LaunchedEffect(Unit) {
        triggered = true
    }

    content(scale)
}

/**
 * A composable that reveals or hides content with a combination of fade and scale animations.
 *
 * @param visible Controls whether the content is shown or hidden.
 * @param modifier Modifier applied to the container.
 * @param animationSpec The animation spec used for fade and scale effects.
 * @param content The composable content to animate.
 *
 * @sample RevealAnimationPreview
 */
@Composable
fun RevealAnimation(
    visible: Boolean,
    modifier: Modifier = Modifier,
    animationSpec: FiniteAnimationSpec<Float> = KptAnimationSpecs.medium,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = fadeIn(animationSpec) + scaleIn(animationSpec, initialScale = 0.8f),
        exit = fadeOut(animationSpec) + scaleOut(animationSpec, targetScale = 0.8f),
    ) {
        content()
    }
}

/**
 * Animates a list of items with a staggered vertical slide-in and fade-in effect.
 *
 * @param items The list of items to animate.
 * @param modifier Modifier applied to the wrapping Column.
 * @param staggerDelayMillis Delay in milliseconds between each item's animation.
 * @param content A composable that renders each item with index.
 *
 * @sample StaggeredAnimationPreview
 */
@Composable
fun <T> StaggeredAnimation(
    items: List<T>,
    modifier: Modifier = Modifier,
    staggerDelayMillis: Long = 100,
    content: @Composable (item: T, index: Int) -> Unit,
) {
    Column(modifier = modifier) {
        items.forEachIndexed { index, item ->
            var visible by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                delay(index * staggerDelayMillis)
                visible = true
            }

            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(300, easing = KptAnimationSpecs.emphasizedEasing),
                ) + fadeIn(animationSpec = tween(300)),
            ) {
                content(item, index)
            }
        }
    }
}

@Preview
@Composable
private fun BounceAnimationPreview() {
    KptTheme {
        BounceAnimation {
            Text(
                text = "Bouncy!",
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Preview
@Composable
private fun RevealAnimationPreview() {
    KptTheme {
        var show by remember { mutableStateOf(true) }
        RevealAnimation(visible = show) {
            Text(
                text = "Revealed!",
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Preview
@Composable
private fun StaggeredAnimationPreview() {
    KptTheme {
        StaggeredAnimation(items = listOf("One", "Two", "Three")) { item, _ ->
            Text(
                text = item,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            )
        }
    }
}
