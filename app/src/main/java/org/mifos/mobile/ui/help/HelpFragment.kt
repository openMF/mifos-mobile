package org.mifos.mobile.ui.help

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.feature.help.HelpScreen
import org.mifos.mobile.feature.help.HelpViewModel
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.location.LocationsFragment

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/
@AndroidEntryPoint
class HelpFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return mifosComposeView(requireContext()) {
            HelpScreen(
                callNow = { callHelpline() },
                leaveEmail = { mailHelpline() },
                findLocations = { findLocations() },
                navigateBack = { activity?.finish() },
            )
        }
    }

    private fun callHelpline() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + getString(R.string.help_line_number))
        startActivity(intent)
    }

    private fun findLocations() {
        (activity as BaseActivity?)?.replaceFragment(
            LocationsFragment.newInstance(),
            true,
            R.id.container,
        )
    }

    private fun mailHelpline() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.contact_email)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.user_query))
        }
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                getString(R.string.no_app_to_support_action),
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): HelpFragment {
            val fragment = HelpFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}