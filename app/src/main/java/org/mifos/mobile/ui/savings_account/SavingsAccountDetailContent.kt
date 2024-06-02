package org.mifos.mobile.ui.savings_account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.MifosLinkText
import org.mifos.mobile.core.ui.component.MifosRoundIcon
import org.mifos.mobile.core.ui.component.MifosTextTitleDescDoubleLine
import org.mifos.mobile.models.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.models.accounts.savings.Status
import org.mifos.mobile.utils.CurrencyUtil
import org.mifos.mobile.utils.DateHelper
import org.mifos.mobile.utils.SymbolsUtils

@Composable
fun SavingsAccountDetailContent(
    savingsAccount: SavingsWithAssociations,
    deposit: (Boolean) -> Unit,
    makeTransfer: (Boolean) -> Unit,
    viewTransaction: () -> Unit,
    viewCharges: () -> Unit,
    viewQrCode: (SavingsWithAssociations) -> Unit,
    callUs: () -> Unit
) {
    val scrollState = rememberScrollState()
    val currencySymbol = savingsAccount.currency?.displaySymbol ?: "$"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        AccountDetailsCard(
            makeTransfer = makeTransfer,
            deposit = deposit,
            savingsAccount = savingsAccount,
            currencySymbol = currencySymbol
        )

        Spacer(modifier = Modifier.height(20.dp))

        LastTransactionCard(
            savingsWithAssociations = savingsAccount,
            currencySymbol = currencySymbol
        )

        Spacer(modifier = Modifier.height(20.dp))

        SavingsMonitorComponent(
            viewTransaction = viewTransaction,
            viewCharges = viewCharges,
            viewQrCode = { viewQrCode.invoke(savingsAccount) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                text = stringResource(id = R.string.need_help)
            )
            Spacer(modifier = Modifier.width(5.dp))
            MifosLinkText(
                text = stringResource(id = R.string.help_line_number),
                onClick = { callUs.invoke() },
                isUnderlined = false
            )
        }
    }
}

