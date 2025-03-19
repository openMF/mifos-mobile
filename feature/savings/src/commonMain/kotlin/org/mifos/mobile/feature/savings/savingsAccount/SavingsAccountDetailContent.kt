/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savings.savingsAccount

import androidx.compose.foundation.background
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
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.savings.generated.resources.Res
import mifos_mobile.feature.savings.generated.resources.account_balance
import mifos_mobile.feature.savings.generated.resources.account_number
import mifos_mobile.feature.savings.generated.resources.account_status
import mifos_mobile.feature.savings.generated.resources.deposit
import mifos_mobile.feature.savings.generated.resources.help_line_number
import mifos_mobile.feature.savings.generated.resources.ic_charges
import mifos_mobile.feature.savings.generated.resources.ic_compare_arrows_black_24dp
import mifos_mobile.feature.savings.generated.resources.ic_qrcode_scan
import mifos_mobile.feature.savings.generated.resources.last_trans
import mifos_mobile.feature.savings.generated.resources.made_on
import mifos_mobile.feature.savings.generated.resources.make_transfer
import mifos_mobile.feature.savings.generated.resources.min_required_balance
import mifos_mobile.feature.savings.generated.resources.monitor
import mifos_mobile.feature.savings.generated.resources.need_help
import mifos_mobile.feature.savings.generated.resources.no_transaction
import mifos_mobile.feature.savings.generated.resources.no_withdrawals
import mifos_mobile.feature.savings.generated.resources.nominal_interest_rate
import mifos_mobile.feature.savings.generated.resources.not_available
import mifos_mobile.feature.savings.generated.resources.qr_code
import mifos_mobile.feature.savings.generated.resources.savings_charges
import mifos_mobile.feature.savings.generated.resources.total_deposits
import mifos_mobile.feature.savings.generated.resources.total_withdrawal
import mifos_mobile.feature.savings.generated.resources.transactions
import mifos_mobile.feature.savings.generated.resources.view_charges
import mifos_mobile.feature.savings.generated.resources.view_qr_code
import mifos_mobile.feature.savings.generated.resources.view_transactions
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.designsystem.component.MifosOutlinedButton
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.model.entity.accounts.savings.Status
import org.mifos.mobile.core.ui.component.MifosLinkText
import org.mifos.mobile.core.ui.component.MifosTextTitleDescDoubleLine
import org.mifos.mobile.core.ui.component.MonitorListItemWithIcon

@Composable
internal fun SavingsAccountDetailContent(
    savingsAccount: SavingsWithAssociations,
    deposit: () -> Unit,
    makeTransfer: () -> Unit,
    viewTransaction: () -> Unit,
    viewCharges: () -> Unit,
    viewQrCode: (SavingsWithAssociations) -> Unit,
    callUs: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(16.dp),
    ) {
        AccountDetailsCard(
            savingsAccount = savingsAccount,
            deposit = deposit,
            makeTransfer = makeTransfer,
        )

        Spacer(modifier = Modifier.height(20.dp))

        LastTransactionCard(
            savingsWithAssociations = savingsAccount,
        )

        Spacer(modifier = Modifier.height(20.dp))

        SavingsMonitorComponent(
            viewTransaction = viewTransaction,
            viewCharges = viewCharges,
            viewQrCode = { viewQrCode.invoke(savingsAccount) },
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = stringResource(Res.string.need_help),
            )
            Spacer(modifier = Modifier.width(5.dp))
            MifosLinkText(
                text = stringResource(Res.string.help_line_number),
                onClick = callUs,
                isUnderlined = false,
            )
        }
    }
}

