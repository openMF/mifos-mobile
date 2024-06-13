package org.mifos.mobile.ui.beneficiary_list

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
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
import org.mifos.mobile.ui.beneficiary_detail.BeneficiaryDetailScreen
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
                onClick = { addBeneficiaryClicked.invoke() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
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

class BeneficiaryListScreenPreviewProvider : PreviewParameterProvider<BeneficiaryUiState> {
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
    override val values: Sequence<BeneficiaryUiState>
        get() = sequenceOf(
            BeneficiaryUiState.ShowBeneficiaryList(beneficiaryList) ,
            BeneficiaryUiState.Loading,
            BeneficiaryUiState.ShowError(R.string.error_fetching_beneficiaries),
            BeneficiaryUiState.Initial
        )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PreviewBeneficiaryListScreen(
    @PreviewParameter(BeneficiaryListScreenPreviewProvider::class) beneficiaryUiState: BeneficiaryUiState
) {
    MifosMobileTheme {
        BeneficiaryListScreen(
            uiState = beneficiaryUiState,
            navigateBack = {},
            addBeneficiaryClicked = {},
            retryConnection = {},
            onBeneficiaryItemClick = {_, _ ->

            },
            refreshBeneficiary = {},
            isRefreshing = false,
            retryLoadingBeneficiary = {}
        )
    }
}

@Composable
@Preview(showSystemUi = true)
fun PreviewEmptyBeneficiary(modifier: Modifier = Modifier) {
    MifosMobileTheme {
        ShowBeneficiaryListEmpty()
    }
}