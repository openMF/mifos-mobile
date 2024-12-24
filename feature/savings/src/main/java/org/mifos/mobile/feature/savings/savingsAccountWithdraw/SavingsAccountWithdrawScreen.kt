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

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.common.utils.getTodayFormatted
import org.mifos.mobile.core.designsystem.components.MifosButton
import org.mifos.mobile.core.designsystem.components.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.components.MifosTopBar
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.MifosTitleDescSingleLineEqual
import org.mifos.mobile.core.ui.component.NoInternet
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.savings.R

@Composable
internal fun SavingsAccountWithdrawScreen(
    navigateBack: (withdrawSuccess: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SavingsAccountWithdrawViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val savingsWithAssociations by viewModel.savingsWithAssociations.collectAsStateWithLifecycle()

    SavingsAccountWithdrawScreen(
        uiState = uiState,
        savingsWithAssociations = savingsWithAssociations,
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
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        MifosTopBar(
            navigateBack = { navigateBack(false) },
            title = { Text(text = stringResource(id = R.string.withdraw_savings_account)) },
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
                    LaunchedEffect(true) {
                        Toast.makeText(
                            context,
                            R.string.savings_account_withdraw_successful,
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                    navigateBack(true)
                }

                is SavingsAccountWithdrawUiState.Error -> {
                    ErrorComponent(errorToast = uiState.message)
                }

                is SavingsAccountWithdrawUiState.WithdrawUiReady -> {}
            }
        }
    }
}

@Composable
private fun ErrorComponent(
    errorToast: String?,
) {
    val context = LocalContext.current
    if (!Network.isConnected(context)) {
        NoInternet(
            error = R.string.no_internet_connection,
            isRetryEnabled = false,
        )
    } else {
        LaunchedEffect(errorToast) {
            Toast.makeText(context, errorToast, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
private fun SavingsAccountWithdrawContent(
    savingsWithAssociations: SavingsWithAssociations?,
    withdraw: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var remark by remember { mutableStateOf(TextFieldValue("")) }
    var remarkFieldError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
    ) {
        MifosTitleDescSingleLineEqual(
            title = stringResource(id = R.string.client_name),
            description = savingsWithAssociations?.clientName ?: "",
        )
        Spacer(modifier = Modifier.height(8.dp))
        MifosTitleDescSingleLineEqual(
            title = stringResource(id = R.string.account_number),
            description = savingsWithAssociations?.accountNo ?: "",
        )
        Spacer(modifier = Modifier.height(8.dp))
        MifosTitleDescSingleLineEqual(
            title = stringResource(id = R.string.withdrawal_date),
            description = getTodayFormatted(),
        )
        Spacer(modifier = Modifier.height(16.dp))
        MifosOutlinedTextField(
            value = remark,
            label = R.string.remark,
            error = remarkFieldError,
            modifier = Modifier.fillMaxWidth(),
            supportingText = stringResource(
                R.string.error_validation_blank,
                stringResource(R.string.remark),
            ),
            onValueChange = {
                remark = it
                remarkFieldError = false
            },
        )
        Spacer(modifier = Modifier.height(16.dp))
        MifosButton(
            textResId = R.string.withdraw_savings_account,
            onClick = {
                if (remark.text.isEmpty()) {
                    remarkFieldError = true
                } else {
                    withdraw.invoke(remark.text)
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

internal class UiStatesParameterProvider : PreviewParameterProvider<SavingsAccountWithdrawUiState> {
    override val values: Sequence<SavingsAccountWithdrawUiState>
        get() = sequenceOf(
            SavingsAccountWithdrawUiState.WithdrawUiReady,
            SavingsAccountWithdrawUiState.Error(message = ""),
            SavingsAccountWithdrawUiState.Loading,
            SavingsAccountWithdrawUiState.Success,
        )
}

@DevicePreviews
@Composable
private fun SavingsAccountWithdrawScreenPreview(
    @PreviewParameter(UiStatesParameterProvider::class)
    savingsAccountWithdrawUiState: SavingsAccountWithdrawUiState,
) {
    MifosMobileTheme {
        SavingsAccountWithdrawScreen(
            uiState = savingsAccountWithdrawUiState,
            savingsWithAssociations = SavingsWithAssociations(
                clientName = "Mifos Mobile",
                accountNo = "0001",
            ),
            navigateBack = {},
            withdraw = {},
        )
    }
}
