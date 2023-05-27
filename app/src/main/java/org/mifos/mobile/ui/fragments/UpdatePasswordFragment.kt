package org.mifos.mobile.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.Toast
import org.mifos.mobile.R
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.databinding.FragmentUpdatePasswordBinding
import org.mifos.mobile.models.UpdatePasswordPayload
import org.mifos.mobile.presenters.UpdatePasswordPresenter
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.views.UpdatePasswordView
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.Toaster
import javax.inject.Inject

/*
* Created by saksham on 13/July/2018
*/   class UpdatePasswordFragment : BaseFragment(), UpdatePasswordView, TextWatcher, OnFocusChangeListener {

    private var _binding: FragmentUpdatePasswordBinding? = null
    private val binding get() = _binding!!


    @kotlin.jvm.JvmField
    @Inject
    var presenter: UpdatePasswordPresenter? = null

    @kotlin.jvm.JvmField
    @Inject
    var preferencesHelper: PreferencesHelper? = null
    private var payload: UpdatePasswordPayload? = null
    private var isFocusLostNewPassword = false
    private var isFocusLostConfirmPassword = false

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdatePasswordBinding.inflate(inflater,container,false)
        setToolbarTitle(getString(R.string.change_password))
        (activity as BaseActivity?)?.activityComponent?.inject(this)
        presenter?.attachView(this)
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
    }

    fun updatePassword() {
        if (isFieldsCompleted) {
            presenter?.updateAccountPassword(updatePasswordPayload)
        }
    }

    private val isFieldsCompleted: Boolean
        get() {
            var rv = true
            val newPassword = binding.tilNewPassword.editText?.text.toString().trim { it <= ' ' }
            val repeatPassword = binding.tilConfirmNewPassword.editText?.text.toString().trim { it <= ' ' }
            if (!checkNewPasswordFieldsComplete()) {
                rv = false
            }
            if (!checkConfirmPasswordFieldsComplete()) {
                rv = false
            }
            if (newPassword != repeatPassword) {
                Toaster.show(binding.root, getString(R.string.error_password_not_match))
                rv = false
            }
            return rv
        }
    private val updatePasswordPayload: UpdatePasswordPayload?
        get() {
            payload = UpdatePasswordPayload()
            payload?.password = binding.tilNewPassword.editText?.text.toString().trim { it <= ' ' }
            payload?.repeatPassword = binding.tilConfirmNewPassword.editText?.text.toString().trim { it <= ' ' }
            return payload
        }

    override fun showError(message: String?) {
        var message = message
        if (!Network.isConnected(activity)) {
            message = getString(R.string.no_internet_connection)
        }
        Toaster.show(binding.root, message)
    }

    override fun showPasswordUpdatedSuccessfully() {
        Toast.makeText(context, getString(R.string.string_changed_successfully,
                getString(R.string.password)), Toast.LENGTH_SHORT).show()
        (activity as BaseActivity).clearFragmentBackStack()
        (activity as BaseActivity).replaceFragment(SettingsFragment.newInstance(), true, R.id.container)
    }

    override fun showProgress() {
        showMifosProgressDialog(getString(R.string.progress_message_loading))
    }

    override fun hideProgress() {
        hideMifosProgressDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.detachView()
        _binding = null
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (binding.tilNewPassword.editText?.hasFocus() == true && isFocusLostNewPassword) {
            checkNewPasswordFieldsComplete()
        }
        if (binding.tilConfirmNewPassword.editText?.hasFocus() == true && isFocusLostConfirmPassword) {
            checkConfirmPasswordFieldsComplete()
        }
    }

    override fun afterTextChanged(s: Editable) {}
    override fun onFocusChange(v: View, hasFocus: Boolean) {
        if (v.id == R.id.et_new_password && !isFocusLostNewPassword && !hasFocus) {
            checkNewPasswordFieldsComplete()
            isFocusLostNewPassword = true
        }
        if (v.id == R.id.et_confirm_password && !isFocusLostConfirmPassword && !hasFocus) {
            checkConfirmPasswordFieldsComplete()
            isFocusLostConfirmPassword = true
        }
    }

    private fun checkNewPasswordFieldsComplete(): Boolean {
        val newPassword = binding.tilNewPassword.editText?.text.toString()
        isFocusLostNewPassword = true
        if (newPassword.isEmpty()) {
            binding.tilNewPassword.error = getString(R.string.error_validation_blank,
                    getString(R.string.new_password))
            return false
        }
        if (newPassword.length < 6) {
            binding.tilNewPassword.error = getString(R.string.error_validation_minimum_chars,
                    getString(R.string.new_password),
                    resources.getInteger(R.integer.password_minimum_length))
            return false
        }
        binding.tilNewPassword.isErrorEnabled = false
        return true
    }

    private fun checkConfirmPasswordFieldsComplete(): Boolean {
        val confirmPassword = binding.tilConfirmNewPassword.editText?.text.toString()
        isFocusLostConfirmPassword = true
        if (confirmPassword.isEmpty()) {
            binding.tilConfirmNewPassword.error = getString(R.string.error_validation_blank,
                    getString(R.string.confirm_password))
            return false
        }
        if (confirmPassword.length < 6) {
            binding.tilConfirmNewPassword.error = getString(R.string.error_validation_minimum_chars,
                    getString(R.string.confirm_password),
                    resources.getInteger(R.integer.password_minimum_length))
            return false
        }
        binding.tilConfirmNewPassword.isErrorEnabled = false
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    companion object {
        @kotlin.jvm.JvmStatic
        fun newInstance(): UpdatePasswordFragment {
            return UpdatePasswordFragment()
        }
    }
}