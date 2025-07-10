
package cmp.navigation.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.Navigator
import androidx.navigation.compose.rememberNavController
import co.touchlab.kermit.Logger

@Composable
fun rememberMifosNavController(
    name: String,
    vararg navigators: Navigator<out NavDestination>,
): NavHostController =
    rememberNavController(navigators = navigators).apply {
        this.addOnDestinationChangedListener { _, destination, _ ->
            val graph = destination.parent?.route?.let { " in $it" }.orEmpty()
            Logger.d("$name destination changed: ${destination.route}$graph")
        }
    }
