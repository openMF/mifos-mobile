package org.mifos.mobile.feature.savingsaccount.savingsAccountUpdate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.savings_account.generated.resources.Res
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_request_update
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_topbar_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosOutlineDropdown
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.utils.EventsEffect
import org.mifos.mobile.feature.savingsaccount.components.AccountDetailsCard

@Composable
internal fun AccountUpdateScreen(
    navigateBack: () -> Unit,
    navigateToStatusScreen: (String, String, String, String, String) -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
    viewModel: AccountUpdateViewModel = koinViewModel()
) {

    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when(event) {
            is AccountUpdateEvent.NavigateBack -> navigateBack.invoke()
            is AccountUpdateEvent.NavigateToStatus -> {
                navigateToStatusScreen.invoke(
                    event.eventType,
                    event.eventDestination,
                    event.title,
                    event.subtitle,
                    event.buttonText
                )
            }

            is AccountUpdateEvent.NavigateToAuthenticate -> navigateToAuthenticateScreen.invoke()
        }
    }

    AccountUpdateScreenContent(
        state = uiState,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        }
    )

}


@Composable
internal fun AccountUpdateScreenContent(
    state: AccountUpdateState,
    onAction: (AccountUpdateAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosElevatedScaffold(
        onNavigateBack = { onAction(AccountUpdateAction.OnNavigateBack) },
        topBarTitle = stringResource(Res.string.feature_savings_update_topbar_title),
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(DesignToken.padding.large)
                .padding(top = DesignToken.padding.medium),
            verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.large)
        ) {
            AccountDetailsCard(
                keyValuePairs = state.details,
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.largeIncreased)
            ) {

                MifosOutlineDropdown(
                    selectedText = state.selectedProduct,
                    items = state.productOptions,
                    onItemSelected = { id, product ->
                        onAction(AccountUpdateAction.OnProductSelected(id, product))
                    },
                    label = "New Product"
                )

                MifosButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DesignToken.sizes.buttonHeight),
                    onClick =  { onAction(AccountUpdateAction.RequestUpdate) },
                    text = {
                        Text(
                            text = stringResource(Res.string.feature_savings_update_request_update),
                            style = MifosTypography.titleMedium,
                        )
                    },

                    enabled = state.selectedProductId != null,
                    shape = DesignToken.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun Account_Update_Preview() {
    MifosMobileTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(DesignToken.padding.large)
        ) {
            AccountUpdateScreenContent(
                state = AccountUpdateState(clientId = 1, accountId = -1L, dialogState = null),
                onAction = {}
            )
        }
    }
}