package org.mifos.mobile.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentUpdatePasswordBinding
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.MFErrorParser
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.Toaster
import org.mifos.mobile.utils.RegistrationUiState
import org.mifos.mobile.viewModels.UpdatePasswordViewModel

/*
* Created by saksham on 13/July/2018
*/
@AndroidEntryPoint
class UpdatePasswordFragment : BaseFragment(), TextWatcher, OnFocusChangeListener {

    private var _binding: FragmentUpdatePasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: UpdatePasswordViewModel
    private var isFocusLostNewPassword = false
    private var isFocusLostConfirmPassword = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUpdatePasswordBinding.inflate(inflater, container, false)
        setToolbarTitle(getString(R.string.change_password))
        viewModel = ViewModelProvider(this)[UpdatePasswordViewModel::class.java]
        binding.tilNewPassword.editText?.addTextChangedListener(this)
        binding.tilConfirmNewPassword.editText?.addTextChangedListener(this)
        binding.tilNewPassword.editText?.onFocusChangeListener = this
        binding.tilConfirmNewPassword.editText?.onFocusChangeListener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnUpdatePassword.setOnClickListener {
            updatePassword()
        }

        viewModel.updatePasswordUiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                RegistrationUiState.Loading -> showProgress()
                RegistrationUiState.Success -> {
                    hideProgress()
                    showPasswordUpdatedSuccessfully()
                }

                is RegistrationUiState.Error -> {
                    hideProgress()
                    showError(MFErrorParser.errorMessage(state.exception))
                }
            }
        }
    }

    private fun updatePassword() {
        val newPassword = binding.tilNewPassword.editText?.text.toString().trim()
        val confirmPassword = binding.tilConfirmNewPassword.editText?.text.toString().trim()

        if (areFieldsValidated(newPassword, confirmPassword)) {
            viewModel.updateAccountPassword(newPassword, confirmPassword)
        }
    }

    private fun areFieldsValidated(newPassword: String, confirmPassword: String): Boolean {
        return when {
            !isNewPasswordValidated() -> false
            !isConfirmPasswordValidated() -> false
            (!viewModel.validatePasswordMatch(newPassword, confirmPassword)) -> {
                Toaster.show(binding.root, getString(R.string.error_password_not_match))
                false
            }

            else -> true
        }
    }

    private fun isNewPasswordValidated(): Boolean {
        with(binding) {
            val newPassword = tilNewPassword.editText?.text.toString()
            isFocusLostNewPassword = true
            return when {
                viewModel.isInputFieldEmpty(newPassword) -> {
                    tilNewPassword.error = getString(
                        R.string.error_validation_blank,
                        getString(R.string.new_password),
                    )
                    false
                }

                viewModel.isInputLengthInadequate(newPassword) -> {
                    tilNewPassword.error = getString(
                        R.string.error_validation_minimum_chars,
                        getString(R.string.new_password),
                        resources.getInteger(R.integer.password_minimum_length),
                    )
                    false
                }

                else -> {
                    tilNewPassword.isErrorEnabled = false
                    return true
                }
            }
        }
    }

    private fun isConfirmPasswordValidated(): Boolean {
        with(binding) {
            val confirmPassword = tilConfirmNewPassword.editText?.text.toString()
            isFocusLostConfirmPassword = true
            return when {
                viewModel.isInputFieldEmpty(confirmPassword) -> {
                    tilConfirmNewPassword.error = getString(
                        R.string.error_validation_blank,
                        getString(R.string.confirm_password),
                    )
                    false
                }

                viewModel.isInputLengthInadequate(confirmPassword) -> {
                    tilConfirmNewPassword.error = getString(
                        R.string.error_validation_minimum_chars,
                        getString(R.string.confirm_password),
                        resources.getInteger(R.integer.password_minimum_length),
                    )
                    return false
                }

                else -> {
                    tilConfirmNewPassword.isErrorEnabled = false
                    return true
                }
            }
        }

    }

    fun showError(message: String?) {
        var errorMessage = message
        if (!Network.isConnected(activity)) {
            errorMessage = getString(R.string.no_internet_connection)
        }
        Toaster.show(binding.root, errorMessage)
    }

    private fun showPasswordUpdatedSuccessfully() {
        Toast.makeText(
            context,
            getString(
                R.string.string_changed_successfully,
                getString(R.string.password),
            ),
            Toast.LENGTH_SHORT,
        ).show()
        (activity as BaseActivity).clearFragmentBackStack()
        (activity as BaseActivity).replaceFragment(
            SettingsFragment.newInstance(),
            true,
            R.id.container,
        )
    }

    fun showProgress() {
        showMifosProgressDialog(getString(R.string.progress_message_loading))
    }

    fun hideProgress() {
        hideMifosProgressDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (binding.tilNewPassword.editText?.hasFocus() == true && isFocusLostNewPassword) {
            isNewPasswordValidated()
        }
        if (binding.tilConfirmNewPassword.editText?.hasFocus() == true && isFocusLostConfirmPassword) {
            isConfirmPasswordValidated()
        }
    }

    override fun afterTextChanged(s: Editable) {}
    override fun onFocusChange(v: View, hasFocus: Boolean) {
        if (v.id == R.id.et_new_password && !isFocusLostNewPassword && !hasFocus) {
            isNewPasswordValidated()
            isFocusLostNewPassword = true
        }
        if (v.id == R.id.et_confirm_password && !isFocusLostConfirmPassword && !hasFocus) {
            isConfirmPasswordValidated()
            isFocusLostConfirmPassword = true
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): UpdatePasswordFragment {
            return UpdatePasswordFragment()
        }
    }
}
