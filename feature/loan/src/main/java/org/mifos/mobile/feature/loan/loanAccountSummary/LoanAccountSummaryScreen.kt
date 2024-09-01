/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.loanAccountSummary

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.designsystem.components.MifosScaffold
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.component.MifosTextTitleDescDrawableSingleLine
import org.mifos.mobile.core.ui.component.MifosTextTitleDescSingleLine
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.loan.R

@Composable
internal fun LoanAccountSummaryScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoanAccountSummaryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.loanUiState.collectAsStateWithLifecycle()

    LoanAccountSummaryScreen(
        uiState = uiState,
        navigateBack = navigateBack,
        modifier = modifier,
    )
}

@Composable
private fun LoanAccountSummaryScreen(
    uiState: LoanAccountSummaryUiState,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    MifosScaffold(
        topBarTitleResId = R.string.loan_summary,
        navigateBack = navigateBack,
        modifier = modifier,
        content = {
            Box(modifier = Modifier.padding(it)) {
                when (uiState) {
                    is LoanAccountSummaryUiState.Loading -> {
                        MifosProgressIndicatorOverlay()
                    }

                    is LoanAccountSummaryUiState.Error -> {
                        MifosErrorComponent(isNetworkConnected = Network.isConnected(context))
                    }

                    is LoanAccountSummaryUiState.Success -> {
                        LoanAccountSummaryContent(
                            loanWithAssociations = uiState.loanWithAssociations,
                        )
                    }
                }
            }
        },
    )
}

@Composable
private fun LoanAccountSummaryContent(
    loanWithAssociations: LoanWithAssociations?,
    modifier: Modifier = Modifier,
) {
    var currencySymbol = loanWithAssociations?.currency?.displaySymbol
    if (currencySymbol == null) {
        currencySymbol = loanWithAssociations?.currency?.code ?: ""
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        MifosTextTitleDescSingleLine(
            modifier = Modifier.padding(horizontal = 14.dp),
            title = stringResource(id = R.string.account_short),
            description = loanWithAssociations?.accountNo ?: "",
        )

        MifosTextTitleDescSingleLine(
            modifier = Modifier.padding(horizontal = 14.dp),
            title = stringResource(id = R.string.loan_product),
            description = loanWithAssociations?.loanProductName ?: "",
        )

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
            ) {
                MifosTextTitleDescSingleLine(
                    title = stringResource(id = R.string.principal),
                    description = stringResource(
                        id = R.string.string_and_double,
                        currencySymbol,
                        loanWithAssociations?.principal ?: 0.0,
                    ),
                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(id = R.string.interest),
                    description = stringResource(
                        id = R.string.string_and_double,
                        currencySymbol,
                        loanWithAssociations?.summary?.interestCharged ?: 0.0,
                    ),
                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(id = R.string.fees),
                    description = stringResource(
                        id = R.string.string_and_double,
                        currencySymbol,
                        loanWithAssociations?.summary?.feeChargesCharged ?: 0.0,
                    ),
                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(id = R.string.penalties),
                    description = stringResource(
                        id = R.string.string_and_double,
                        currencySymbol,
                        loanWithAssociations?.summary?.penaltyChargesCharged ?: 0.0,
                    ),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
            ) {
                MifosTextTitleDescSingleLine(
                    title = stringResource(id = R.string.total_repayment),
                    description = stringResource(
                        id = R.string.string_and_double,
                        currencySymbol,
                        loanWithAssociations?.summary?.totalExpectedRepayment ?: 0.0,
                    ),
                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(id = R.string.total_paid),
                    description = stringResource(
                        id = R.string.string_and_double,
                        currencySymbol,
                        loanWithAssociations?.summary?.totalRepayment ?: 0.0,
                    ),
                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(id = R.string.interest_waived),
                    description = stringResource(
                        id = R.string.string_and_double,
                        currencySymbol,
                        loanWithAssociations?.summary?.interestWaived ?: 0.0,
                    ),
                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(id = R.string.penalties_waived),
                    description = stringResource(
                        id = R.string.string_and_double,
                        currencySymbol,
                        loanWithAssociations?.summary?.penaltyChargesWaived ?: 0.0,
                    ),
                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(id = R.string.fees_waived),
                    description = stringResource(
                        id = R.string.string_and_double,
                        currencySymbol,
                        loanWithAssociations?.summary?.feeChargesWaived ?: 0.0,
                    ),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
            ) {
                MifosTextTitleDescSingleLine(
                    title = stringResource(id = R.string.outstanding_balance),
                    description = stringResource(
                        id = R.string.string_and_double,
                        currencySymbol,
                        loanWithAssociations?.summary?.totalOutstanding ?: 0.0,
                    ),
                )
                MifosTextTitleDescDrawableSingleLine(
                    title = stringResource(id = R.string.account_status),
                    description = if (loanWithAssociations?.status?.active == true) {
                        stringResource(
                            id = R.string.active_uc,
                        )
                    } else {
                        stringResource(id = R.string.inactive_uc)
                    },
                    imageResId = if (loanWithAssociations?.status?.active == true) {
                        R.drawable.ic_check_circle_green_24px
                    } else {
                        R.drawable.ic_report_problem_red_24px
                    },
                )
            }
        }
    }
}

@DevicePreviews
@Composable
private fun LoanAccountSummaryPreview() {
    MifosMobileTheme {
        LoanAccountSummaryScreen(
            navigateBack = {},
            uiState = LoanAccountSummaryUiState.Success(loanWithAssociations = null),
        )
    }
}
