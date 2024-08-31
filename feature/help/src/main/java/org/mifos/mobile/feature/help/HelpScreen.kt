/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.help

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.designsystem.components.MifosScaffold
import org.mifos.mobile.core.designsystem.components.MifosTopBar
import org.mifos.mobile.core.designsystem.icons.MifosIcons
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.FAQ
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.FaqItemHolder
import org.mifos.mobile.core.ui.component.MifosTextButtonWithTopDrawable
import org.mifos.mobile.core.ui.component.MifosTitleSearchCard
import org.mifos.mobile.core.ui.utils.DevicePreviews

@Composable
internal fun HelpScreen(
    callNow: () -> Unit,
    leaveEmail: () -> Unit,
    findLocations: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HelpViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.helpUiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.loadFaq(
            context.resources?.getStringArray(R.array.faq_qs),
            context.resources?.getStringArray(R.array.faq_ans),
        )
    }

    HelpScreen(
        uiState = uiState,
        callNow = callNow,
        leaveEmail = leaveEmail,
        findLocations = findLocations,
        navigateBack = navigateBack,
        searchQuery = viewModel::filterList,
        modifier = modifier,
        onSearchDismiss = { viewModel.loadFaq(qs = null, ans = null) },
        updateFaqPosition = viewModel::updateSelectedFaqPosition,
    )
}

@Composable
private fun HelpScreen(
    uiState: HelpUiState,
    callNow: () -> Unit,
    leaveEmail: () -> Unit,
    findLocations: () -> Unit,
    navigateBack: () -> Unit,
    searchQuery: (String) -> Unit,
    onSearchDismiss: () -> Unit,
    updateFaqPosition: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosScaffold(
        topBar = {
            MifosTopBar(
                navigateBack = navigateBack,
                title = {
                    MifosTitleSearchCard(
                        searchQuery = searchQuery,
                        titleResourceId = R.string.help,
                        onSearchDismiss = onSearchDismiss,
                    )
                },
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                when (uiState) {
                    is HelpUiState.Initial -> Unit
                    is HelpUiState.ShowFaq -> {
                        HelpContent(
                            faqArrayList = uiState.faqArrayList.toList().filterNotNull(),
                            selectedFaqPosition = uiState.selectedFaqPosition,
                            callNow = callNow,
                            leaveEmail = leaveEmail,
                            findLocations = findLocations,
                            updateFaqPosition = updateFaqPosition,
                        )
                    }
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
    callNow: () -> Unit,
    leaveEmail: () -> Unit,
    findLocations: () -> Unit,
    updateFaqPosition: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Text(
            text = stringResource(id = R.string.faq),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 8.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        if (faqArrayList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                itemsIndexed(items = faqArrayList) { index, faqItem ->
                    FaqItemHolder(
                        index = index,
                        isSelected = selectedFaqPosition == index,
                        onItemSelected = { updateFaqPosition(it) },
                        question = faqItem.question,
                        answer = faqItem.answer,
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
            ) {
                MifosTextButtonWithTopDrawable(
                    modifier = Modifier
                        .weight(1f),
                    onClick = callNow,
                    textResourceId = R.string.call_now,
                    icon = MifosIcons.Phone,
                    contentDescription = "Phone Icon",
                )
                MifosTextButtonWithTopDrawable(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onClick = leaveEmail,
                    textResourceId = R.string.leave_email,
                    icon = MifosIcons.Mail,
                    contentDescription = "Mail Icon",
                )
                MifosTextButtonWithTopDrawable(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onClick = findLocations,
                    textResourceId = R.string.find_locations,
                    icon = MifosIcons.LocationOn,
                    contentDescription = "Location Icon",
                )
            }
        } else {
            EmptyDataView(
                modifier = Modifier.fillMaxSize(),
                error = R.string.no_questions_found,
            )
        }
    }
}

internal class HelpScreenPreviewProvider : PreviewParameterProvider<HelpUiState> {
    override val values: Sequence<HelpUiState>
        get() = sequenceOf(
            HelpUiState.Initial,
            HelpUiState.ShowFaq(arrayListOf()),
        )
}

@DevicePreviews
@Composable
private fun HelpScreenPreview(
    @PreviewParameter(HelpScreenPreviewProvider::class)
    helpUiState: HelpUiState,
) {
    MifosMobileTheme {
        HelpScreen(
            uiState = helpUiState,
            callNow = { },
            leaveEmail = { },
            findLocations = {},
            updateFaqPosition = { _ -> },
            navigateBack = {},
            searchQuery = { _ -> },
            onSearchDismiss = { },
        )
    }
}
