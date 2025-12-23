/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.notification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.notification.generated.resources.Res
import mifos_mobile.feature.notification.generated.resources.dialog_action_ok
import mifos_mobile.feature.notification.generated.resources.ic_notifications
import mifos_mobile.feature.notification.generated.resources.no_notification
import mifos_mobile.feature.notification.generated.resources.notification
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosTextButton
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.model.entity.MifosNotification
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import template.core.base.designsystem.theme.KptTheme

/**
 * This is the main entry point for the Notification Screen feature. It's a composable function
 * that sets up the screen, observes state from the [NotificationViewModel], and delegates the UI
 * rendering to other composable functions.
 *
 * @param navigateBack A lambda function to be invoked when the user wants to navigate back from
 *   this screen.
 * @param modifier The [Modifier] to be applied to this composable.
 * @param viewModel An instance of [NotificationViewModel] which holds the business logic for this
 *   screen. It's provided by Koin's `koinViewModel()`.
 */
@Composable
internal fun NotificationScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotificationViewModel = koinViewModel(),
) {
    val uiState by viewModel.notificationUiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsStateWithLifecycle()

    NotificationScreen(
        uiState = uiState,
        isNetworkAvailable = isNetworkAvailable,
        navigateBack = navigateBack,
        onRetry = viewModel::loadNotifications,
        dismissNotification = viewModel::dismissNotification,
        onRefresh = viewModel::refreshNotifications,
        isRefreshing = isRefreshing,
        modifier = modifier,
    )
}

/**
 * This composable function is responsible for displaying the main content of the Notification
 * Screen. It acts as a presentation layer that reacts to different UI states like Loading, Error,
 * Success, or Empty. Based on the `uiState`, it renders the appropriate composable.
 *
 * @param uiState The current state of the UI, which determines what to display.
 * @param isNetworkAvailable A boolean that indicates whether the device has an active network
 *   connection.
 * @param navigateBack A lambda function to handle the back navigation event.
 * @param onRetry A lambda function to be called when the user wants to retry loading notifications
 *   after an error.
 * @param dismissNotification A lambda function to handle the dismissal of a notification.
 * @param isRefreshing A boolean that indicates if the screen is currently in a refresh state.
 * @param onRefresh A lambda function to be called to refresh the list of notifications.
 * @param modifier The [Modifier] to be applied to this composable.
 */
@Composable
private fun NotificationScreen(
    uiState: NotificationUiState,
    isNetworkAvailable: Boolean,
    navigateBack: () -> Unit,
    onRetry: () -> Unit,
    dismissNotification: (MifosNotification) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        topBarTitle = stringResource(Res.string.notification),
        onNavigateBack = navigateBack,
        modifier = modifier,
        content = {
            Box(modifier = Modifier) {
                when (uiState) {
                    is NotificationUiState.Loading -> MifosProgressIndicatorOverlay()

                    is NotificationUiState.Error -> {
                        MifosErrorComponent(
                            message = uiState.errorMessage,
                            isNetworkConnected = isNetworkAvailable,
                            isRetryEnabled = true,
                            onRetry = onRetry,
                        )
                    }

                    is NotificationUiState.Success -> {
                        NotificationContent(
                            isRefreshing = isRefreshing,
                            notifications = uiState.notifications,
                            dismissNotification = dismissNotification,
                            onRefresh = onRefresh,
                        )
                    }

                    is NotificationUiState.Empty -> {
                        EmptyDataView(
                            icon = MifosIcons.Notification,
                            error = Res.string.no_notification,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        },
    )
}

/**
 * This composable function is responsible for displaying the list of notifications. It uses a
 * [PullToRefreshBox], allowing the user to swipe down to refresh the list. The actual list is
 * rendered using a `LazyColumn` for efficient display of potentially long lists.
 *
 * @param isRefreshing A boolean indicating if the pull-to-refresh action is currently active.
 * @param notifications The list of [MifosNotification] objects to be displayed.
 * @param dismissNotification A lambda function that handles the action of dismissing a
 *   notification.
 * @param onRefresh A lambda function that's triggered when the user performs a pull-to-refresh
 *   gesture.
 * @param modifier The [Modifier] to be applied to this composable.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationContent(
    isRefreshing: Boolean,
    notifications: List<MifosNotification>,
    dismissNotification: (MifosNotification) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pullRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        state = pullRefreshState,
        onRefresh = onRefresh,
        isRefreshing = isRefreshing,
        modifier = modifier,
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(items = notifications) { index, notification ->
                NotificationItem(
                    notification = notification,
                    dismissNotification = dismissNotification,
                )
                if (index < notifications.lastIndex) {
                    HorizontalDivider(color = KptTheme.colorScheme.surfaceDim)
                }
            }
        }
    }
}

/**
 * This composable function is designed to display a single notification item in the list. It shows
 * the notification's message, the timestamp, and provides an "OK" button to dismiss it if it's
 * unread. The visual style of the item changes based on whether the notification has been read.
 *
 * @param notification The [MifosNotification] object to be displayed.
 * @param dismissNotification A lambda function that's called when the user dismisses the
 *   notification.
 * @param modifier The [Modifier] to be applied to this composable.
 */
@Composable
private fun NotificationItem(
    notification: MifosNotification,
    dismissNotification: (MifosNotification) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isRead = rememberSaveable { mutableStateOf(notification.isRead()) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(KptTheme.spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.md),
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_notifications),
            contentDescription = "Notifications Icon",
            tint = if (isRead.value) {
                KptTheme.colorScheme.onSurfaceVariant
            } else {
                KptTheme.colorScheme.primary
            },
        )
        Column(
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = notification.msg ?: "",
                style = KptTheme.typography.bodyMedium,
            )
            Text(
                modifier = Modifier.alpha(0.7f),
                text = DateHelper.getDateAsStringFromLong(notification.timeStamp),
                style = KptTheme.typography.labelMedium,
            )
            if (!isRead.value) {
                MifosTextButton(
                    content = {
                        Text(stringResource(Res.string.dialog_action_ok))
                    },
                    onClick = {
                        isRead.value = true
                        dismissNotification(notification)
                    },
                )
            }
        }
    }
}
