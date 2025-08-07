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

@Preview
@Composable
private fun FAQCardPreview() {
    MifosMobileTheme {
        FAQCard(onClick = {})
    }
}

@Preview
@Composable
private fun PhoneSupportCardPreview() {
    MifosMobileTheme {
        PhoneSupportCard(onCallClick = {})
    }
}

@Preview
@Composable
private fun EmailSupportCardPreview() {
    MifosMobileTheme {
        EmailSupportCard(onMailClick = {})
    }
}

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
