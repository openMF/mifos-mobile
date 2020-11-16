package org.mifos.mobile.ui.activities

import android.os.Bundle

import org.mifos.mobile.R
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.BeneficiaryAddOptionsFragment

/**
 * @author Rajan Maurya
 * On 04/06/18.
 */
class AddBeneficiaryActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        showBackButton()
        replaceFragment(BeneficiaryAddOptionsFragment.newInstance(), false, R.id.container)
    }
}