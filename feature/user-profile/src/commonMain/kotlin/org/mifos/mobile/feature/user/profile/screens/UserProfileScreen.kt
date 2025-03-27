/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.user.profile.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import mifos_mobile.feature.user_profile.generated.resources.Res
import mifos_mobile.feature.user_profile.generated.resources.account_number
import mifos_mobile.feature.user_profile.generated.resources.activation_date
import mifos_mobile.feature.user_profile.generated.resources.change_password
import mifos_mobile.feature.user_profile.generated.resources.client_classification
import mifos_mobile.feature.user_profile.generated.resources.client_type
import mifos_mobile.feature.user_profile.generated.resources.gender
import mifos_mobile.feature.user_profile.generated.resources.groups
import mifos_mobile.feature.user_profile.generated.resources.ic_keyboard_arrow_right_black_24dp
import mifos_mobile.feature.user_profile.generated.resources.internet_not_connected
import mifos_mobile.feature.user_profile.generated.resources.office_name
import mifos_mobile.feature.user_profile.generated.resources.phone_number
import mifos_mobile.feature.user_profile.generated.resources.user_details
import mifos_mobile.feature.user_profile.generated.resources.username
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.client.Group
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.component.MifosUserImage
import org.mifos.mobile.core.ui.component.UserProfileField
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.feature.user.profile.viewmodel.UserDetailAction
import org.mifos.mobile.feature.user.profile.viewmodel.UserDetailEvent
import org.mifos.mobile.feature.user.profile.viewmodel.UserDetailState
import org.mifos.mobile.feature.user.profile.viewmodel.UserDetailViewModel

@Composable
internal fun UserProfileScreen(
    navigateBack: () -> Unit,
    changePassword: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UserDetailViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            UserDetailEvent.ChangePassword -> changePassword.invoke()
            UserDetailEvent.Navigate -> navigateBack.invoke()
            is UserDetailEvent.ShowToast -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    UserProfileScreen(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
        modifier = modifier,
        snackbarHostState = snackbarHostState,
    )
}

@Composable
private fun UserDetailsDialog(
    state: UserDetailState,
    onAction: (UserDetailAction) -> Unit,
) {
    when (state.dialogState) {
        is UserDetailState.DialogState.Error -> {
            MifosErrorComponent(
                isNetworkConnected = state.isOnline,
                isRetryEnabled = true,
                onRetry = { onAction(UserDetailAction.OnRetry) },
                message = stringResource(Res.string.internet_not_connected),
            )
        }
        UserDetailState.DialogState.Loading -> MifosProgressIndicatorOverlay()
        null -> Unit
    }
}

@Composable
private fun UserProfileScreen(
    state: UserDetailState,
    onAction: (UserDetailAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    MifosScaffold(
        backPress = { onAction(UserDetailAction.OnNavigate) },
        topBarTitle = stringResource(Res.string.user_details),
        actions = {
            Icon(
                imageVector = MifosIcons.Edit,
                contentDescription = "Edit User Profile",
                modifier = Modifier.padding(end = 16.dp),
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier,
    ) { paddingValues ->
        state.client?.let {
            UserProfileContent(
                state = state,
                onAction = onAction,
                modifier = Modifier.padding(paddingValues),
            )
        }
    }
    UserDetailsDialog(
        state = state,
        onAction = onAction,
    )
}

@Composable
private fun UserProfileContent(
    state: UserDetailState,
    onAction: (UserDetailAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            MifosUserImage(
                bitmap = state.profileImage,
                modifier = Modifier.size(100.dp),
                username = state.client?.displayName,
            )
        }
        HorizontalDivider()

        state.client?.let {
            it.displayName?.let { displayName ->
                UserProfileField(label = Res.string.username, value = displayName)
            }
            it.accountNo?.let { accountNo ->
                UserProfileField(label = Res.string.account_number, value = accountNo)
            }
            it.activationDate.takeIf { it.isNotEmpty() }?.let { dateList ->
                UserProfileField(
                    label = Res.string.activation_date,
                    value = DateHelper.getDateAsString(dateList),
                )
            }

            it.officeName?.let { officeName ->
                UserProfileField(label = Res.string.office_name, value = officeName)
            }
            it.clientType?.name?.let { clientType ->
                UserProfileField(label = Res.string.client_type, value = clientType)
            }
            it.groups.takeIf { groupList ->
                groupList.isNotEmpty()
            }?.let { groups ->
                UserProfileField(label = Res.string.groups, value = getGroups(groups))
            }
            it.clientClassification?.name?.let { client ->
                UserProfileField(label = Res.string.client_classification, value = client)
            }
            it.mobileNo?.let { mobileNo ->
                UserProfileField(label = Res.string.phone_number, value = mobileNo)
            }
            it.gender?.name?.let { gender ->
                UserProfileField(label = Res.string.gender, value = gender)
            }

            UserProfileField(
                text = Res.string.change_password,
                icon = Res.drawable.ic_keyboard_arrow_right_black_24dp,
                onClick = { onAction(UserDetailAction.OnChangePassword) },
            )

            UserProfileDetails(userDetails = state.client)
        }
    }
}

private fun getGroups(groups: List<Group>?): String {
    return if (groups.isNullOrEmpty()) {
        "Not assigned to any group"
    } else {
        groups.joinToString(separator = " | ") { it.name ?: "Unnamed Group" }
    }
}

@Preview
@Composable
private fun UserProfileScreenPreview() {
    MifosMobileTheme {
        UserProfileScreen(
            state = UserDetailState(dialogState = null),
            onAction = {},
            snackbarHostState = SnackbarHostState(),
        )
    }
}
