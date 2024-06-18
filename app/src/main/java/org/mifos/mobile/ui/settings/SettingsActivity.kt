package org.mifos.mobile.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat
import org.mifos.mobile.R
import org.mifos.mobile.databinding.ActivityUserProfileBinding
import org.mifos.mobile.ui.activities.HomeActivity
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.utils.Constants

class SettingsActivity : BaseActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    var hasSettingsChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(SettingsComposeFragment.newInstance(), false, R.id.container)
        if (intent.hasExtra(Constants.HAS_SETTINGS_CHANGED)) {
            hasSettingsChanged = intent.getBooleanExtra(
                Constants.HAS_SETTINGS_CHANGED,
                false,
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            super.onBackPressed()
            if (hasSettingsChanged) {
                ActivityCompat.finishAffinity(this)
                startActivity(Intent(this, HomeActivity::class.java))
            }
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}