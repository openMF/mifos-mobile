package org.mifos.mobile.ui.activities

import android.content.Intent
import android.os.Bundle

import com.mifos.mobile.passcode.utils.PasscodePreferencesHelper
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.utils.Constants

/*
* Created by saksham on 01/June/2018
*/
class SplashActivity : BaseActivity() {

    private var passcodePreferencesHelper: PasscodePreferencesHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val intent: Intent?
        super.onCreate(savedInstanceState)
        activityComponent?.inject(this)
        passcodePreferencesHelper = PasscodePreferencesHelper(this)
        if (passcodePreferencesHelper?.passCode?.isNotEmpty() == true) {
            intent = Intent(this, PassCodeActivity::class.java)
            intent.putExtra(Constants.INTIAL_LOGIN, true)
        } else {
            intent = Intent(this, LoginActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}