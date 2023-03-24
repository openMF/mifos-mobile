package org.mifos.mobile.ui.activities

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.mifos.mobile.passcode.MifosPassCodeActivity
import com.mifos.mobile.passcode.MifosPassCodeView
import com.mifos.mobile.passcode.utils.EncryptionUtil
import com.mifos.mobile.passcode.utils.EncryptionUtil.TYPE
import com.mifos.mobile.passcode.utils.PasscodePreferencesHelper
import org.mifos.mobile.R
import org.mifos.mobile.utils.CheckSelfPermissionAndRequest
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.MaterialDialog
import org.mifos.mobile.utils.Toaster

class PassCodeActivity : MifosPassCodeActivity() {

    lateinit var passCodeHelper: PasscodePreferencesHelper
    lateinit var mifosPassCodeView:MifosPassCodeView
    private var counter=0
    private var currPassCode: String? = null
    private var isToUpdatePassCode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        passCodeHelper = PasscodePreferencesHelper(this)
        if (!CheckSelfPermissionAndRequest.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)) {
            requestPermission()
        }

        mifosPassCodeView = findViewById(com.mifos.mobile.passcode.R.id.pv_passcode)

        intent?.let {
            currPassCode = it.getStringExtra(Constants.CURR_PASSWORD)
            isToUpdatePassCode = it.getBooleanExtra(Constants.IS_TO_UPDATE_PASS_CODE, false)
        }

    }

    /**
     * Uses [CheckSelfPermissionAndRequest] to check for runtime permissions
     */
    private fun requestPermission() {
        CheckSelfPermissionAndRequest.requestPermission(
            this,
            Manifest.permission.READ_PHONE_STATE,
            Constants.PERMISSIONS_REQUEST_READ_PHONE_STATE,
            resources.getString(
                R.string.dialog_message_phone_state_permission_denied_prompt),
            resources.getString(R.string.dialog_message_phone_state_permission_never_ask_again),
            Constants.PERMISSIONS_READ_PHONE_STATE_STATUS)
    }

    override fun getLogo(): Int {
        return R.drawable.mifos_logo
    }

    override fun startNextActivity() {
        startActivity(Intent(this@PassCodeActivity, HomeActivity::class.java))
    }

    override fun startLoginActivity() {
        MaterialDialog.Builder().init(this@PassCodeActivity)
            .setCancelable(false)
            .setMessage(R.string.login_using_password_confirmation)
            .setPositiveButton(getString(R.string.logout),
                DialogInterface.OnClickListener { _, _ ->
                    val i = Intent(this@PassCodeActivity,
                        LoginActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(i)
                    finish()
                })
            .setNegativeButton(getString(R.string.cancel),
                DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })
            .createMaterialDialog()
            .show()
    }

    override fun showToaster(view: View, msg: Int) {
        Toaster.show(view, msg)
    }

    override fun getEncryptionType(): Int {
        return EncryptionUtil.MOBILE_BANKING
    }

    override fun passCodeEntered(passcode: String?) {
            val passwordEntered = EncryptionUtil.getMobileBankingHash(mifosPassCodeView.passcode)
            if (passCodeHelper.passCode.equals(passwordEntered)) {
                counter=0
                startNextActivity()
            } else {
                counter++
                mifosPassCodeView.clearPasscodeField()
                showToaster(findViewById(com.mifos.mobile.passcode.R.id.cl_rootview), R.string.incorrect_passcode)
            }
        if (counter > 3) {
            Toast.makeText(
                applicationContext, R.string.incorrect_passcode_more_than_three,
                Toast.LENGTH_SHORT
            ).show()
            mifosPassCodeView.clearPasscodeField()
            startLoginActivity()
        }
    }

    override fun onResume() {
        mifosPassCodeView.clearPasscodeField()
        super.onResume()
    }
}