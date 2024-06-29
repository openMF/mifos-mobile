package org.mifos.mobile.ui.registration

import android.os.Bundle
import org.mifos.mobile.R
import org.mifos.mobile.databinding.ActivityRegistrationBinding
import org.mifos.mobile.ui.activities.base.BaseActivity

class RegistrationActivity : BaseActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(RegistrationFragment.newInstance(), false, R.id.container)
    }

}
