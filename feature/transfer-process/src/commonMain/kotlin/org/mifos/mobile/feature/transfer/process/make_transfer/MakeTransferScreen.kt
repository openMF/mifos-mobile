package org.mifos.mobile.feature.transfer.process.make_transfer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.component.MifosTextFieldConfig
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosOutlineDropdown
import org.mifos.mobile.core.ui.component.MifosPayFromDropdownUI
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.utils.EventsEffect


@Composable
internal fun MakeTransferScreen(
    navigateBack: () -> Unit,
    viewModel: MakeTransferViewModel = koinViewModel()
) {
    EventsEffect(viewModel.eventFlow){ event->
        when(event){
            MakeTransferEvent.NavigateBack -> {
                navigateBack.invoke()
            }
            MakeTransferEvent.NavigateToStatus -> TODO()
        }
    }
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    MakeTransferScreenContent(
        state = state,
        onAction = {
            viewModel.trySendAction(it)
        }
    )
}

@Composable
internal fun MakeTransferScreenContent(
    state: MakeTransferState,
    onAction: (MakeTransferAction) -> Unit,
) {
    MifosElevatedScaffold(
        topBarTitle = "Make Transfer",
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                )
            }
        },
        onNavigateBack = {
            onAction(MakeTransferAction.NavigateBack)
        }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(
                    horizontal = DesignToken.padding.large,
                    vertical = DesignToken.padding.extraLargeIncreased
                ),
            verticalArrangement = Arrangement.spacedBy(DesignToken.padding.largeIncreased)
        ) {
            MifosOutlineDropdown(
                selectedText = state.selectedToAccount,
                items = emptyMap(),
                onItemSelected = { accountNo, _ ->
                    onAction(MakeTransferAction.OnToAccountSelected(accountNo.toString()))
                },
                label = "Select Account To Pay To",
                enabled = false
            )

            MifosPayFromDropdownUI(
                accounts = state.payFromAccounts,
                onAccountSelected = { account,balance ->
                    onAction(MakeTransferAction.OnFromAccountSelected(account))
                }
            )

            MifosOutlinedTextField(
                value = state.amount,
                onValueChange = { onAction(MakeTransferAction.OnAmountChanged(it)) },
                label = "Amount",
                shape = DesignToken.shapes.medium,
                textStyle = MifosTypography.bodyLarge,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                ),
                config = MifosTextFieldConfig(
                    isError = state.amountError.isNotEmpty(),
                    errorText = state.amountError,
                    trailingIcon = if (state.amountError.isNotEmpty()) {
                        {
                            Icon(
                                imageVector = MifosIcons.ErrorCircle,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error,
                            )
                        }
                    } else {
                        null
                    },
                ),
            )

            MifosOutlinedTextField(
                value = state.remarks,
                onValueChange = { onAction(MakeTransferAction.OnRemarksChanged(it)) },
                label = "Remarks",
                shape = DesignToken.shapes.medium,
                textStyle = MifosTypography.bodyLarge,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                ),
            )

            MifosButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DesignToken.sizes.buttonHeight),
                onClick = {
                    onAction(MakeTransferAction.OnMakeTransferClicked)
                },
                text = {
                    Text(
                        text = "Make Transfer",
                        style = MifosTypography.titleMedium,
                    )
                },
                enabled = true,
                shape = DesignToken.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
            )
        }
    }
}

@Composable
@Preview
fun MakeTransferScreenPreview(){
    MifosMobileTheme {
        MakeTransferScreenContent(
            state = MakeTransferState(),
            onAction = {}
        )
    }
}