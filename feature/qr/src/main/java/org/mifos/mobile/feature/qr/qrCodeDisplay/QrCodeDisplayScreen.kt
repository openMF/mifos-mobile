/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.qr.qrCodeDisplay

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.common.utils.Utils
import org.mifos.mobile.core.designsystem.components.MifosScaffold
import org.mifos.mobile.core.designsystem.components.MifosTopBar
import org.mifos.mobile.core.designsystem.icons.MifosIcons
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.qr.R

@Composable
internal fun QrCodeDisplayScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QrCodeDisplayViewModel = hiltViewModel(),
) {
    val uiState by viewModel.qrCodeDisplayUiState.collectAsStateWithLifecycle()

    QrCodeDisplayScreen(
        uiState = uiState,
        navigateBack = navigateBack,
        modifier = modifier,
    )
}

@Composable
private fun QrCodeDisplayScreen(
    uiState: QrCodeDisplayUiState,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var qrBitmap by rememberSaveable { mutableStateOf<Bitmap?>(null) }

    MifosScaffold(
        modifier = modifier,
        topBar = {
            MifosTopBar(
                navigateBack = navigateBack,
                title = { Text(text = stringResource(id = R.string.qr_code)) },
                actions = {
                    IconButton(
                        onClick = {
                            qrBitmap?.let { qrBitmap ->
                                onShareClicked(context = context, qrBitmap = qrBitmap)
                            }
                        },
                        content = {
                            Icon(
                                imageVector = MifosIcons.Share,
                                contentDescription = null,
                            )
                        },
                    )
                },
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues = paddingValues)
                    .fillMaxSize(),
            ) {
                when (uiState) {
                    is QrCodeDisplayUiState.Error -> {
                        MifosErrorComponent(message = stringResource(id = R.string.something_went_wrong))
                    }

                    is QrCodeDisplayUiState.Loading -> {
                        MifosProgressIndicatorOverlay()
                    }

                    is QrCodeDisplayUiState.Success -> {
                        qrBitmap = uiState.qrBitmap
                        QrCodeDisplayContent(qrBitmap = uiState.qrBitmap)
                    }
                }
            }
        },
    )
}

@Composable
private fun QrCodeDisplayContent(
    qrBitmap: Bitmap,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            bitmap = qrBitmap.asImageBitmap(),
            contentDescription = stringResource(id = R.string.qr_code),
            modifier = Modifier
                .padding(20.dp)
                .aspectRatio(1f),
        )
    }
}

private fun onShareClicked(context: Context, qrBitmap: Bitmap) {
    val uri = Utils.getImageUri(context, qrBitmap)

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    startActivity(
        context,
        Intent.createChooser(intent, context.getString(R.string.choose_option)),
        null,
    )
}

internal class QrCodeDisplayScreenPreviewProvider : PreviewParameterProvider<QrCodeDisplayUiState> {
    override val values: Sequence<QrCodeDisplayUiState>
        get() = sequenceOf(
            QrCodeDisplayUiState.Loading,
            QrCodeDisplayUiState.Error(""),
            QrCodeDisplayUiState.Success(Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)),
        )
}

@DevicePreviews
@Composable
private fun QrCodeDisplayScreenPreview(
    @PreviewParameter(QrCodeDisplayScreenPreviewProvider::class)
    qrCodeDisplayUiState: QrCodeDisplayUiState,
) {
    MifosMobileTheme {
        QrCodeDisplayScreen(
            uiState = qrCodeDisplayUiState,
            navigateBack = {},
        )
    }
}
