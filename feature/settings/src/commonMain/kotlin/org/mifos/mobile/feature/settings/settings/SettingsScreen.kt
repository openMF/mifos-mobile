/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.settings

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import mifos_mobile.feature.settings.generated.resources.Res
import mifos_mobile.feature.settings.generated.resources.feature_settings_customer_account_no
import mifos_mobile.feature.settings.generated.resources.feature_settings_top_bar_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosActionCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.MifosUserImage
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.feature.settings.componenets.SettingsItems

@Composable
internal fun SettingsScreen(
    navigateBack: () -> Unit,
    navigateToScreen: (SettingsItems) -> Unit,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { events ->
        when (events) {
            SettingsEvents.NavigateBack -> navigateBack.invoke()
            is SettingsEvents.NavigateTo -> {
                // Using inside of if condition to resolve crash for other screens
                if (
                    events.item == SettingsItems.Help ||
                    events.item == SettingsItems.AboutUs ||
                    events.item == SettingsItems.AppInfo
                ) {
                    navigateToScreen.invoke(events.item)
                }
            }
        }
    }

    SettingsScreenContent(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    SettingsDialog(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
private fun SettingsDialog(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit,
) {
    when (state.dialogState) {
        is SettingsState.DialogState.Error -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    message = stringResource(state.dialogState.message),
                ),
                onDismissRequest = { onAction(SettingsAction.DismissDialog) },
            )
        }
        SettingsState.DialogState.Loading -> MifosProgressIndicator()
        null -> Unit
    }
}

@Composable
internal fun SettingsScreenContent(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        modifier = modifier,
        onNavigateBack = { onAction(SettingsAction.OnNavigateBack) },
        topBarTitle = stringResource(Res.string.feature_settings_top_bar_title),
    ) {
        if (state.dialogState == null) {
            Column(
                modifier = Modifier
                    .padding(vertical = DesignToken.padding.large)
                    .verticalScroll(rememberScrollState()),

            ) {
                if (state.client != null) {
                    Column(
                        modifier = Modifier.padding(DesignToken.padding.extraLarge),
                    ) {
                        SettingsProfileCard(
                            state = state,
                        )
                    }
                }
                Column {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(0.99997.dp),
                    )
                    SettingsActions(state.settingsItems) {
                        onAction(SettingsAction.NavigateTo(it))
                    }
                }
            }
        }
    }
}

@Composable
internal fun SettingsProfileCard(
    state: SettingsState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(DesignToken.sizes.profile)
                .clip(CircleShape)
                .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape),
        ) {
            MifosUserImage(
                bitmap = state.profileImage,
                modifier = Modifier.size(100.dp),
                username = state.client?.displayName,
            )
        }

        Spacer(modifier = Modifier.width(DesignToken.spacing.large))

        Column(
            verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.extraSmall),
        ) {
            Text(
                text = state.client?.displayName ?: "",
                style = MifosTypography.labelMediumEmphasized,
                color = MaterialTheme.colorScheme.primary,
            )

//            TODO email is not receiving from api response
//            Text(text = state.client.email, style = AppTypography.subtitleMedium)

            Text(
                text = stringResource(Res.string.feature_settings_customer_account_no, state.client?.accountNo ?: ""),
                style = MifosTypography.labelMediumEmphasized,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

@Composable
internal fun SettingsActions(
    items: ImmutableList<SettingsItems>,
    onActionClick: (SettingsItems) -> Unit,
) {
    Column {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
        ) {
            items.forEach { item ->
                MifosActionCard(
                    modifier = Modifier.padding(horizontal = DesignToken.padding.large),
                    title = item.title,
                    subTitle = item.subTitle,
                    icon = item.icon,
                    onClick = {
                        onActionClick(item)
                    },
                )
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.99997.dp),
                )
            }
        }
    }
}
