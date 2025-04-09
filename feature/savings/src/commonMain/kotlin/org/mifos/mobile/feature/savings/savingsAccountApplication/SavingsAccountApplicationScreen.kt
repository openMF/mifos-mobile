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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import mifos_mobile.feature.savings.generated.resources.Res
import mifos_mobile.feature.savings.generated.resources.apply_savings_account
import mifos_mobile.feature.savings.generated.resources.update_savings_account
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.model.enums.SavingsAccountState
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay

@Composable
internal fun SavingsAccountApplicationScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SavingsAccountApplicationViewModel = koinViewModel(),
) {
    val uiState by viewModel.savingsAccountApplicationUiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    SavingsAccountApplicationScreen(
        uiState = uiState,
        navigateBack = navigateBack,
        submit = viewModel::onSubmit,
        onRetry = viewModel::onRetry,
        modifier = modifier,
        snackbarHostState = snackbarHostState,
    )

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(event.message)
                    }
                }
                UiEvent.NavigateBack -> navigateBack.invoke()
            }
        }
    }
}

@Composable
private fun SavingsAccountApplicationScreen(
    uiState: SavingsAccountApplicationUiState,
    navigateBack: () -> Unit,
    submit: (Int, Int, showToast: (StringResource) -> Unit) -> Unit,
    onRetry: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    savingsWithAssociations: SavingsWithAssociations? = null,
) {
    var topBarTitleText by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    MifosScaffold(
        backPress = navigateBack,
        topBarTitle = topBarTitleText,
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = {
            Box(modifier = Modifier.padding(it)) {
                when (uiState) {
                    is SavingsAccountApplicationUiState.Error -> {
                        MifosErrorComponent(
                            onRetry = onRetry,
                        )
                    }

                    is SavingsAccountApplicationUiState.Loading -> {
                        MifosProgressIndicatorOverlay()
                    }

                    is SavingsAccountApplicationUiState.ShowUserInterface -> {
                        val (titleResourceId, existingProduct) = when (uiState.requestType) {
                            SavingsAccountState.CREATE -> Res.string.apply_savings_account to null
                            else -> Res.string.update_savings_account to savingsWithAssociations?.savingsProductName
                        }

                        topBarTitleText = stringResource(titleResourceId)
                        SavingsAccountApplicationContent(
                            submit = submit,
                            existingProduct = existingProduct,
                            savingsAccountTemplate = uiState.template,
                        )
                    }
                }
            }
        },
    )
}
