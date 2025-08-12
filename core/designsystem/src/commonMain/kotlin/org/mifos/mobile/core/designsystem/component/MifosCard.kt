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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fluent.ui.system.icons.FluentIcons
import fluent.ui.system.icons.filled.Document
import mifos_mobile.core.designsystem.generated.resources.Res
import mifos_mobile.core.designsystem.generated.resources.feature_upload_id_remove_file
import mifos_mobile.core.designsystem.generated.resources.feature_upload_id_select_new_file
import mifos_mobile.core.designsystem.generated.resources.feature_upload_id_view_file
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.AppColors
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
fun MifosUploadStateCard(
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
        MifosUploadStateCardContent(
            text = text,
            icon = icon,
            modifier = modifier,
        )
    }
}

@Composable
fun MifosUploadStateCardContent(
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

@Composable
fun MifosUploadedStateCard(
    icon: ImageVector,
    label: String,
    fileName: String,
    fileSize: String,
    onRemoveClick: () -> Unit,
    onViewClick: () -> Unit,
    onSelectNewClick: () -> Unit,
    modifier: Modifier = Modifier,
    removeText: String = stringResource(Res.string.feature_upload_id_remove_file),
    selectText: String = stringResource(Res.string.feature_upload_id_select_new_file),
    viewText: String = stringResource(Res.string.feature_upload_id_view_file),
    height: Dp = 112.dp,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        MifosCustomCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            enabled = false,
            variant = CardVariant.OUTLINED,
            shape = DesignToken.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = DesignToken.elevation.none),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
            borderStroke = BorderStroke(1.dp, MaterialTheme.colorScheme.secondaryContainer),
        ) {
            MifosUploadedCardContent(
                icon = icon,
                fileName = fileName,
                fileSize = fileSize,
                onRemoveClick = onRemoveClick,
                onViewClick = onViewClick,
                onSelectNewClick = onSelectNewClick,
                removeText = removeText,
                selectText = selectText,
                viewText = viewText,
            )
        }

        Box(
            modifier = Modifier
                .padding(start = DesignToken.padding.large)
                .offset(y = (-7).dp),
        ) {
            Text(
                text = label,
                style = MifosTypography.bodySmall,
                modifier = Modifier
                    .padding(horizontal = DesignToken.padding.extraSmall),
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MifosUploadedCardContent(
    removeText: String,
    selectText: String,
    viewText: String,
    icon: ImageVector,
    fileName: String,
    fileSize: String,
    onRemoveClick: () -> Unit,
    onViewClick: () -> Unit,
    onSelectNewClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(DesignToken.padding.largeIncreased),
    ) {
        Box(
            modifier = Modifier
                .size(DesignToken.sizes.avatarSmall + 4.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = DesignToken.shapes.small,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                imageVector = icon,
                contentDescription = "file icon",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .size(DesignToken.sizes.iconMedium)
                    .align(Alignment.Center),
            )
        }

        Spacer(modifier = Modifier.width(DesignToken.spacing.large))

        Column {
            Text(
                text = fileName,
                style = MifosTypography.titleSmallEmphasized,
                color = AppColors.customBlack,
            )

            Text(
                text = fileSize,
                style = MifosTypography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
            )

            Spacer(modifier = Modifier.height(DesignToken.spacing.largeIncreased))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.large),
            ) {
                Text(
                    modifier = Modifier.clickable {
                        onRemoveClick()
                    },
                    text = removeText,
                    color = MaterialTheme.colorScheme.primary,
                    style = MifosTypography.labelMedium,
                )

                Text(
                    modifier = Modifier.clickable {
                        onViewClick()
                    },
                    text = viewText,
                    color = MaterialTheme.colorScheme.primary,
                    style = MifosTypography.labelMedium,
                )

                Text(
                    modifier = Modifier.clickable {
                        onSelectNewClick()
                    },
                    text = selectText,
                    color = MaterialTheme.colorScheme.primary,
                    style = MifosTypography.labelMedium,
                )
            }
        }
    }
}

@Composable
fun MifosExploreCard(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    multiline: Boolean = true,
    maxLines: Int = 2,
    onClick: () -> Unit,
) {
    MifosCustomCard(
        modifier = modifier
            .height(DesignToken.sizes.inputHeight)
            .border(
                1.dp,
                MaterialTheme.colorScheme.secondaryContainer,
                DesignToken.shapes.medium,
            ),
        variant = CardVariant.OUTLINED,
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        ),
    ) {
        Row(
            modifier = Modifier.padding(DesignToken.padding.large),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.small),
        ) {
            Image(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(DesignToken.sizes.iconMedium),
            )

            Text(
                text = if (multiline) {
                    text.replaceFirst(" ", "\n")
                } else {
                    text
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                style = MifosTypography.bodyMediumEmphasized,
                maxLines = maxLines,
                overflow = TextOverflow.MiddleEllipsis,
                textAlign = TextAlign.Start,
            )
        }
    }
}

@Preview
@Composable
private fun Mifos_Explore_Card_Preview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row {
            // Method 1: Automatic multiline (spaces become line breaks)
            MifosExploreCard(
                modifier = Modifier.weight(0.5f, true),
                icon = MifosIcons.Money,
                text = "Home Loan",
                onClick = { /* Handle click */ },
            )

            // Method 2: Custom line breaks with explicit control
            MifosExploreCard(
                modifier = Modifier.weight(0.5f, true),
                icon = MifosIcons.Money,
                text = "Personal Loan",
                onClick = { /* Handle click */ },
            )
        }

        // Method 3: Single line version
        MifosExploreCard(
            icon = MifosIcons.Money,
            text = "Savings Account",
            multiline = true,
            onClick = { /* Handle click */ },
        )

        MifosExploreCard(
            icon = MifosIcons.Money,
            text = "Business Account",
            multiline = true,
            onClick = { /* Handle click */ },
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

            MifosUploadStateCard(
                text = "Upload Your Id",
                icon = MifosIcons.UploadId,
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
fun FloatingTitleCardPreview() {
    MifosMobileTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(DesignToken.padding.large),
            verticalArrangement = Arrangement.spacedBy(DesignToken.padding.largeIncreased),
        ) {
            MifosUploadedStateCard(
                label = "Profile Photo",
                icon = FluentIcons.Filled.Document,
                fileName = "profile photo 67883.png",
                fileSize = "346 KB",
                onRemoveClick = {},
                onViewClick = {},
                onSelectNewClick = {},
                removeText = "Remove File",
                selectText = "Select New File",
                viewText = "View File",
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
