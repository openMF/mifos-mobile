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
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.databinding.ActivityHomeBinding
import org.mifos.mobile.databinding.NavDrawerHeaderBinding
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.ui.about.AboutUsActivity
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.enums.AccountType
import org.mifos.mobile.ui.enums.ChargeType
import org.mifos.mobile.ui.fragments.*
import org.mifos.mobile.ui.getThemeAttributeColor
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.TextDrawable
import org.mifos.mobile.utils.Toaster
import org.mifos.mobile.utils.UserDetailUiState
import org.mifos.mobile.utils.fcm.RegistrationIntentService
import org.mifos.mobile.viewModels.UserDetailViewModel
import javax.inject.Inject

/**
 * @author Vishwajeet
 * @since 14/07/2016
 */
@AndroidEntryPoint
class HomeActivity :
    BaseActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navHeaderBinding: NavDrawerHeaderBinding

    @JvmField
    @Inject
    var preferencesHelper: PreferencesHelper? = null

    private val viewModel: UserDetailViewModel by viewModels()

    private var tvUsername: TextView? = null
    private var drawerUserImage: ShapeableImageView? = null
    private var clientId: Long? = 0
    private var userProfileBitmap: Bitmap? = null
    private var client: Client? = null
    private var isReceiverRegistered = false
    var checkedItem = 0
        private set
    var doubleBackToExitPressedOnce = false
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clientId = preferencesHelper?.clientId
        setupNavigationBar()
        setToolbarElevation()
        setToolbarTitle(getString(R.string.home))
        replaceFragment(HomeOldFragment.newInstance(), false, R.id.container)
        if (intent != null && intent.getBooleanExtra(
                getString(R.string.notification),
                false,
            )
        ) {
            replaceFragment(NotificationFragment.newInstance(), true, R.id.container)
        }
        if (savedInstanceState == null) {
            viewModel.userDetails
            viewModel.userImage
            showUserImage(null)
        } else {
            client = savedInstanceState.getParcelable(Constants.USER_DETAILS)
            viewModel.setUserProfile(preferencesHelper?.userProfileImage)
            showUserDetails(client)
        }
        if (checkPlayServices() && preferencesHelper?.sentTokenToServerState() == false) {
            // Start IntentService to register this application with GCM.
            val intent = Intent(this, RegistrationIntentService::class.java)
            startService(intent)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.userDetailUiState.collect {
                when (it) {
                    is UserDetailUiState.Loading -> showProgress()
                    is UserDetailUiState.ShowUserDetails -> {
                        hideProgress()
                        showUserDetails(it.client)
                    }
                    is UserDetailUiState.ShowUserImage -> {
                        hideProgress()
                        showUserImage(it.image)
                    }
                    is UserDetailUiState.ShowError -> {
                        hideProgress()
                        showError(getString(it.message))
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(Constants.USER_DETAILS, client)
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
     * Called whenever any item is selected in [NavigationView]
     *
     * @param item [MenuItem] which is selected by the user
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // select which item to open
        setToolbarElevation()
        checkedItem = item.itemId
        if (checkedItem != R.id.item_settings && checkedItem != R.id.item_share && checkedItem != R.id.item_about_us && checkedItem != R.id.item_help) {
            // If we have clicked something other than settings or share
            // we can safely clear the back stack as a new fragment will replace
            // the current fragment.
            clearFragmentBackStack()
        }
        when (item.itemId) {
            R.id.item_home -> {
                hideToolbarElevation()
                replaceFragment(HomeOldFragment.newInstance(), true, R.id.container)
            }

            R.id.item_accounts -> {
                hideToolbarElevation()
                replaceFragment(
                    ClientAccountsFragment.newInstance(AccountType.SAVINGS),
                    true,
                    R.id.container,
                )
            }

            R.id.item_recent_transactions -> replaceFragment(
                RecentTransactionsFragment.newInstance(),
                true,
                R.id.container,
            )

            R.id.item_charges -> replaceFragment(
                ClientChargeFragment.newInstance(clientId, ChargeType.CLIENT),
                true,
                R.id.container,
            )

            R.id.item_third_party_transfer -> replaceFragment(
                ThirdPartyTransferFragment.newInstance(),
                true,
                R.id.container,
            )

            R.id.item_beneficiaries -> replaceFragment(
                BeneficiaryListFragment.newInstance(),
                true,
                R.id.container,
            )

            R.id.item_settings -> startActivity(
                Intent(
                    this@HomeActivity,
                    SettingsActivity::class.java,
                ),
            )

            R.id.item_about_us -> startActivity(
                Intent(
                    this@HomeActivity,
                    AboutUsActivity::class.java,
                ),
            )

            R.id.item_help -> startActivity(Intent(this@HomeActivity, HelpActivity::class.java))
            R.id.item_app_info -> {
                val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }

            R.id.item_share -> {
                val i = Intent(Intent.ACTION_SEND)
                i.type = "text/plain"
                i.putExtra(
                    Intent.EXTRA_TEXT,
                    getString(
                        R.string.playstore_link,
                        getString(R.string.share_msg),
                        application.packageName,
                    ),
                )
                startActivity(Intent.createChooser(i, getString(R.string.choose)))
            }

            R.id.item_logout -> showLogoutDialog()
        }

        // close the drawer
        binding.drawer.closeDrawer(GravityCompat.START)
        return true
    }

    /**
     * Asks users to confirm whether he want to logout or not
     */
    private fun showLogoutDialog() {
        MaterialAlertDialogBuilder(this, R.style.RedDialog)
            .setTitle(R.string.dialog_logout)
            .setIcon(R.drawable.ic_logout)
            .setPositiveButton(getString(R.string.logout)) { _, _ ->
                preferencesHelper?.clear()
                val i = Intent(this@HomeActivity, LoginActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
                finish()
                Toast.makeText(this, R.string.logged_out_successfully, Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialogInterface, _ -> dialogInterface.dismiss() }
            .create()
            .show()
    }

    /**
     * This method is used to set up the navigation drawer for
     * self-service application
     */
    private fun setupNavigationBar() {
        binding.navigationView.setNavigationItemSelectedListener(this)
        val actionBarDrawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            binding.drawer,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer,
        ) {

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                hideKeyboard(drawerView)
            }
        }
        binding.drawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        binding.navigationView.getHeaderView(0)?.let { setupHeaderView(it) }
        setUpBackStackListener()
    }

    /**
     * Used for initializing values for HeaderView of NavigationView
     *
     * @param headerView Header view of NavigationView
     */
    private fun setupHeaderView(headerView: View) {
        navHeaderBinding = NavDrawerHeaderBinding.bind(headerView)
        tvUsername = navHeaderBinding.tvUserName
        drawerUserImage = navHeaderBinding.userImageRound
        drawerUserImage?.setOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java))
            binding.drawer.closeDrawer(GravityCompat.START)
        }
    }

    /**
     * Shows Client username in HeaderView of NavigationView
     *
     * @param client Contains details about the client
     */
    fun showUserDetails(client: Client?) {
        this.client = client
        preferencesHelper?.clientName = client?.displayName
        tvUsername?.text = client?.displayName
    }

    /**
     * Displays UserProfile Picture in HeaderView in NavigationView
     *
     * @param bitmap UserProfile Picture
     */
    fun showUserImage(bitmap: Bitmap?) {
        if (bitmap != null) {
            runOnUiThread {
                userProfileBitmap = bitmap
                drawerUserImage?.setImageBitmap(bitmap)
            }
        } else {
            runOnUiThread {
                val userName: String? = if (preferencesHelper?.clientName?.isEmpty() == false) {
                    preferencesHelper?.clientName
                } else {
                    getString(R.string.app_name)
                }
                val drawable = TextDrawable.builder()
                    .beginConfig()
                    .toUpperCase()
                    .endConfig()
                    .buildRound(
                        userName?.substring(0, 1),
                        getThemeAttributeColor(R.attr.colorPrimaryVariant),
                    )
                drawerUserImage?.setImageDrawable(drawable)
            }
        }
    }

    fun showProgress() {
        // empty, no need to show/hide progress in headerview
    }

    fun hideProgress() {
        // empty
    }

    /**
     * It is called whenever any error occurs while executing a request
     *
     * @param message contains information about error occurred
     */
    fun showError(message: String?) {
        showToast(message, Toast.LENGTH_SHORT)
    }

    /**
     * Handling back press
     */
    override fun onBackPressed() {
        if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
            binding.drawer.closeDrawer(GravityCompat.START)
            return
        }
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        if (fragment is HomeOldFragment) {
            if (doubleBackToExitPressedOnce && stackCount() == 0) {
                finish()
                return
            }
            doubleBackToExitPressedOnce = true
            Toaster.show(findViewById(android.R.id.content), getString(R.string.exit_message))
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        } else if (fragment is TransferProcessFragment) {
            fragment.cancelTransferProcess()
        }

        if (stackCount() != 0) {
            super.onBackPressed()
        }
    }

    private fun setUpBackStackListener() {
        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.container)
            setToolbarElevation()
            when (fragment) {
                is HomeOldFragment -> {
                    setNavigationViewSelectedItem(R.id.item_home)
                }

                is ClientAccountsFragment -> {
                    hideToolbarElevation()
                    setNavigationViewSelectedItem(R.id.item_accounts)
                }

                is RecentTransactionsFragment -> {
                    setNavigationViewSelectedItem(R.id.item_recent_transactions)
                }

                is ClientChargeFragment -> {
                    setNavigationViewSelectedItem(R.id.item_charges)
                }

                is ThirdPartyTransferFragment -> {
                    setNavigationViewSelectedItem(R.id.item_third_party_transfer)
                }

                is BeneficiaryListFragment -> {
                    setNavigationViewSelectedItem(R.id.item_beneficiaries)
                }
            }
        }
    }

    fun setNavigationViewSelectedItem(id: Int) {
        binding.navigationView.setCheckedItem(id)
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

    fun hideKeyboard(view: View) {
        val inputManager = this
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            view.windowToken,
            InputMethodManager.RESULT_UNCHANGED_SHOWN,
        )
    }

    companion object {
        private const val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
    }
}
