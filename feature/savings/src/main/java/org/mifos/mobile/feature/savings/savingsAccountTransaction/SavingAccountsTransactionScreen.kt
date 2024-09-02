/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savings.savingsAccountTransaction

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.designsystem.components.MifosScaffold
import org.mifos.mobile.core.designsystem.components.MifosTopBar
import org.mifos.mobile.core.designsystem.icons.MifosIcons
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.accounts.savings.Transactions
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.savings.R
import java.time.Instant

@Composable
internal fun SavingsAccountTransactionScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SavingAccountsTransactionViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SavingsAccountTransactionScreen(
        uiState = uiState,
        navigateBack = navigateBack,
        retryConnection = viewModel::loadSavingsWithAssociations,
        filterList = viewModel::filterList,
        modifier = modifier,
    )
}

@Composable
internal fun SavingsAccountTransactionScreen(
    uiState: SavingsAccountTransactionUiState,
    navigateBack: () -> Unit,
    retryConnection: () -> Unit,
    filterList: (SavingsTransactionFilterDataModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var transactionList by rememberSaveable { mutableStateOf(listOf<Transactions>()) }
    var isDialogOpen by rememberSaveable { mutableStateOf(false) }
    var savingsTransactionFilterDataModel by rememberSaveable {
        mutableStateOf(
            SavingsTransactionFilterDataModel(
                startDate = Instant.now().toEpochMilli(),
                endDate = Instant.now().toEpochMilli(),
                radioFilter = null,
                checkBoxFilters = mutableListOf(),
            ),
        )
    }

    MifosScaffold(
        modifier = modifier,
        topBar = {
            MifosTopBar(
                navigateBack = navigateBack,
                title = {
                    Text(
                        text = stringResource(id = R.string.savings_account_transaction),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                },
                actions = {
                    IconButton(onClick = { isDialogOpen = true }) {
                        Icon(
                            imageVector = MifosIcons.FilterList,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                },
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues = paddingValues)) {
                when (uiState) {
                    is SavingsAccountTransactionUiState.Loading -> {
                        MifosProgressIndicatorOverlay()
                    }

                    is SavingsAccountTransactionUiState.Error -> {
                        MifosErrorComponent(
                            isNetworkConnected = Network.isConnected(context),
                            isEmptyData = false,
                            isRetryEnabled = true,
                            onRetry = retryConnection,
                        )
                    }

                    is SavingsAccountTransactionUiState.Success -> {
                        if (uiState.savingAccountsTransactionList.isNullOrEmpty()) {
                            EmptyDataView(
                                icon = R.drawable.ic_compare_arrows_black_24dp,
                                error = R.string.no_transaction_found,
                            )
                        } else {
                            transactionList = uiState.savingAccountsTransactionList
                            SavingsAccountTransactionContent(transactionList = transactionList)
                        }
                    }
                }
            }
        },
    )

    if (isDialogOpen) {
        SavingsTransactionFilterDialog(
            savingsTransactionFilterDataModel = savingsTransactionFilterDataModel,
            onDismiss = { isDialogOpen = false },
            filter = { filters ->
                savingsTransactionFilterDataModel = filters
                filterList(filters)
            },
        )
    }
}

internal class SavingsAccountTransactionUiStatesParameterProvider :
    PreviewParameterProvider<SavingsAccountTransactionUiState> {
    override val values: Sequence<SavingsAccountTransactionUiState>
        get() = sequenceOf(
            SavingsAccountTransactionUiState.Success(listOf()),
            SavingsAccountTransactionUiState.Error(""),
            SavingsAccountTransactionUiState.Loading,
        )
}

@DevicePreviews
@Composable
private fun SavingsAccountTransactionScreenPreview(
    @PreviewParameter(SavingsAccountTransactionUiStatesParameterProvider::class)
    savingsAccountUiState: SavingsAccountTransactionUiState,
) {
    MifosMobileTheme {
        SavingsAccountTransactionScreen(
            uiState = savingsAccountUiState,
            navigateBack = { },
            retryConnection = { },
            filterList = { },
        )
    }
}
