package org.mifos.mobile.ui.activities

import android.os.Bundle

import org.mifos.mobile.R
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.AboutUsFragment

/**
 * @author Rajan Maurya
 * On 11/03/19.
 */
class AboutUsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        setToolbarTitle(getString(R.string.about_us))
        showBackButton()
        replaceFragment(AboutUsFragment.newInstance(), false, R.id.container)
    }
}