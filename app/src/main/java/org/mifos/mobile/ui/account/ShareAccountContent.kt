package org.mifos.mobile.ui.account

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.mifos.mobile.R
import org.mifos.mobile.core.model.entity.accounts.share.ShareAccount
import org.mifos.mobile.utils.AccountTypeItemIndicator

@Composable
fun AccountScreenShareContent(
    accountsList: List<ShareAccount>,
    isSearching: Boolean,
    getUpdatedSearchList: (accountsList: List<ShareAccount>) -> List<ShareAccount>,
    isFiltered: Boolean,
    getUpdatedFilterList: (accountsList: List<ShareAccount>) -> List<ShareAccount>
) {

    var accounts by rememberSaveable {
        mutableStateOf(accountsList)
    }

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

    val lazyColumnState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyColumnState
    ) {
        items(items = accounts) { shareAccount ->
            AccountScreenShareListItem(
                shareAccount = shareAccount
            )
        }
    }
}

@Composable
fun AccountScreenShareListItem(
    shareAccount: ShareAccount
) {
    val (color, setSharingAccountDetail) = when {
        shareAccount.status?.active == true -> {
            Pair(colorResource(R.color.deposit_green), true)
        }
        shareAccount.status?.approved == true -> {
            Pair(colorResource(R.color.light_green), false)
        }
        shareAccount.status?.submittedAndPendingApproval == true -> {
            Pair(colorResource(R.color.light_yellow), false)
        }
        else -> {
            Pair(colorResource(R.color.light_blue), false)
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        AccountTypeItemIndicator(color)

        Column(modifier = Modifier.padding(all = 12.dp)) {
            shareAccount.accountNo?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            shareAccount.productName?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelLarge,
                    color = colorResource(id = R.color.gray_dark),
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Text(
                        text = stringResource(id = R.string.pending),
                        style = MaterialTheme.typography.labelLarge,
                        color = colorResource(id = R.color.gray_dark),
                    )

                    Text(
                        text = " ${shareAccount.totalPendingForApprovalShares}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorResource(id = R.color.black),
                    )
                }

                if (setSharingAccountDetail) {
                    Row {
                        Text(
                            text = stringResource(id = R.string.approved),
                            style = MaterialTheme.typography.labelLarge,
                            color = colorResource(id = R.color.gray_dark),
                        )

                        Text(
                            modifier = Modifier.padding(end = 12.dp),
                            text = " ${shareAccount.totalApprovedShares}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorResource(id = R.color.black),
                        )
                    }
                }
            }
        }
        Spacer(Modifier.weight(1f))
    }
}
