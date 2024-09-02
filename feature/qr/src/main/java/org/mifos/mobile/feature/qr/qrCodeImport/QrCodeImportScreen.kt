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
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.designsystem.components.MifosButton
import org.mifos.mobile.core.designsystem.components.MifosScaffold
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.qr.R

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

    var bitmap: Bitmap? by rememberSaveable {
        mutableStateOf(null)
    }
    var showErrorScreen by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        /**
         * responsible for image picking,
         * & cropping image
         */
        QrCodeImportFunctions(
            setImageBitmap = { bitmap = it },
            showErrorScreen = { showErrorScreen = it },
        )

        if (showErrorScreen) {
            MifosErrorComponent(
                isNetworkConnected = Network.isConnected(context),
                isRetryEnabled = false,
                isEmptyData = true,
                message = stringResource(
                    id = R.string.no_image_selected_or_something_went_wrong,
                ),
            )
        } else if (bitmap != null) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.seleted_qr_image),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = null,
            )

            Spacer(modifier = Modifier.height(16.dp))

            MifosButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textResId = R.string.proceed,
                onClick = { proceedClicked.invoke(bitmap!!) },
            )
        }
    }
}

@Composable
@Suppress("ModifierMissing")
private fun QrCodeImportFunctions(
    setImageBitmap: (Bitmap) -> Unit,
    showErrorScreen: (error: Boolean) -> Unit,
) {
    val context = LocalContext.current

    var imageUri: Uri? by rememberSaveable { mutableStateOf(null) }
    var bitmap: Bitmap? by rememberSaveable { mutableStateOf(null) }
    var hasImagePicked by rememberSaveable { mutableStateOf(false) }

    /**
     * responsible for image cropping
     * [https://github.com/CanHub/Android-Image-Cropper]
     */
    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            hasImagePicked = true
            val uri = result.uriContent
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            } else {
                val source = uri?.let { ImageDecoder.createSource(context.contentResolver, it) }
                val hardwareBitmap = source?.let { ImageDecoder.decodeBitmap(it) }
                hardwareBitmap?.let { convertToMutableBitmap(it) }
            }
        } else {
            /**
             *  handles if the user presses back button on top-left (that comes with the cropLauncher)
             *  instead of cropping the image
             */
            showErrorScreen.invoke(true)
            Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
        }
        bitmap?.let { setImageBitmap.invoke(it) }
    }

    /**
     * uri is null when the user presses back button instead of
     * selecting images when on gallery. so we have to handle it as well
     */
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        imageUri = uri
        if (imageUri == null) {
            Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
            showErrorScreen.invoke(true)
        } else {
            imageCropLauncher.launch(CropImageContractOptions(imageUri, CropImageOptions()))
        }
    }

    LaunchedEffect(Unit) {
        if (!hasImagePicked) {
            imagePickerLauncher.launch("image/*")
        }
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
