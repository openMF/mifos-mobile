package org.mifos.mobile.ui.guarantor.guarantor_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.FloatingActionButtonContent
import org.mifos.mobile.core.ui.component.MFScaffold
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.utils.Network


@Composable
fun GuarantorListScreen(
    viewModel: GuarantorListViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    addGuarantor: () -> Unit,
    onGuarantorClicked: (Int) -> Unit
) {
    val uiState = viewModel.guarantorUiState.collectAsStateWithLifecycle()
    GuarantorListScreen(
        uiState = uiState.value,
        navigateBack = navigateBack,
        addGuarantor = addGuarantor,
        onGuarantorClicked = onGuarantorClicked
    )
}

@Composable
fun GuarantorListScreen(
    uiState: GuarantorListUiState,
    navigateBack: () -> Unit,
    addGuarantor: () -> Unit,
    onGuarantorClicked: (Int) -> Unit
) {
    MFScaffold(
        topBarTitleResId = R.string.view_guarantor,
        navigateBack = navigateBack,
        floatingActionButtonContent = FloatingActionButtonContent(
            onClick = { addGuarantor.invoke() },
            content = {
                Image(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                )
            },
            contentColor = MaterialTheme.colorScheme.primary
        ),
        scaffoldContent = {
            GuarantorListContent(
                modifier = Modifier.padding(it),
                uiState = uiState,
                onGuarantorClicked = onGuarantorClicked
            )
        }
    )
}

@Composable
fun GuarantorListContent(
    modifier: Modifier = Modifier,
    uiState: GuarantorListUiState,
    onGuarantorClicked: (Int) -> Unit
) {
    val context = LocalContext.current
    var guarantorList by rememberSaveable { mutableStateOf(listOf<GuarantorPayload?>()) }

    Box(modifier = modifier) {
        GuarantorList(guarantorList = guarantorList, onGuarantorClicked = onGuarantorClicked)
        when(uiState) {
            is GuarantorListUiState.Loading -> {
                MifosProgressIndicatorOverlay()
            }

            is GuarantorListUiState.Error -> {
                MifosErrorComponent(
                    isNetworkConnected = Network.isConnected(context),
                    isEmptyData = false,
                    isRetryEnabled = false,
                )
            }

            is GuarantorListUiState.Success -> {
                if(uiState.list.isNullOrEmpty()) {
                    MifosErrorComponent(isEmptyData = true)
                } else {
                    guarantorList = uiState.list
                }
            }
        }
    }
}

@Composable
fun GuarantorList(
    guarantorList: List<GuarantorPayload?>,
    onGuarantorClicked: (Int) -> Unit
) {
    LazyColumn {
        itemsIndexed(items = guarantorList) { index, guarantor ->
            guarantor?.let {
                GuarantorListItem(
                    guarantor = it,
                    onGuarantorClicked = { onGuarantorClicked.invoke(index) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuarantorListItem(
    guarantor: GuarantorPayload = GuarantorPayload(),
    onGuarantorClicked: () -> Unit
) {
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.background),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        onClick = { onGuarantorClicked.invoke() },
        content = {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = guarantor.firstname + " " + guarantor.lastname,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = guarantor.guarantorType?.value ?: "",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
    )
}


class UiStatesParameterProvider : PreviewParameterProvider<GuarantorListUiState> {
    override val values: Sequence<GuarantorListUiState>
        get() = sequenceOf(
            GuarantorListUiState.Loading,
            GuarantorListUiState.Error,
            GuarantorListUiState.Success(listOf()),
        )
}

@Preview(showSystemUi = true)
@Composable
fun GuarantorListScreenPreview(
    @PreviewParameter(UiStatesParameterProvider::class) guarantorListUiState: GuarantorListUiState
) {
    MifosMobileTheme {
        GuarantorListScreen(
            uiState = guarantorListUiState,
            navigateBack = {  },
            addGuarantor = {  },
            onGuarantorClicked = {  }
        )
    }
}