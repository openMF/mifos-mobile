package org.mifos.mobile.ui.activities

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.core.widget.NestedScrollView
import butterknife.BindView
import android.provider.Settings
import androidx.appcompat.widget.AppCompatButton
import butterknife.ButterKnife
import com.mifos.mobile.passcode.MifosPassCodeActivity
import com.mifos.mobile.passcode.utils.EncryptionUtil
import org.mifos.mobile.R
import org.mifos.mobile.ui.enums.BiometricCapability
import org.mifos.mobile.utils.CheckSelfPermissionAndRequest
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.MaterialDialog
import org.mifos.mobile.utils.Toaster

class PassCodeActivity : MifosPassCodeActivity() {
    private var currPassCode: String? = null
    private var isToUpdatePassCode: Boolean = false

    @JvmField
    @BindView(R.id.btn_save)
    var btnSave: AppCompatButton? = null

    @JvmField
    @BindView(R.id.cl_rootview)
    var nestedScrollView : NestedScrollView? = null

    private var biometricAuthentication: BiometricAuthentication? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ButterKnife.bind(this)
        biometricAuthentication = BiometricAuthentication(this)

        if (!CheckSelfPermissionAndRequest.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            )
        ) {
            requestPermission()
        }
        setBackgroundColor()

        if (btnSave?.text?.equals(getString(R.string.use_touch_id)) == true) {
            biometricAuthentication?.authenticateWithBiometrics()
        }
        if (biometricAuthentication?.getBiometricCapabilities() == BiometricCapability.HAS_BIOMETRIC_AUTH) {
            if (btnSave?.visibility == View.GONE) {
                btnSave?.visibility = View.VISIBLE
                btnSave?.text = getString(R.string.use_touch_id)
                btnSave?.setOnClickListener {
                    biometricAuthentication?.authenticateWithBiometrics()
                }
            }
        } else if (biometricAuthentication?.getBiometricCapabilities() == BiometricCapability.NOT_SUPPORTED) {
            startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS))
        }

        intent?.let {
            currPassCode = it.getStringExtra(Constants.CURR_PASSWORD)
            isToUpdatePassCode = it.getBooleanExtra(Constants.IS_TO_UPDATE_PASS_CODE, false)
        }
    }

    /**
     * Uses [CheckSelfPermissionAndRequest] to check fomifosr runtime permissions
     */
    private fun requestPermission() {
        CheckSelfPermissionAndRequest.requestPermission(
            this,
            Manifest.permission.READ_PHONE_STATE,
            Constants.PERMISSIONS_REQUEST_READ_PHONE_STATE,
            resources.getString(
                R.string.dialog_message_phone_state_permission_denied_prompt
            ),
            resources.getString(R.string.dialog_message_phone_state_permission_never_ask_again),
            Constants.PERMISSIONS_READ_PHONE_STATE_STATUS
        )
    }

    private fun setBackgroundColor(){
        val typedValue = TypedValue()
        theme.resolveAttribute(android.R.attr.colorBackground, typedValue, true)
        nestedScrollView?.setBackgroundColor(typedValue.data)
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
                    val i = Intent(
                        this@PassCodeActivity,
                        LoginActivity::class.java
                    )
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

    override fun onResume() {
        if (btnSave?.text?.equals(getString(R.string.use_touch_id)) == true) {
            biometricAuthentication?.authenticateWithBiometrics()
        }
        super.onResume()
    }
}