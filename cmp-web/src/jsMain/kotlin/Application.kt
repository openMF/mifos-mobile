import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import org.jetbrains.skiko.wasm.onWasmReady
import org.mifos.mobile.shared.MifosMobileSharedApp
import org.mifos.mobile.shared.di.initKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()

    onWasmReady {
        ComposeViewport(document.body!!) {
            MifosMobileSharedApp()
        }
    }
}