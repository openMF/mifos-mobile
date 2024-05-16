package org.mifos.mobile.ui.qrCodeReader

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import me.dm7.barcodescanner.zxing.ZXingScannerView
import org.mifos.mobile.MifosSelfServiceApp.Companion.context
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.MifosTopBar
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import com.google.zxing.Result

@Composable
fun QrCodeReaderScreen(
    scannerView: ZXingScannerView,
    onBackPressed: () -> Unit,
) {
    var flashOn by rememberSaveable { mutableStateOf(false) }

    DisposableEffect(Unit) {
        scannerView.startCamera()
        onDispose {
            scannerView.stopCamera()
        }
    }

    Scaffold(
        topBar = {
            MifosTopBar(navigateBack = { onBackPressed.invoke() }) {
                Text(text = stringResource(id = R.string.add_beneficiary))
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            AndroidView({ scannerView })
            IconButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp),
                onClick = {
                    flashOn = !flashOn
                    scannerView.flash = flashOn
                }) {
                val icon: Painter = if (flashOn) {
                    painterResource(id = R.drawable.ic_flash_on)
                } else {
                    painterResource(id = R.drawable.ic_flash_off)
                }
                Icon(
                    painter = icon,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PreviewQrCodeScreen() {
    MifosMobileTheme {
        QrCodeReaderScreen(
            scannerView = ZXingScannerView(context),
            onBackPressed = {}
        )
    }
}
