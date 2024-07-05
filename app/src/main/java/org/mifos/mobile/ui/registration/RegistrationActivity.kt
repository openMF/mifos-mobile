package org.mifos.mobile.ui.registration

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.WindowInsets
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.feature.registration.navigation.RegistrationNavGraph
import org.mifos.mobile.feature.registration.navigation.RegistrationScreen
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.login.LoginActivity

class RegistrationActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            MifosMobileTheme {
                val navController = rememberNavController()
                RegistrationNavGraph(
                    startDestination = RegistrationScreen.Registration.route,
                    navController = navController,
                    navigateBack = { this.finish() },
                    startLoginActivity = { startLoginActivity() }
                )
            }
        }
    }

    private fun startLoginActivity()
    {
        this.startActivity(Intent(this, LoginActivity::class.java))
        this.finish()
    }
}
