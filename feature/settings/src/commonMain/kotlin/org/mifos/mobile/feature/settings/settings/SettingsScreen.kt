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
import mifos_mobile.feature.settings.generated.resources.feature_settings_logout_action
import mifos_mobile.feature.settings.generated.resources.feature_settings_logout_message
import mifos_mobile.feature.settings.generated.resources.feature_settings_top_bar_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosActionCard
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.MifosUserImage
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.feature.settings.componenets.LogoutDialogState
import org.mifos.mobile.feature.settings.componenets.MifosLogoutDialog
import org.mifos.mobile.feature.settings.componenets.SettingsItems

/**
 * A stateful composable that serves as the entry point for the main "Settings" screen.
 *
 * It connects to the [SettingsViewModel] to observe state and events, and orchestrates
 * the display of the UI content and dialogs. It handles navigation events for backing
 * out of the screen or navigating to other specific settings pages.
 *
 * @param navigateBack A lambda function to handle back navigation events.
 * @param navigateToScreen A lambda function to handle navigation to a specific [SettingsItems] destination.
 * @param viewModel The ViewModel responsible for the screen's logic and state.
 */
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
                navigateToScreen.invoke(events.item)
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

/**
 * A composable responsible for displaying dialogs based on the current [SettingsState].
 * It currently handles the display of the logout confirmation dialog.
 *
 * @param state The current state of the settings screen, used to determine which dialog to show.
 * @param onAction A callback to send actions to the ViewModel, such as logout confirmation or dismissal.
 */
@Composable
private fun SettingsDialog(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit,
) {
    when (state.dialogState) {
        is SettingsState.DialogState.Logout -> {
            MifosLogoutDialog(
                visibilityState = LogoutDialogState.Shown(
                    description = state.dialogState.message,
                    title = state.dialogState.title,
                    message = Res.string.feature_settings_logout_message,
                    messageActionText = Res.string.feature_settings_logout_action,
                    onLogout = { onAction(SettingsAction.Logout) },
                    onNavigateToHome = { onAction(SettingsAction.OnNavigateBack) },
                    onDismiss = { onAction(SettingsAction.DismissDialog) },
                ),
            )
        }

        null -> Unit
    }
}

/**
 * A stateless composable that renders the main UI for the "Settings" screen.
 *
 * It includes the scaffold, top bar, and conditionally displays content based on the
 * [ScreenUiState] (e.g., success, loading, error).
 *
 * @param state The current state of the settings screen.
 * @param onAction A callback to send actions to the ViewModel.
 * @param modifier The [Modifier] to be applied to the layout.
 */
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
        when (state.uiState) {
            ScreenUiState.Success -> {
                Column(
                    modifier = Modifier
                        .padding(vertical = DesignToken.padding.large)
                        .verticalScroll(rememberScrollState()),

                ) {
                    when {
                        state.isUserLoading -> {
                            MifosProgressIndicator()
                        }

                        state.isUserLoaded -> {
                            SettingsProfileCard(state = state)
                        }

                        else -> {
                            MifosErrorComponent(
                                isRetryEnabled = true,
                                message = "Failed to load user. Please try again.",
                                onRetry = { onAction(SettingsAction.Retry) },
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
                            if (it.route == Constants.LOGOUT) {
                                onAction(SettingsAction.LogoutDialog)
                            } else {
                                onAction(SettingsAction.NavigateTo(it))
                            }
                        }
                    }
                }
            }

            else -> { }
        }
    }
}

/**
 * A composable that displays the user's profile information card at the top of the settings screen.
 * It includes the user's profile picture, name, and account number.
 *
 * @param state The current state containing the client's profile data.
 * @param modifier The [Modifier] to be applied to the card layout.
 */
@Composable
internal fun SettingsProfileCard(
    state: SettingsState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(DesignToken.padding.extraLarge),
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

/**
 * A composable that renders a list of actionable settings items.
 * Each item is displayed as a [MifosActionCard] and triggers a callback when clicked.
 *
 * @param items An immutable list of [SettingsItems] to be displayed.
 * @param onActionClick A lambda function invoked with the [SettingsItems] that was clicked.
 */
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
