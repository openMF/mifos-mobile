package org.mifos.mobile.ui.registration

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.login.LoginActivity

/**
 * Created by dilpreet on 31/7/17.
 */
@AndroidEntryPoint
class RegistrationVerificationFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return mifosComposeView(requireContext()) {
            RegistrationVerificationScreen(
                navigateBack = { activity?.finish() } ,
                onVerified = {
                    startActivity(Intent(activity, LoginActivity::class.java))
                    activity?.finish()
                }
            )
        }
    }
    companion object {
        fun newInstance(): RegistrationVerificationFragment {
            return RegistrationVerificationFragment()
        }
    }
}
