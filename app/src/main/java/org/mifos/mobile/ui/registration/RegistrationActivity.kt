package org.mifos.mobile.ui.registration

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.feature.registration.navigation.RegistrationNavGraph
import org.mifos.mobile.feature.registration.navigation.RegistrationScreen
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.login.LoginActivity

class RegistrationActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
