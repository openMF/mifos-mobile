/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.application.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import mifos_mobile.core.ui.generated.resources.ic_icon_dashboard
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.component.CardVariant
import org.mifos.mobile.core.designsystem.component.MifosCustomCard
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import mifos_mobile.core.ui.generated.resources.Res as UiRes

/**
 * A compact loan summary card displaying an image background with overlay text.
 *
 * @param cardImage The background image resource for the card.
 * @param title The loan product title (e.g., "Home Loan").
 * @param amount The formatted loan amount string.
 * @param interestRate The interest rate string to display.
 * @param onClick Optional callback invoked when the card is clicked.
 */
@Composable
fun LoanCard(
    cardImage: DrawableResource,
    title: String,
    amount: String,
    interestRate: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    MifosCustomCard(
        modifier = modifier
            .fillMaxWidth()
            .height(128.dp)
            .then(
                if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                },
            ),
        shape = DesignToken.shapes.medium,
        variant = CardVariant.ELEVATED,
        elevation = CardDefaults.cardElevation(defaultElevation = DesignToken.elevation.elevation),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp)
                    .clip(DesignToken.shapes.medium),
                painter = painterResource(cardImage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(DesignToken.padding.largeIncreased),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text(
                        text = title,
                        style = MifosTypography.bodySmall,
                        color = AppColors.customWhite,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = amount,
                        style = MifosTypography.titleMediumEmphasized,
                        color = AppColors.customWhite,
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = interestRate,
                    style = MifosTypography.bodySmall,
//                    TODO didn't find color in theme that is mentioned in figma
                    color = Color.White,
                )
            }
        }
    }
}

@Preview
@Composable
fun LoanCardPreview() {
    LoanCard(
        modifier = Modifier.padding(16.dp),
        cardImage = UiRes.drawable.ic_icon_dashboard,
        onClick = {},
        title = "title",
        amount = "amount",
        interestRate = "7.5 %",
    )
}

/**
 * An expanded loan card featuring a top-section image followed by a colored content area.
 *
 * @param cardImage The hero image displayed at the top of the card.
 * @param title The loan product title.
 * @param amount The formatted loan amount string.
 * @param interestRate The interest rate string.
 * @param backgroundColor The background color for the text content area.
 * @param contentColor The color used for text elements.
 * @param onClick Optional callback invoked when the card is clicked.
 */
@Composable
fun LoanCardCustom(
    cardImage: DrawableResource,
    title: String,
    amount: String,
    interestRate: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = Color.White,
    onClick: (() -> Unit)? = null,
) {
    MifosCustomCard(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                },
            ),
        shape = DesignToken.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            // Image section
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                painter = painterResource(cardImage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )

            // Content section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            ) {
                Text(
                    text = title,
                    style = MifosTypography.bodySmall,
                    color = contentColor,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = amount,
                    style = MifosTypography.titleMediumEmphasized,
                    color = contentColor,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = interestRate,
                    style = MifosTypography.bodySmall,
                    color = contentColor.copy(alpha = 0.8f),
                )
            }
        }
    }
}

@Preview
@Composable
fun LoanCardPreviewCustom() {
    LoanCardCustom(
        modifier = Modifier.padding(16.dp),
        cardImage = UiRes.drawable.ic_icon_dashboard,
        onClick = {},
        title = "title",
        amount = "amount",
        interestRate = "7.5 %",
    )
}
