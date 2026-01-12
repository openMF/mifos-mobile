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

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset

/**
 * Centralized animation specifications following Material Motion design guidelines.
 *
 * This object provides consistent animation timing and easing curves throughout the KPT design system.
 * All animations should use these predefined specifications to ensure visual consistency.
 *
 * The specifications are organized into:
 * - **Duration-based animations**: [fast], [medium], [slow] with tween interpolation
 * - **Spring-based animations**: [fastSpring], [mediumSpring], [slowSpring] with physics simulation
 * - **Material Motion easing**: Standard easing curves from Material Design guidelines
 *
 * Example usage:
 * ```
 * // For simple property animations
 * val animatedAlpha by animateFloatAsState(
 *     targetValue = if (visible) 1f else 0f,
 *     animationSpec = KptAnimationSpecs.medium
 * )
 *
 * // For spring-based animations
 * val animatedScale by animateFloatAsState(
 *     targetValue = if (pressed) 0.95f else 1f,
 *     animationSpec = KptAnimationSpecs.fastSpring
 * )
 * ```
 *
 * @see androidx.compose.animation.core.AnimationSpec
 */
object KptAnimationSpecs {
    /**
     * Fast tween animation (150ms) for quick transitions like state changes.
     * Best used for: button states, small UI element appearances/disappearances.
     */
    val fast = tween<Float>(durationMillis = 150, easing = FastOutSlowInEasing)

    /**
     * Medium tween animation (300ms) for standard UI transitions.
     * Best used for: screen transitions, modal appearances, content changes.
     */
    val medium = tween<Float>(durationMillis = 300, easing = FastOutSlowInEasing)

    /**
     * Slow tween animation (500ms) for complex or large-scale transitions.
     * Best used for: page transitions, complex layout changes, dramatic effects.
     */
    val slow = tween<Float>(durationMillis = 500, easing = FastOutSlowInEasing)

    /**
     * Fast spring animation with medium bounce for responsive interactions.
     * Best used for: button presses, interactive feedback, quick selections.
     */
    val fastSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessHigh,
    )

    /**
     * Medium spring animation with low bounce for smooth transitions.
     * Best used for: drawer openings, sheet expansions, smooth scrolling effects.
     */
    val mediumSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessMedium,
    )

    /**
     * Slow spring animation with no bounce for stable, smooth animations.
     * Best used for: large content movements, settling animations, smooth stops.
     */
    val slowSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessLow,
    )

    // Material Motion easing curves following Material Design 3 specifications

    /**
     * Emphasized easing for important transitions that should draw attention.
     * Creates a slow start with a quick finish.
     */
    val emphasizedEasing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)

    /**
     * Emphasized accelerate easing for elements leaving the screen.
     * Quick start that maintains momentum.
     */
    val emphasizedAccelerate = CubicBezierEasing(0.3f, 0.0f, 0.8f, 0.15f)

    /**
     * Emphasized decelerate easing for elements entering the screen.
     * Maintains momentum then slows to a smooth stop.
     */
    val emphasizedDecelerate = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)

    /**
     * Standard easing for general purpose animations.
     * Provides a balanced, natural feeling motion.
     */
    val standardEasing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
}

/**
 * Slide in animation from the start edge of the screen (left in LTR, right in RTL).
 *
 * This extension function provides a convenient way to create slide-in animations
 * that respect the current layout direction.
 *
 * @param animationSpec The animation specification to use. Defaults to 300ms with emphasized easing.
 * @return An [EnterTransition] that slides content in from the start edge
 *
 * @see slideInFromEnd
 * @see AnimatedVisibilityScope
 */
@Composable
fun AnimatedVisibilityScope.slideInFromStart(
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(
        300,
        easing = KptAnimationSpecs.emphasizedEasing,
    ),
): EnterTransition = slideInHorizontally(animationSpec) { -it }

/**
 * Slide in animation from the end edge of the screen (right in LTR, left in RTL).
 *
 * This extension function provides a convenient way to create slide-in animations
 * that respect the current layout direction.
 *
 * @param animationSpec The animation specification to use. Defaults to 300ms with emphasized easing.
 * @return An [EnterTransition] that slides content in from the end edge
 *
 * @see slideInFromStart
 * @see AnimatedVisibilityScope
 */
@Composable
fun AnimatedVisibilityScope.slideInFromEnd(
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(
        300,
        easing = KptAnimationSpecs.emphasizedEasing,
    ),
): EnterTransition = slideInHorizontally(animationSpec) { it }

/**
 * Slide in animation from the top edge of the screen.
 *
 * Creates a smooth vertical slide-in effect commonly used for notifications,
 * drop-down menus, or top-anchored content.
 *
 * @param animationSpec The animation specification to use. Defaults to 300ms with emphasized easing.
 * @return An [EnterTransition] that slides content in from the top
 *
 * @see slideInFromBottom
 * @see AnimatedVisibilityScope
 */
@Composable
fun AnimatedVisibilityScope.slideInFromTop(
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(
        300,
        easing = KptAnimationSpecs.emphasizedEasing,
    ),
): EnterTransition = slideInVertically(animationSpec) { -it }

/**
 * Slide in animation from the bottom edge of the screen.
 *
 * Creates a smooth vertical slide-in effect commonly used for bottom sheets,
 * action panels, or bottom-anchored content.
 *
 * @param animationSpec The animation specification to use. Defaults to 300ms with emphasized easing.
 * @return An [EnterTransition] that slides content in from the bottom
 *
 * @see slideInFromTop
 * @see AnimatedVisibilityScope
 */
@Composable
fun AnimatedVisibilityScope.slideInFromBottom(
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(
        300,
        easing = KptAnimationSpecs.emphasizedEasing,
    ),
): EnterTransition = slideInVertically(animationSpec) { it }
