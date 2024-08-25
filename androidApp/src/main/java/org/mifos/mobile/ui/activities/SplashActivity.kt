package org.mifos.mobile.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity

/*
* Created by saksham on 01/June/2018
*/
class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO:: check for user logged in or not, and if logged in move to PasscodeActivity instead.
        val intent = Intent(this, LoginActivity::class.java)

        startActivity(intent)
        finish()
    }
}
