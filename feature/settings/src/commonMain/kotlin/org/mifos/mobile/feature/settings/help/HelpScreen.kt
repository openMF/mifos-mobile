/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.help

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import mifos_mobile.feature.settings.generated.resources.Res
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_help
import mifos_mobile.feature.settings.generated.resources.feature_settings_doubt
import mifos_mobile.feature.settings.generated.resources.feature_settings_doubt_message
import mifos_mobile.feature.settings.generated.resources.feature_settings_still_have_doubt
import mifos_mobile.feature.settings.generated.resources.feature_settings_still_have_doubt_action
import mifos_mobile.feature.settings.generated.resources.feature_settings_still_have_doubt_message
import mifos_mobile.feature.settings.generated.resources.feature_settings_still_have_issue
import mifos_mobile.feature.settings.generated.resources.feature_settings_still_have_issue_action
import mifos_mobile.feature.settings.generated.resources.feature_settings_still_have_issue_message
import mifos_mobile.feature.settings.generated.resources.ic_help_mail
import mifos_mobile.feature.settings.generated.resources.ic_help_mobile
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosCard
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.utils.ShareUtils

/**
 * A stateful composable that constructs the "Help" screen, including its scaffold and top bar.
 * It handles navigation and initiates external actions like calling or mailing.
 *
 * @param onBackClick Lambda to handle back navigation events.
 * @param modifier The [Modifier] to be applied to this screen.
 * @param navigateToFAQ Lambda to navigate to the FAQ screen.
 */
@Composable
internal fun HelpScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToFAQ: () -> Unit,
) {
    MifosElevatedScaffold(
        onNavigateBack = onBackClick,
        topBarTitle = stringResource(resource = Res.string.feature_settings_action_help),
        modifier = modifier,
    ) {
        HelpScreenContent(
            onCallClick = {
                ShareUtils.callHelpline()
            },
            onMailClick = {
                ShareUtils.mailHelpline()
            },
            navigateToFAQ = navigateToFAQ,
        )
    }
}

/**
 * A stateless composable that renders the main content of the "Help" screen,
 * which includes various support cards.
 *
 * @param onCallClick Lambda to be invoked when the call support action is triggered.
 * @param onMailClick Lambda to be invoked when the mail support action is triggered.
 * @param modifier The [Modifier] to be applied to the layout.
 * @param navigateToFAQ Lambda to navigate to the FAQ screen.
 */
@Composable
internal fun HelpScreenContent(
    onCallClick: () -> Unit,
    onMailClick: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToFAQ: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = DesignToken.padding.large,
                vertical = DesignToken.padding.medium,
            )
            .statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(DesignToken.padding.medium),
    ) {
        FAQCard(onClick = navigateToFAQ)
        PhoneSupportCard(onCallClick = onCallClick)
        EmailSupportCard(onMailClick = onMailClick)
    }
}

/**
 * A specialized [HelpCard] that directs the user to the FAQ screen.
 *
 * @param modifier The [Modifier] to be applied to the card.
 * @param onClick Lambda to be executed when the card is clicked.
 */
@Composable
private fun FAQCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    HelpCard(
        backgroundColor = AppColors.lightRed,
        onClick = onClick,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(DesignToken.padding.extraLarge),
        ) {
            Text(
                text = stringResource(Res.string.feature_settings_doubt),
                style = MifosTypography.titleSmallEmphasized,
                color = Color.White,
            )
            Spacer(modifier = Modifier.height(DesignToken.padding.small))
            Text(
                text = stringResource(Res.string.feature_settings_doubt_message),
                style = MifosTypography.labelMedium,
                color = Color.White,
            )
        }
    }
}

/**
 * A specialized [SupportCard] for initiating a phone call to the helpline.
 *
 * @param modifier The [Modifier] to be applied to the card.
 * @param onCallClick Lambda to be executed when the call action button is clicked.
 */
@Composable
private fun PhoneSupportCard(
    modifier: Modifier = Modifier,
    onCallClick: () -> Unit,
) {
    SupportCard(
        backgroundColor = AppColors.peach,
        titleRes = Res.string.feature_settings_still_have_doubt,
        messageRes = Res.string.feature_settings_still_have_doubt_message,
        actionRes = Res.string.feature_settings_still_have_doubt_action,
        iconRes = Res.drawable.ic_help_mobile,
        iconContentDescription = "Phone Support",
        onActionClick = onCallClick,
        modifier = modifier,
    )
}

/**
 * A specialized [SupportCard] for opening a mail client to contact support.
 *
 * @param modifier The [Modifier] to be applied to the card.
 * @param onMailClick Lambda to be executed when the mail action button is clicked.
 */
@Composable
private fun EmailSupportCard(
    modifier: Modifier = Modifier,
    onMailClick: () -> Unit,
) {
    SupportCard(
        backgroundColor = AppColors.lightPurple,
        titleRes = Res.string.feature_settings_still_have_issue,
        messageRes = Res.string.feature_settings_still_have_issue_message,
        actionRes = Res.string.feature_settings_still_have_issue_action,
        iconRes = Res.drawable.ic_help_mail,
        iconContentDescription = "Mail Support",
        onActionClick = onMailClick,
        modifier = modifier,
    )
}

