/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.qr.qrCodeImport

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.mr0xf00.easycrop.CropError
import com.mr0xf00.easycrop.CropResult
import com.mr0xf00.easycrop.crop
import com.mr0xf00.easycrop.rememberImageCropper
import com.mr0xf00.easycrop.rememberImagePicker
import kotlinx.coroutines.launch
import org.mifos.mobile.core.designsystem.components.MifosScaffold
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.qr.R
import org.mifos.mobile.feature.qr.components.CropContent
import org.mifos.mobile.feature.qr.components.CropErrorDialog

@Composable
internal fun QrCodeImportScreen(
    navigateBack: () -> Unit,
    openBeneficiaryApplication: (Beneficiary, BeneficiaryState) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QrCodeImportViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val gson by lazy { Gson() }
    val uiState by viewModel.qrCodeImportUiState.collectAsStateWithLifecycle()

    QrCodeImportScreen(
        uiState = uiState,
        navigateBack = navigateBack,
        modifier = modifier,
        proceedClicked = { bitmap: Bitmap ->
            viewModel.getDecodedResult(bitmap = bitmap)
        },
        handleDecodedResult = {
            try {
                val beneficiary = gson.fromJson(it.text, Beneficiary::class.java)
                openBeneficiaryApplication.invoke(beneficiary, BeneficiaryState.CREATE_QR)
            } catch (e: JsonSyntaxException) {
                Toast.makeText(
                    context,
                    context.getString(R.string.invalid_qr),
                    Toast.LENGTH_SHORT,
                ).show()
            }
        },
    )
}

@Composable
private fun QrCodeImportScreen(
    uiState: QrCodeImportUiState,
    navigateBack: () -> Unit,
    proceedClicked: (bitmap: Bitmap) -> Unit,
    handleDecodedResult: (result: Result) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    MifosScaffold(
        topBarTitleResId = R.string.import_qr,
        navigateBack = navigateBack,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                QrCodeImportContent(proceedClicked = proceedClicked)

                when (uiState) {
                    is QrCodeImportUiState.HandleDecodedResult -> {
                        handleDecodedResult.invoke(uiState.result)
                    }

                    QrCodeImportUiState.Initial -> Unit

                    QrCodeImportUiState.Loading -> {
                        MifosProgressIndicatorOverlay()
                    }

                    is QrCodeImportUiState.ShowError -> {
                        Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

@Composable
private fun QrCodeImportContent(
    proceedClicked: (bitmap: Bitmap) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val imageCropper = rememberImageCropper()
    val scope = rememberCoroutineScope()

    var selectedImage by remember { mutableStateOf<ImageBitmap?>(null) }
    var error by remember { mutableStateOf<CropError?>(null) }

    val imagePicker = rememberImagePicker(
        onImage = { uri ->
            scope.launch {
                when (val result = imageCropper.crop(uri, context)) {
                    is CropResult.Cancelled -> {}
                    is CropError -> error = result
                    is CropResult.Success -> {
                        selectedImage = result.bitmap
                    }
                }
            }
        },
    )

    CropContent(
        cropState = imageCropper.cropState,
        loadingStatus = imageCropper.loadingStatus,
        selectedImage = selectedImage,
        onPick = { imagePicker.pick() },
        onProceed = { selectedImage?.let { proceedClicked.invoke(it.asAndroidBitmap()) } },
        modifier = modifier,
    )

    error?.let {
        CropErrorDialog(it, onDismiss = { error = null })
    }
}

internal class QrCodeImportPreviewProvider : PreviewParameterProvider<QrCodeImportUiState> {
    override val values: Sequence<QrCodeImportUiState>
        get() = sequenceOf(
            QrCodeImportUiState.Initial,
            QrCodeImportUiState.Loading,
            QrCodeImportUiState.ShowError(0),
            QrCodeImportUiState.HandleDecodedResult(
                Result(
                    "",
                    byteArrayOf(),
                    arrayOf(),
                    BarcodeFormat.CODE_128,
                ),
            ),
        )
}

@DevicePreviews
@Composable
private fun QrCodeImportPreview(
    @PreviewParameter(QrCodeImportPreviewProvider::class)
    qrCodeImportUiState: QrCodeImportUiState,
) {
    MifosMobileTheme {
        QrCodeImportScreen(
            uiState = qrCodeImportUiState,
            navigateBack = {},
            proceedClicked = { _ -> },
            handleDecodedResult = { _ -> },
        )
    }
}
