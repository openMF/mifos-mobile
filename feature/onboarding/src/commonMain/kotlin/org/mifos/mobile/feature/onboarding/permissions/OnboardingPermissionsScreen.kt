/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.onboarding.permissions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.calf.permissions.ExperimentalPermissionsApi
import com.mohamedrejeb.calf.permissions.Permission
import com.mohamedrejeb.calf.permissions.PermissionState
import com.mohamedrejeb.calf.permissions.isGranted
import com.mohamedrejeb.calf.permissions.rememberMultiplePermissionsState
import mifos_mobile.core.ui.generated.resources.ic_icon_logo_1
import mifos_mobile.feature.onboarding.generated.resources.Res
import mifos_mobile.feature.onboarding.generated.resources.feature_onboarding_continue
import mifos_mobile.feature.onboarding.generated.resources.feature_onboarding_grant
import mifos_mobile.feature.onboarding.generated.resources.feature_onboarding_permission_camera_desc
import mifos_mobile.feature.onboarding.generated.resources.feature_onboarding_permission_camera_title
import mifos_mobile.feature.onboarding.generated.resources.feature_onboarding_permission_granted
import mifos_mobile.feature.onboarding.generated.resources.feature_onboarding_permission_location_desc
import mifos_mobile.feature.onboarding.generated.resources.feature_onboarding_permission_location_title
import mifos_mobile.feature.onboarding.generated.resources.feature_onboarding_permission_notification_desc
import mifos_mobile.feature.onboarding.generated.resources.feature_onboarding_permission_notification_title
import mifos_mobile.feature.onboarding.generated.resources.feature_onboarding_permissions_subtitle
import mifos_mobile.feature.onboarding.generated.resources.feature_onboarding_permissions_title
import mifos_mobile.feature.onboarding.generated.resources.feature_onboarding_powered_by
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosCard
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.utils.EventsEffect
import template.core.base.designsystem.theme.KptTheme

@OptIn(ExperimentalPermissionsApi::class)
private data class PermissionItemData(
    val permission: Permission,
    val titleRes: StringResource,
    val descRes: StringResource,
    val icon: ImageVector,
    val state: PermissionState,
)

@Composable
internal fun OnboardingPermissionsScreen(
    onNavigateToLanguageOnboarding: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OnboardingPermissionsViewModel = koinViewModel(),
) {
    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            OnboardingPermissionsEvent.NavigateToLanguage -> onNavigateToLanguageOnboarding()
        }
    }

    OnboardingPermissionsScreenContent(
        modifier = modifier,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun OnboardingPermissionsScreenContent(
    modifier: Modifier = Modifier,
    onAction: (OnboardingPermissionsAction) -> Unit,
) {
    val multiplePermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Permission.Notification,
            Permission.Camera,
            Permission.FineLocation,
            Permission.CoarseLocation,
        ),
        onPermissionsResult = { _ ->
            onAction(OnboardingPermissionsAction.CompletePermissionOnboarding)
        },
    )

    val permissionItems = remember(multiplePermissionsState) {
        if (multiplePermissionsState.permissions.isEmpty()) {
            emptyList()
        } else {
            listOf(
                PermissionItemData(
                    permission = Permission.Notification,
                    titleRes = Res.string.feature_onboarding_permission_notification_title,
                    descRes = Res.string.feature_onboarding_permission_notification_desc,
                    icon = MifosIcons.Notifications,
                    state = multiplePermissionsState.permissions[0],
                ),
                PermissionItemData(
                    permission = Permission.Camera,
                    titleRes = Res.string.feature_onboarding_permission_camera_title,
                    descRes = Res.string.feature_onboarding_permission_camera_desc,
                    icon = MifosIcons.Camera,
                    state = multiplePermissionsState.permissions[1],
                ),
                PermissionItemData(
                    permission = Permission.FineLocation,
                    titleRes = Res.string.feature_onboarding_permission_location_title,
                    descRes = Res.string.feature_onboarding_permission_location_desc,
                    icon = MifosIcons.LocationOn,
                    state = multiplePermissionsState.permissions[2],
                ),
            )
        }
    }

    val scrollState = rememberScrollState()

    MifosScaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                MifosButton(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = KptTheme.spacing.md)
                        .height(DesignToken.sizes.buttonHeight),
                    shape = KptTheme.shapes.medium,
                    onClick = {
                        onAction(OnboardingPermissionsAction.CompletePermissionOnboarding)
                    },
                ) {
                    Text(
                        text = stringResource(Res.string.feature_onboarding_continue),
                        style = MifosTypography.titleMedium,
                    )
                }

                Spacer(Modifier.height(15.dp))

                MifosPoweredCard(
                    text = stringResource(Res.string.feature_onboarding_powered_by),
                )
            }
        },
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(top = DesignToken.padding.dp75),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = KptTheme.spacing.md),
            ) {
                Image(
                    modifier = Modifier
                        .height(DesignToken.sizes.imageDp48)
                        .width(DesignToken.sizes.imageDp165),
                    painter = painterResource(
                        mifos_mobile.core.ui.generated.resources.Res.drawable.ic_icon_logo_1,
                    ),
                    contentDescription = null,
                )

                Spacer(modifier = Modifier.height(DesignToken.spacing.extraExtraLarge))

                Text(
                    text = stringResource(Res.string.feature_onboarding_permissions_title),
                    style = MifosTypography.headlineMedium,
                    color = KptTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(DesignToken.spacing.medium))

                Text(
                    text = stringResource(Res.string.feature_onboarding_permissions_subtitle),
                    style = MifosTypography.bodySmall,
                    color = KptTheme.colorScheme.secondary,
                )

                Spacer(modifier = Modifier.height(KptTheme.spacing.md))

                Column(
                    verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
                ) {
                    permissionItems.forEach { item ->
                        PermissionCard(item = item)
                    }
                }

                Spacer(modifier = Modifier.height(KptTheme.spacing.md))
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionCard(
    item: PermissionItemData,
    modifier: Modifier = Modifier,
) {
    MifosCard(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(KptTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = AppColors.primaryBlue.copy(alpha = 0.12f),
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = AppColors.primaryBlue,
                    modifier = Modifier.size(24.dp),
                )
            }

            Spacer(modifier = Modifier.width(KptTheme.spacing.md))

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = stringResource(item.titleRes),
                    style = MifosTypography.titleSmallEmphasized,
                    color = KptTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = stringResource(item.descRes),
                    style = MifosTypography.bodySmall,
                    color = KptTheme.colorScheme.secondary,
                )
            }

            if (item.state.status.isGranted) {
                Spacer(modifier = Modifier.width(KptTheme.spacing.xs))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = MifosIcons.CheckCircle1,
                        contentDescription = null,
                        tint = AppColors.customEnable,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(Res.string.feature_onboarding_permission_granted),
                        style = MifosTypography.labelMedium,
                        color = AppColors.customEnable,
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(KptTheme.spacing.xs))
                TextButton(
                    onClick = { item.state.launchPermissionRequest() },
                ) {
                    Text(
                        text = stringResource(Res.string.feature_onboarding_grant),
                        style = MifosTypography.titleSmallEmphasized,
                        color = KptTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun OnboardingPermissionsScreen_Preview() {
    MifosMobileTheme {
        OnboardingPermissionsScreenContent(
            onAction = {},
        )
    }
}
