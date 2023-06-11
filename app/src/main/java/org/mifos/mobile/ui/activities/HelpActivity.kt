package org.mifos.mobile.ui.activities

import android.os.Bundle
import org.mifos.mobile.R
import org.mifos.mobile.databinding.ActivityContainerBinding
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.HelpFragment

/**
 * @author Rajan Maurya
 * On 11/03/19.
 */
class HelpActivity : BaseActivity() {
    private lateinit var binding: ActivityContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbarTitle(getString(R.string.help))
        showBackButton()
        replaceFragment(HelpFragment.newInstance(), false, R.id.container)
    }
}
