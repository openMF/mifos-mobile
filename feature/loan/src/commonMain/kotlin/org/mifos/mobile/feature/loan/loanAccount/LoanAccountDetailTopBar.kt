/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
@file:Suppress("unusedPrivateProperty", "unusedParameter")

package org.mifos.mobile.feature.loan.loanAccount

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme

@Suppress("UnusedParameter", "UnusedPrivateProperty")
@Composable
internal fun LoanAccountDetailTopBar(
    navigateBack: () -> Unit,
    viewGuarantor: () -> Unit,
    updateLoan: () -> Unit,
    withdrawLoan: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showMenu by remember { mutableStateOf(false) }

//    MifosTopAppBar(
//        modifier = modifier,
//        topBarTitle = stringResource(Res.string.loan_account_details),
//        backPress = navigateBack,
//        actions = {
//            IconButton(onClick = { showMenu = !showMenu }) {
//                Icon(
//                    imageVector = MifosIcons.MoreVert,
//                    contentDescription = "Menu",
//                )
//            }
//            DropdownMenu(
//                expanded = showMenu,
//                modifier = Modifier.padding(start = 16.dp, end = 32.dp),
//                onDismissRequest = { showMenu = false },
//            ) {
//                DropdownMenuItem(
//                    text = {
//                        Text(text = stringResource(Res.string.view_guarantor))
//                    },
//                    onClick = viewGuarantor,
//                )
//                DropdownMenuItem(
//                    text = {
//                        Text(text = stringResource(Res.string.update_loan))
//                    },
//                    onClick = updateLoan,
//                )
//                DropdownMenuItem(
//                    text = {
//                        Text(text = stringResource(Res.string.withdraw_loan))
//                    },
//                    onClick = withdrawLoan,
//                )
//            }
//        },
//    )
}

@Preview
@Composable
private fun LoanAccountDetailTopBarPreview() {
    MifosMobileTheme {
        LoanAccountDetailTopBar(
            navigateBack = {},
            viewGuarantor = {},
            updateLoan = {},
            withdrawLoan = {},
        )
    }
}
