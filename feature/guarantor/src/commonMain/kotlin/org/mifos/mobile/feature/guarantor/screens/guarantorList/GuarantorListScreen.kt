/*
 * Copyright 2026 Mifos Initiative
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
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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
import template.core.base.designsystem.theme.KptTheme

/**
 * Main composable function for "Guarantor List" screen.
 *
 * This screen displays a list of all guarantors for a specific loan,
 * with options to add new guarantors and view details of existing ones.
 * It handles loading, error, and empty states appropriately.
 *
 * @param navigateBack A lambda function to handle back navigation event.
 * @param addGuarantor A lambda to navigate to add guarantor screen with loan ID.
 * @param onGuarantorClicked A lambda to navigate to guarantor details with index and loan ID.
 * @param modifier Modifier for styling and positioning screen.
 * @param viewModel The ViewModel that manages screen's state and business logic.
 *   Provided by Koin dependency injection.
 */
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

/**
 * Private composable function for "Guarantor List" screen content.
 *
 * This composable renders the main UI structure including scaffold,
 * floating action button for adding guarantors, and content based on UI state.
 * It displays loading indicators, error messages, or the list of guarantors.
 *
 * @param state The current UI state containing all necessary data for rendering.
 * @param onAction Callback function to handle user actions and UI events.
 * @param modifier Modifier for styling and positioning screen.
 */
@Composable
private fun GuarantorListScreen(
    state: GuarantorListState,
    modifier: Modifier = Modifier,
    onAction: (GuarantorListAction) -> Unit,
) {
    MifosScaffold(
        topBarTitle = stringResource(Res.string.view_guarantor),
        onNavigationIconClick = { (onAction(GuarantorListAction.OnNavigateBackClick)) },
        modifier = modifier,
        floatingActionButtonContent = FloatingActionButtonContent(
            onClick = { onAction(GuarantorListAction.OnAddGuarantor) },
            content = {
                Icon(
                    imageVector = MifosIcons.Add,
                    contentDescription = null,
                )
            },
            contentColor = KptTheme.colorScheme.primary,
        ),
        content = {
            if (state.guarantorList == null) {
                MifosProgressIndicator()
            } else if (state.guarantorList.isEmpty()) {
                MifosErrorComponent(isEmptyData = true)
            } else {
                GuarantorList(
                    modifier = Modifier,
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

/**
 * Composable function for displaying the list of guarantors.
 *
 * This composable renders a lazy column of guarantor items,
 * each showing basic guarantor information with click handlers.
 *
 * @param guarantorList List of guarantor payloads to display.
 * @param onAction Callback function to handle user actions (clicking on guarantors).
 * @param modifier Modifier for styling and positioning the list.
 */
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

/**
 * Composable function for displaying a single guarantor item.
 *
 * This composable renders a card showing guarantor's name and type,
 * with click functionality to view details.
 *
 * @param onGuarantorClicked A lambda function to handle when the guarantor item is clicked.
 * @param modifier Modifier for styling and positioning the item.
 * @param guarantor The guarantor data to display.
 */
@Composable
private fun GuarantorListItem(
    onGuarantorClicked: () -> Unit,
    modifier: Modifier = Modifier,
    guarantor: GuarantorPayload = GuarantorPayload(),
) {
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(containerColor = KptTheme.colorScheme.background),
        modifier = modifier
            .padding(horizontal = KptTheme.spacing.md, vertical = KptTheme.spacing.sm)
            .fillMaxWidth(),
        onClick = { onGuarantorClicked.invoke() },
        content = {
            Column(modifier = Modifier.padding(KptTheme.spacing.sm)) {
                Text(
                    text = guarantor.firstname + " " + guarantor.lastname,
                    style = KptTheme.typography.bodyMedium,
                )
                Text(
                    text = guarantor.guarantorType?.value ?: "",
                    style = KptTheme.typography.labelMedium,
                )
            }
        },
    )
}

/**
 * Dialog composable for showing dialogs on "Guarantor List" screen.
 *
 * This composable handles display of loading overlays and error messages
 * based on current dialog state in [GuarantorListState].
 *
 * @param dialogState The current state containing dialog information.
 * @param state The current UI state containing network status.
 * @param onDismissRequest Callback function to handle dialog dismissal.
 */
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
