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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.datetime.Clock
import mifos_mobile.feature.savings.generated.resources.Res
import mifos_mobile.feature.savings.generated.resources.feature_account_error_black
import mifos_mobile.feature.savings.generated.resources.no_transaction_found
import mifos_mobile.feature.savings.generated.resources.savings_account_transaction
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.model.entity.accounts.savings.Transactions
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay

@Composable
internal fun SavingsAccountTransactionScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SavingAccountsTransactionViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsStateWithLifecycle()

    SavingsAccountTransactionScreen(
        uiState = uiState,
        isNetworkConnected = isNetworkAvailable,
        navigateBack = navigateBack,
        retryConnection = viewModel::loadSavingsWithAssociations,
        filterList = viewModel::filterList,
        modifier = modifier,
    )
}

@Composable
internal fun SavingsAccountTransactionScreen(
    uiState: SavingsAccountTransactionUiState,
    isNetworkConnected: Boolean,
    navigateBack: () -> Unit,
    retryConnection: () -> Unit,
    filterList: (SavingsTransactionFilterDataModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    var transactionList by remember { mutableStateOf(listOf<Transactions>()) }
    var isDialogOpen by rememberSaveable { mutableStateOf(false) }

    var savingsTransactionFilterDataModel by rememberSaveable(stateSaver = SavingsTransactionFilterDataModelSaver) {
        mutableStateOf(
            SavingsTransactionFilterDataModel(
                startDate = Clock.System.now().toEpochMilliseconds(),
                endDate = Clock.System.now().toEpochMilliseconds(),
                radioFilter = null,
                checkBoxFilters = mutableListOf(),
            ),
        )
    }

    MifosScaffold(
        modifier = modifier,
        backPress = navigateBack,
        topBarTitle = stringResource(Res.string.savings_account_transaction),
        actions = {
            IconButton(onClick = { isDialogOpen = true }) {
                Icon(
                    imageVector = MifosIcons.FilterList,
                    contentDescription = null,
                )
            }
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues = paddingValues)) {
                when (uiState) {
                    is SavingsAccountTransactionUiState.Loading -> {
                        MifosProgressIndicatorOverlay()
                    }

                    is SavingsAccountTransactionUiState.Error -> {
                        MifosErrorComponent(
                            isNetworkConnected = isNetworkConnected,
                            isEmptyData = false,
                            isRetryEnabled = true,
                            onRetry = retryConnection,
                        )
                    }

                    is SavingsAccountTransactionUiState.Success -> {
                        transactionList = uiState.savingAccountsTransactionList
                        if (uiState.savingAccountsTransactionList.isNotEmpty()) {
                            SavingsAccountTransactionContent(
                                currencyCode = uiState.savingAccountsTransactionList.first().currency?.code ?: "USD",
                                transactionList = transactionList,
                            )
                        }
                    }

                    SavingsAccountTransactionUiState.Empty -> {
                        EmptyDataView(
                            icon = vectorResource(resource = Res.drawable.feature_account_error_black),
                            error = Res.string.no_transaction_found,
                            modifier = Modifier.fillMaxSize(),
                        )
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