/**
 * A generic, styled card used as a base for different help options.
 *
 * @param backgroundColor The background color of the card.
 * @param modifier The [Modifier] to be applied to the card.
 * @param onClick Optional lambda to handle click events on the card.
 * @param content The composable content to be displayed inside the card.
 */
@Composable
private fun HelpCard(
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    MifosCard(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
    ) {
        content()
    }
}

/**
 * A reusable composable for displaying a support option with text, an action button, and an icon.
 *
 * @param backgroundColor The background color of the card.
 * @param titleRes The string resource for the title.
 * @param messageRes The string resource for the descriptive message.
 * @param actionRes The string resource for the action button text.
 * @param iconRes The drawable resource for the decorative icon.
 * @param iconContentDescription The content description for the icon.
 * @param modifier The [Modifier] to be applied to the card.
 * @param onActionClick Lambda to be executed when the action button is clicked.
 */
@Composable
private fun SupportCard(
    backgroundColor: Color,
    titleRes: StringResource,
    messageRes: StringResource,
    actionRes: StringResource,
    iconRes: DrawableResource,
    iconContentDescription: String,
    modifier: Modifier = Modifier,
    onActionClick: () -> Unit,
) {
    HelpCard(
        backgroundColor = backgroundColor,
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = DesignToken.padding.small),
        ) {
            SupportCardContent(
                titleRes = titleRes,
                messageRes = messageRes,
                actionRes = actionRes,
                onActionClick = onActionClick,
                modifier = Modifier.fillMaxWidth().zIndex(1f),
            )
            SupportCardIcon(
                iconRes = iconRes,
                contentDescription = iconContentDescription,
                modifier = Modifier.align(Alignment.BottomEnd).zIndex(0f),
            )
        }
    }
}

/**
 * Renders the textual content and action button within a [SupportCard].
 *
 * @param titleRes The string resource for the title.
 * @param messageRes The string resource for the descriptive message.
 * @param actionRes The string resource for the action button text.
 * @param modifier The [Modifier] to be applied to the content layout.
 * @param onActionClick Lambda to be executed when the action button is clicked.
 */
@Composable
private fun SupportCardContent(
    titleRes: StringResource,
    messageRes: StringResource,
    actionRes: StringResource,
    modifier: Modifier = Modifier,
    onActionClick: () -> Unit,
) {
    Column(
        modifier = modifier.padding(DesignToken.padding.extraLarge),
        verticalArrangement = Arrangement.spacedBy(DesignToken.padding.large),
    ) {
        Text(
            text = stringResource(titleRes),
            style = MifosTypography.titleSmallEmphasized,
            color = Color.White,
        )
        Text(
            text = stringResource(messageRes),
            style = MifosTypography.labelMedium,
            color = Color.White,
        )
        HelpActionButton(
            textRes = actionRes,
            onClick = onActionClick,
        )
    }
}

/**
 * Displays the decorative icon within a [SupportCard].
 *
 * @param iconRes The drawable resource for the icon.
 * @param contentDescription The content description for the icon.
 * @param modifier The [Modifier] to be applied to the image.
 */
@Composable
private fun SupportCardIcon(
    iconRes: DrawableResource,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(iconRes),
        contentDescription = contentDescription,
        modifier = modifier,
    )
}

/**
 * A styled button used for actions within the help cards.
 *
 * @param textRes The string resource for the button's text.
 * @param modifier The [Modifier] to be applied to the button.
 * @param onClick Lambda to be executed when the button is clicked.
 */
@Composable
private fun HelpActionButton(
    textRes: StringResource,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    MifosButton(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White.copy(alpha = 0.3f),
        ),
        shape = RoundedCornerShape(DesignToken.padding.small),
        contentPadding = PaddingValues(
            horizontal = DesignToken.padding.extraLarge,
            vertical = DesignToken.padding.medium,
        ),
        modifier = modifier,
    ) {
        Text(
            text = stringResource(textRes),
            color = Color.White,
            style = MifosTypography.bodyMediumEmphasized,
        )
    }
}

/**
 * A Jetpack Compose preview for the [HelpScreenContent].
 */
@Preview
@Composable
private fun HelpScreenContentPreview() {
    MifosMobileTheme {
        HelpScreenContent(
            onCallClick = {},
            onMailClick = {},
            navigateToFAQ = {},
        )
    }
}

/**
 * A Jetpack Compose preview for the [FAQCard].
 */
@Preview
@Composable
private fun FAQCardPreview() {
    MifosMobileTheme {
        FAQCard(onClick = {})
    }
}

/**
 * A Jetpack Compose preview for the [PhoneSupportCard].
 */
@Preview
@Composable
private fun PhoneSupportCardPreview() {
    MifosMobileTheme {
        PhoneSupportCard(onCallClick = {})
    }
}

/**
 * A Jetpack Compose preview for the [EmailSupportCard].
 */
@Preview
@Composable
private fun EmailSupportCardPreview() {
    MifosMobileTheme {
        EmailSupportCard(onMailClick = {})
    }
}

/**
 * A Jetpack Compose preview for the [HelpActionButton].
 */
@Preview
@Composable
private fun HelpActionButtonPreview() {
    MifosMobileTheme {
        HelpActionButton(
            textRes = Res.string.feature_settings_still_have_doubt_action,
            onClick = {},
        )
    }
}
