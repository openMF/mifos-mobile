package org.mifos.mobile.feature.account.account.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.mifos.mobile.feature.account.R
import org.mifos.mobile.feature.account.account.utils.AccountTypeItemIndicator
import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount
import org.mifos.mobile.core.common.utils.CurrencyUtil
import org.mifos.mobile.core.common.utils.DateHelper

@Composable
fun LoanAccountContent(
    accountsList: List<LoanAccount>,
    isSearching: Boolean,
    getUpdatedSearchList: (accountsList: List<LoanAccount>) -> List<LoanAccount>,
    isFiltered: Boolean,
    getUpdatedFilterList: (accountsList: List<LoanAccount>) -> List<LoanAccount>,
    onItemClick: (accountType: String, accountId: Long) -> Unit,
) {

    var accounts by rememberSaveable {
        mutableStateOf(accountsList)
    }

    accounts = when {
        isFiltered && isSearching -> {
            getUpdatedSearchList(getUpdatedFilterList(accountsList))
        }

        isSearching -> {
            getUpdatedSearchList(accountsList)
        }

        isFiltered -> {
            getUpdatedFilterList(accountsList)
        }

        else -> {
            accountsList
        }
    }

    val lazyColumnState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyColumnState
    ) {
        items(items = accounts) { loanAccount ->
            AccountScreenLoanListItem(
                loanAccount = loanAccount,
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
fun AccountScreenLoanListItem(
    loanAccount: LoanAccount,
    onItemClick: (accountType: String, accountId: Long) -> Unit
) {
    val context = LocalContext.current

    val (color, stringResource, numColor) = when {
        loanAccount.status?.active == true && loanAccount.inArrears == true -> {
            Triple(
                colorResource(R.color.red),
                "${stringResource(id = R.string.disbursement)} ${DateHelper.getDateAsString(loanAccount.timeline?.actualDisbursementDate)}",
                colorResource(R.color.red)
            )
        }
        loanAccount.status?.active == true -> {
            Triple(
                colorResource(R.color.deposit_green),
                "${stringResource(id = R.string.disbursement)} ${DateHelper.getDateAsString(loanAccount.timeline?.actualDisbursementDate)}",
                colorResource(R.color.deposit_green)
            )
        }
        loanAccount.status?.waitingForDisbursal == true -> {
            Triple(
                colorResource(R.color.blue),
                "${stringResource(id = R.string.approved)} ${DateHelper.getDateAsString(loanAccount.timeline?.approvedOnDate)}",
                null
            )
        }
        loanAccount.status?.pendingApproval == true -> {
            Triple(
                colorResource(R.color.light_yellow),
                "${stringResource(id = R.string.submitted)} ${DateHelper.getDateAsString(loanAccount.timeline?.submittedOnDate)}",
                null
            )
        }
        loanAccount.status?.overpaid == true -> {
            Triple(
                colorResource(R.color.purple),
                "${stringResource(id = R.string.approved)} ${DateHelper.getDateAsString(loanAccount.timeline?.actualDisbursementDate)}",
                colorResource(R.color.purple)
            )
        }
        loanAccount.status?.closed == true -> {
            Triple(
                colorResource(R.color.black),
                "${stringResource(id = R.string.closed)} ${DateHelper.getDateAsString(loanAccount.timeline?.closedOnDate)}",
                null
            )
        }
        else -> {
            Triple(
                colorResource(R.color.gray_dark),
                "${stringResource(id = R.string.withdrawn)} ${DateHelper.getDateAsString(loanAccount.timeline?.withdrawnOnDate)}",
                null
            )
        }
    }

    Row(
        modifier = Modifier.clickable {
            onItemClick.invoke(org.mifos.mobile.core.common.Constants.LOAN_ACCOUNTS, loanAccount.id)
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AccountTypeItemIndicator(color)

        Column(modifier = Modifier.padding(all = 12.dp)) {
            loanAccount.accountNo?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            loanAccount.productName?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelLarge,
                    color = colorResource(id = R.color.gray_dark)
                )
            }

            Text(
                text = stringResource,
                style = MaterialTheme.typography.labelLarge,
                color = colorResource(id = R.color.gray_dark),
            )
        }

        Spacer(Modifier.weight(1f))

        numColor?.let {
            val amountBalance = if (loanAccount.loanBalance != 0.0) loanAccount.loanBalance else 0.0
            Text(
                text = CurrencyUtil.formatCurrency(context, amountBalance),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 16.dp),
                color = it
            )
        }
    }
}
