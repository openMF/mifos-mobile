/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.accounts.transactionDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.accounts.generated.resources.Res
import mifos_mobile.feature.accounts.generated.resources.feature_generic_error_server
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_detail_account_ref
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_detail_balance
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_detail_breakdown
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_detail_date
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_detail_default_type
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_detail_fees
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_detail_id
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_detail_interest
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_detail_penalties
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_detail_principal
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_detail_status
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_detail_status_reversed
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_detail_status_success
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_detail_type
import mifos_mobile.feature.accounts.generated.resources.feature_transaction_details
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.DevicePreview
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState

@Composable
fun TransactionDetailsScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TransactionDetailsViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            TransactionDetailsEvent.NavigateBack -> navigateBack()
        }
    }

    MifosElevatedScaffold(
        modifier = modifier,
        topBarTitle = stringResource(Res.string.feature_transaction_details),
        onNavigateBack = { viewModel.trySendAction(TransactionDetailsAction.OnNavigateBack) },
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                )
            }
        },
    ) {
        when (state.uiState) {
            ScreenUiState.Loading -> MifosProgressIndicator()
            is ScreenUiState.Error -> MifosErrorComponent(
                message = stringResource(Res.string.feature_generic_error_server),
                onRetry = { viewModel.trySendAction(TransactionDetailsAction.Retry) },
            )
            ScreenUiState.Success -> {
                state.transaction?.let { transaction ->
                    TransactionDetailContent(transaction)
                }
            }
            else -> {}
        }
    }
}

@Composable
fun TransactionDetailContent(
    transaction: UiTransactionDetails,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(DesignToken.padding.large),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TransactionHeader(transaction)

        Spacer(modifier = Modifier.height(DesignToken.spacing.large))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .padding(DesignToken.padding.medium),
        ) {
            DetailItem(
                stringResource(Res.string.feature_transaction_detail_id),
                transaction.id.toString(),
            )
            DetailItem(
                stringResource(Res.string.feature_transaction_detail_date),
                DateHelper.getDateAsString(transaction.date),
            )

            val statusLabel = if (transaction.status == "reversed") {
                stringResource(Res.string.feature_transaction_detail_status_reversed)
            } else {
                stringResource(Res.string.feature_transaction_detail_status_success)
            }

            val statusColor = if (transaction.status == "reversed") {
                MaterialTheme.colorScheme.error
            } else {
                AppColors.customEnable
            }

            DetailItem(
                label = stringResource(Res.string.feature_transaction_detail_status),
                value = statusLabel,
                valueColor = statusColor,
            )

            if (!transaction.accountNo.isNullOrEmpty() && transaction.accountNo != "N/A") {
                DetailItem(
                    stringResource(Res.string.feature_transaction_detail_account_ref),
                    transaction.accountNo,
                )
            }

            if (transaction.typeValue != null) {
                DetailItem(
                    stringResource(Res.string.feature_transaction_detail_type),
                    transaction.typeValue,
                )
            }

            TransactionBreakdown(transaction)

            if (transaction.outstandingBalance != null) {
                DetailItem(
                    label = stringResource(Res.string.feature_transaction_detail_balance),
                    value = CurrencyFormatter.format(
                        transaction.outstandingBalance,
                        transaction.currency,
                        maximumFractionDigits = 2,
                    ),
                )
            }
        }
    }
}

@Composable
private fun TransactionHeader(transaction: UiTransactionDetails) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            val isCredit = transaction.isCredit == true
            Icon(
                imageVector = if (isCredit) MifosIcons.ArrowDropDown else MifosIcons.ArrowDropUp,
                contentDescription = null,
                tint = if (isCredit) AppColors.customEnable else AppColors.lightRed,
                modifier = Modifier.size(46.dp),
            )
        }

        Spacer(modifier = Modifier.height(DesignToken.spacing.medium))

        Text(
            text = CurrencyFormatter.format(
                transaction.amount,
                transaction.currency,
                maximumFractionDigits = 2,
            ),
            style = MifosTypography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Text(
            text = transaction.typeValue ?: stringResource(Res.string.feature_transaction_detail_default_type),
            style = MifosTypography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun TransactionBreakdown(transaction: UiTransactionDetails) {
    if (transaction.principal != null ||
        transaction.interest != null ||
        transaction.fees != null ||
        transaction.penalties != null
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.height(DesignToken.spacing.medium))

            Text(
                text = stringResource(Res.string.feature_transaction_detail_breakdown),
                style = MifosTypography.labelLargeEmphasized,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            if (transaction.principal != null && transaction.principal > 0) {
                DetailItem(
                    label = stringResource(Res.string.feature_transaction_detail_principal),
                    value = CurrencyFormatter.format(
                        transaction.principal,
                        transaction.currency,
                        maximumFractionDigits = 2,
                    ),
                )
            }
            if (transaction.interest != null && transaction.interest > 0) {
                DetailItem(
                    label = stringResource(Res.string.feature_transaction_detail_interest),
                    value = CurrencyFormatter.format(
                        transaction.interest,
                        transaction.currency,
                        maximumFractionDigits = 2,
                    ),
                )
            }
            if (transaction.fees != null && transaction.fees > 0) {
                DetailItem(
                    label = stringResource(Res.string.feature_transaction_detail_fees),
                    value = CurrencyFormatter.format(
                        transaction.fees,
                        transaction.currency,
                        maximumFractionDigits = 2,
                    ),
                )
            }
            if (transaction.penalties != null && transaction.penalties > 0) {
                DetailItem(
                    label = stringResource(Res.string.feature_transaction_detail_penalties),
                    value = CurrencyFormatter.format(
                        transaction.penalties,
                        transaction.currency,
                        maximumFractionDigits = 2,
                    ),
                )
            }
        }
    }
}

@Composable
fun DetailItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = DesignToken.padding.small),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MifosTypography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MifosTypography.bodyMediumEmphasized,
            color = valueColor,
        )
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
}

@DevicePreview
@Composable
@Suppress("UnusedMember")
fun TransactionDetailContentPreview() {
    val sampleTransaction = UiTransactionDetails(
        id = 12345L,
        date = listOf(2025, 12, 13),
        amount = 559.88,
        status = "success",
        typeValue = "Repayment",
        isCredit = false,
        currency = "USD",
        accountNo = "000000123",
        principal = 500.0,
        interest = 59.88,
        fees = 0.0,
        penalties = 0.0,
        outstandingBalance = 1200.0,
    )

    MifosMobileTheme {
        Surface {
            TransactionDetailContent(transaction = sampleTransaction)
        }
    }
}
