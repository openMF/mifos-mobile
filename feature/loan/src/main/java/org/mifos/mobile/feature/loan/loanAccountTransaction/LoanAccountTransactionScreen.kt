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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.common.utils.CurrencyUtil
import org.mifos.mobile.core.common.utils.DateHelper.getDateAsString
import org.mifos.mobile.core.common.utils.Utils.formatTransactionType
import org.mifos.mobile.core.designsystem.components.MifosTopBar
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.Transaction
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.loan.R

@Composable
internal fun LoanAccountTransactionScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoanAccountTransactionViewModel = hiltViewModel(),
) {
    val uiState by viewModel.loanUiState.collectAsStateWithLifecycle()

    LoanAccountTransactionScreen(
        uiState = uiState,
        navigateBack = navigateBack,
        modifier = modifier,
    )
}

@Composable
private fun LoanAccountTransactionScreen(
    uiState: LoanAccountTransactionUiState,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var loanWithAssociations by rememberSaveable { mutableStateOf(LoanWithAssociations()) }

    Column(modifier = modifier.fillMaxSize()) {
        MifosTopBar(
            navigateBack = navigateBack,
            title = { Text(text = stringResource(id = R.string.transactions)) },
        )

        Box(modifier = Modifier.weight(1f)) {
            LoanAccountTransactionContent(loanWithAssociations = loanWithAssociations)

            when (uiState) {
                is LoanAccountTransactionUiState.Success -> {
                    if (uiState.loanWithAssociations != null &&
                        uiState.loanWithAssociations.transactions?.isNotEmpty() == true
                    ) {
                        loanWithAssociations = uiState.loanWithAssociations
                    } else {
                        MifosErrorComponent(isEmptyData = true)
                    }
                }

                is LoanAccountTransactionUiState.Loading -> {
                    MifosProgressIndicatorOverlay()
                }

                is LoanAccountTransactionUiState.Error -> {
                    MifosErrorComponent(
                        isNetworkConnected = Network.isConnected(context),
                        isEmptyData = false,
                    )
                }
            }
        }
    }
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
    ) {
        Text(
            text = loanWithAssociations.loanProductName ?: "",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
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
    val context = LocalContext.current

    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_local_atm_black_24dp),
            contentDescription = stringResource(id = R.string.atm_icon),
            modifier = Modifier.size(39.dp),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = formatTransactionType(transaction?.type?.value),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Row {
                Text(
                    text = stringResource(
                        id = R.string.string_and_string,
                        transaction?.currency?.displaySymbol ?: transaction?.currency?.code ?: "",
                        CurrencyUtil.formatCurrency(
                            context = context,
                            transaction?.amount ?: 0.0,
                        ),
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .weight(1f)
                        .alpha(0.7f),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = getDateAsString(transaction?.submittedOnDate),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.alpha(0.7f),
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

internal class LoanAccountTransactionUiStatesParameterProvider :
    PreviewParameterProvider<LoanAccountTransactionUiState> {
    override val values: Sequence<LoanAccountTransactionUiState>
        get() = sequenceOf(
            LoanAccountTransactionUiState.Success(LoanWithAssociations()),
            LoanAccountTransactionUiState.Error,
            LoanAccountTransactionUiState.Error,
            LoanAccountTransactionUiState.Loading,
        )
}

@DevicePreviews
@Composable
private fun LoanAccountTransactionScreenPreview(
    @PreviewParameter(LoanAccountTransactionUiStatesParameterProvider::class)
    loanAccountTransactionUiState: LoanAccountTransactionUiState,
) {
    MifosMobileTheme {
        LoanAccountTransactionScreen(
            uiState = loanAccountTransactionUiState,
            navigateBack = {},
        )
    }
}
