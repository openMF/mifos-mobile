/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.componenets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import mifos_mobile.feature.settings.generated.resources.Res
import mifos_mobile.feature.settings.generated.resources.feature_settings_logout_action
import mifos_mobile.feature.settings.generated.resources.feature_settings_logout_description
import mifos_mobile.feature.settings.generated.resources.feature_settings_logout_message
import mifos_mobile.feature.settings.generated.resources.feature_settings_logout_now
import mifos_mobile.feature.settings.generated.resources.feature_settings_logout_title
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography

@Composable
fun MifosLogoutDialog(
    visibilityState: LogoutDialogState,
): Unit = when (visibilityState) {
    LogoutDialogState.Hidden -> Unit
    is LogoutDialogState.Shown -> {
        AlertDialog(
            onDismissRequest = visibilityState.onDismiss,
            confirmButton = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(
                        space = DesignToken.spacing.medium,
                        alignment = Alignment.CenterVertically,
                    ),
                ) {
                    MifosButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(DesignToken.sizes.buttonHeight),
                        shape = DesignToken.shapes.medium,
                        onClick = visibilityState.onLogout,
                    ) {
                        Text(
                            text = stringResource(Res.string.feature_settings_logout_now),
                            style = MifosTypography.titleMedium,
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            space = DesignToken.spacing.extraSmall,
                            alignment = Alignment.CenterHorizontally,
                        ),
                    ) {
                        Text(
                            text = stringResource(visibilityState.message),
                            style = MifosTypography.bodySmallEmphasized,
                            color = MaterialTheme.colorScheme.secondary,
                        )

                        Text(
                            text = stringResource(visibilityState.messageActionText),
                            style = MifosTypography.bodySmallEmphasized,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable {
                                visibilityState.onNavigateToHome.invoke()
                            },
                        )
                    }
                }
            },
            title = {
                Text(
                    text = stringResource(visibilityState.title),
                    style = MifosTypography.headlineSmallEmphasized,
                    color = AppColors.customBlack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("AlertTitleText"),
                )
            },
            text = {
                Text(
                    text = stringResource(visibilityState.description),
                    style = MifosTypography.labelMediumEmphasized,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("AlertContentText"),
                )
            },
            containerColor = Color.White,
            shape = DesignToken.shapes.medium,
        )
    }
}

sealed interface LogoutDialogState {

    data object Hidden : LogoutDialogState

    data class Shown(
        val description: StringResource,
        val title: StringResource,
        val message: StringResource,
        val messageActionText: StringResource,
        val onLogout: () -> Unit,
        val onNavigateToHome: () -> Unit,
        val onDismiss: () -> Unit,
    ) : LogoutDialogState
}

@Preview
@Composable
fun MifosLogoutDialogPreview() {
    MifosLogoutDialog(
        visibilityState = LogoutDialogState.Shown(
            description = Res.string.feature_settings_logout_description,
            title = Res.string.feature_settings_logout_title,
            message = Res.string.feature_settings_logout_message,
            messageActionText = Res.string.feature_settings_logout_action,
            onLogout = { },
            onNavigateToHome = { },
            onDismiss = { },
        ),
    )
}
