/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.application.loanProductDescription

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.core.ui.generated.resources.ic_icon_dashboard
import mifos_mobile.feature.loan_application.generated.resources.Res
import mifos_mobile.feature.loan_application.generated.resources.feature_loan_amendments_and_termination
import mifos_mobile.feature.loan_application.generated.resources.feature_loan_apply_loan
import mifos_mobile.feature.loan_application.generated.resources.feature_loan_continue_legal_compliance
import mifos_mobile.feature.loan_application.generated.resources.feature_loan_default
import mifos_mobile.feature.loan_application.generated.resources.feature_loan_documentation
import mifos_mobile.feature.loan_application.generated.resources.feature_loan_get_loan
import mifos_mobile.feature.loan_application.generated.resources.feature_loan_insurance
import mifos_mobile.feature.loan_application.generated.resources.feature_loan_interest_rate
import mifos_mobile.feature.loan_application.generated.resources.feature_loan_interest_rate_in_numbers
import mifos_mobile.feature.loan_application.generated.resources.feature_loan_jurisdiction
import mifos_mobile.feature.loan_application.generated.resources.feature_loan_repayment
import mifos_mobile.feature.loan_application.generated.resources.feature_loan_sanction_and_disbursement
import mifos_mobile.feature.loan_application.generated.resources.feature_loan_security_and_collateral
import mifos_mobile.feature.loan_application.generated.resources.feature_loan_terms_and_conditions
import mifos_mobile.feature.loan_application.generated.resources.feature_loan_up_to
import mifos_mobile.feature.loan_application.generated.resources.feature_personal_loan_amendments_and_termination_details
import mifos_mobile.feature.loan_application.generated.resources.feature_personal_loan_continue_legal_compliance_details
import mifos_mobile.feature.loan_application.generated.resources.feature_personal_loan_default_details
import mifos_mobile.feature.loan_application.generated.resources.feature_personal_loan_documentation_details
import mifos_mobile.feature.loan_application.generated.resources.feature_personal_loan_insurance_details
import mifos_mobile.feature.loan_application.generated.resources.feature_personal_loan_interest_rate_description
import mifos_mobile.feature.loan_application.generated.resources.feature_personal_loan_jurisdiction_details
import mifos_mobile.feature.loan_application.generated.resources.feature_personal_loan_repayment_details
import mifos_mobile.feature.loan_application.generated.resources.feature_personal_loan_sanction_and_disbursement_details
import mifos_mobile.feature.loan_application.generated.resources.feature_personal_loan_security_and_collateral_details
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.feature.loan.application.component.ApplyLoanBottomBar
import org.mifos.mobile.feature.loan.application.component.LoanCard
import org.mifos.mobile.feature.loan.application.component.TermsAndConditionItem
import mifos_mobile.core.ui.generated.resources.Res as UiRes

