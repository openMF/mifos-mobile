/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loanaccount.loanAccountDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import mifos_mobile.feature.loan_account.generated.resources.Res
import mifos_mobile.feature.loan_account.generated.resources.feature_account_details_action
import mifos_mobile.feature.loan_account.generated.resources.feature_account_details_top_bar_title
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_next_installment_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_product_type_label
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.core.ui.component.MifosActionCard
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosLabelValueCard
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.feature.loanaccount.component.LoanActionItems
import org.mifos.mobile.feature.loanaccount.component.loanAccountActions

@Composable
internal fun LoanAccountDetailsScreen(
    navigateBack: () -> Unit,
    navigateToMakePaymentScreen: () -> Unit,
    navigateToRepaymentScheduleScreen: (Long) -> Unit,
    navigateToLoanSummaryScreen: (Long) -> Unit,
    navigateToQrCodeScreen: (String) -> Unit,
    navigateToClientChargeScreen: (String, Long) -> Unit,
    navigateToLoanAccountTransactionScreen: (Long) -> Unit,
    viewModel: LoanAccountDetailsViewModel = koinViewModel(),
) {
    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            LoanAccountDetailsEvent.NavigateBack -> navigateBack.invoke()

            is LoanAccountDetailsEvent.NavigateToAction -> {
                when {
                    event.route == Constants.CHARGES -> {
                        navigateToClientChargeScreen(ChargeType.LOAN.name, uiState.accountId)
                    }
                    event.route == Constants.TRANSACTIONS -> {
                        navigateToLoanAccountTransactionScreen(uiState.accountId)
                    }

                    event.route == Constants.MAKE_PAYMENT -> {
                        navigateToMakePaymentScreen.invoke()
                    }
                    event.route == Constants.REPAYMENT_SCHEDULE -> {
                        navigateToRepaymentScheduleScreen(uiState.accountId)
                    }
                    event.route == Constants.LOAN_SUMMARY -> {
                        navigateToLoanSummaryScreen(uiState.accountId)
                    }
                    event.route == Constants.QR_CODE -> {
                        navigateToQrCodeScreen(viewModel.getQrString())
                    }
                }
            }
        }
    }

    LoanAccountDetailsContent(
        state = uiState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    LoanAccountDialogs(
        dialogState = uiState.dialogState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
internal fun LoanAccountDetailsContent(
    state: LoanAccountDetailsState,
    onAction: (LoanAccountDetailsAction) -> Unit,
//    onActionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        onNavigateBack = { onAction(LoanAccountDetailsAction.OnNavigateBack) },
        topBarTitle = stringResource(Res.string.feature_account_details_top_bar_title),
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                )
            }
        },
    ) {
        if (state.dialogState == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(DesignToken.padding.large),
                verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.large),
            ) {
                AccountDetailsGrid(
                    details = state.displayItems,
                )

                if (state.transactionList != null) {
                    AccountDetailsGrid(
                        label = stringResource(Res.string.feature_loan_next_installment_label),
                        details = state.transactionList,
                    )
                }

                SavingsAccountActions(
                    items = state.items,
                    onActionClick = {
                        onAction(LoanAccountDetailsAction.OnNavigateToAction(it))
                    },
                )
            }
        }
    }
}

@Composable
internal fun AccountDetailsGrid(
    label: String? = null,
    details: List<LabelValueItem>? = emptyList(),
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.largeIncreased),
    ) {
        if (label != null) {
            Text(
                text = label,
                style = MifosTypography.labelLargeEmphasized,
                color = AppColors.customBlack,
            )
        }
        if (details != null) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
                horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
                maxItemsInEachRow = 2,
            ) {
                details.forEach { item ->
                    MifosLabelValueCard(
                        modifier = Modifier
                            .height(64.dp)
                            .weight(1f),
                        label = stringResource(item.label),
                        value = item.value,
                        color = if (item.label == Res.string.feature_loan_product_type_label) {
                            AppColors
                                .customEnable
                        } else {
                            MaterialTheme.colorScheme.onBackground
                        },
                    )
                }
            }
        }
    }
}

@Composable
internal fun SavingsAccountActions(
    items: ImmutableList<LoanActionItems>,
//    onAction: (LoanAccountDetailsAction) -> Unit,
    onActionClick: (String) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.large),
    ) {
        Text(
            text = stringResource(Res.string.feature_account_details_action),
            style = MifosTypography.labelLargeEmphasized,
            color = AppColors.customBlack,
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
        ) {
            items.forEach { item ->
                MifosActionCard(
                    title = item.title,
                    subTitle = item.subTitle,
                    icon = item.icon,
                    onClick = {
                        onActionClick(item.route)
                    },
                )
            }
        }
    }
}

@Composable
internal fun LoanAccountDialogs(
    dialogState: LoanAccountDetailsState.DialogState?,
    onAction: (LoanAccountDetailsAction) -> Unit,
) {
    when (dialogState) {
        is LoanAccountDetailsState.DialogState.Error -> {
            MifosErrorComponent(
                message = dialogState.message,
                onRetry = { onAction(LoanAccountDetailsAction.OnRetry) },
                isRetryEnabled = true,
            )
        }

        is LoanAccountDetailsState.DialogState.Loading -> {
            MifosProgressIndicator()
        }

        null -> Unit
    }
}

data class LabelValueItem(
    val label: StringResource,
    val value: String,
)

@Preview
@Composable
private fun Account_Details_Overview() {
    MifosMobileTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            LoanAccountDetailsContent(
                state = LoanAccountDetailsState(
                    items = loanAccountActions,
                    dialogState = null,
                ),
                onAction = {},
            )
        }
    }
}
