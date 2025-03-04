/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.help

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.help.generated.resources.Res
import mifos_mobile.feature.help.generated.resources.call_now
import mifos_mobile.feature.help.generated.resources.faq
import mifos_mobile.feature.help.generated.resources.find_locations
import mifos_mobile.feature.help.generated.resources.help
import mifos_mobile.feature.help.generated.resources.leave_email
import mifos_mobile.feature.help.generated.resources.no_questions_found
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.component.MifosTopBarTitleComposable
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.model.entity.FAQ
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.FaqItemHolder
import org.mifos.mobile.core.ui.component.MifosTextButtonWithTopDrawable
import org.mifos.mobile.core.ui.component.MifosTitleSearchCard
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun HelpScreen(
    callNow: () -> Unit,
    leaveEmail: () -> Unit,
    findLocations: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HelpViewModel = koinViewModel(),
) {
    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) {
            event ->
        when (event) {
            HelpEvent.CallHelpLine -> callNow()
            HelpEvent.Location -> findLocations()
            HelpEvent.MailHelpLine -> leaveEmail()
        }
    }

    HelpScreenContent(
        uiState = uiState,
        navigateBack = navigateBack,
        onAction = { action ->
            when (action) {
                is HelpScreenAction.SearchFaq -> viewModel.trySendAction(HelpAction.SearchFaq(action.query))
                HelpScreenAction.DismissSearch -> viewModel.trySendAction(HelpAction.LoadFaq)
                is HelpScreenAction.UpdateFaqPosition ->
                    viewModel.trySendAction(HelpAction.UpdateFaqPosition(action.position))
                HelpScreenAction.CallHelpLine -> viewModel.trySendAction(HelpAction.OnCallHelpLine)
                HelpScreenAction.MailHelpLine -> viewModel.trySendAction(HelpAction.OnMailHelpLine)
                HelpScreenAction.FindLocations -> viewModel.trySendAction(HelpAction.Location)
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun HelpScreenContent(
    uiState: HelpUiState,
    navigateBack: () -> Unit,
    onAction: (HelpScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosScaffold(
        topBar = {
            MifosTopBarTitleComposable(
                navigateBack = navigateBack,
                title = {
                    MifosTitleSearchCard(
                        searchQuery = { query -> onAction(HelpScreenAction.SearchFaq(query)) },
                        titleResourceId = Res.string.help,
                        onSearchDismiss = { onAction(HelpScreenAction.DismissSearch) },
                    )
                },
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                if (uiState.faqList.isNotEmpty()) {
                    HelpContent(
                        faqArrayList = uiState.faqList,
                        selectedFaqPosition = uiState.selectedFaqPosition,
                        onAction = onAction,
                    )
                }
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun HelpContent(
    faqArrayList: List<FAQ>,
    selectedFaqPosition: Int,
    onAction: (HelpScreenAction) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(Res.string.faq),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        if (faqArrayList.isNotEmpty()) {
            LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth()) {
                itemsIndexed(items = faqArrayList) { index, faqItem ->
                    FaqItemHolder(
                        index = index,
                        isSelected = selectedFaqPosition == index,
                        onItemSelected = { onAction(HelpScreenAction.UpdateFaqPosition(it)) },
                        question = faqItem.question,
                        answer = faqItem.answer,
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                MifosTextButtonWithTopDrawable(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    onClick = { onAction(HelpScreenAction.CallHelpLine) },
                    textResourceId = Res.string.call_now,
                    icon = MifosIcons.Phone,
                    contentDescription = "Phone Icon",
                )
                MifosTextButtonWithTopDrawable(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    onClick = { onAction(HelpScreenAction.MailHelpLine) },
                    textResourceId = Res.string.leave_email,
                    icon = MifosIcons.Mail,
                    contentDescription = "Mail Icon",
                )
                MifosTextButtonWithTopDrawable(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    onClick = { onAction(HelpScreenAction.FindLocations) },
                    textResourceId = Res.string.find_locations,
                    icon = MifosIcons.LocationOn,
                    contentDescription = "Location Icon",
                )
            }
        } else {
            EmptyDataView(
                modifier = Modifier.fillMaxSize(),
                error = Res.string.no_questions_found,
            )
        }
    }
}

sealed interface HelpScreenAction {
    data class SearchFaq(val query: String) : HelpScreenAction
    data object DismissSearch : HelpScreenAction
    data class UpdateFaqPosition(val position: Int) : HelpScreenAction
    data object CallHelpLine : HelpScreenAction
    data object MailHelpLine : HelpScreenAction
    data object FindLocations : HelpScreenAction
}
