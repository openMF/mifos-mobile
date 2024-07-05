package org.mifos.mobile.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.feature.login.screens.LoginScreen
import org.mifos.mobile.ui.activities.PassCodeActivity
import org.mifos.mobile.ui.registration.RegistrationActivity
import org.mifos.mobile.ui.activities.base.BaseActivity

/**
 * @author Vishwajeet
 * @since 05/06/16
 */
@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MifosMobileTheme {

                LoginScreen(
                    startRegisterActivity = { onRegisterClicked() },
                    startPassCodeActivity = { startPassCodeActivity() },
                )
            }
        }
    }

    private fun onRegisterClicked() {
        startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
    }
    /**
     * Starts [PassCodeActivity] with `Constans.INTIAL_LOGIN` as true
     */

    private fun startPassCodeActivity() {
        val intent = Intent(this@LoginActivity, PassCodeActivity::class.java)
        intent.putExtra(org.mifos.mobile.core.common.Constants.INTIAL_LOGIN, true)
        startActivity(intent)
        finish()
    }

}
