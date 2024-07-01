package org.mifos.mobile.ui.registration

/*
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.login.LoginActivity

/**
 * Created by dilpreet on 31/7/17.
 */
@AndroidEntryPoint
class RegistrationVerificationComposeFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return mifosComposeView(requireContext()) {
            org.mifos.mobile.feature.registration.screens.RegistrationVerificationScreen(
                navigateBack = { activity?.finish() },
                onVerified = {
                    startActivity(Intent(activity, LoginActivity::class.java))
                    activity?.finish()
                }
            )
        }
    }
    companion object {
        fun newInstance(): RegistrationVerificationComposeFragment {
            return RegistrationVerificationComposeFragment()
        }
    }
}
*/