package org.mifos.mobile.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentRegistrationVerificationBinding
import org.mifos.mobile.models.register.UserVerify
import org.mifos.mobile.presenters.RegistrationVerificationPresenter
import org.mifos.mobile.ui.activities.LoginActivity
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.views.RegistrationVerificationView
import org.mifos.mobile.utils.Toaster
import javax.inject.Inject

/**
 * Created by dilpreet on 31/7/17.
 */
class RegistrationVerificationFragment : BaseFragment(), RegistrationVerificationView {
    private var _binding: FragmentRegistrationVerificationBinding? = null
    private val binding get() = _binding!!

    @JvmField
    @Inject
    var presenter: RegistrationVerificationPresenter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegistrationVerificationBinding.inflate(inflater, container, false)
        val rootView = binding.root
        (activity as BaseActivity?)?.activityComponent?.inject(this)
        presenter?.attachView(this)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnVerify.setOnClickListener {
            verifyClicked()
        }
    }

    private fun verifyClicked() {
        val userVerify = UserVerify()
        userVerify.authenticationToken = binding.etAuthenticationToken.text.toString()
        userVerify.requestId = binding.etRequestId.text.toString()
        presenter?.verifyUser(userVerify)
    }

    override fun showVerifiedSuccessfully() {
        startActivity(Intent(activity, LoginActivity::class.java))
        Toast.makeText(context, getString(R.string.verified), Toast.LENGTH_SHORT).show()
        activity?.finish()
    }

    override fun showError(msg: String?) {
        Toaster.show(binding.root, msg)
    }

    override fun showProgress() {
        showMifosProgressDialog(getString(R.string.verifying))
    }

    override fun hideProgress() {
        hideMifosProgressDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.detachView()
        _binding = null
    }

    companion object {
        fun newInstance(): RegistrationVerificationFragment {
            return RegistrationVerificationFragment()
        }
    }
}
