package org.mifos.mobile.ui.activities

import android.content.Intent
import androidx.biometric.BiometricManager
import android.os.Build
import android.provider.Settings
import android.provider.Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED
import androidx.activity.result.ActivityResultLauncher
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import org.mifos.mobile.R
import org.mifos.mobile.ui.enums.BiometricCapability

open class BiometricAuthentication(
    val context: FragmentActivity,
) {
    private val executor = ContextCompat.getMainExecutor(context)
    private val callback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            val intent = Intent(context, HomeActivity::class.java)
            context.startActivity(intent)
        }

        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
        }
    }

    private val biometricPrompt = BiometricPrompt(context, executor, callback)

    fun launchBiometricEnrollment(resultLauncher: ActivityResultLauncher<Intent>) {
        val intent: Intent = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                Intent(Settings.ACTION_BIOMETRIC_ENROLL).putExtra(
                    EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> {
                Intent(Settings.ACTION_FINGERPRINT_ENROLL)
            }
            else -> {
                Intent(Settings.ACTION_SECURITY_SETTINGS)
            }
        }
        resultLauncher.launch(intent)
    }

    fun getBiometricCapabilities(): BiometricCapability {
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                BiometricCapability.HAS_BIOMETRIC_AUTH
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                BiometricCapability.NOT_ENROLLED
            }
            else -> {
                BiometricCapability.NOT_SUPPORTED
            }
        }
    }

    fun authenticateWithBiometrics() {
        val promptInfo = BiometricPrompt.PromptInfo.Builder().apply {
            setTitle(context.getString(R.string.sign_in_fingerprint))
            setDescription(context.getString(R.string.scan_your_fingerprint))
            setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
        }.build()

        biometricPrompt.authenticate(promptInfo)
    }
}

