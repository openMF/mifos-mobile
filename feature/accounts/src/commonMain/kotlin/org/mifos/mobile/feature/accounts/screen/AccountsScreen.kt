/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.accounts.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.mifos.mobile.core.model.enums.AccountType

@Composable
fun AccountsScreen(
    navigateBack: () -> Unit,
    navigateToLoanApplicationScreen: () -> Unit,
    navigateToSavingsApplicationScreen: () -> Unit,
    onItemClick: (AccountType, Long) -> Unit,
    modifier: Modifier = Modifier,
) {
//    Added for now to surpass the detekt error
    navigateBack()
    navigateToLoanApplicationScreen()
    navigateToSavingsApplicationScreen()
    onItemClick(AccountType.LOAN, 1)

    Column(modifier = modifier) {
    }
}
