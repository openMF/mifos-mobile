/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savings.savingsAccountApplication

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.designsystem.components.MifosScaffold
import org.mifos.mobile.core.designsystem.components.MifosTopBar
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.model.enums.SavingsAccountState
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.savings.R

@Composable
internal fun SavingsAccountApplicationScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SavingsAccountApplicationViewModel = hiltViewModel(),
) {
    val uiState by viewModel.savingsAccountApplicationUiState.collectAsStateWithLifecycle()

    SavingsAccountApplicationScreen(
        uiState = uiState,
        navigateBack = navigateBack,
        submit = viewModel::onSubmit,
        modifier = modifier,
    )
}

@Composable
private fun SavingsAccountApplicationScreen(
    uiState: SavingsAccountApplicationUiState,
    navigateBack: () -> Unit,
    submit: (Int, Int, showToast: (Int) -> Unit) -> Unit,
    modifier: Modifier = Modifier,
    savingsWithAssociations: SavingsWithAssociations? = null,
) {
    var topBarTitleText by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    MifosScaffold(
        topBar = {
            MifosTopBar(
                navigateBack = navigateBack,
                title = { Text(text = topBarTitleText) },
            )
        },
        modifier = modifier,
        content = {
            Box(modifier = Modifier.padding(it)) {
                when (uiState) {
                    is SavingsAccountApplicationUiState.Error -> MifosErrorComponent()

                    is SavingsAccountApplicationUiState.Loading -> {
                        MifosProgressIndicatorOverlay()
                    }

                    is SavingsAccountApplicationUiState.ShowUserInterface -> {
                        val (titleResourceId, existingProduct) = when (uiState.requestType) {
                            SavingsAccountState.CREATE -> R.string.apply_savings_account to null
                            else -> R.string.update_savings_account to savingsWithAssociations?.savingsProductName
                        }

                        topBarTitleText = stringResource(id = titleResourceId)
                        SavingsAccountApplicationContent(
                            submit = submit,
                            existingProduct = existingProduct,
                            savingsAccountTemplate = uiState.template,
                        )
                    }

                    is SavingsAccountApplicationUiState.Success -> {
                        val messageResourceId = when (uiState.requestType) {
                            SavingsAccountState.CREATE -> R.string.new_saving_account_created_successfully
                            else -> R.string.saving_account_updated_successfully
                        }
                        Toast.makeText(
                            context,
                            stringResource(id = messageResourceId),
                            Toast.LENGTH_SHORT,
                        ).show()
                        navigateBack.invoke()
                    }
                }
            }
        },
    )
}

@DevicePreviews
@Composable
private fun SavingsAccountApplicationScreenPreview() {
    MifosMobileTheme {
        SavingsAccountApplicationScreen(
            SavingsAccountApplicationUiState.Success(requestType = SavingsAccountState.UPDATE),
            navigateBack = {},
            submit = { _, _, _ -> },
            savingsWithAssociations = null,
        )
    }
}
