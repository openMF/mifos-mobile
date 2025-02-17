/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.shareaccount.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.share_account.generated.resources.Res
import mifos_mobile.feature.share_account.generated.resources.feature_account_empty_share_accounts
import mifos_mobile.feature.share_account.generated.resources.feature_account_error_black
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.feature.shareaccount.utils.AccountState
import org.mifos.mobile.feature.shareaccount.viewmodel.ShareAccountViewModel

@Composable
internal fun ShareAccountScreen(
    searchQuery: String,
    isSearchActive: Boolean,
    isFiltered: Boolean,
    modifier: Modifier = Modifier,
    viewModel: ShareAccountViewModel = koinViewModel(),
) {
    val uiState = viewModel.accountUiState.collectAsStateWithLifecycle()
    val isNetworkAvailable = viewModel.isNetworkAvailable.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadSavingsAccounts()
    }

    when (val state = uiState.value) {
        is AccountState.Error -> {
            MifosErrorComponent(
                isNetworkConnected = isNetworkAvailable.value,
                isRetryEnabled = true,
                onRetry = viewModel::loadSavingsAccounts,
            )
        }

        is AccountState.Loading -> {
            MifosProgressIndicatorOverlay()
        }

        is AccountState.Success -> {
            val shareAccounts = state.shareAccounts
            if (shareAccounts.isNullOrEmpty()) {
                EmptyDataView(
                    icon = vectorResource(resource = Res.drawable.feature_account_error_black),
                    error = Res.string.feature_account_empty_share_accounts,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                val updatedFilteredAccounts =
                    remember(searchQuery, isFiltered, isSearchActive, shareAccounts) {
                        viewModel.getUpdatedFilteredAccountList(
                            searchQuery = searchQuery,
                            isFiltered = isFiltered,
                            isSearchActive = isSearchActive,
                            accountList = shareAccounts,
                        )
                    }

                LoanAccountScreenContent(
                    accountList = updatedFilteredAccounts,
                    modifier = modifier,
                )
            }
        }
    }
}
