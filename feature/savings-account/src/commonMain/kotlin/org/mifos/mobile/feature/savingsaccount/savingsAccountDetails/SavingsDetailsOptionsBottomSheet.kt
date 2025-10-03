/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savingsaccount.savingsAccountDetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsDetailsOptionsBottomSheet(
    onDismissRequest: () -> Unit,
    onOptionSelected: (SavingsDetailsOption) -> Unit,
    // Used to conditionally enable/disable options based on account status (Acceptance Criteria)
    isActive: Boolean,
) {
    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column(modifier = Modifier.padding(bottom = DesignToken.spacing.large)) {
            // Header for the Bottom Sheet (Optional but good UX)
            Text(
                text = "Account Options",
                // Replace with stringResource if available
                modifier = Modifier.padding(DesignToken.padding.large),
                style = MaterialTheme.typography.titleMedium,
            )

            // Iterate through all defined options
            SAVINGS_DETAILS_OPTIONS.forEach { option ->

                // Determine if the option should be clickable based on the Acceptance Criteria
                val isEnabled = when (option) {
                    // All options are available regardless of status, but we must restrict the actual *action*
                    // For the simplest implementation, we enable all for now, as per the scope.
                    // If an option like 'Charges' should be grayed out for 'Closed' accounts, you'd add:
                    // SavingsDetailsOption.Charges -> status != "Closed"
                    else -> true
                }

                ListItem(
                    modifier = Modifier.clickable(enabled = isEnabled) {
                        onOptionSelected(option)
                        onDismissRequest()
                    },
                    headlineContent = {
                        Text(
                            text = option.label,
                            color = if (isEnabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            style = MifosTypography.bodyMedium,
                            // Example style
                        )
                    },
                )
            }
        }
    }
}