@Composable
fun AccountDetailsCard(
    modifier: Modifier = Modifier,
    savingsAccount: SavingsWithAssociations,
    deposit: (Boolean) -> Unit,
    makeTransfer: (Boolean) -> Unit,
    currencySymbol: String
) {
    val context = LocalContext.current
    OutlinedCard(modifier = modifier) {
        Column(modifier = Modifier.padding(14.dp)) {
            MifosTextTitleDescDoubleLine(
                title = stringResource(id = R.string.account_balance),
                description = stringResource(
                    id = R.string.string_and_string,
                    currencySymbol,
                    CurrencyUtil.formatCurrency(
                        context = context,
                        amt = savingsAccount.summary?.accountBalance ?: 0.0
                    )
                ),
                descriptionStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            StatusField(
                title = stringResource(id = R.string.account_status),
                accountStatus = savingsAccount.status ?: Status()
            )

            Spacer(modifier = Modifier.height(8.dp))

            MifosTextTitleDescDoubleLine(
                title = stringResource(id = R.string.account_number),
                description = savingsAccount.accountNo ?: "",
                descriptionStyle = MaterialTheme.typography.bodyLarge,
            )

            Spacer(modifier = Modifier.height(8.dp))

            MifosTextTitleDescDoubleLine(
                title = stringResource(id = R.string.nominal_interest_rate),
                description = stringResource(
                    id = R.string.double_and_string,
                    savingsAccount.getNominalAnnualInterestRate(),
                    SymbolsUtils.PERCENT
                ),
                descriptionStyle = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            MifosTextTitleDescDoubleLine(
                title = stringResource(id = R.string.total_deposits),
                description = if (savingsAccount.summary?.totalDeposits != null) {
                    stringResource(
                        id = R.string.string_and_string,
                        currencySymbol,
                        CurrencyUtil.formatCurrency(
                            context = context,
                            amt = savingsAccount.summary?.totalDeposits ?: 0.0,
                        )
                    )
                } else {
                    stringResource(id = R.string.not_available)
                },
                descriptionStyle = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            MifosTextTitleDescDoubleLine(
                title = stringResource(id = R.string.total_withdrawal),
                descriptionStyle = MaterialTheme.typography.bodyLarge,
                description = if (savingsAccount.summary?.totalDeposits != null) {
                    stringResource(
                        id = R.string.string_and_string, currencySymbol,
                        CurrencyUtil.formatCurrency(
                            context = context,
                            amt = savingsAccount.summary?.totalWithdrawals ?: 0.0,
                        ),
                    )
                } else {
                    stringResource(id = R.string.no_withdrawals)
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                OutlinedButton(onClick = {
                    deposit.invoke(
                        savingsAccount.status?.active ?: false
                    )
                }) { Text(text = stringResource(id = R.string.deposit)) }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(onClick = {
                    makeTransfer.invoke(
                        savingsAccount.status?.active ?: false
                    )
                }) { Text(text = stringResource(id = R.string.make_transfer)) }
            }
        }
    }
}

@Composable
fun LastTransactionCard(
    savingsWithAssociations: SavingsWithAssociations,
    currencySymbol: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isTransactionEmpty = savingsWithAssociations.transactions.isEmpty()

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.last_trans),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(14.dp)) {
                MifosTextTitleDescDoubleLine(
                    title = stringResource(id = R.string.last_transaction),
                    descriptionStyle = MaterialTheme.typography.bodyLarge,
                    description = if (isTransactionEmpty) {
                        stringResource(id = R.string.no_transaction)
                    } else {
                        stringResource(
                            id = R.string.string_and_double,
                            currencySymbol,
                            savingsWithAssociations.transactions[0].amount ?: 0.0
                        )
                    }
                )

                if (!isTransactionEmpty) {
                    Spacer(modifier = Modifier.height(8.dp))
                    MifosTextTitleDescDoubleLine(
                        title = stringResource(id = R.string.made_on),
                        descriptionStyle = MaterialTheme.typography.bodyLarge,
                        description = DateHelper.getDateAsString(
                            savingsWithAssociations.lastActiveTransactionDate,
                        )
                    )
                }

                if (savingsWithAssociations.minRequiredOpeningBalance != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    MifosTextTitleDescDoubleLine(
                        title = stringResource(id = R.string.min_required_balance),
                        descriptionStyle = MaterialTheme.typography.bodyLarge,
                        description = stringResource(
                            id = R.string.string_and_string, currencySymbol,
                            CurrencyUtil.formatCurrency(
                                context = context,
                                amt = savingsWithAssociations.minRequiredOpeningBalance ?: 0.0,
                            ),
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun SavingsMonitorComponent(
    modifier: Modifier = Modifier,
    viewTransaction: () -> Unit,
    viewCharges: () -> Unit,
    viewQrCode: () -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.monitor),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        MonitorListItemWithIcon(
            titleId = R.string.transactions,
            subTitleId = R.string.view_transactions,
            iconId = R.drawable.ic_compare_arrows_black_24dp,
            onClick = viewTransaction
        )
        MonitorListItemWithIcon(
            titleId = R.string.savings_charges,
            subTitleId = R.string.view_charges,
            iconId = R.drawable.ic_charges,
            onClick = viewCharges
        )
        MonitorListItemWithIcon(
            titleId = R.string.qr_code,
            subTitleId = R.string.view_qr_code,
            iconId = R.drawable.ic_qrcode_scan,
            onClick = viewQrCode
        )
    }
}

@Composable
fun MonitorListItemWithIcon(
    modifier: Modifier = Modifier,
    titleId: Int,
    subTitleId: Int,
    iconId: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable { onClick.invoke() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MifosRoundIcon(
            iconId = iconId,
            modifier = Modifier.size(39.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = stringResource(id = titleId),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = stringResource(id = subTitleId),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .alpha(0.7f)
                    .fillMaxWidth(),
            )
        }
    }
}

@Composable
fun StatusField(
    title: String,
    accountStatus: Status
) {
    val (color, textResId) = accountStatus.getStatusColorAndText()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .alpha(0.7f)
                .fillMaxWidth()
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = color)
                    .size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(id = textResId),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}