@Composable
private fun AccountDetailsCard(
    savingsAccount: SavingsWithAssociations,
    deposit: () -> Unit,
    makeTransfer: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(14.dp),
        ) {
            MifosTextTitleDescDoubleLine(
                title = stringResource(Res.string.account_balance),
                description =
                CurrencyFormatter
                    .format(
                        savingsAccount.summary?.accountBalance ?: 0.0,
                        currencyCode = savingsAccount.transactions[0].currency?.code ?: "USD",
                        maximumFractionDigits = 5,
                    ),
                descriptionStyle = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )

            Spacer(modifier = Modifier.height(8.dp))

            StatusField(
                title = stringResource(Res.string.account_status),
                accountStatus = savingsAccount.status ?: Status(),
            )

            Spacer(modifier = Modifier.height(8.dp))

            MifosTextTitleDescDoubleLine(
                title = stringResource(Res.string.account_number),
                description = savingsAccount.accountNo ?: "",
                descriptionStyle = MaterialTheme.typography.bodyLarge,
            )

            Spacer(modifier = Modifier.height(8.dp))

            MifosTextTitleDescDoubleLine(
                title = stringResource(Res.string.nominal_interest_rate),
                description = CurrencyFormatter.format(
                    savingsAccount.getNominalAnnualInterestRate(),
                    savingsAccount.currency?.code,
                    2,
                ),
                descriptionStyle = MaterialTheme.typography.bodyLarge,
            )

            Spacer(modifier = Modifier.height(8.dp))

            MifosTextTitleDescDoubleLine(
                title = stringResource(Res.string.total_deposits),
                description = if (savingsAccount.summary?.totalDeposits != null) {
                    CurrencyFormatter
                        .format(
                            savingsAccount.summary?.totalDeposits ?: 0.0,
                            currencyCode = savingsAccount.transactions[0].currency?.code ?: "USD",
                            maximumFractionDigits = 3,
                        )
                } else {
                    stringResource(Res.string.not_available)
                },
                descriptionStyle = MaterialTheme.typography.bodyLarge,
            )

            Spacer(modifier = Modifier.height(8.dp))

            MifosTextTitleDescDoubleLine(
                title = stringResource(Res.string.total_withdrawal),
                descriptionStyle = MaterialTheme.typography.bodyLarge,
                description = if (savingsAccount.summary?.totalDeposits != null) {
                    CurrencyFormatter
                        .format(
                            savingsAccount.summary?.totalWithdrawals ?: 0.0,
                            currencyCode = savingsAccount.transactions[0].currency?.code ?: "USD",
                            maximumFractionDigits = 3,
                        )
                } else {
                    stringResource(Res.string.no_withdrawals)
                },
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                MifosOutlinedButton(
                    content = { Text(text = stringResource(Res.string.deposit)) },
                    onClick = {
                        if (savingsAccount.status?.active == true) {
                            deposit()
                        }
                    },
                )

                Spacer(modifier = Modifier.width(8.dp))

                MifosOutlinedButton(
                    content = { Text(text = stringResource(Res.string.make_transfer)) },
                    onClick = {
                        if (savingsAccount.status?.active == true) {
                            makeTransfer()
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun LastTransactionCard(
    savingsWithAssociations: SavingsWithAssociations,
    modifier: Modifier = Modifier,
) {
    val isTransactionEmpty = savingsWithAssociations.transactions.isEmpty()

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(Res.string.last_trans),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(14.dp)) {
                MifosTextTitleDescDoubleLine(
                    title = stringResource(Res.string.last_trans),
                    descriptionStyle = MaterialTheme.typography.bodyLarge,
                    description = if (isTransactionEmpty) {
                        stringResource(Res.string.no_transaction)
                    } else {
                        CurrencyFormatter
                            .format(
                                savingsWithAssociations.transactions[0].amount ?: 0.0,
                                currencyCode = savingsWithAssociations.transactions[0].currency?.code ?: "USD",
                                maximumFractionDigits = 5,
                            )
                    },
                )

                if (!isTransactionEmpty) {
                    Spacer(modifier = Modifier.height(8.dp))
                    MifosTextTitleDescDoubleLine(
                        title = stringResource(Res.string.made_on),
                        descriptionStyle = MaterialTheme.typography.bodyLarge,
                        description = DateHelper.getDateAsString(
                            savingsWithAssociations.lastActiveTransactionDate!!,
                        ),
                    )
                }

                if (savingsWithAssociations.minRequiredOpeningBalance != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    MifosTextTitleDescDoubleLine(
                        title = stringResource(Res.string.min_required_balance),
                        descriptionStyle = MaterialTheme.typography.bodyLarge,
                        description =
                        CurrencyFormatter
                            .format(
                                savingsWithAssociations.minRequiredOpeningBalance ?: 0.0,
                                currencyCode = savingsWithAssociations.transactions[0].currency?.code ?: "USD",
                                maximumFractionDigits = 3,
                            ),
                    )
                }
            }
        }
    }
}

@Composable
private fun SavingsMonitorComponent(
    viewTransaction: () -> Unit,
    viewCharges: () -> Unit,
    viewQrCode: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(Res.string.monitor),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        MonitorListItemWithIcon(
            titleId = Res.string.transactions,
            subTitleId = Res.string.view_transactions,
            iconId = Res.drawable.ic_compare_arrows_black_24dp,
            onClick = viewTransaction,
        )
        MonitorListItemWithIcon(
            titleId = Res.string.savings_charges,
            subTitleId = Res.string.view_charges,
            iconId = Res.drawable.ic_charges,
            onClick = viewCharges,
        )
        MonitorListItemWithIcon(
            titleId = Res.string.qr_code,
            subTitleId = Res.string.view_qr_code,
            iconId = Res.drawable.ic_qrcode_scan,
            onClick = viewQrCode,
        )
    }
}

@Composable
private fun StatusField(
    title: String,
    accountStatus: Status,
    modifier: Modifier = Modifier,
) {
    val (color, textResId) = accountStatus.getStatusColorAndText()

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .alpha(0.7f)
                .fillMaxWidth(),
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = color)
                    .size(15.dp),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(textResId),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
