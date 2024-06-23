package org.mifos.mobile.ui.qr

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.MFScaffold
import org.mifos.mobile.core.ui.component.MifosIcons
import org.mifos.mobile.core.ui.theme.MifosMobileTheme

@Composable
fun QrCodeReaderScreen(
    qrScanned: (String) -> Unit,
    navigateBack: () -> Unit
) {
    MFScaffold(
        topBarTitleResId = R.string.add_beneficiary,
        navigateBack = navigateBack,
        scaffoldContent = {
            Box(modifier = Modifier
                .padding(it)
                .fillMaxSize()) {
                QrCodeReaderContent(
                    qrScanned = qrScanned,
                    navigateBack = navigateBack
                )
            }
        }
    )
}

@OptIn(ExperimentalGetImage::class)
@Composable
fun QrCodeReaderContent(
    qrScanned: (String) -> Unit,
    navigateBack: () -> Unit
) {
    val camera = remember { BarcodeCamera() }
    var isFlashOn by remember { mutableStateOf(false) }

    Box {
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
                    imageVector = if(isFlashOn) MifosIcons.FlashOn
                    else MifosIcons.FlashOff,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun QrCodeReaderScreenPreview() {
    MifosMobileTheme {
        QrCodeReaderScreen(
            qrScanned = {},
            navigateBack = {}
        )
    }
}