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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
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
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.component.MifosTextButton
import org.mifos.mobile.core.model.entity.MifosNotification
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay

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
    MifosScaffold(
        topBarTitle = stringResource(Res.string.notification),
        backPress = navigateBack,
        modifier = modifier,
        content = {
            Box(modifier = Modifier.padding(it)) {
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
                            image = mifos_mobile.feature.notification.generated.resources
                                .Res.drawable.ic_notifications,
                            error = Res.string.no_notification,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        },
    )
}

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
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceDim)
                }
            }
        }
    }
}

@Composable
private fun NotificationItem(
    notification: MifosNotification,
    dismissNotification: (MifosNotification) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isRead = rememberSaveable { mutableStateOf(notification.isRead()) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_notifications),
            contentDescription = "Notifications Icon",
            tint = if (isRead.value) {
                MaterialTheme.colorScheme.onSurfaceVariant
            } else {
                MaterialTheme.colorScheme.primary
            },
        )

        Column(
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = notification.msg ?: "",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                modifier = Modifier.alpha(0.7f),
                text = DateHelper.getDateAsStringFromLong(notification.timeStamp),
                style = MaterialTheme.typography.labelMedium,
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
