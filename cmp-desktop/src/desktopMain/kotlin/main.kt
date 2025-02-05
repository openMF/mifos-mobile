import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.mifos.mobile.shared.di.initKoin
import org.mifos.mobile.shared.MifosMobileSharedApp

fun main() {
    application {
        initKoin()
        val windowState = rememberWindowState()
        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "MifosMobile",
        ) {
            MifosMobileSharedApp()
        }
    }
}
