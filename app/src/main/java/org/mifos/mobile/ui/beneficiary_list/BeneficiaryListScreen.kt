package org.mifos.mobile.ui.beneficiary_list

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.component.MifosTopBar
import org.mifos.mobile.core.ui.component.NoInternet
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.utils.Network

@Composable
fun BeneficiaryListScreen(
    viewModel: BeneficiaryListViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    addBeneficiaryClicked: () -> Unit,
    retryConnection: () -> Unit,
    onBeneficiaryItemClick: (position: Int, List<Beneficiary?>?) -> Unit,
    retryLoadingBeneficiary: () -> Unit,
) {
    val uiState by viewModel.beneficiaryUiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    BeneficiaryListScreen(
        uiState = uiState,
        navigateBack = navigateBack,
        addBeneficiaryClicked = addBeneficiaryClicked,
        retryConnection = retryConnection,
        onBeneficiaryItemClick = onBeneficiaryItemClick,
        retryLoadingBeneficiary = retryLoadingBeneficiary,
        isRefreshing = isRefreshing,
        refreshBeneficiary = { viewModel.refresh() },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeneficiaryListScreen(
    uiState: BeneficiaryUiState,
    navigateBack: () -> Unit,
    addBeneficiaryClicked: () -> Unit,
    retryConnection: () -> Unit,
    onBeneficiaryItemClick: (position: Int, List<Beneficiary?>?) -> Unit,
    retryLoadingBeneficiary: () -> Unit,
    isRefreshing: Boolean,
    refreshBeneficiary: () -> Unit,
) {

    val pullRefreshState = rememberPullToRefreshState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            MifosTopBar(navigateBack = { navigateBack.invoke() }) {
                Text(text = stringResource(id = R.string.beneficiaries))
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(bottom = 50.dp, end = 32.dp),
                onClick = { addBeneficiaryClicked.invoke() }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "",
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .nestedScroll(pullRefreshState.nestedScrollConnection)

        ) {
            when (uiState) {

                BeneficiaryUiState.Loading -> {
                    MifosProgressIndicatorOverlay()
                }

                is BeneficiaryUiState.ShowError -> {
                    ErrorComponent(
                        retryConnection = retryConnection,
                        retryLoadingBeneficiary = retryLoadingBeneficiary
                    )
                }

                is BeneficiaryUiState.ShowBeneficiaryList -> {
                    if (uiState.beneficiaries?.isNotEmpty() == true) {
                        ShowBeneficiary(
                            beneficiaryList = uiState.beneficiaries,
                            onClick = { position ->
                                onBeneficiaryItemClick.invoke(position, uiState.beneficiaries)
                            }
                        )
                    } else {
                        ShowBeneficiaryListEmpty()
                    }
                }

                else -> Unit
            }

            PullToRefreshContainer(
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }

        LaunchedEffect(key1 = isRefreshing) {
            if (isRefreshing)
                pullRefreshState.startRefresh()
        }

        LaunchedEffect(key1 = pullRefreshState.isRefreshing) {
            if (pullRefreshState.isRefreshing) {
                if (Network.isConnected(context)) {
                    refreshBeneficiary.invoke()
                } else {
                    Toast.makeText(
                        context,
                        context.resources.getText(R.string.internet_not_connected),
                        Toast.LENGTH_SHORT,
                    ).show()
                }
                pullRefreshState.endRefresh()
            }
        }
    }

}


@Composable
fun ErrorComponent(
    retryConnection: () -> Unit,
    retryLoadingBeneficiary: () -> Unit
) {
    val context = LocalContext.current
    if (!Network.isConnected(context)) {
        NoInternet(
            icon = R.drawable.ic_portable_wifi_off_black_24dp,
            error = R.string.no_internet_connection,
            isRetryEnabled = true,
            retry = retryConnection
        )
        Toast.makeText(
            context,
            stringResource(R.string.internet_not_connected),
            Toast.LENGTH_SHORT,
        ).show()
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmptyDataView(
                icon = R.drawable.ic_error_black_24dp,
                error = R.string.error_fetching_beneficiaries,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Button(
                modifier = Modifier.padding(top = 16.dp),
                onClick = { retryLoadingBeneficiary.invoke() }
            ) {
                Text(text = stringResource(id = R.string.try_again))
            }
        }
    }
}

class BeneficiaryListScreenUiStatesParameterProvider :
    PreviewParameterProvider<BeneficiaryUiState> {
    val beneficiaryList = listOf(
        Beneficiary(
            id = 1,
            name = "John Doe",
            officeName = "Mifos Head Office",
            clientName = "Jane Smith",
            accountType = null,
            accountNumber = "1234567890",
            transferLimit = 1000.00
        ),
        Beneficiary(
            id = 2,
            name = "Alice Johnson",
            officeName = "Mifos Branch 1",
            clientName = "Bob Smith",
            accountType = null,
            accountNumber = "0987654321",
            transferLimit = 500.00
        )
    )

    override val values: Sequence<BeneficiaryUiState>
        get() = sequenceOf(
            BeneficiaryUiState.ShowBeneficiaryList(beneficiaryList),
            BeneficiaryUiState.Loading,
            BeneficiaryUiState.ShowError(R.string.error_fetching_beneficiaries)
        )
}

@Composable
@Preview(showSystemUi = true)
fun PreviewBeneficiaryListScreen(
    modifier: Modifier = Modifier,
    viewModel: BeneficiaryListViewModel = hiltViewModel(),
    @PreviewParameter(BeneficiaryListScreenUiStatesParameterProvider::class) beneficiaryUiState: BeneficiaryUiState
) {
    val uiState by viewModel.beneficiaryUiState.collectAsStateWithLifecycle()
    MifosMobileTheme {
        BeneficiaryListScreen(
            uiState = uiState,
            navigateBack = {},
            addBeneficiaryClicked = {},
            retryConnection = {},
            onBeneficiaryItemClick = { _, _ ->

            },
            retryLoadingBeneficiary = {},
            isRefreshing = false,
            refreshBeneficiary = {}
        )
    }
}
