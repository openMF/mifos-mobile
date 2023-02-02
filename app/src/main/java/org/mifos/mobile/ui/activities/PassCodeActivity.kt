package org.mifos.mobile.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat

import com.mifos.mobile.passcode.MifosPassCodeActivity
import com.mifos.mobile.passcode.utils.EncryptionUtil

import org.mifos.mobile.R
import org.mifos.mobile.utils.CheckSelfPermissionAndRequest
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.MaterialDialog
import org.mifos.mobile.utils.Toaster

@RequiresApi(Build.VERSION_CODES.M)
class PassCodeActivity : MifosPassCodeActivity() {

    private var cancellationSignal: CancellationSignal? = null

    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() = @RequiresApi(Build.VERSION_CODES.P)
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)
            }
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                startNextActivity()
            }
        }

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!CheckSelfPermissionAndRequest.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE ))
        {
            requestPermission()
        }
        if(findViewById<AppCompatButton>(R.id.btn_save).visibility==View.GONE){
            val btn=findViewById<AppCompatButton>(R.id.btn_save)
            btn.visibility=View.VISIBLE
            btn.text="Use Touch Id"
            btn.setOnClickListener { openBiometric() }
        }
        checkBiometricSupport() // checks if the device has finger print enabled
        if(findViewById<AppCompatButton>(R.id.btn_save).text.equals("Use Touch Id")){
            openBiometric()
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
    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {

        }
        return cancellationSignal as CancellationSignal
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkBiometricSupport(): Boolean {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isDeviceSecure) {
            notifyUser("Fingerprint authentication has not been enabled in settings")
            return false
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
            notifyUser("Fingerprint Authentication Permission is not enabled")
            return false
        }
        return if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
    }
    private fun openBiometric(){
        val biometricPrompt = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            BiometricPrompt.Builder(this)
                .setTitle("Sign in Using fingerprint")
                .setNegativeButton(
                    "Enter Pin",
                    this.mainExecutor,
                    DialogInterface.OnClickListener { dialog, which ->
                    }).build()
        } else {
            TODO("VERSION.SDK_INT < P")
        }
        biometricPrompt.authenticate(getCancellationSignal(), mainExecutor, authenticationCallback)
    }

    override fun showToaster(view: View, msg: Int) {
        Toaster.show(view, msg)
    }

    override fun getEncryptionType(): Int {
        return EncryptionUtil.MOBILE_BANKING
    }
    private fun notifyUser(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        if(findViewById<AppCompatButton>(R.id.btn_save).text.equals("Use Touch Id")){
            openBiometric()
        }
        super.onResume()
    }
}