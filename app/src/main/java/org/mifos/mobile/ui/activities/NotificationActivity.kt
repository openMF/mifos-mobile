package org.mifos.mobile.ui.activities

import android.os.Bundle
import org.mifos.mobile.R
import org.mifos.mobile.databinding.ActivityNotificationBinding
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.notification.NotificationFragment

class NotificationActivity : BaseActivity() {

    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        replaceFragment(NotificationFragment.newInstance(), false, R.id.container)
    }
}
