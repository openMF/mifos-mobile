package org.mifos.mobile.ui.beneficiary_list

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.FloatingActionButtonContent
import org.mifos.mobile.core.ui.component.MFScaffold
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.component.MifosTopBar
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.utils.Network

@Composable
fun BeneficiaryListScreen(
    viewModel: BeneficiaryListViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    addBeneficiaryClicked: () -> Unit,
    onBeneficiaryItemClick: (position: Int, List<Beneficiary?>?) -> Unit,
    retryLoadingBeneficiary: () -> Unit
) {
    val uiState by viewModel.beneficiaryListUiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    BeneficiaryListScreen(
        uiState = uiState,
        navigateBack = navigateBack,
        addBeneficiaryClicked = addBeneficiaryClicked,
        onBeneficiaryItemClick = onBeneficiaryItemClick,
        retryLoadingBeneficiary = retryLoadingBeneficiary,
        isRefreshing = isRefreshing,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeneficiaryListScreen(
    viewModel: BeneficiaryListViewModel = hiltViewModel(),
    uiState: BeneficiaryListUiState,
    navigateBack: () -> Unit,
    addBeneficiaryClicked: () -> Unit,
    onBeneficiaryItemClick: (position: Int, List<Beneficiary?>?) -> Unit,
    retryLoadingBeneficiary: () -> Unit,
    isRefreshing: Boolean,
) {
    val pullRefreshState = rememberPullToRefreshState()
    val context = LocalContext.current

    MFScaffold(
        topBarTitleResId = R.string.beneficiaries,
        navigateBack = { navigateBack.invoke() },
        floatingActionButtonContent = FloatingActionButtonContent(
            onClick = addBeneficiaryClicked,
            contentColor = MaterialTheme.colorScheme.onBackground,
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_white_24dp),
                    contentDescription = "add beneficiary list"
                )
            }
        )
    )
    {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .nestedScroll(pullRefreshState.nestedScrollConnection)

        ) {
            when (uiState) {

                BeneficiaryListUiState.Initial -> Unit

                BeneficiaryListUiState.Loading -> {
                    MifosProgressIndicatorOverlay()
                }

                is BeneficiaryListUiState.ShowError -> {
                    MifosErrorComponent(
                        isNetworkConnected = Network.isConnected(context),
                        isRetryEnabled = true,
                        onRetry = retryLoadingBeneficiary,
                        message = stringResource(R.string.error_fetching_beneficiaries)
                    )
                }

                is BeneficiaryListUiState.ShowBeneficiaryList -> {
                    ShowBeneficiary(
                        beneficiaryList = uiState.beneficiaries,
                        onClick = { position ->
                            onBeneficiaryItemClick.invoke(position, uiState.beneficiaries)
                        }
                    )
                }

                is BeneficiaryListUiState.EmptyBeneficiaryList -> {
                    EmptyDataView(
                        modifier = Modifier.fillMaxSize(),
                        icon = R.drawable.ic_error_black_24dp,
                        error = R.string.no_beneficiary_found_please_add
                    )
                }
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
                    viewModel.refresh()
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

class BeneficiaryListScreenPreviewProvider : PreviewParameterProvider<BeneficiaryListUiState> {
    val beneficiaryList = listOf(
        Beneficiary(
            id = 982098302,
            name = "John Doe",
            officeName = "Mifos Head Office",
            clientName = "Jane Smith",
            accountType = null,
            accountNumber = "1234567890",
            transferLimit = 1000.00
        ),
        Beneficiary(
            id = 982098302,
            name = "Alice Johnson",
            officeName = "Mifos Branch 1",
            clientName = "Bob Smith",
            accountType = null,
            accountNumber = "0987654321",
            transferLimit = 500.00
        ),
        Beneficiary(
            id = 982098302,
            name = "Michael Brown",
            officeName = "Mifos Branch 2",
            clientName = "Sarah Jones",
            accountType = null,
            accountNumber = "9876543210",
            transferLimit = 2000.00
        ),
        Beneficiary(
            id = 982098302,
            name = "David Williams",
            officeName = "Mifos Head Office",
            clientName = "Emily Miller",
            accountType = null,
            accountNumber = "1011121314",
            transferLimit = 750.00
        )
    )
    override val values: Sequence<BeneficiaryListUiState>
        get() = sequenceOf(
            BeneficiaryListUiState.ShowBeneficiaryList(beneficiaryList),
            BeneficiaryListUiState.Loading,
            BeneficiaryListUiState.ShowError(R.string.error_fetching_beneficiaries),
            BeneficiaryListUiState.Initial
        )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PreviewBeneficiaryListScreen(
    @PreviewParameter(BeneficiaryListScreenPreviewProvider::class) beneficiaryUiState: BeneficiaryListUiState
) {
    MifosMobileTheme {
        BeneficiaryListScreen(
            uiState = beneficiaryUiState,
            navigateBack = {},
            addBeneficiaryClicked = {},
            onBeneficiaryItemClick = { _, _ ->

            },

            isRefreshing = false,
            retryLoadingBeneficiary = {}
        )
    }
}
