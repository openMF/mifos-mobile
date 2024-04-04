package org.mifos.mobile.ui.activities

import android.os.Bundle
import org.mifos.mobile.R
import org.mifos.mobile.databinding.ActivityEditUserDetailBinding
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.update_password.UpdatePasswordFragment

/*
* Created by saksham on 14/July/2018
*/
class EditUserDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityEditUserDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbarTitle(
            getString(
                R.string.string_and_string,
                getString(R.string.edit),
                getString(R.string.user_details),
            ),
        )
        showBackButton()
        replaceFragment(UpdatePasswordFragment.newInstance(), false, R.id.container)
    }
}
