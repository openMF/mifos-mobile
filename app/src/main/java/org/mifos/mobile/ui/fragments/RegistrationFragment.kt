package org.mifos.mobile.ui.fragments

import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.hbb20.CountryCodePicker
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentRegistrationBinding
import org.mifos.mobile.models.register.RegisterPayload
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.MFErrorParser
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.PasswordStrength
import org.mifos.mobile.utils.Toaster
import org.mifos.mobile.viewModels.RegistrationViewModel
import org.mifos.mobile.viewModels.RegistrationViewModelFactory
import javax.inject.Inject

/**
 * Created by dilpreet on 31/7/17.
 */
class RegistrationFragment : BaseFragment() {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : RegistrationViewModel

    @Inject
    lateinit var viewModelFactory: RegistrationViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        val rootView = binding.root
        (activity as BaseActivity?)?.activityComponent?.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[RegistrationViewModel::class.java]
        with(binding) {
            progressBar.visibility = View.GONE
            passwordStrength.visibility = View.GONE
            etPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int ) {}
                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    if (charSequence.isEmpty()) {
                        progressBar.visibility = View.GONE
                        passwordStrength.visibility = View.GONE
                    } else {
                        progressBar.visibility = View.VISIBLE
                        passwordStrength.visibility = View.VISIBLE
                        updatePasswordStrengthView(charSequence.toString())
                    }
                }
                override fun afterTextChanged(editable: Editable) {}
            })
        }
        return rootView
    }

    private fun updatePasswordStrengthView(password: String) {
        with(binding) {
            if (TextView.VISIBLE != passwordStrength.visibility) return
            if (password.isEmpty()) {
                passwordStrength.text = ""
                progressBar.progress = 0
                return
            }
            val str = PasswordStrength.calculateStrength(password)
            passwordStrength.text = str.getText(context)
            passwordStrength.setTextColor(str.color)
            val mode = PorterDuff.Mode.SRC_IN
            progressBar.progressDrawable?.setColorFilter(str.color, mode)
            if (str.getText(context) == getString(R.string.password_strength_weak)) {
                progressBar.progress = 25
            } else if (str.getText(context) == getString(R.string.password_strength_medium)) {
                progressBar.progress = 50
            } else if (str.getText(context) == getString(R.string.password_strength_strong)) {
                progressBar.progress = 75
            } else {
                progressBar.progress = 100
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel.readRegistrationResultSuccess.observe(viewLifecycleOwner) { registrationResultSuccess ->
            hideProgress()
            if (registrationResultSuccess == true) {
                showRegisteredSuccessfully()
            } else {
                showError(MFErrorParser.errorMessage(viewModel.readExceptionOnRegistration))
            }
        }

        binding.btnRegister.setOnClickListener {
            registerClicked()
        }
    }

    private fun registerClicked() {
        if (areFieldsValidated()) {
            with(binding) {
                val radioButton = rgVerificationMode.checkedRadioButtonId.let {
                    root.findViewById<RadioButton>(it)
                }
                val payload = RegisterPayload().apply {
                    accountNumber = etAccountNumber.text.toString()
                    authenticationMode = radioButton?.text.toString()
                    email = etEmail.text.toString()
                    firstName = etFirstName.text.toString()
                    lastName = etLastName.text.toString()
                    mobileNumber =
                        countryCodePicker.selectedCountryCode.toString() + etPhoneNumber.text.toString()
                    if (etPassword.text.toString() != etConfirmPassword.text.toString()) {
                        Toaster.show(root, getString(R.string.error_password_not_match))
                        return
                    } else {
                        password = etPassword.text.toString()
                    }
                    password = etPassword.text.toString()
                    username = etUsername.text.toString().replace(" ", "")
                }
                if (Network.isConnected(context)) {
                    showProgress()
                    viewModel.registerUser(payload)
                } else {
                    Toaster.show(root, getString(R.string.no_internet_connection))
                }
            }
        }
    }

    private fun areFieldsValidated(): Boolean {
        val rootView = binding.root
        with(binding) {
            if (viewModel.isInputFieldBlank(etAccountNumber.text.toString())) {
                Toaster.show(
                    rootView,
                    getString(R.string.error_validation_blank, getString(R.string.account_number)),
                )
                return false
            } else if (viewModel.isInputFieldBlank(etUsername.text.toString())) {
                Toaster.show(
                    rootView,
                    getString(R.string.error_validation_blank, getString(R.string.username)),
                )
                return false
            } else if (viewModel.isInputLengthInadequate(etUsername.text.toString())) {
                Toaster.show(rootView, getString(R.string.error_username_greater_than_six))
                return false
            } else if (viewModel.inputHasSpaces(etUsername.text.toString())) {
                Toaster.show(
                    rootView,
                    getString(
                        R.string.error_validation_cannot_contain_spaces,
                        getString(R.string.username),
                        getString(R.string.not_contain_username),
                    ),
                )
                return false
            } else if (viewModel.isInputFieldBlank(etFirstName.text.toString())) {
                Toaster.show(
                    rootView,
                    getString(R.string.error_validation_blank, getString(R.string.first_name)),
                )
                return false
            } else if (viewModel.isInputFieldBlank(etLastName.text.toString())) {
                Toaster.show(
                    rootView,
                    getString(R.string.error_validation_blank, getString(R.string.last_name)),
                )
                return false
            } else if (viewModel.isInputFieldBlank(etEmail.text.toString())) {
                Toaster.show(
                    rootView,
                    getString(R.string.error_validation_blank, getString(R.string.email)),
                )
                return false
            } else if (viewModel.isInputFieldBlank(etPassword.text.toString())) {
                Toaster.show(
                    rootView,
                    getString(R.string.error_validation_blank, getString(R.string.password)),
                )
                return false
            } else if (viewModel.hasLeadingTrailingSpaces(etPassword.text.toString())) {
                Toaster.show(
                    rootView,
                    getString(
                        R.string.error_validation_cannot_contain_leading_or_trailing_spaces,
                        getString(R.string.password),
                    ),
                )
                return false
            } else if (viewModel.isEmailInvalid(etEmail.text.toString())) {
                Toaster.show(rootView, getString(R.string.error_invalid_email))
                return false
            } else if (viewModel.isInputLengthInadequate(etPassword.text.toString())) {
                Toaster.show(
                    rootView,
                    getString(
                        R.string.error_validation_minimum_chars,
                        getString(R.string.password),
                        resources.getInteger(R.integer.password_minimum_length),
                    ),
                )
                return false
            } else if (!isPhoneNumberValid(countryCodePicker)) {
                Toaster.show(rootView, getString(R.string.invalid_phn_number))
                return false
            }
            return true
        }
    }

    private fun showRegisteredSuccessfully() {
        (activity as BaseActivity?)?.replaceFragment(
            RegistrationVerificationFragment.newInstance(),
            true,
            R.id.container,
        )
    }

    fun showError(msg: String?) {
        Toaster.show(binding.root, msg)
    }

    fun showProgress() {
        showMifosProgressDialog(getString(R.string.sign_up))
    }

    fun hideProgress() {
        hideMifosProgressDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
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
