package org.mifos.mobile.ui.savings_account_transaction

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.models.accounts.savings.Transactions
import org.mifos.mobile.utils.CurrencyUtil
import org.mifos.mobile.utils.DateHelper

@Composable
fun SavingsAccountTransactionContent(
    transactionList: List<Transactions>,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn {
            items(items = transactionList) {
                SavingsAccountTransactionListItem(it)
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = stringResource(id = R.string.need_help),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = stringResource(id = R.string.help_line_number),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Composable
fun SavingsAccountTransactionListItem(transaction: Transactions) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Image(
            painter = painterResource(
                id = getTransactionTriangleResId(transaction.transactionType)
            ),
            contentDescription = stringResource(id = R.string.savings_account_transaction),
            modifier = Modifier
                .size(56.dp)
                .padding(4.dp)
        )
        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = DateHelper.getDateAsString(transaction.date),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(
                        id = R.string.string_and_string,
                        transaction.currency?.displaySymbol ?: transaction.currency?.code ?: "",
                        CurrencyUtil.formatCurrency(context, transaction.amount,)
                    ),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = transaction.transactionType?.value ?: "",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.alpha(0.7f),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(
                        id = R.string.string_and_string,
                        transaction.currency?.displaySymbol ?: transaction.currency?.code ?: "",
                        CurrencyUtil.formatCurrency(context, transaction.runningBalance)
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.alpha(0.7f),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = transaction.paymentDetailData?.paymentType?.name.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.alpha(0.7f),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview
@Composable
fun SavingsAccountTransactionContentPreview() {
    MifosMobileTheme {
        SavingsAccountTransactionContent(transactionList = listOf())
    }
}