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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount

@Composable
fun AccountCard(
    loanAccount: LoanAccount,
    accountStatus: String?,
    balance: String,
    indicatorColor: Color,
    textColor: Color?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Box {
            AccountTypeItemIndicator(
                indicatorColor,
                modifier = Modifier.align(Alignment.CenterStart),
            )
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    loanAccount.accountNo?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                    loanAccount.productName?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    if (accountStatus != null) {
                        Text(
                            text = accountStatus,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                if (textColor != null) {
                    Text(
                        text = balance,
                        modifier = Modifier.padding(end = 16.dp),
                        color = textColor,
                    )
                }
            }
        }
    }
}
