/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.loanAccountTransaction

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.loan.generated.resources.Res
import mifos_mobile.feature.loan.generated.resources.atm_icon
import mifos_mobile.feature.loan.generated.resources.ic_local_atm_black_24dp
import mifos_mobile.feature.loan.generated.resources.transactions
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.common.Utils.formatTransactionType
import org.mifos.mobile.core.designsystem.component.MifosTopAppBar
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.Transaction
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun LoanAccountTransactionScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoanAccountTransactionViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            LoanAccountTransactionEvent.NavigateBack -> navigateBack.invoke()
        }
    }

    LoanAccountTransactionScreen(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
        modifier = modifier,
    )
}

@Composable
private fun LoanAccountTransactionDialog(
    dialogState: LoanAccountTransactionState.DialogState?,
    state: LoanAccountTransactionState,
) {
    when (dialogState) {
        is LoanAccountTransactionState.DialogState.Error -> {
            MifosErrorComponent(
                isNetworkConnected = state.isOnline,
                isEmptyData = false,
            )
        }
        LoanAccountTransactionState.DialogState.Loading -> MifosProgressIndicatorOverlay()
        null -> Unit
    }
}

@Composable
private fun LoanAccountTransactionScreen(
    state: LoanAccountTransactionState,
    modifier: Modifier = Modifier,
    onAction: (LoanAccountTransactionAction) -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        MifosTopAppBar(
            backPress = { (onAction(LoanAccountTransactionAction.BackPress)) },
            topBarTitle = stringResource(Res.string.transactions),
        )

        Box(modifier = Modifier.weight(1f)) {
            state.loanWithAssociations?.let {
                LoanAccountTransactionContent(loanWithAssociations = it)
            }
        }
    }

    LoanAccountTransactionDialog(
        dialogState = state.dialogState,
        state = state,
    )
}

@Composable
private fun LoanAccountTransactionContent(
    loanWithAssociations: LoanWithAssociations,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = loanWithAssociations.loanProductName ?: "",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(items = loanWithAssociations.transactions?.toList().orEmpty()) {
                LoanAccountTransactionListItem(it)
            }
        }
    }
}

@Composable
private fun LoanAccountTransactionListItem(
    transaction: Transaction?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_local_atm_black_24dp),
            contentDescription = stringResource(Res.string.atm_icon),
            modifier = Modifier.size(39.dp),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = formatTransactionType(transaction?.type?.value),
                style = MaterialTheme.typography.bodyMedium,
            )

            Row {
                Text(
                    text = CurrencyFormatter.format(
                        transaction?.amount ?: 0.0,
                        transaction?.currency?.code,
                        2,
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .weight(1f)
                        .alpha(0.7f),
                )
                Text(
                    text = transaction?.submittedOnDate?.let { DateHelper.getDateAsString(it) } ?: "",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.alpha(0.7f),
                )
            }
        }
    }
}

@Preview
@Composable
private fun LoanAccountTransactionScreenPreview() {
    MifosMobileTheme {
        LoanAccountTransactionScreen(
            state = LoanAccountTransactionState(dialogState = null),
            modifier = Modifier,
            onAction = {},
        )
    }
}
