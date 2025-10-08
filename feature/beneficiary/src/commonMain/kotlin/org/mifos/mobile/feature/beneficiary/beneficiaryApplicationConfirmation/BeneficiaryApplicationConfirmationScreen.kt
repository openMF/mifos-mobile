/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.beneficiary.beneficiaryApplicationConfirmation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.beneficiary.generated.resources.Res
import mifos_mobile.feature.beneficiary.generated.resources.confirm_details
import mifos_mobile.feature.beneficiary.generated.resources.validate_details
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosDetailsCard
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.core.ui.utils.ScreenUiState

/**
 * Composable function to render the beneficiary application confirmation screen.
 *
 * @param navigateBack a function to navigate back to the previous screen.
 * @param navigateToStatusScreen a function to navigate to the status screen.
 * @param navigateToAuthenticateScreen a function to navigate to the authenticate screen.
 * @param viewModel the view model for the beneficiary application confirmation screen.
 */
@Composable
internal fun BeneficiaryApplicationConfirmationScreen(
    navigateBack: () -> Unit,
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
    viewModel: BeneficiaryApplicationConfirmationViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            BeneficiaryApplicationConfirmationEvent.Navigate -> {
                navigateBack.invoke()
            }
            is BeneficiaryApplicationConfirmationEvent.NavigateToStatus -> {
                navigateToStatusScreen.invoke(
                    event.eventType,
                    event.eventDestination,
                    event.title,
                    event.subtitle,
                    event.buttonText,
                )
            }

            is BeneficiaryApplicationConfirmationEvent.NavigateToAuthenticate -> {
                navigateToAuthenticateScreen.invoke()
            }
        }
    }
    BeneficiaryApplicationConfirmationScreenContent(
        state = state,
        onAction = remember {
            {
                viewModel.trySendAction(it)
            }
        },
    )
    BeneficiaryApplicationConfirmationScreenDialogs(
        state = state,
    )
}

/**
 * Composable function to display error dialogs for the Beneficiary Application Confirmation screen.
 * @param state the state of the Beneficiary Application Confirmation screen.
 * @return a Unit or null if no dialog should be shown.
 */
@Composable
private fun BeneficiaryApplicationConfirmationScreenDialogs(
    state: BeneficiaryApplicationConfirmationState,
) {
    when (state.dialogState) {
        BeneficiaryApplicationConfirmationState.DialogState.Network -> {
            MifosErrorComponent(
                isNetworkConnected = !state.networkUnavailable,
            )
        }
        null -> Unit
    }
}

/**
 * Composable function to render the content of the Beneficiary Application confirmation screen.
 *
 * @param state the state of the Beneficiary Application confirmation screen.
 * @param modifier the modifier to apply to the content.
 * @param onAction the action to perform when the confirm button is clicked.
 * @return a Unit or null if no content should be rendered.
 */
@Composable
fun BeneficiaryApplicationConfirmationScreenContent(
    state: BeneficiaryApplicationConfirmationState,
    modifier: Modifier = Modifier,
    onAction: (BeneficiaryApplicationConfirmationAction) -> Unit,
) {
    MifosElevatedScaffold(
        topBarTitle = stringResource(state.topBarTitle),
        onNavigateBack = { onAction(BeneficiaryApplicationConfirmationAction.OnNavigate) },
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                )
            }
        },
    ) {
        when (state.uiState) {
            ScreenUiState.Loading -> MifosProgressIndicator()

            ScreenUiState.Network -> {
                MifosErrorComponent(
                    isNetworkConnected = state.networkStatus,
                )
            }

            ScreenUiState.Success -> {
                Column(
                    Modifier.padding(DesignToken.padding.large),
                    verticalArrangement = Arrangement.spacedBy(DesignToken.padding.largeIncreased),
                ) {
                    Text(
                        text = stringResource(Res.string.validate_details),
                        style = MifosTypography.labelLargeEmphasized,
                    )

                    MifosDetailsCard(state.details)
                    MifosButton(
                        onClick = {
                            onAction(BeneficiaryApplicationConfirmationAction.SubmitBeneficiary)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        text = {
                            Text(stringResource(Res.string.confirm_details))
                        },
                    )
                }

                if (state.showOverlay) {
                    MifosProgressIndicatorOverlay()
                }
            }

            else -> { }
        }
    }
}
