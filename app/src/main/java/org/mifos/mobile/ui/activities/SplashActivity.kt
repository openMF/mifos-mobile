package org.mifos.mobile.ui.activities

import android.content.Intent
import android.os.Bundle
import com.mifos.mobile.passcode.utils.PasscodePreferencesHelper
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.login.LoginActivity
import org.mifos.mobile.utils.Constants

/*
* Created by saksham on 01/June/2018
*/
class SplashActivity : BaseActivity() {

    private var passcodePreferencesHelper: PasscodePreferencesHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        passcodePreferencesHelper = PasscodePreferencesHelper(this)
        val intent: Intent = if (passcodePreferencesHelper?.passCode?.isNotEmpty() == true) {
            Intent(this, PassCodeActivity::class.java).apply {
                putExtra(Constants.INTIAL_LOGIN, true)
            }
        } else {
            Intent(this, LoginActivity::class.java)
        }

        startActivity(intent)
        finish()
    }




    }

