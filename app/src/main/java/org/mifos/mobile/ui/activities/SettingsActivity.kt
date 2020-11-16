package org.mifos.mobile.ui.activities

import android.content.Intent
import android.os.Bundle

import androidx.core.app.ActivityCompat

import org.mifos.mobile.R
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.SettingsFragment
import org.mifos.mobile.utils.Constants

class SettingsActivity : BaseActivity() {

    private var hasSettingsChanged = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setToolbarTitle(getString(R.string.settings))
        showBackButton()
        replaceFragment(SettingsFragment.newInstance(), false, R.id.container)
        if (intent.hasExtra(Constants.HAS_SETTINGS_CHANGED)) {
            hasSettingsChanged = intent.getBooleanExtra(Constants.HAS_SETTINGS_CHANGED,
                    false)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (hasSettingsChanged) {
            ActivityCompat.finishAffinity(this)
            val i = Intent(this, HomeActivity::class.java)
            startActivity(i)
        }
    }
}