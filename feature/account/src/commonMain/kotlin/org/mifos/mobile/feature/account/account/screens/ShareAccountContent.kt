/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.account.account.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.account.generated.resources.Res
import mifos_mobile.feature.account.generated.resources.feature_account_approved
import mifos_mobile.feature.account.generated.resources.feature_account_pending
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.model.entity.accounts.share.ShareAccount
import org.mifos.mobile.feature.account.account.utils.AccountTypeItemIndicator

@Composable
internal fun AccountScreenShareContent(
    isSearching: Boolean,
    isFiltered: Boolean,
    accountsList: List<ShareAccount>,
    getUpdatedSearchList: (accountsList: List<ShareAccount>) -> List<ShareAccount>,
    getUpdatedFilterList: (accountsList: List<ShareAccount>) -> List<ShareAccount>,
    modifier: Modifier = Modifier,
) {
    val lazyColumnState = rememberLazyListState()

    var accounts by rememberSaveable { mutableStateOf(accountsList) }

    when {
        isFiltered && isSearching -> {
            accounts = getUpdatedSearchList(getUpdatedFilterList(accountsList))
        }

        isSearching -> {
            accounts = getUpdatedSearchList(accountsList)
        }

        isFiltered -> {
            accounts = getUpdatedFilterList(accountsList)
        }

        else -> {
            accounts = accountsList
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyColumnState,
    ) {
        items(items = accounts) { shareAccount ->
            AccountScreenShareListItem(
                shareAccount = shareAccount,
            )
        }
    }
}

@Composable
private fun AccountScreenShareListItem(
    shareAccount: ShareAccount,
    modifier: Modifier = Modifier,
) {
    val (color, setSharingAccountDetail) = when {
        shareAccount.status?.active == true -> {
            Pair(MaterialTheme.colorScheme.primary, true)
        }

        shareAccount.status?.approved == true -> {
            Pair(MaterialTheme.colorScheme.secondaryContainer, false)
        }

        shareAccount.status?.submittedAndPendingApproval == true -> {
            Pair(MaterialTheme.colorScheme.tertiaryContainer, false)
        }

        else -> {
            Pair(MaterialTheme.colorScheme.surfaceVariant, false)
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AccountTypeItemIndicator(color)

        Column(modifier = Modifier.padding(all = 12.dp)) {
            shareAccount.accountNo?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            shareAccount.productName?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row {
                    Text(
                        text = stringResource(resource = Res.string.feature_account_pending),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Text(
                        text = " ${shareAccount.totalPendingForApprovalShares}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }

                if (setSharingAccountDetail) {
                    Row {
                        Text(
                            text = stringResource(resource = Res.string.feature_account_approved),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )

                        Text(
                            modifier = Modifier.padding(end = 12.dp),
                            text = " ${shareAccount.totalApprovedShares}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        }
        Spacer(Modifier.weight(1f))
    }
}
