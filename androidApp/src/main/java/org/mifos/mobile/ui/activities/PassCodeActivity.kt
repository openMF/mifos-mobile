package org.mifos.mobile.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.navigation.PASSCODE_SCREEN
import org.mifos.mobile.navigation.RootNavGraph

@AndroidEntryPoint
class PassCodeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MifosMobileTheme {
                val navController = rememberNavController()

                Box(modifier = Modifier.statusBarsPadding()) {
                    RootNavGraph(
                        startDestination = PASSCODE_SCREEN,
                        navController = navController,
                    )
                }
            }
        }
    }
}
