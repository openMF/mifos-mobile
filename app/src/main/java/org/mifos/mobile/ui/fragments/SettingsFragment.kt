package org.mifos.mobile.ui.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.DialogFragment
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mifos.mobile.passcode.utils.PasscodePreferencesHelper
import org.mifos.mobile.R
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.ui.activities.PassCodeActivity
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.utils.ConfigurationDialogFragmentCompat
import org.mifos.mobile.utils.ConfigurationPreference
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.LanguageHelper

/**
 * Created by dilpreet on 02/10/17.
 */
class SettingsFragment : PreferenceFragmentCompat(), OnSharedPreferenceChangeListener {

    private val prefsHelper by lazy { PreferencesHelper(requireContext().applicationContext) }
    var preference: android.preference.Preference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_preference)
        findPreference(getString(R.string.theme_type)).setOnPreferenceClickListener {
            val previouslySelectedTheme = prefsHelper.appTheme

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.change_app_theme))
                .setSingleChoiceItems(resources.getStringArray(R.array.themes), previouslySelectedTheme) { dialog, selectedTheme ->
                    prefsHelper.applyTheme(AppTheme.fromIndex(selectedTheme))
                    dialog.dismiss()
                }
                .show()
            return@setOnPreferenceClickListener true
        }
        findPreference(getString(R.string.passcode)).setOnPreferenceClickListener {
            val passCodePreferencesHelper = PasscodePreferencesHelper(activity)
            val currPassCode = passCodePreferencesHelper.passCode
            passCodePreferencesHelper.savePassCode("")
            val intent = Intent(activity, PassCodeActivity::class.java).apply {
                putExtra(Constants.CURR_PASSWORD, currPassCode)
                putExtra(Constants.IS_TO_UPDATE_PASS_CODE, true)
            }
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
            startActivity(intent)
            Toast.makeText(context,"Passcode clicked",Toast.LENGTH_SHORT).show()
            true
        }
        when (preference?.key) {
            getString(R.string.password) -> {
                //    TODO("create changePasswordActivity and implement the logic for password change")
            }
            getString(R.string.passcode) -> {
                activity?.let {
                    val passCodePreferencesHelper = PasscodePreferencesHelper(activity)
                    val currPassCode = passCodePreferencesHelper.passCode
                    passCodePreferencesHelper.savePassCode("")
                    val intent = Intent(it, PassCodeActivity::class.java).apply {
                        putExtra(Constants.CURR_PASSWORD, currPassCode)
                        putExtra(Constants.IS_TO_UPDATE_PASS_CODE, true)
                    }
                    preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
                    startActivity(intent)
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.settings)
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onDisplayPreferenceDialog(preference: Preference) {
        var dialogFragment: DialogFragment? = null
        if (preference is ConfigurationPreference) {
            dialogFragment = ConfigurationDialogFragmentCompat()
            val bundle = Bundle(1)
            bundle.putString("key", preference.getKey())
            dialogFragment.setArguments(bundle)
        }
        if (dialogFragment != null) {
            dialogFragment.setTargetFragment(this, 0)
            dialogFragment.show(parentFragmentManager,
                    "android.support.v7.preference.PreferenceFragment.DIALOG")
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, s: String) {
        val preference = findPreference(s)
        if (preference is ListPreference) {
            LanguageHelper.setLocale(context, preference.value)
            val intent = Intent(activity, activity?.javaClass)
            intent.putExtra(Constants.HAS_SETTINGS_CHANGED, true)
            startActivity(intent)
            activity?.finish()
        }
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            Constants.PASSWORD -> (activity as BaseActivity?)?.replaceFragment(UpdatePasswordFragment.newInstance(), true, R.id.container)
        }
        return super.onPreferenceTreeClick(preference)
    }

    companion object {
        @kotlin.jvm.JvmStatic
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}


enum class AppTheme{
    SYSTEM, LIGHT, DARK;
    companion object{
        fun fromIndex(index: Int): AppTheme = when (index){
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
        }
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
        }
    )
}