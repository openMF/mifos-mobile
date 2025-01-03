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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.common.utils.DateHelper
import org.mifos.mobile.core.designsystem.components.MifosScaffold
import org.mifos.mobile.core.designsystem.components.MifosTextButton
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.MifosNotification
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.DevicePreviews

@Composable
internal fun NotificationScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotificationViewModel = hiltViewModel(),
) {
    val uiState by viewModel.notificationUiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    NotificationScreen(
        uiState = uiState,
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
    navigateBack: () -> Unit,
    onRetry: () -> Unit,
    dismissNotification: (MifosNotification) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    MifosScaffold(
        topBarTitleResId = R.string.notification,
        navigateBack = navigateBack,
        modifier = modifier,
        content = {
            Box(modifier = Modifier.padding(it)) {
                when (uiState) {
                    is NotificationUiState.Loading -> MifosProgressIndicatorOverlay()

                    is NotificationUiState.Error -> {
                        MifosErrorComponent(
                            message = uiState.errorMessage,
                            isNetworkConnected = Network.isConnected(context),
                            isRetryEnabled = true,
                            onRetry = onRetry,
                        )
                    }

                    is NotificationUiState.Success -> {
                        if (uiState.notifications.isEmpty()) {
                            EmptyDataView(
                                icon = R.drawable.ic_notifications,
                                error = R.string.no_notification,
                                modifier = Modifier.fillMaxSize(),
                            )
                        } else {
                            NotificationContent(
                                isRefreshing = isRefreshing,
                                notifications = uiState.notifications,
                                dismissNotification = dismissNotification,
                                onRefresh = onRefresh,
                            )
                        }
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
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_notifications),
            contentDescription = null,
            tint = if (isRead.value) {
                MaterialTheme.colorScheme.onSurfaceVariant
            } else {
                MaterialTheme.colorScheme.primary
            },
        )
        Spacer(modifier = Modifier.width(8.dp))
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
                text = DateHelper.getDateAndTimeAsStringFromLong(notification.timeStamp),
                style = MaterialTheme.typography.labelMedium,
            )
            if (!isRead.value) {
                MifosTextButton(
                    text = stringResource(id = R.string.dialog_action_ok),
                    onClick = {
                        isRead.value = true
                        dismissNotification(notification)
                    },
                )
            }
        }
    }
}

internal class NotificationUiStatePreviews : PreviewParameterProvider<NotificationUiState> {
    override val values: Sequence<NotificationUiState>
        get() = sequenceOf(
            NotificationUiState.Success(
                notifications = listOf(
                    MifosNotification(
                        timeStamp = 13231331L,
                        msg = "Your payment is successful",
                        read = false,
                    ),
                    MifosNotification(
                        timeStamp = 13231331L,
                        msg = "Your payment is successful",
                        read = true,
                    ),
                ),
            ),
            NotificationUiState.Error(errorMessage = ""),
            NotificationUiState.Loading,
        )
}

@DevicePreviews
@Composable
private fun NotificationScreenPreview(
    @PreviewParameter(NotificationUiStatePreviews::class)
    notificationUiState: NotificationUiState,
) {
    MifosMobileTheme {
        NotificationScreen(
            navigateBack = {},
            uiState = notificationUiState,
            onRetry = {},
            dismissNotification = {},
            onRefresh = {},
            isRefreshing = false,
        )
    }
}
