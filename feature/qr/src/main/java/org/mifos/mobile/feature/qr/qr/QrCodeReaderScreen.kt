/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.qr.qr

import android.widget.Toast
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.mifos.mobile.core.designsystem.components.MifosScaffold
import org.mifos.mobile.core.designsystem.icons.MifosIcons
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.core.qr.BarcodeCamera
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.qr.R

@Composable
internal fun QrCodeReaderScreen(
    navigateBack: () -> Unit,
    openBeneficiaryApplication: (Beneficiary, BeneficiaryState) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val gson = Gson()

    MifosScaffold(
        topBarTitleResId = R.string.add_beneficiary,
        navigateBack = navigateBack,
        modifier = modifier,
        content = {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
            ) {
                QrCodeReaderContent(
                    qrScanned = { text ->
                        try {
                            val beneficiary = gson.fromJson(text, Beneficiary::class.java)
                            openBeneficiaryApplication.invoke(
                                beneficiary,
                                BeneficiaryState.CREATE_QR,
                            )
                        } catch (e: JsonSyntaxException) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.invalid_qr),
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    },
                    navigateBack = navigateBack,
                )
            }
        },
    )
}

@OptIn(ExperimentalGetImage::class)
@Composable
private fun QrCodeReaderContent(
    qrScanned: (String) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val camera = remember { BarcodeCamera() }
    var isFlashOn by remember { mutableStateOf(false) }

    Box(modifier) {
        camera.BarcodeReaderCamera(
            onBarcodeScanned = { barcode ->
                barcode?.let {
                    qrScanned(it)
                    navigateBack()
                }
            },
            isFlashOn = isFlashOn,
        )

        IconButton(
            onClick = { isFlashOn = !isFlashOn },
            content = {
                Icon(
                    imageVector = if (isFlashOn) {
                        MifosIcons.FlashOn
                    } else {
                        MifosIcons.FlashOff
                    },
                    contentDescription = null,
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
        )
    }
}

@DevicePreviews
@Composable
private fun QrCodeReaderScreenPreview() {
    MifosMobileTheme {
        QrCodeReaderScreen(
            openBeneficiaryApplication = { _, _ -> },
            navigateBack = {},
        )
    }
}
