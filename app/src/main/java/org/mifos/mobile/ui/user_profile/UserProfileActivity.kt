package org.mifos.mobile.ui.user_profile

import android.os.Bundle
import org.mifos.mobile.R
import org.mifos.mobile.databinding.ActivityUserProfileBinding
import org.mifos.mobile.ui.activities.base.BaseActivity

class UserProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(UserProfileFragment.newInstance(), false, R.id.container)
    }
}
