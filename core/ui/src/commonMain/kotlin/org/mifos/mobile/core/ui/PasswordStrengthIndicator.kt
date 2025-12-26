/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import template.core.base.designsystem.theme.KptTheme

@Suppress("LongMethod", "CyclomaticComplexMethod", "MagicNumber")
@Composable
fun PasswordStrengthIndicator(
    state: PasswordStrengthState,
    currentCharacterCount: Int,
    modifier: Modifier = Modifier,
    minimumCharacterCount: Int? = null,
) {
    val widthPercent by animateFloatAsState(
        targetValue = when (state) {
            PasswordStrengthState.NONE -> 0f
            PasswordStrengthState.WEAK_1 -> .25f
            PasswordStrengthState.WEAK_2 -> .5f
            PasswordStrengthState.WEAK_3 -> .66f
            PasswordStrengthState.GOOD -> .82f
            PasswordStrengthState.STRONG -> 1f
            PasswordStrengthState.VERY_STRONG -> 1f
        },
        label = "Width Percent State",
    )
    val indicatorColor = when (state) {
        PasswordStrengthState.NONE -> KptTheme.colorScheme.error
        PasswordStrengthState.WEAK_1 -> KptTheme.colorScheme.error
        PasswordStrengthState.WEAK_2 -> KptTheme.colorScheme.error
        PasswordStrengthState.WEAK_3 -> weakColor
        PasswordStrengthState.GOOD -> KptTheme.colorScheme.primary
        PasswordStrengthState.STRONG -> strongColor
        PasswordStrengthState.VERY_STRONG -> Color.Magenta
    }
    val animatedIndicatorColor by animateColorAsState(
        targetValue = indicatorColor,
        label = "Indicator Color State",
    )
    val label = when (state) {
        PasswordStrengthState.NONE -> ""
        PasswordStrengthState.WEAK_1 -> "Weak"
        PasswordStrengthState.WEAK_2 -> "Weak"
        PasswordStrengthState.WEAK_3 -> "Weak"
        PasswordStrengthState.GOOD -> "Good"
        PasswordStrengthState.STRONG -> "Strong"
        PasswordStrengthState.VERY_STRONG -> "Very Strong"
    }
    Column(
        modifier = modifier,
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(DesignToken.sizes.boxDp4)
                .background(KptTheme.colorScheme.surfaceContainerHigh),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .clip(DesignToken.shapes.dp2)
                    .graphicsLayer {
                        transformOrigin = TransformOrigin(pivotFractionX = 0f, pivotFractionY = 0f)
                        scaleX = widthPercent
                    }
                    .drawBehind {
                        drawRect(animatedIndicatorColor)
                    },
            )
        }
        Spacer(Modifier.height(KptTheme.spacing.xs))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            minimumCharacterCount?.let { minCount ->
                MinimumCharacterCount(
                    minimumRequirementMet = currentCharacterCount >= minCount,
                    minimumCharacterCount = minCount,
                )
            }
            Text(
                text = label,
                style = KptTheme.typography.labelSmall,
                color = indicatorColor,
            )
        }
    }
}

@Composable
private fun MinimumCharacterCount(
    minimumRequirementMet: Boolean,
    minimumCharacterCount: Int,
    modifier: Modifier = Modifier,
) {
    val characterCountColor by animateColorAsState(
        targetValue = if (minimumRequirementMet) {
            strongColor
        } else {
            KptTheme.colorScheme.surfaceDim
        },
        label = "minimumCharacterCountColor",
    )
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AnimatedContent(
            targetState = if (minimumRequirementMet) {
                MifosIcons.CheckCircle
            } else {
                MifosIcons.Close
            },
            label = "iconForMinimumCharacterCount",
        ) {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = characterCountColor,
                modifier = Modifier.size(DesignToken.sizes.iconTiny),
            )
        }
        Spacer(modifier = Modifier.width(DesignToken.spacing.dp2))
        Text(
            text = "$minimumCharacterCount characters",
            color = characterCountColor,
            style = KptTheme.typography.labelSmall,
        )
    }
}

