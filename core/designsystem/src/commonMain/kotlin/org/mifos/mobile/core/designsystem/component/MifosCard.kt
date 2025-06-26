/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography

@Composable
fun MifosCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    elevation: Dp = 1.dp,
    onClick: (() -> Unit)? = null,
    colors: CardColors = CardDefaults.cardColors(),
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        shape = shape,
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation,
        ),
        colors = colors,
        content = content,
    )
}

/**
 * A flexible and theme-aware card component for the CMP design system.
 *
 * This composable abstracts over Material3 [Card], [ElevatedCard], and [OutlinedCard],
 * and selects the appropriate variant based on [CardVariant].
 *
 * @param modifier Modifier applied to the card container. *(Default: [Modifier])*
 * @param onClick Lambda triggered when the card is clicked. *(Default: `{}`)*
 * @param enabled Whether the card is enabled and responds to click events. *(Default: `true`)*
 * @param variant Determines the visual style of the card. *(Default: [CardVariant.FILLED])*
 * @param shape The shape of the card. *(Optional; defaults to the variant's shape via [CardDefaults])*
 * @param colors The color configuration of the card. *(Optional; defaults to the variant's colors via [CardDefaults])*
 * @param elevation Elevation of the card surface. *(Optional; defaults to the variant's elevation via [CardDefaults])*
 * @param borderStroke Border stroke for the card.
 * (Optional; defaults to [CardDefaults.outlinedCardBorder] when [variant] is [CardVariant.OUTLINED], `null` otherwise)*
 * @param interactionSource The [MutableInteractionSource] to observe user interaction states. *(Optional)*
 * @param content The content of the card, scoped to a [ColumnScope] for vertical layout flexibility.
 */

@Composable
fun MifosCustomCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    variant: CardVariant = CardVariant.FILLED,
    shape: Shape? = null,
    colors: CardColors? = null,
    elevation: CardElevation? = null,
    borderStroke: BorderStroke? = null,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    when (variant) {
        CardVariant.FILLED -> Card(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = shape ?: CardDefaults.shape,
            colors = colors ?: CardDefaults.cardColors(),
            elevation = elevation ?: CardDefaults.cardElevation(),
            border = borderStroke,
            interactionSource = interactionSource,
        ) {
            content()
        }

        CardVariant.ELEVATED -> ElevatedCard(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = shape ?: CardDefaults.elevatedShape,
            colors = colors ?: CardDefaults.elevatedCardColors(),
            elevation = elevation ?: CardDefaults.elevatedCardElevation(),
            interactionSource = interactionSource,
        ) {
            content()
        }

        CardVariant.OUTLINED -> OutlinedCard(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = shape ?: CardDefaults.outlinedShape,
            colors = colors ?: CardDefaults.outlinedCardColors(),
            elevation = elevation ?: CardDefaults.outlinedCardElevation(),
            border = borderStroke ?: CardDefaults.outlinedCardBorder(enabled),
            interactionSource = interactionSource,
        ) {
            content()
        }
    }
}

@Composable
fun MifosUploadCard(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 112.dp,
) {
    MifosCustomCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        onClick = onClick,
        enabled = true,
        variant = CardVariant.FILLED,
        shape = DesignToken.shapes.medium,
        elevation = CardDefaults.cardElevation(
            defaultElevation = DesignToken.elevation.none,
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        borderStroke = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.secondaryContainer,
        ),
    ) {
        UploadCardContent(
            text = text,
            icon = icon,
            modifier = modifier,
        )
    }
}

@Composable
fun UploadCardContent(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(DesignToken.padding.medium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(DesignToken.sizes.iconMedium),
        )
        Spacer(modifier = Modifier.height(DesignToken.spacing.extraSmall))
        Text(
            text = text,
            style = MifosTypography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Preview
@Composable
private fun Upload_Card_Preview() {
    MifosMobileTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(DesignToken.padding.large),
            verticalArrangement = Arrangement.spacedBy(DesignToken.padding.largeIncreased),
        ) {
            Text(
                text = "Different variant cards",
                style = MifosTypography.headlineMedium,
            )
            CardVariant.entries.forEach {
                MifosCustomCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(112.dp),
                    onClick = {},
                    enabled = true,
                    variant = it,
                    shape = DesignToken.shapes.medium,
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = if (it == CardVariant.ELEVATED) {
                            DesignToken.elevation.elevation
                        } else {
                            DesignToken.elevation.none
                        },
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                    ),
                    borderStroke = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.secondaryContainer,
                    ),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(DesignToken.padding.medium),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            imageVector = MifosIcons.UploadId,
                            contentDescription = null,
                            modifier = Modifier.size(DesignToken.sizes.iconMedium),
                        )
                        Spacer(modifier = Modifier.height(DesignToken.spacing.extraSmall))
                        Text(
                            text = "Upload Documents",
                            style = MifosTypography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                }
            }

            Text(
                text = "Upload Card",
                style = MifosTypography.headlineMedium,
            )

            MifosUploadCard(
                text = "Upload Your Id",
                icon = MifosIcons.UploadId,
                onClick = {},
            )
        }
    }
}

enum class CardVariant {

    /**
     * A standard filled card with background color and elevation.
     * Corresponds to [Card].
     * Recommended for most use cases where elevation and theming are desired.
     */
    FILLED,

    /**
     * A card with extra emphasis via elevation and contrast, but no border.
     * Corresponds to [ElevatedCard].
     * Useful for drawing attention to important or interactive content.
     */
    ELEVATED,

    /**
     * A low-emphasis card with a visible border and no elevation.
     * Corresponds to [OutlinedCard].
     * Suitable for lightweight, non-intrusive containers or when visual grouping is needed without elevation.
     */
    OUTLINED,
}
