package org.mifos.mobile.ui.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.snapshots.Snapshot.Companion.withMutableSnapshot
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import com.mifos.mobile.passcode.utils.PasscodePreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.ui.activities.HomeActivity
import org.mifos.mobile.ui.activities.PassCodeActivity
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.login.LoginActivity
import org.mifos.mobile.ui.update_password.UpdatePasswordFragment
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.LanguageHelper
import java.util.Locale

@AndroidEntryPoint
class SettingsComposeFragment : BaseFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val viewModel: SettingsViewModel by viewModels()
    private val prefsHelper by lazy { PreferencesHelper(requireContext().applicationContext) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return mifosComposeView(requireContext()) {
            SettingsScreen(
                prefsHelper = prefsHelper,
                settingsCard = viewModel.getSettingsCards(),
                onBackPressed = { goBackToPreviousScreen() },
                handleEndpointupdate = { baseUrl, tenant ->
                    handleEndpointUpdate(etTenant = tenant, etBaseURL = baseUrl)
                },
                changePasscode = { changePasscode() },
                changePassword = { changePassword() }
            )
        }
    }

    private fun changePassword() {
        (activity as BaseActivity?)?.replaceFragment(
            UpdatePasswordFragment.newInstance(),
            true,
            R.id.container,
        )
    }

    private fun changePasscode() {
        val passCodePreferencesHelper = PasscodePreferencesHelper(activity)
        val currPassCode = passCodePreferencesHelper.passCode
        val intent = Intent(activity, PassCodeActivity::class.java).apply {
            putExtra(Constants.CURR_PASSWORD, currPassCode)
            putExtra(Constants.IS_TO_UPDATE_PASS_CODE, true)
        }
        startActivity(intent)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, p1: String?) {
        if (p1 == getString(R.string.language_type)) {
            val languageValue = sharedPreferences?.getString(p1, null)
            languageValue?.let {
                val isSystemLanguage = (it == resources.getStringArray(R.array.languages_value)[0])
                prefsHelper.putBoolean(
                    getString(R.string.default_system_language), isSystemLanguage
                )
                if (!isSystemLanguage) {
                    LanguageHelper.setLocale(requireContext(), it)
                } else {
                    if (!resources.getStringArray(R.array.languages_value)
                            .contains(Locale.getDefault().language)
                    ) {
                        LanguageHelper.setLocale(requireContext(), "en")
                    } else {
                        LanguageHelper.setLocale(requireContext(), Locale.getDefault().language)
                    }
                }
                val intent = Intent(activity, activity?.javaClass)
                intent.putExtra(Constants.HAS_SETTINGS_CHANGED, true)
                startActivity(intent)
                activity?.finish()
            }
        }
    }

    private fun handleEndpointUpdate(etBaseURL: String, etTenant: String) {
        val intentToLogin = viewModel.tryUpdatingEndpoint(etBaseURL, etTenant)
        if (intentToLogin) {
            val loginIntent = Intent(activity, LoginActivity::class.java)
            loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(loginIntent)
            activity?.finish()
        }
    }

    private fun goBackToPreviousScreen() {
        val settingsActivity = activity as? SettingsActivity
        val hasSettingsChanged = settingsActivity?.hasSettingsChanged

        if (hasSettingsChanged == true) {
            activity?.finish()
            startActivity(Intent(activity, HomeActivity::class.java))
        } else {
            activity?.finish()
        }
    }

    override fun onResume() {
        super.onResume()
        PreferenceManager.getDefaultSharedPreferences(context)
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        PreferenceManager.getDefaultSharedPreferences(context)
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    companion object {
        fun newInstance(): SettingsComposeFragment {
            return SettingsComposeFragment()
        }
    }
}


enum class AppTheme {
    SYSTEM, LIGHT, DARK;

    companion object {
        fun fromIndex(index: Int): AppTheme = when (index) {
            1 -> LIGHT
            2 -> DARK
            else -> SYSTEM
        }
    }
}


fun PreferencesHelper.applySavedTheme() {
    val applicationTheme = AppTheme.fromIndex(this.appTheme)
    AppCompatDelegate.setDefaultNightMode(
        when {
            applicationTheme == AppTheme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            applicationTheme == AppTheme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            Build.VERSION.SDK_INT > Build.VERSION_CODES.P -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            else -> AppCompatDelegate.MODE_NIGHT_NO
        },
    )
}

fun PreferencesHelper.applyTheme(applicationTheme: AppTheme) {
    this.appTheme = applicationTheme.ordinal
    AppCompatDelegate.setDefaultNightMode(
        when {
            applicationTheme == AppTheme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            applicationTheme == AppTheme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            Build.VERSION.SDK_INT > Build.VERSION_CODES.P -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            else -> AppCompatDelegate.MODE_NIGHT_NO
        },
    )

}