import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import androidx.compose.ui.window.ComposeViewport
import androidx.compose.ui.window.ComposeViewportConfiguration
import cmp.shared.SharedApp
import cmp.shared.utils.initKoin
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.browser.window
import org.jetbrains.compose.resources.configureWebResources
import org.jetbrains.skiko.wasm.onWasmReady

/**
 * Main function.
 * This function is used to start the application and initialize essential components.
 * It performs the following tasks:
 * 1. Initializes Koin for dependency injection.
 * 2. Configures the web resources, specifically setting up resource path mapping.
 * 3. Creates a canvas-based window to host the Compose UI, setting the window title and the canvas element ID.
 * 4. Calls `SharedApp()` to render the root composable of the application.
 *
 * @see CanvasBasedWindow
 * @see SharedApp
 */
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    /*
     * Initializes the Koin dependency injection framework.
     * This function sets up the necessary dependencies for the application to function correctly.
     */
    initKoin()

    // Apply stored language preference on startup
    val storedLanguage = localStorage.getItem("app_language")
    if (storedLanguage != null) {
        document.documentElement?.setAttribute("lang", storedLanguage)
    }

    /*
     * Configures the web resources for the application.
     * Specifically, it sets a path mapping for resources (e.g., CSS, JS).
     */
    configureWebResources {
        resourcePathMapping { path -> "./$path" }
    }

    /*
     * Creates a Canvas-based window for rendering the Compose UI.
     * This window uses the canvas element with the ID "ComposeTarget" and has the title "WebApp".
     */
    onWasmReady {
        ComposeViewport(document.body!!) {
            // State to trigger recomposition when locale changes
            var localeVersion by remember { mutableStateOf(0) }

            // Use key() to force complete recomposition when locale changes
            key(localeVersion) {
                /*
             * Invokes the root composable of the application.
             * This function is responsible for setting up the entire UI structure of the app.
             */
                SharedApp(
                    updateScreenCapture = {},
                    handleRecreate = {
                        // Reload the page to apply locale changes
                        window.location.reload()
                    },
                    handleThemeMode = {},
                    handleAppLocale = { languageTag ->
                        if (languageTag != null) {
                            // Store language preference in localStorage
                            localStorage.setItem("app_language", languageTag)
                            // Set HTML lang attribute for accessibility
                            document.documentElement?.setAttribute("lang", languageTag)
                        } else {
                            // System Default: remove stored language preference
                            localStorage.removeItem("app_language")
                            // Reset to browser's default language
                            val browserLang = window.navigator.language
                            document.documentElement?.setAttribute("lang", browserLang)
                        }
                        // Reload page to apply language changes (required for web)
                        // Note: This will reload the page, and locale selection depends on browser settings
                        // window.location.reload()
                    },
                    onSplashScreenRemoved = {},
                )
            }
        }
    }
}
