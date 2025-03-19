/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.guarantor.screens.guarantorList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import mifos_mobile.feature.guarantor.generated.resources.Res
import mifos_mobile.feature.guarantor.generated.resources.view_guarantor
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.FloatingActionButtonContent
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.model.entity.guarantor.GuarantorPayload
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun GuarantorListScreen(
    navigateBack: () -> Unit,
    addGuarantor: (Long) -> Unit,
    onGuarantorClicked: (Int, Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GuarantorListViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is GuarantorListEvent.AddGuarantor -> addGuarantor(event.value)

            is GuarantorListEvent.GuarantorClicked -> {
                onGuarantorClicked.invoke(event.index, event.loanId)
            }

            GuarantorListEvent.NavigateBack -> navigateBack.invoke()

            is GuarantorListEvent.ShowToast -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    GuarantorListScreen(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
        modifier = modifier,
    )
}

@Composable
private fun GuarantorListScreen(
    state: GuarantorListState,
    modifier: Modifier = Modifier,
    onAction: (GuarantorListAction) -> Unit,
) {
    MifosScaffold(
        topBarTitle = stringResource(Res.string.view_guarantor),
        backPress = { (onAction(GuarantorListAction.OnNavigateBackClick)) },
        modifier = modifier,
        floatingActionButtonContent = FloatingActionButtonContent(
            onClick = { onAction(GuarantorListAction.OnAddGuarantor) },
            content = {
                Icon(
                    imageVector = MifosIcons.Add,
                    contentDescription = null,
                )
            },
            contentColor = MaterialTheme.colorScheme.primary,
        ),
        content = {
            if (state.guarantorList == null) {
                MifosProgressIndicator()
            } else if (state.guarantorList.isEmpty()) {
                MifosErrorComponent(isEmptyData = true)
            } else {
                GuarantorList(
                    modifier = Modifier.padding(it),
                    guarantorList = state.guarantorList,
                    onAction = onAction,
                )
            }
        },
    )
    GuarantorListDialog(
        dialogState = state.dialogState,
        state = state,
        onDismissRequest = { onAction.invoke(GuarantorListAction.DismissDialog) },
    )
}

@Composable
private fun GuarantorList(
    guarantorList: List<GuarantorPayload?>,
    onAction: (GuarantorListAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier) {
        itemsIndexed(items = guarantorList) { index, guarantor ->
            guarantor?.let {
                GuarantorListItem(
                    guarantor = it,
                    onGuarantorClicked = { onAction(GuarantorListAction.OnGuarantorClicked(index)) },
                )
            }
        }
    }
}

@Composable
private fun GuarantorListItem(
    onGuarantorClicked: () -> Unit,
    modifier: Modifier = Modifier,
    guarantor: GuarantorPayload = GuarantorPayload(),
) {
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.background),
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        onClick = { onGuarantorClicked.invoke() },
        content = {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = guarantor.firstname + " " + guarantor.lastname,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = guarantor.guarantorType?.value ?: "",
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        },
    )
}

@Composable
private fun GuarantorListDialog(
    dialogState: GuarantorListState.DialogState?,
    state: GuarantorListState,
    onDismissRequest: () -> Unit = {},
) {
    when (dialogState) {
        GuarantorListState.DialogState.Loading -> MifosErrorComponent(isNetworkConnected = state.isOnline)

        is GuarantorListState.DialogState.ShowToast -> MifosBasicDialog(
            visibilityState = BasicDialogState.Shown(
                message = dialogState.message,
            ),
            onDismissRequest = onDismissRequest,
        )

        null -> Unit
    }
}
