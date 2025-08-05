/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loanaccount.loanAccountRepaymentSchedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.loan_account.generated.resources.Res
import mifos_mobile.feature.loan_account.generated.resources.repayment_schedule
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.accounts.loan.Periods
import org.mifos.mobile.core.ui.component.MifosDetailsCard
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.feature.loanaccount.component.RepaymentScheduleItem

@Composable
internal fun ChargeDetailScreen(
    navigateBack: () -> Unit,
    viewModel: RepaymentScheduleViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            RepaymentScheduleEvent.NavigateBack -> navigateBack.invoke()
        }
    }

    RepaymentScreenContent(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    RepaymentDialogs(
        dialogState = state.dialogState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
internal fun RepaymentScreenContent(
    state: RepaymentScheduleState,
    onAction: (RepaymentScheduleAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        topBarTitle = stringResource(Res.string.repayment_schedule),
        onNavigateBack = {
            onAction(RepaymentScheduleAction.OnNavigateBack)
        },
        modifier = modifier,
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
        if (state.dialogState == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        vertical = DesignToken.padding.extraLarge,
                        horizontal = DesignToken.padding.large,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                MifosDetailsCard(keyValuePairs = state.basicDetails)

                Spacer(Modifier.height(DesignToken.padding.large))

                RepaymentScheduleList(
                    periods = state.loanWithAssociations?.repaymentSchedule?.periods.orEmpty(),
                    currencyCode = state.loanWithAssociations?.currency?.code ?: "",
                    maxDigits = state.loanWithAssociations?.currency?.decimalPlaces?.toInt(),
                    onPayClick = { period ->
                    },
                )
            }
        }
    }
}

@Composable
fun RepaymentScheduleList(
    periods: List<Periods>,
    currencyCode: String?,
    maxDigits: Int?,
    modifier: Modifier = Modifier,
    onPayClick: (Periods) -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
    ) {
        periods.forEach { period ->
            RepaymentScheduleItem(
                period = period,
                currencyCode = currencyCode,
                maxDigits = maxDigits,
                onPayClick = { onPayClick(period) },
            )
        }
    }
}

@Composable
internal fun RepaymentDialogs(
    dialogState: RepaymentScheduleState.DialogState?,
    onAction: (RepaymentScheduleAction) -> Unit,
) {
    when (dialogState) {
        is RepaymentScheduleState.DialogState.Error -> {
            MifosErrorComponent(
                message = dialogState.message,
                onRetry = { onAction(RepaymentScheduleAction.RetryClicked) },
                isRetryEnabled = true,
            )
        }

        is RepaymentScheduleState.DialogState.Loading -> {
            MifosProgressIndicator()
        }

        null -> Unit
    }
}

@Preview
@Composable
private fun Repayment_Preview() {
    MifosMobileTheme {
        Column(
            modifier = Modifier.padding(DesignToken.padding.large),
        ) {
            RepaymentScreenContent(
                state = RepaymentScheduleState(dialogState = null),
                onAction = {},
            )
        }
    }
}
