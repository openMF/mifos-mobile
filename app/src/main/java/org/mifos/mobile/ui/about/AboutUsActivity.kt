package org.mifos.mobile.ui.about

import android.os.Bundle
import org.mifos.mobile.R
import org.mifos.mobile.databinding.ActivityContainerBinding
import org.mifos.mobile.ui.activities.base.BaseActivity

/**
 * @author Rajan Maurya
 * On 11/03/19.
 */
class AboutUsActivity : BaseActivity() {
    private lateinit var binding: ActivityContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbarTitle(getString(R.string.about_us))
        showBackButton()
        replaceFragment(AboutUsFragment.newInstance(), false, R.id.container)
    }
}
