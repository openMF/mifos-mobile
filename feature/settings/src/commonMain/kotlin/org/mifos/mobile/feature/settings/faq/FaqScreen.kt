/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.faq

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.settings.generated.resources.Res
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_faq
import mifos_mobile.feature.settings.generated.resources.feature_settings_faq_contact_us
import mifos_mobile.feature.settings.generated.resources.feature_settings_faq_doubt
import mifos_mobile.feature.settings.generated.resources.no_questions_found
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.model.entity.FAQ
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.FaqItemHolder
import org.mifos.mobile.core.ui.utils.DevicePreview
import org.mifos.mobile.core.ui.utils.EventsEffect

/**
 * A stateful composable that displays the FAQ screen. It collects state and events from
 * the [FaqViewModel] and delegates the UI rendering to [FaqScreenContent].
 *
 * @param onNavigateBack Callback to handle back navigation.
 * @param onClickHelp Callback to navigate to the help/contact screen.
 * @param modifier The [Modifier] to be applied to this screen.
 * @param viewModel The ViewModel responsible for the screen's logic and state.
 */
@Composable
internal fun FaqScreen(
    onNavigateBack: () -> Unit,
    onClickHelp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FaqViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            FaqEvent.OnNavigateBack -> onNavigateBack.invoke()
            FaqEvent.OnNavigateToHelp -> onClickHelp.invoke()
        }
    }
    FaqScreenContent(
        uiState = state,
        modifier = modifier,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

/**
 * A stateless composable that renders the UI for the FAQ screen based on the provided [uiState].
 *
 * @param uiState The current state of the FAQ screen.
 * @param onAction Callback to send actions to the ViewModel.
 * @param modifier The [Modifier] to be applied to the layout.
 */
@Composable
private fun FaqScreenContent(
    uiState: FaqState,
    onAction: (FaqAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        topBarTitle = stringResource(Res.string.feature_settings_action_faq),
        onNavigateBack = { onAction(FaqAction.NavigateBack) },
        content = {
            Box(modifier = Modifier) {
                if (uiState.faqList.isNotEmpty()) {
                    FaqContent(
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

/**
 * Renders the main content of the FAQ screen, including the list of questions and answers
 * or an empty state view if no FAQs are available.
 *
 * @param faqArrayList The list of [FAQ] items to display.
 * @param selectedFaqPosition The index of the currently expanded FAQ item.
 * @param onAction Callback to send actions to the ViewModel.
 */
@Composable
private fun FaqContent(
    faqArrayList: List<FAQ>,
    selectedFaqPosition: Int,
    onAction: (FaqAction) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (faqArrayList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth()
                    .padding(horizontal = DesignToken.padding.large)
                    .padding(top = DesignToken.padding.extraLarge),
                verticalArrangement = Arrangement.spacedBy(DesignToken.padding.small),
            ) {
                itemsIndexed(items = faqArrayList) { index, faqItem ->
                    FaqItemHolder(
                        index = index,
                        isSelected = selectedFaqPosition == index,
                        onItemSelected = { onAction(FaqAction.UpdateFaqPosition(it)) },
                        question = faqItem.question,
                        answer = faqItem.answer,
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(DesignToken.padding.medium),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(Res.string.feature_settings_faq_doubt),
                    style = MifosTypography.bodySmall,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(Res.string.feature_settings_faq_contact_us),
                    style = MifosTypography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        onAction(FaqAction.NavigateToHelp)
                    },
                )
            }
        } else {
            EmptyDataView(
                modifier = Modifier.fillMaxSize(),
                icon = MifosIcons.Error,
                error = Res.string.no_questions_found,
            )
        }
    }
}

/**
 * A Jetpack Compose preview for the [FaqScreenContent]. This allows for
 * visualizing the UI component in Android Studio's preview pane.
 */
@DevicePreview
@Composable
fun FaqScreenPreview() {
    val faqArrayList = listOf(
        FAQ(
            question = "How do I apply for new loan account?",
            answer = "To apply for loan account, click on \"Apply for Loan\" given on the Home Screen.",
        ),
        FAQ(
            question = "What is Mifos?",
            answer = "Mifos is a platform for financial inclusion.",
        ),
    )
    MifosMobileTheme {
        FaqScreenContent(
            uiState = FaqState(faqList = faqArrayList, selectedFaqPosition = 0),
            onAction = {},
        )
    }
}
