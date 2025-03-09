/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savings.savingsAccountWithdraw

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import mifos_mobile.feature.savings.generated.resources.Res
import mifos_mobile.feature.savings.generated.resources.account_number
import mifos_mobile.feature.savings.generated.resources.client_name
import mifos_mobile.feature.savings.generated.resources.error_validation_blank
import mifos_mobile.feature.savings.generated.resources.remark
import mifos_mobile.feature.savings.generated.resources.savings_account_withdraw_successful
import mifos_mobile.feature.savings.generated.resources.withdraw_savings_account
import mifos_mobile.feature.savings.generated.resources.withdrawal_date
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.common.FileUtils.Companion.logger
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.component.MifosTextFieldConfig
import org.mifos.mobile.core.designsystem.component.MifosTopBar
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.MifosTitleDescSingleLineEqual

@Composable
internal fun SavingsAccountWithdrawScreen(
    navigateBack: (withdrawSuccess: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SavingsAccountWithdrawViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val savingsWithAssociations by viewModel.savingsWithAssociations.collectAsStateWithLifecycle()

    SavingsAccountWithdrawScreen(
        uiState = uiState,
        savingsWithAssociations = savingsWithAssociations?.data,
        navigateBack = navigateBack,
        withdraw = viewModel::submitWithdrawSavingsAccount,
        modifier = modifier,
    )
}

@Composable
private fun SavingsAccountWithdrawScreen(
    uiState: SavingsAccountWithdrawUiState,
    savingsWithAssociations: SavingsWithAssociations?,
    navigateBack: (withdrawSuccess: Boolean) -> Unit,
    withdraw: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        MifosTopBar(
            backPress = { navigateBack(false) },
            topBarTitle = stringResource(Res.string.withdraw_savings_account),
        )

        Box(modifier = Modifier.weight(1f)) {
            SavingsAccountWithdrawContent(
                savingsWithAssociations = savingsWithAssociations,
                withdraw = withdraw,
            )

            when (uiState) {
                is SavingsAccountWithdrawUiState.Loading -> {
                    MifosProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f)),
                    )
                }

                is SavingsAccountWithdrawUiState.Success -> {
                    logger.e { "Savings Account WithDraw Success" }
                    LaunchedEffect(true) {
                        scope.launch {
                            snackbarHostState.showSnackbar(Res.string.savings_account_withdraw_successful.toString())
                        }
                    }
                    navigateBack(true)
                }

                is SavingsAccountWithdrawUiState.Error -> {
                    logger.e { "Savings Account WithDraw ${uiState.message}" }
                    LaunchedEffect(true) {
                        scope.launch {
                            uiState.message?.let { snackbarHostState.showSnackbar(it) }
                        }
                    }
                }

                is SavingsAccountWithdrawUiState.WithdrawUiReady -> {}
            }
        }
    }
}

@Composable
private fun SavingsAccountWithdrawContent(
    savingsWithAssociations: SavingsWithAssociations?,
    withdraw: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var remark by remember { mutableStateOf("") }
    var remarkFieldError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
    ) {
        MifosTitleDescSingleLineEqual(
            title = stringResource(Res.string.client_name),
            description = savingsWithAssociations?.clientName ?: "",
        )
        Spacer(modifier = Modifier.height(8.dp))
        MifosTitleDescSingleLineEqual(
            title = stringResource(Res.string.account_number),
            description = savingsWithAssociations?.accountNo ?: "",
        )
        Spacer(modifier = Modifier.height(8.dp))
        MifosTitleDescSingleLineEqual(
            title = stringResource(Res.string.withdrawal_date),
            description = DateHelper.formattedFullDate,
        )
        Spacer(modifier = Modifier.height(16.dp))
        MifosOutlinedTextField(
            value = remark,
            label = stringResource(Res.string.remark),
            config = MifosTextFieldConfig(
                errorText = stringResource(
                    Res.string.error_validation_blank,
                    stringResource(Res.string.remark),
                ),
                isError = remarkFieldError,
            ),
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {
                remark = it
                remarkFieldError = false
            },
        )
        Spacer(modifier = Modifier.height(16.dp))
        MifosButton(
            content = { Text(stringResource(Res.string.withdraw_savings_account)) },
            onClick = {
                if (remark.isEmpty()) {
                    remarkFieldError = true
                } else {
                    withdraw.invoke(remark)
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
