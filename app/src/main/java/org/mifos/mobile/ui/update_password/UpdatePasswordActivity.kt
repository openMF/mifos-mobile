package org.mifos.mobile.ui.update_password

import android.os.Bundle
import org.mifos.mobile.R
import org.mifos.mobile.databinding.ActivityContainerBinding
import org.mifos.mobile.ui.activities.base.BaseActivity

class UpdatePasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(UpdatePasswordFragment.newInstance(), false, R.id.container)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (fragmentManager?.backStackEntryCount!! > 0) {
            fragmentManager?.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}