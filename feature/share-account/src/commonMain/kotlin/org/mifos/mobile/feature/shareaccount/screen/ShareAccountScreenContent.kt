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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.model.entity.accounts.share.ShareAccount
import org.mifos.mobile.feature.shareaccount.component.ShareAccountCard

@Composable
fun LoanAccountScreenContent(
    accountList: List<ShareAccount>,
    modifier: Modifier = Modifier,
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = lazyListState,
    ) {
        items(items = accountList, key = { account -> account.id }) { loanAccount ->
            LoanAccountListItem(
                shareAccount = loanAccount,
            )
        }
    }
}

@Composable
private fun LoanAccountListItem(
    shareAccount: ShareAccount,
    modifier: Modifier = Modifier,
) {
    val (indicatorColor, shouldShowAccountDetail) = when {
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

    ShareAccountCard(
        shareAccount = shareAccount,
        indicatorColor = indicatorColor,
        shouldShowAccountDetail = shouldShowAccountDetail,
        modifier = modifier,
    )
}
