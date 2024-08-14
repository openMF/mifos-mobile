package org.mifos.mobile.ui.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.utils.Toaster
import org.mifos.mobile.utils.fcm.RegistrationIntentService
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.feature.home.navigation.HomeNavigation
import org.mifos.mobile.feature.user_profile.viewmodel.UserDetailViewModel
import org.mifos.mobile.navigation.RootNavGraph

/**
 * @author Vishwajeet
 * @since 14/07/2016
 */
@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    private val viewModel: UserDetailViewModel by viewModels()
    private var isReceiverRegistered = false
    private var doubleBackToExitPressedOnce = false
    private lateinit var navHostController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            val intent = Intent(this, RegistrationIntentService::class.java)
            startService(intent)
        }

        enableEdgeToEdge()
        setContent {
            MifosMobileTheme {
                navHostController = rememberNavController()
                RootNavGraph(
                    startDestination = HomeNavigation.HomeBase.route,
                    navController = navHostController,
                )
            }
        }
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(registerReceiver)
        isReceiverRegistered = false
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(
                registerReceiver,
                IntentFilter(Constants.REGISTER_ON_SERVER),
            )
            isReceiverRegistered = true
        }
    }

    /**
     * Handling back press
     */
    override fun onBackPressed() {
        val currentRoute = navHostController.currentBackStackEntry?.destination?.route

        if (currentRoute == HomeNavigation.HomeScreen.route) {
            if (doubleBackToExitPressedOnce && stackCount() == 0) {
                finish()
                return
            }
            doubleBackToExitPressedOnce = true
            Toaster.show(findViewById(android.R.id.content), getString(R.string.exit_message))
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }

        if (stackCount() != 0) {
            super.onBackPressed()
        }
    }


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                    ?.show()
            } else {
                Log.i(HomeActivity::class.java.name, "This device is not supported.")
                finish()
            }
            return false
        }
        return true
    }

    private val registerReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val token = intent.getStringExtra(Constants.TOKEN)
            token?.let { viewModel.registerNotification(it) }
        }
    }

    companion object {
        private const val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
    }
}