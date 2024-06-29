package org.mifos.mobile.ui.help

import android.os.Bundle
import android.view.View
import org.mifos.mobile.R
import org.mifos.mobile.databinding.ActivityContainerBinding
import org.mifos.mobile.ui.activities.base.BaseActivity

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
        toolbar?.visibility = View.GONE
        replaceFragment(HelpFragment.newInstance(), false, R.id.container)
    }
}
