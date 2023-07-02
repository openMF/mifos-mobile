package org.mifos.mobile.ui.fragments

import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import com.hbb20.CountryCodePicker
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentRegistrationBinding
import org.mifos.mobile.models.register.RegisterPayload
import org.mifos.mobile.presenters.RegistrationPresenter
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.views.RegistrationView
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.PasswordStrength
import org.mifos.mobile.utils.Toaster
import javax.inject.Inject

/**
 * Created by dilpreet on 31/7/17.
 */
@AndroidEntryPoint
class RegistrationFragment : BaseFragment(), RegistrationView {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    @JvmField
    @Inject
    var presenter: RegistrationPresenter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        val rootView = binding.root
        //(activity as BaseActivity?)?.activityComponent?.inject(this)
        presenter?.attachView(this)
        binding.progressBar.visibility = View.GONE
        binding.passwordStrength.visibility = View.GONE
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.isEmpty()) {
                    binding.progressBar.visibility = View.GONE
                    binding.passwordStrength.visibility = View.GONE
                } else {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.passwordStrength.visibility = View.VISIBLE
                    updatePasswordStrengthView(charSequence.toString())
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        return rootView
    }

    private fun updatePasswordStrengthView(password: String) {
        if (TextView.VISIBLE != binding.passwordStrength.visibility) return
        if (password.isEmpty()) {
            binding.passwordStrength.text = ""
            binding.progressBar.progress = 0
            return
        }
        val str = PasswordStrength.calculateStrength(password)
        binding.passwordStrength.text = str.getText(context)
        binding.passwordStrength.setTextColor(str.color)
        val mode = PorterDuff.Mode.SRC_IN
        binding.progressBar.progressDrawable?.setColorFilter(str.color, mode)
        if (str.getText(context) == getString(R.string.password_strength_weak)) {
            binding.progressBar.progress = 25
        } else if (str.getText(context) == getString(R.string.password_strength_medium)) {
            binding.progressBar.progress = 50
        } else if (str.getText(context) == getString(R.string.password_strength_strong)) {
            binding.progressBar.progress = 75
        } else {
            binding.progressBar.progress = 100
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegister.setOnClickListener {
            registerClicked()
        }
    }

    private fun registerClicked() {
        if (areFieldsValidated()) {
            val radioButton = binding.rgVerificationMode.checkedRadioButtonId.let {
                binding.root.findViewById<RadioButton>(it)
            }
            val payload = RegisterPayload()
            payload.accountNumber = binding.etAccountNumber.text.toString()
            payload.authenticationMode = radioButton?.text.toString()
            payload.email = binding.etEmail.text.toString()
            payload.firstName = binding.etFirstName.text.toString()
            payload.lastName = binding.etLastName.text.toString()
            payload.mobileNumber =
                binding.countryCodePicker.selectedCountryCode.toString() + binding.etPhoneNumber.text.toString()
            if (binding.etPassword.text.toString() != binding.etConfirmPassword.text.toString()) {
                Toaster.show(binding.root, getString(R.string.error_password_not_match))
                return
            } else {
                payload.password = binding.etPassword.text.toString()
            }
            payload.password = binding.etPassword.text.toString()
            payload.username = binding.etUsername.text.toString().replace(" ", "")
            if (Network.isConnected(context)) {
                presenter?.registerUser(payload)
            } else {
                Toaster.show(binding.root, getString(R.string.no_internet_connection))
            }
        }
    }

    private fun areFieldsValidated(): Boolean {
        val rootView = binding.root
        if (binding.etAccountNumber.text.toString().trim { it <= ' ' }.isEmpty()) {
            Toaster.show(
                rootView,
                getString(R.string.error_validation_blank, getString(R.string.account_number)),
            )
            return false
        } else if (binding.etUsername.text.toString().trim { it <= ' ' }.isEmpty()) {
            Toaster.show(
                rootView,
                getString(R.string.error_validation_blank, getString(R.string.username)),
            )
            return false
        } else if (binding.etUsername.text.toString().trim { it <= ' ' }.length < 6) {
            Toaster.show(rootView, getString(R.string.error_username_greater_than_six))
            return false
        } else if (binding.etUsername.text.toString().trim { it <= ' ' }.contains(" ")) {
            Toaster.show(
                rootView,
                getString(
                    R.string.error_validation_cannot_contain_spaces,
                    getString(R.string.username),
                    getString(R.string.not_contain_username),
                ),
            )
            return false
        } else if (binding.etFirstName.text?.isEmpty() == true) {
            Toaster.show(
                rootView,
                getString(R.string.error_validation_blank, getString(R.string.first_name)),
            )
            return false
        } else if (binding.etLastName.text.toString().trim { it <= ' ' }.isEmpty()) {
            Toaster.show(
                rootView,
                getString(R.string.error_validation_blank, getString(R.string.last_name)),
            )
            return false
        } else if (binding.etEmail.text.toString().trim { it <= ' ' }.isEmpty()) {
            Toaster.show(
                rootView,
                getString(R.string.error_validation_blank, getString(R.string.email)),
            )
            return false
        } else if (binding.etPassword.text.toString().trim { it <= ' ' }.isEmpty()) {
            Toaster.show(
                rootView,
                getString(R.string.error_validation_blank, getString(R.string.password)),
            )
            return false
        } else if (binding.etPassword.text.toString().trim { it <= ' ' }.length
            < binding.etPassword.text.toString().length
        ) {
            Toaster.show(
                rootView,
                getString(
                    R.string.error_validation_cannot_contain_leading_or_trailing_spaces,
                    getString(R.string.password),
                ),
            )
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(
                binding.etEmail.text.toString().trim { it <= ' ' },
            )
                .matches()
        ) {
            Toaster.show(rootView, getString(R.string.error_invalid_email))
            return false
        } else if (binding.etPassword.text.toString().trim { it <= ' ' }.length < 6) {
            Toaster.show(
                rootView,
                getString(
                    R.string.error_validation_minimum_chars,
                    getString(R.string.password),
                    resources.getInteger(R.integer.password_minimum_length),
                ),
            )
            return false
        } else if (!isPhoneNumberValid(binding.countryCodePicker)) {
            Toaster.show(rootView, getString(R.string.invalid_phn_number))
            return false
        }
        return true
    }

    override fun showRegisteredSuccessfully() {
        (activity as BaseActivity?)?.replaceFragment(
            RegistrationVerificationFragment.newInstance(),
            true,
            R.id.container,
        )
    }

    override fun showError(msg: String?) {
        Toaster.show(binding.root, msg)
    }

    override fun showProgress() {
        showMifosProgressDialog(getString(R.string.sign_up))
    }

    override fun hideProgress() {
        hideMifosProgressDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.detachView()
        _binding = null
    }

    private fun isPhoneNumberValid(ccp: CountryCodePicker): Boolean {
        binding.countryCodePicker.registerCarrierNumberEditText(binding.etPhoneNumber)
        return ccp.isValidFullNumber
    }

    companion object {
        fun newInstance(): RegistrationFragment {
            return RegistrationFragment()
        }
    }
}
