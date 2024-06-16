package org.mifos.mobile.ui.client_charge

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MFScaffold
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.MifosTextTitleDescSingleLine
import org.mifos.mobile.core.ui.theme.GreenSuccess
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.theme.RedErrorDark
import org.mifos.mobile.models.Charge
import org.mifos.mobile.utils.CurrencyUtil
import org.mifos.mobile.utils.DateHelper
import org.mifos.mobile.utils.Network


@Composable
fun ClientChargeScreen(
    viewModel: ClientChargeViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val uiState by viewModel.clientChargeUiState.collectAsStateWithLifecycle()
    ClientChargeScreen(
        uiState = uiState,
        navigateBack = navigateBack,
        onRetry = { viewModel.loadCharges() }
    )
}

@Composable
fun ClientChargeScreen(
    uiState: ClientChargeUiState,
    navigateBack: () -> Unit,
    onRetry: () -> Unit
) {
    val context = LocalContext.current
    MFScaffold(
        topBarTitleResId = R.string.client_charges,
        navigateBack = navigateBack,
        scaffoldContent = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues = paddingValues)
                    .fillMaxSize()
            ) {
                when (uiState) {
                    is ClientChargeUiState.Loading -> {
                        MifosProgressIndicator()
                    }

                    is ClientChargeUiState.Error -> {
                        MifosErrorComponent(
                            isNetworkConnected = Network.isConnected(context),
                            isRetryEnabled = true,
                            onRetry = onRetry
                        )
                    }

                    is ClientChargeUiState.Success -> {
                        if (uiState.charges.isEmpty()) {
                            EmptyDataView(
                                modifier = Modifier.fillMaxSize(),
                                icon = R.drawable.ic_charges,
                                error = R.string.error_no_charge
                            )
                        } else {
                            ClientChargeContent(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                chargesList = uiState.charges
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun ClientChargeContent(
    modifier: Modifier = Modifier,
    chargesList: List<Charge>
) {
    LazyColumn(modifier = modifier) {
        items(items = chargesList) { charge ->
            ClientChargeItem(charge = charge)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ClientChargeItem(
    charge: Charge
) {
    val currencyRepresentation = charge.currency?.displaySymbol ?: charge.currency?.code ?: ""

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.height(intrinsicSize = IntrinsicSize.Max)
        ) {
            VerticalDivider(
                thickness = 5.dp,
                color = if (charge.isChargePaid || charge.isChargeWaived || charge.paid || charge.waived) RedErrorDark
                else GreenSuccess,
                modifier = Modifier.fillMaxHeight()
            )
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Text(
                    text = charge.name ?: "",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (charge.dueDate.isNotEmpty()) DateHelper.getDateAsString(charge.dueDate) else "",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                MifosTextTitleDescSingleLine(
                    title = stringResource(id = R.string.amount_due),
                    description = stringResource(
                        id = R.string.string_and_string,
                        currencyRepresentation,
                        CurrencyUtil.formatCurrency(charge.amount)
                    )
                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(id = R.string.amount_paid),
                    description = stringResource(
                        id = R.string.string_and_string,
                        currencyRepresentation,
                        CurrencyUtil.formatCurrency(charge.amountPaid)
                    )
                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(id = R.string.amount_waived),
                    description = stringResource(
                        id = R.string.string_and_string,
                        currencyRepresentation,
                        CurrencyUtil.formatCurrency(charge.amountWaived)
                    )
                )

                MifosTextTitleDescSingleLine(
                    title = stringResource(id = R.string.amount_outstanding),
                    description = stringResource(
                        id = R.string.string_and_string,
                        currencyRepresentation,
                        CurrencyUtil.formatCurrency(charge.amountOutstanding)
                    )
                )
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun ClientChargeScreenPreview(
    @PreviewParameter(ClientChargeUiStatesPreviews::class) uiState: ClientChargeUiState
) {
    MifosMobileTheme {
        ClientChargeScreen(
            uiState = uiState,
            navigateBack = { },
            onRetry = { }
        )
    }
}

class ClientChargeUiStatesPreviews : PreviewParameterProvider<ClientChargeUiState> {
    override val values: Sequence<ClientChargeUiState>
        get() = sequenceOf(
            ClientChargeUiState.Success(listOf(Charge(), Charge())),
            ClientChargeUiState.Error(""),
            ClientChargeUiState.Loading,
        )
}