@Composable
internal fun LoanProductDetailsScreen(
    navigateBack: () -> Unit,
    navigateToApplyLoanScreen: (productId: Int?, productName: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoanProductDetailsViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val eventFlow = viewModel.eventFlow

    EventsEffect(eventFlow) { event ->
        when (event) {
            is LoanProductDetailsEvent.ApplyLoan ->
                navigateToApplyLoanScreen.invoke(event.productId, event.productName)
            LoanProductDetailsEvent.NavigateBack -> navigateBack.invoke()
        }
    }

    LoanProductDetailsScreenContent(
        state = state,
        modifier = modifier,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    LoanProductDetailsDialog(
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
internal fun LoanProductDetailsDialog(
    state: LoanProductDetailsState,
    onAction: (LoanProductDetailsAction) -> Unit,
) {
    when (state.dialogState) {
        is LoanProductDetailsState.DialogState.Error -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    message = stringResource(state.dialogState.error),
                ),
                onDismissRequest = { onAction(LoanProductDetailsAction.NavigateBack) },
            )
        }

        null -> {}
    }
}

@Composable
internal fun LoanProductDetailsScreenContent(
    state: LoanProductDetailsState,
    modifier: Modifier = Modifier,
    onAction: (LoanProductDetailsAction) -> Unit,
) {
    MifosElevatedScaffold(
        modifier = modifier,
        topBarTitle = stringResource(Res.string.feature_loan_apply_loan, state.productName.lowercase()),
        onNavigateBack = { onAction(LoanProductDetailsAction.NavigateBack) },
        bottomBar = {
            Column {
                when (state.uiState) {
                    ScreenUiState.Success -> {
                        ApplyLoanBottomBar(
                            modifier = Modifier,
                            checked = state.checked,
                            isEnabled = state.isEnabled,
                            onCheckedChange = {
                                onAction(LoanProductDetailsAction.OnChecked(it))
                            },
                            onApplyClick = {
                                onAction(LoanProductDetailsAction.ApplyLoan)
                            },
                        )
                    }

                    else -> { }
                }

                Surface {
                    MifosPoweredCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding(),
                    )
                }
            }
        },
    ) {
        when (state.uiState) {
            ScreenUiState.Loading -> MifosProgressIndicator()

            is ScreenUiState.Error -> {
                MifosErrorComponent(
                    isRetryEnabled = true,
                    message = stringResource(state.uiState.message),
                    onRetry = { onAction(LoanProductDetailsAction.Retry) },
                )
            }

            ScreenUiState.Network -> {
                MifosErrorComponent(
                    isNetworkConnected = state.networkStatus,
                    isRetryEnabled = true,
                    onRetry = { onAction(LoanProductDetailsAction.Retry) },
                )
            }

            ScreenUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(DesignToken.padding.large)
                        .padding(top = DesignToken.padding.large),
                    verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.largeIncreased),
                ) {
                    item {
                        LoanCard(
                            cardImage = UiRes.drawable.ic_icon_dashboard,
                            title = stringResource(Res.string.feature_loan_get_loan, state.productName),
                            amount = stringResource(Res.string.feature_loan_up_to, state.principalText),
                            interestRate = stringResource(
                                Res.string.feature_loan_interest_rate_in_numbers,
                                state.minInterest,
                                state.maxInterest,
                            ),
                        )
                    }

                    item {
                        Text(
                            text = stringResource(Res.string.feature_loan_terms_and_conditions),
                            style = MifosTypography.titleMediumEmphasized,
                        )
                        Spacer(modifier = Modifier.height(DesignToken.spacing.largeIncreased))
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.extraSmall),
                        ) {
                            TermsAndConditionItem(
                                title = Res.string.feature_loan_sanction_and_disbursement,
                                description = Res.string
                                    .feature_personal_loan_sanction_and_disbursement_details,
                            )

                            TermsAndConditionItem(
                                title = Res.string.feature_loan_interest_rate,
                                description = Res.string.feature_personal_loan_interest_rate_description,
                            )

                            TermsAndConditionItem(
                                title = Res.string.feature_loan_repayment,
                                description = Res.string.feature_personal_loan_repayment_details,
                            )

                            TermsAndConditionItem(
                                title = Res.string.feature_loan_security_and_collateral,
                                description = Res.string.feature_personal_loan_security_and_collateral_details,
                            )

                            TermsAndConditionItem(
                                title = Res.string.feature_loan_insurance,
                                description = Res.string.feature_personal_loan_insurance_details,
                            )

                            TermsAndConditionItem(
                                title = Res.string.feature_loan_default,
                                description = Res.string.feature_personal_loan_default_details,
                            )

                            TermsAndConditionItem(
                                title = Res.string.feature_loan_documentation,
                                description = Res.string.feature_personal_loan_documentation_details,
                            )

                            TermsAndConditionItem(
                                title = Res.string.feature_loan_continue_legal_compliance,
                                description = Res.string.feature_personal_loan_continue_legal_compliance_details,
                            )

                            TermsAndConditionItem(
                                title = Res.string.feature_loan_amendments_and_termination,
                                description = Res.string.feature_personal_loan_amendments_and_termination_details,
                            )

                            TermsAndConditionItem(
                                title = Res.string.feature_loan_jurisdiction,
                                description = Res.string.feature_personal_loan_jurisdiction_details,
                            )
                        }
                    }
                }
            }
            else -> { }
        }
    }
}
