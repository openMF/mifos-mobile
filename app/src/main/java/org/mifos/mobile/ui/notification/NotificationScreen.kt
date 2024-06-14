package org.mifos.mobile.ui.notification

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.checkerframework.checker.units.qual.UnitsMultiple
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MFScaffold
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.models.notification.MifosNotification
import org.mifos.mobile.utils.DateHelper
import org.mifos.mobile.utils.Network

@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
) {
    val uiState by viewModel.notificationUiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    NotificationScreen(
        uiState = uiState,
        navigateBack = navigateBack,
        onRetry = { viewModel.loadNotifications() },
        dismissNotification = { viewModel.dismissNotification(it) },
        onRefresh = { viewModel.refreshNotifications() },
        isRefreshing = isRefreshing
    )
}

@Composable
fun NotificationScreen(
    uiState: NotificationUiState,
    navigateBack: () -> Unit,
    onRetry: () -> Unit,
    dismissNotification: (MifosNotification) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    val context = LocalContext.current
    MFScaffold(
        topBarTitleResId = R.string.notification,
        navigateBack = navigateBack,
        scaffoldContent = {
            Box(modifier = Modifier.padding(it)) {
                when (uiState) {
                    is NotificationUiState.Loading -> {
                        MifosProgressIndicatorOverlay()
                    }

                    is NotificationUiState.Error -> {
                        MifosErrorComponent(
                            message = uiState.errorMessage,
                            isNetworkConnected = Network.isConnected(context),
                            isRetryEnabled = true,
                            onRetry = onRetry
                        )
                    }

                    is NotificationUiState.Success -> {
                        if (uiState.notifications.isEmpty()) {
                            EmptyDataView(
                                icon = R.drawable.ic_notifications,
                                error = R.string.no_notification,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            NotificationContent(
                                notifications = uiState.notifications,
                                dismissNotification = dismissNotification,
                                onRefresh = onRefresh,
                                isRefreshing = isRefreshing
                            )
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationContent(
    notifications: List<MifosNotification>,
    dismissNotification: (MifosNotification) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    val pullRefreshState = rememberPullToRefreshState()

    if (pullRefreshState.isRefreshing) {
        LaunchedEffect(key1 = true) {
            onRefresh()
        }
    }

    LaunchedEffect(key1 = isRefreshing) {
        if (isRefreshing)
            pullRefreshState.startRefresh()
        else
            pullRefreshState.endRefresh()
    }

    Box(modifier = Modifier.fillMaxSize().nestedScroll(pullRefreshState.nestedScrollConnection)) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(items = notifications) { index, notification ->
                NotificationItem(
                    notification = notification,
                    dismissNotification = dismissNotification
                )
                if(index < notifications.lastIndex) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceDim)
                }
            }
        }
        PullToRefreshContainer(
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}

@Composable
fun NotificationItem(
    notification: MifosNotification,
    dismissNotification: (MifosNotification) -> Unit
) {
    var isRead = rememberSaveable { mutableStateOf(notification.isRead()) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_notifications),
            contentDescription = null,
            tint = if(isRead.value) MaterialTheme.colorScheme.onSurfaceVariant
            else MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = notification.msg ?: "",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                modifier = Modifier.alpha(0.7f),
                text = DateHelper.getDateAndTimeAsStringFromLong(notification.timeStamp),
                style = MaterialTheme.typography.labelMedium,
            )
            if(!isRead.value) {
                TextButton(
                    onClick = {
                        isRead.value = true
                        dismissNotification(notification)
                    }
                ) {
                    Text(text = stringResource(id = R.string.dialog_action_ok))
                }
            }
        }
    }
}

class NotificationUiStatePreviews : PreviewParameterProvider<NotificationUiState> {
    override val values: Sequence<NotificationUiState>
        get() = sequenceOf(
            NotificationUiState.Success(notifications = listOf(MifosNotification(), MifosNotification())),
            NotificationUiState.Error(errorMessage = ""),
            NotificationUiState.Loading,
        )
}


@Preview(showSystemUi = true)
@Composable
fun NotificationScreenPreview(
    @PreviewParameter(NotificationUiStatePreviews::class) notificationUiState: NotificationUiState
) {
    MifosMobileTheme {
        NotificationScreen(
            navigateBack = {},
            uiState = notificationUiState,
            onRetry = {},
            dismissNotification = {},
            onRefresh = {},
            isRefreshing = false
        )
    }
}