/*
 * Copyright 2026 Mifos Initiative
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
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import org.mifos.mobile.core.common.Constants.TRANSFER_PAY_TO
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.model.StatusNavigationDestination
import org.mifos.mobile.core.model.entity.AccountDetails
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.core.ui.component.MifosActionCard
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosLabelValueCard
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.feature.loanaccount.component.LoanActionItems
import org.mifos.mobile.feature.loanaccount.component.loanAccountActions
import template.core.base.designsystem.theme.KptTheme

/**
 * The main composable for the loan account details screen.
 * It displays the details of a loan account and provides actions that can be performed on it.
 *
 * @param navigateBack A callback to navigate back to the previous screen.
 * @param navigateToMakePaymentScreen A callback to navigate to the make payment screen.
 * @param navigateToRepaymentScheduleScreen A callback to navigate to the repayment schedule screen.
 * @param navigateToLoanSummaryScreen A callback to navigate to the loan summary screen.
 * @param navigateToQrCodeScreen A callback to navigate to the QR code screen.
 * @param navigateToClientChargeScreen A callback to navigate to the client charge screen.
 * @param navigateToLoanAccountTransactionScreen A callback to navigate to the loan account transaction screen.
 * @param viewModel The [LoanAccountDetailsViewModel] for this screen.
 */
@Composable
internal fun LoanAccountDetailsScreen(
    navigateBack: () -> Unit,
    navigateToMakePaymentScreen: (AccountDetails) -> Unit,
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
                        val transferArgs = AccountDetails(
                            accountId = uiState.accountId,
                            transferType = TRANSFER_PAY_TO,
                            transferTarget = TransferType.SELF,
                            transferSuccessDestination = StatusNavigationDestination.LOAN_ACCOUNT.name,
                        )
                        navigateToMakePaymentScreen(transferArgs)
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

/**
 * The content of the loan account details screen.
 * It displays the account details and actions in a scaffold.
 *
 * @param state The [LoanAccountDetailsState] for this screen.
 * @param onAction A callback to handle actions from the screen.
 * @param modifier The modifier to be applied to the component.
 */
@Composable
internal fun LoanAccountDetailsContent(
    state: LoanAccountDetailsState,
    onAction: (LoanAccountDetailsAction) -> Unit,
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
        when (state.uiState) {
            is ScreenUiState.Error -> {
                MifosErrorComponent(
                    isRetryEnabled = true,
                    message = stringResource(state.uiState.message),
                    onRetry = { onAction(LoanAccountDetailsAction.OnRetry) },
                )
            }

            ScreenUiState.Loading -> MifosProgressIndicator()

            ScreenUiState.Network -> {
                MifosErrorComponent(
                    isNetworkConnected = state.networkStatus,
                    isRetryEnabled = true,
                    onRetry = { onAction(LoanAccountDetailsAction.OnRetry) },
                )
            }
            ScreenUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(KptTheme.spacing.md),
                    verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.md),
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

                    val visibleActions = state.accountStatus?.allowedActions ?: emptySet()

                    SavingsAccountActions(
                        visibleActions = visibleActions,
                        onActionClick = {
                            onAction(LoanAccountDetailsAction.OnNavigateToAction(it))
                        },
                    )
                }
            }
            else -> { }
        }
    }
}

/**
 * A composable that displays a grid of account details.
 *
 * @param label The label for the grid.
 * @param details A list of [LabelValueItem]s to display.
 */
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
                color = KptTheme.colorScheme.onSurface,
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
                            .defaultMinSize(minHeight = DesignToken.sizes.cardDp64)
                            .weight(1f),
                        label = stringResource(item.label),
                        value = item.value,
                        color = if (item.label == Res.string.feature_loan_product_type_label) {
                            AppColors
                                .customEnable
                        } else {
                            KptTheme.colorScheme.onBackground
                        },
                    )
                }
            }
        }
    }
}

/**
 * A composable that displays the actions that can be performed on a savings account.
 *
 * @param visibleActions A set of [LoanActionItems] that should be visible.
 * @param onActionClick A callback that is invoked when an action is clicked.
 */
@Composable
internal fun SavingsAccountActions(
    visibleActions: Set<LoanActionItems>,
    onActionClick: (String) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.md),
    ) {
        Text(
            text = stringResource(Res.string.feature_account_details_action),
            style = MifosTypography.labelLargeEmphasized,
            color = KptTheme.colorScheme.onSurface,
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
        ) {
            visibleActions
                .forEach { item ->
                    MifosActionCard(
                        title = item.title,
                        subTitle = item.subTitle,
                        icon = item.icon,
                        onClick = { onActionClick(item.route) },
                    )
                }
        }
    }
}

/**
 * A composable that displays a dialog for the loan account details screen.
 * It can show an error dialog.
 *
 * @param dialogState The state of the dialog to display.
 * @param onAction A callback to handle actions from the dialog.
 */
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

        null -> Unit
    }
}

/**
 * A data class that represents a label-value pair.
 *
 * @property label The label.
 * @property value The value.
 */
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
