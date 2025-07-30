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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography

@Composable
fun LoanAccountCard(
    loanId: Long,
    date: String,
    amount: String,
    status: String,
    onLoanClick: (Long) -> Unit,
    onPaymentClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val loanStatusColor = when (status.lowercase()) {
        "Paid".lowercase() -> AppColors.customEnable
        "due" -> MaterialTheme.colorScheme.error
        else -> AppColors.customYellow
    }

    val isPaid = status.lowercase() == "Paid".lowercase()
    val isNotActive = !isPaid && status.lowercase() != "due".lowercase()

    Box(
        modifier = modifier
            .padding(vertical = DesignToken.padding.medium, horizontal = DesignToken.padding.large)
            .fillMaxWidth()
            .border(
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.secondaryContainer),
                shape = RoundedCornerShape(12.dp),
            )
            .clickable { onLoanClick(loanId) }
            .padding(DesignToken.padding.medium)
            .then(
                if (isNotActive) Modifier.alpha(0.6f) else Modifier,
            ),
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
                    style = MifosTypography.titleSmallEmphasized,
                    color = AppColors.customWhite,
                )
            }

            Spacer(modifier = Modifier.width(DesignToken.spacing.medium))

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = date,
                    style = MifosTypography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                )
                Text(
                    text = amount,
                    style = MifosTypography.titleSmallEmphasized,
                    color = AppColors.customBlack,
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
                    if (!isPaid && !isNotActive) {
                        onPaymentClick(loanId)
                    }
                },
                enabled = !isPaid && !isNotActive,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = if (isPaid) {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    } else {
                        MaterialTheme.colorScheme.onPrimary
                    },
                ),
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier.size(width = 60.dp, height = 32.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    text = if (isPaid) "Paid" else "pay",
                    style = MifosTypography.labelLarge,
                    color = if (isPaid) {
                        MaterialTheme.colorScheme.outline
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
                .fillMaxSize(),
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
