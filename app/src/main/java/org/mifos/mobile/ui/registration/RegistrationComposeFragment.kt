package org.mifos.mobile.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment

/*
/**
 * Created by dilpreet on 31/7/17.
 */
@AndroidEntryPoint
class RegistrationComposeFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return mifosComposeView(requireContext()) {
            org.mifos.mobile.feature.registration.screens.RegistrationScreen(
                onVerified = { showRegisteredSuccessfully() },
                navigateBack = { activity?.onBackPressed() },
            )
        }
    }

    private fun showRegisteredSuccessfully() {
        (activity as BaseActivity?)?.replaceFragment(
            RegistrationVerificationComposeFragment.newInstance(),
            true,
            R.id.container,
        )
    }

    companion object {
        fun newInstance(): RegistrationComposeFragment {
            return RegistrationComposeFragment()
        }
    }
}
*/