/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.guarantor.screens.guarantorList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.designsystem.components.FloatingActionButtonContent
import org.mifos.mobile.core.designsystem.components.MifosScaffold
import org.mifos.mobile.core.designsystem.icons.MifosIcons
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.guarantor.GuarantorPayload
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.guarantor.R

@Composable
internal fun GuarantorListScreen(
    navigateBack: () -> Unit,
    addGuarantor: (Long) -> Unit,
    onGuarantorClicked: (Int, Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GuarantorListViewModel = hiltViewModel(),
) {
    val uiState = viewModel.guarantorUiState.collectAsStateWithLifecycle()

    GuarantorListScreen(
        uiState = uiState.value,
        navigateBack = navigateBack,
        modifier = modifier,
        addGuarantor = { addGuarantor(viewModel.loanId.value) },
        onGuarantorClicked = { onGuarantorClicked(it, viewModel.loanId.value) },
    )
}

@Composable
private fun GuarantorListScreen(
    uiState: GuarantorListUiState,
    navigateBack: () -> Unit,
    addGuarantor: () -> Unit,
    onGuarantorClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    MifosScaffold(
        topBarTitleResId = R.string.view_guarantor,
        navigateBack = navigateBack,
        modifier = modifier,
        floatingActionButtonContent = FloatingActionButtonContent(
            onClick = addGuarantor,
            content = {
                Icon(
                    imageVector = MifosIcons.Add,
                    contentDescription = null,
                    tint = Color.White,
                )
            },
            contentColor = MaterialTheme.colorScheme.primary,
        ),
        content = {
            GuarantorListContent(
                modifier = Modifier.padding(it),
                uiState = uiState,
                onGuarantorClicked = onGuarantorClicked,
            )
        },
    )
}

@Composable
private fun GuarantorListContent(
    uiState: GuarantorListUiState,
    onGuarantorClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var guarantorList by rememberSaveable { mutableStateOf(listOf<GuarantorPayload?>()) }

    Box(modifier = modifier) {
        GuarantorList(guarantorList = guarantorList, onGuarantorClicked = onGuarantorClicked)
        when (uiState) {
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
                if (uiState.list.isNullOrEmpty()) {
                    MifosErrorComponent(isEmptyData = true)
                } else {
                    guarantorList = uiState.list
                }
            }
        }
    }
}

@Composable
private fun GuarantorList(
    guarantorList: List<GuarantorPayload?>,
    onGuarantorClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier) {
        itemsIndexed(items = guarantorList) { index, guarantor ->
            guarantor?.let {
                GuarantorListItem(
                    guarantor = it,
                    onGuarantorClicked = { onGuarantorClicked.invoke(index) },
                )
            }
        }
    }
}

@Composable
private fun GuarantorListItem(
    onGuarantorClicked: () -> Unit,
    modifier: Modifier = Modifier,
    guarantor: GuarantorPayload = GuarantorPayload(),
) {
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.background),
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        onClick = { onGuarantorClicked.invoke() },
        content = {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = guarantor.firstname + " " + guarantor.lastname,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = guarantor.guarantorType?.value ?: "",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
    )
}

internal class UiStatesParameterProvider : PreviewParameterProvider<GuarantorListUiState> {
    override val values: Sequence<GuarantorListUiState>
        get() = sequenceOf(
            GuarantorListUiState.Loading,
            GuarantorListUiState.Error,
            GuarantorListUiState.Success(listOf()),
        )
}

@DevicePreviews
@Composable
private fun GuarantorListScreenPreview(
    @PreviewParameter(UiStatesParameterProvider::class)
    guarantorListUiState: GuarantorListUiState,
) {
    MifosMobileTheme {
        GuarantorListScreen(
            uiState = guarantorListUiState,
            navigateBack = { },
            addGuarantor = { },
            onGuarantorClicked = { },
        )
    }
}
