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
import mifos_mobile.feature.share_account.generated.resources.Res
import mifos_mobile.feature.share_account.generated.resources.feature_account_active
import mifos_mobile.feature.share_account.generated.resources.feature_account_approval_pending
import mifos_mobile.feature.share_account.generated.resources.feature_account_approved
import mifos_mobile.feature.share_account.generated.resources.feature_account_closed
import mifos_mobile.feature.share_account.generated.resources.feature_account_rejected
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.model.entity.accounts.share.ShareAccount
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.feature.shareaccount.component.ShareAccountCard

@Composable
internal fun ShareAccountScreenContent(
    accountList: List<ShareAccount>,
    onAccountSelected: (accountType: AccountType, accountId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = lazyListState,
    ) {
        items(
            items = accountList,
            key = { account -> account.id },
        ) { loanAccount ->
            ShareAccountListItem(
                shareAccount = loanAccount,
                onAccountSelected = onAccountSelected,
            )
        }
    }
}

@Composable
private fun ShareAccountListItem(
    shareAccount: ShareAccount,
    onAccountSelected: (accountType: AccountType, accountId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (indicatorColor, statusDescription, shouldShowAccountDetail) = when {
        shareAccount.status?.active == true -> {
            Triple(
                first = MaterialTheme.colorScheme.primary,
                second = stringResource(Res.string.feature_account_active),
                third = true,
            )
        }

        shareAccount.status?.approved == true -> {
            Triple(
                first = MaterialTheme.colorScheme.secondaryContainer,
                second = stringResource(Res.string.feature_account_approved),
                third = false,
            )
        }

        shareAccount.status?.submittedAndPendingApproval == true -> {
            Triple(
                first = MaterialTheme.colorScheme.tertiaryContainer,
                second = stringResource(Res.string.feature_account_approval_pending),
                third = false,
            )
        }

        shareAccount.status?.rejected == true -> {
            Triple(
                first = MaterialTheme.colorScheme.surfaceVariant,
                second = stringResource(Res.string.feature_account_rejected),
                third = false,
            )
        }

        else -> {
            Triple(
                first = MaterialTheme.colorScheme.errorContainer,
                second = stringResource(Res.string.feature_account_closed),
                third = false,
            )
        }
    }

    ShareAccountCard(
        shareAccount = shareAccount,
        indicatorColor = indicatorColor,
        statusDescription = statusDescription,
        shouldShowAccountDetail = shouldShowAccountDetail,
        onClick = {
            onAccountSelected(AccountType.SHARE, shareAccount.id)
        },
        modifier = modifier,
    )
}