@Suppress("LongMethod", "CyclomaticComplexMethod")
@Composable
fun CombinedPasswordErrorCard(
    passwordStrengthState: PasswordStrengthState,
    currentCharacterCount: Int,
    modifier: Modifier = Modifier,
    errorText: StringResource? = null,
    errors: List<StringResource> = emptyList(),
    minimumCharacterCount: Int? = null,
) {
    val hasErrors = errorText != null || errors.isNotEmpty()

    val widthPercent by animateFloatAsState(
        targetValue = when (passwordStrengthState) {
            PasswordStrengthState.NONE -> 0f
            PasswordStrengthState.WEAK_1 -> .25f
            PasswordStrengthState.WEAK_2 -> .5f
            PasswordStrengthState.WEAK_3 -> .66f
            PasswordStrengthState.GOOD -> .82f
            PasswordStrengthState.STRONG -> 1f
            PasswordStrengthState.VERY_STRONG -> 1f
        },
        label = "Width Percent State",
    )

    val indicatorColor = when (passwordStrengthState) {
        PasswordStrengthState.NONE -> KptTheme.colorScheme.error
        PasswordStrengthState.WEAK_1 -> KptTheme.colorScheme.error
        PasswordStrengthState.WEAK_2 -> KptTheme.colorScheme.error
        PasswordStrengthState.WEAK_3 -> weakColor
        PasswordStrengthState.GOOD -> Color.Magenta
        PasswordStrengthState.STRONG -> strongColor
        PasswordStrengthState.VERY_STRONG -> KptTheme.colorScheme.primary
    }

    val animatedIndicatorColor by animateColorAsState(
        targetValue = indicatorColor,
        label = "Indicator Color State",
    )

    val strengthLabel = when (passwordStrengthState) {
        PasswordStrengthState.NONE -> ""
        PasswordStrengthState.WEAK_1 -> "Weak"
        PasswordStrengthState.WEAK_2 -> "Weak"
        PasswordStrengthState.WEAK_3 -> "Weak"
        PasswordStrengthState.GOOD -> "Good"
        PasswordStrengthState.STRONG -> "Strong"
        PasswordStrengthState.VERY_STRONG -> "Very Strong"
    }

    AnimatedVisibility(visible = hasErrors) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .testTag("passwordErrorCard"),
            shape = KptTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = KptTheme.colorScheme.error.copy(alpha = 0.05f),
            ),
            border = BorderStroke(
                width = DesignToken.strokes.thin,
                color = KptTheme.colorScheme.error.copy(alpha = 0.2f),
            ),
        ) {
            Column {
                // Top border strength indicator
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DesignToken.sizes.boxDp4)
                        .background(KptTheme.colorScheme.surfaceContainerHigh),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .clip(DesignToken.shapes.topCornerDp8)
                            .graphicsLayer {
                                transformOrigin = TransformOrigin(pivotFractionX = 0f, pivotFractionY = 0f)
                                scaleX = widthPercent
                            }
                            .drawBehind {
                                drawRect(animatedIndicatorColor)
                            },
                    )
                }

                Column(
                    modifier = Modifier.padding(DesignToken.padding.medium),
                    verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.xs),
                ) {
                    // Header row with "Password Requirements" and strength label
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.dp6),
                        ) {
                            Icon(
                                imageVector = MifosIcons.OutlinedInfo,
                                contentDescription = "Error",
                                tint = KptTheme.colorScheme.error,
                                modifier = Modifier.size(DesignToken.sizes.iconSmall),
                            )
                            Text(
                                text = "Password Requirements",
                                style = MifosTypography.labelMedium,
                                color = KptTheme.colorScheme.error,
                                fontWeight = FontWeight.Medium,
                            )
                        }

                        if (strengthLabel.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = animatedIndicatorColor,
                                        shape = KptTheme.shapes.extraSmall,
                                    )
                                    .padding(horizontal = KptTheme.spacing.sm, vertical = KptTheme.spacing.xs),
                            ) {
                                Text(
                                    text = strengthLabel,
                                    style = MifosTypography.labelMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium,
                                )
                            }
                        }
                    }

                    // Minimum character count indicator if provided
                    minimumCharacterCount?.let { minCount ->
                        MinimumCharacterCount(
                            minimumRequirementMet = currentCharacterCount >= minCount,
                            minimumCharacterCount = minCount,
                        )
                    }

                    // Error text if provided
                    errorText?.let {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("passwordError"),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm),
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(DesignToken.sizes.boxDp4)
                                    .background(
                                        color = KptTheme.colorScheme.error,
                                        shape = CircleShape,
                                    )
                                    .padding(top = DesignToken.padding.dp6),
                            )
                            Text(
                                text = stringResource(it),
                                style = MifosTypography.tag,
                                color = KptTheme.colorScheme.error,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }

                    // Error list
                    errors.forEachIndexed { index, error ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("passwordError_$index"),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(
                                space = KptTheme.spacing.xs,
                                alignment = Alignment.CenterHorizontally,
                            ),
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(DesignToken.sizes.boxDp4)
                                    .background(
                                        color = KptTheme.colorScheme.error,
                                        shape = CircleShape,
                                    )
                                    .padding(top = DesignToken.padding.dp6),
                            )
                            Text(
                                text = stringResource(error),
                                style = MifosTypography.tag,
                                color = KptTheme.colorScheme.error,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }
        }
    }
}

enum class PasswordStrengthState {
    NONE,
    WEAK_1,
    WEAK_2,
    WEAK_3,
    GOOD,
    STRONG,
    VERY_STRONG,
}

private val strongColor = Color(0xFF41B06D)
private val weakColor = Color(0xFF8B6609)

@Preview
@Composable
private fun PasswordStrengthIndicatorPreview_minCharMet() {
    MifosMobileTheme {
        PasswordStrengthIndicator(
            state = PasswordStrengthState.WEAK_3,
            currentCharacterCount = 12,
            minimumCharacterCount = 12,
        )
    }
}

@Preview
@Composable
private fun PasswordStrengthIndicatorPreview_minCharNotMet() {
    MifosMobileTheme {
        PasswordStrengthIndicator(
            state = PasswordStrengthState.WEAK_3,
            currentCharacterCount = 11,
            minimumCharacterCount = 12,
        )
    }
}

@Preview
@Composable
private fun PasswordStrengthIndicatorPreview_noMinChar() {
    MifosMobileTheme {
        PasswordStrengthIndicator(
            state = PasswordStrengthState.WEAK_3,
            currentCharacterCount = 12,
        )
    }
}
