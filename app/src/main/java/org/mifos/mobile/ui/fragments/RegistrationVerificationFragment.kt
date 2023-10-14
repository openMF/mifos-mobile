package org.mifos.mobile.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentRegistrationVerificationBinding
import org.mifos.mobile.ui.activities.LoginActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.RegistrationUiState
import org.mifos.mobile.utils.Toaster
import org.mifos.mobile.viewModels.RegistrationViewModel

/**
 * Created by dilpreet on 31/7/17.
 */
@AndroidEntryPoint
class RegistrationVerificationFragment : BaseFragment() {
    private var _binding: FragmentRegistrationVerificationBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RegistrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegistrationVerificationBinding.inflate(inflater, container, false)
        val rootView = binding.root
        viewModel = ViewModelProvider(this)[RegistrationViewModel::class.java]
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.registrationVerificationUiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                RegistrationUiState.Loading -> showProgress()

                RegistrationUiState.Success -> {
                    hideProgress()
                    showVerifiedSuccessfully()
                }

                is RegistrationUiState.Error -> {
                    hideProgress()
                    showError(getString(state.exception))
                }
            }
        }

        binding.btnVerify.setOnClickListener {
            verifyClicked()
        }
    }

    private fun verifyClicked() {
        val authenticationToken = binding.etAuthenticationToken.text.toString()
        val requestId = binding.etRequestId.text.toString()
        viewModel.verifyUser(authenticationToken, requestId)
    }

    private fun showVerifiedSuccessfully() {
        startActivity(Intent(activity, LoginActivity::class.java))
        Toast.makeText(context, getString(R.string.verified), Toast.LENGTH_SHORT).show()
        activity?.finish()
    }

    fun showError(msg: String?) {
        Toaster.show(binding.root, msg)
    }

    fun showProgress() {
        showMifosProgressDialog(getString(R.string.verifying))
    }

    fun hideProgress() {
        hideMifosProgressDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): RegistrationVerificationFragment {
            return RegistrationVerificationFragment()
        }
    }
}
