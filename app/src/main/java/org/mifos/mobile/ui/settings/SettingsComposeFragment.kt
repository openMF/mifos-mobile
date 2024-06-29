package org.mifos.mobile.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.ui.activities.HomeActivity
import org.mifos.mobile.ui.activities.PassCodeActivity
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.login.LoginActivity
import org.mifos.mobile.ui.update_password.UpdatePasswordFragment

@AndroidEntryPoint
class SettingsComposeFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return mifosComposeView(requireContext()) {
            MifosMobileTheme {
                SettingsScreen(
                    navigateBack = { goBackToPreviousScreen() },
                    navigateToLoginScreen = { navigateToLoginScreen() },
                    changePassword = { changePassword() },
                    changePasscode = { changePasscode(it) },
                    languageChanged = { languageChanged() }
                )
            }
        }
    }

    private fun navigateToLoginScreen() {
        val loginIntent = Intent(activity, LoginActivity::class.java)
        loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(loginIntent)
        activity?.finish()
    }

    private fun changePassword() {
        (activity as BaseActivity?)?.replaceFragment(
            UpdatePasswordFragment.newInstance(),
            true,
            R.id.container,
        )
    }

    private fun changePasscode(passcode: String) {
        val intent = Intent(activity, PassCodeActivity::class.java).apply {
            putExtra(org.mifos.mobile.core.common.Constants.CURR_PASSWORD, passcode)
            putExtra(org.mifos.mobile.core.common.Constants.IS_TO_UPDATE_PASS_CODE, true)
        }
        startActivity(intent)
    }

    private fun languageChanged() {
        val intent = Intent(activity, activity?.javaClass)
        intent.putExtra(org.mifos.mobile.core.common.Constants.HAS_SETTINGS_CHANGED, true)
        startActivity(intent)
        activity?.finish()
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

    companion object {
        fun newInstance(): SettingsComposeFragment {
            return SettingsComposeFragment()
        }
    }
}


