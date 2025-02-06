import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import cmp.shared.SharedApp
import cmp.shared.utils.initKoin
import org.jetbrains.compose.resources.configureWebResources

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
    CanvasBasedWindow(
        title = "WebApp", // Window title
        canvasElementId = "ComposeTarget", // The canvas element where the Compose UI will be rendered
    ) {
        /*
         * Invokes the root composable of the application.
         * This function is responsible for setting up the entire UI structure of the app.
         */
        SharedApp()
    }
}
