import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import org.jetbrains.compose.resources.configureWebResources
import org.mifos.mobile.shared.MifosMobileSharedApp
import org.mifos.mobile.shared.di.initKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()

    configureWebResources {
        resourcePathMapping { path -> "./$path" }
    }

    CanvasBasedWindow(
        title = "MifosMobile",
        canvasElementId = "ComposeTarget",
    ) {
        MifosMobileSharedApp()
    }
}