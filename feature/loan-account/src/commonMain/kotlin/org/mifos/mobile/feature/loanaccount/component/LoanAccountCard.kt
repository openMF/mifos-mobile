/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loanaccount.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography

@Composable
internal fun LoanAccountCard(
    loanId: Long,
    date: String,
    amount: String,
    status: String,
    onLoanClick: (Long) -> Unit,
    onPaymentClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val loanStatusColor = when (status.lowercase()) {
        "paid" -> Color(0xFF4CAF50)
        "due" -> Color(0xFFFF5722)
        "not active" -> Color(0xFFFF9800)
        else -> MaterialTheme.colorScheme.secondary
    }

    val isPaid = status.lowercase() == "paid"

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onLoanClick(loanId) }
            .padding(vertical = DesignToken.padding.medium),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape,
                    )
                    .size(36.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = loanId.toString().padStart(2, '0'),
                    style = MifosTypography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }

            Spacer(modifier = Modifier.width(DesignToken.spacing.medium))

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = date,
                    style = MifosTypography.titleSmallEmphasized,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Text(
                    text = amount,
                    style = MifosTypography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondary,
                )
                Text(
                    text = status,
                    style = MifosTypography.labelSmall,
                    color = loanStatusColor,
                )
            }

            Spacer(modifier = Modifier.width(DesignToken.spacing.medium))

            Button(
                onClick = {
                    if (!isPaid) {
                        onPaymentClick(loanId)
                    }
                },
                enabled = !isPaid,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.onSurface,
                ),
                modifier = Modifier.size(width = 60.dp, height = 32.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    text = if (isPaid) "Paid" else "Pay",
                    style = MifosTypography.labelSmall,
                    color = if (isPaid) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun Loan_Account_Preview() {
    MifosMobileTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(DesignToken.padding.large),
            verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
        ) {
            LoanAccountCard(
                loanId = 1,
                date = "4 March 2025",
                amount = "$ 5000",
                status = "Paid",
                onLoanClick = {},
                onPaymentClick = {},
            )

            LoanAccountCard(
                loanId = 2,
                date = "4 May 2025",
                amount = "$ 5000",
                status = "Due",
                onLoanClick = {},
                onPaymentClick = {},
            )

            LoanAccountCard(
                loanId = 3,
                date = "4 June 2025",
                amount = "$ 5000",
                status = "Not Active",
                onLoanClick = {},
                onPaymentClick = {},
            )
        }
    }
}
