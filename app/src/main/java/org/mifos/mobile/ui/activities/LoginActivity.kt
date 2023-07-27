package org.mifos.mobile.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.MifosSelfServiceApp.Companion.context
import org.mifos.mobile.R
import org.mifos.mobile.databinding.ActivityLoginBinding
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.LoginUiState
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.Toaster
import org.mifos.mobile.viewModels.LoginViewModel

/**
 * @author Vishwajeet
 * @since 05/06/16
 */
@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        dismissSoftKeyboardOnBkgTap(binding.nsvBackground)
        binding.btnLogin.setOnClickListener {
            onLoginClicked()
        }
        binding.btnRegister.setOnClickListener {
            onRegisterClicked()
        }
        binding.etUsername.setOnTouchListener { view, _ ->
            onTouch(view)
        }

        binding.etPassword.setOnTouchListener { view, _ ->
            onTouch(view)
        }

        viewModel.loginUiState.observe(this) { state ->
            when (state) {
                LoginUiState.Loading -> showProgress()

                LoginUiState.Error -> {
                    hideProgress()
                    showMessage(context?.getString(R.string.login_failed))
                }

                LoginUiState.LoginSuccess -> {
                    onLoginSuccess()
                }

                is LoginUiState.LoadClientSuccess -> {
                    hideProgress()
                    showPassCodeActivity(state.clientName)
                }
            }
        }
    }

    private fun dismissSoftKeyboardOnBkgTap(view: View) {
        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                hideKeyboard(this@LoginActivity)
                false
            }
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                dismissSoftKeyboardOnBkgTap(innerView)
            }
        }
    }

    private fun onTouch(v: View): Boolean {
        when (v) {
            binding.etUsername -> clearUsernameError()
            binding.etPassword -> clearPasswordError()
        }
        return false
    }

    /**
     * Called when Login is user has successfully logged in
     */
    private fun onLoginSuccess() {
        viewModel.loadClient()
    }

    /**
     * Shows ProgressDialog when called
     */
    private fun showProgress() {
        showProgressDialog(getString(R.string.progress_message_login))
    }

    /**
     * Hides the progressDialog which is being shown
     */
    private fun hideProgress() {
        hideProgressDialog()
    }

    /**
     * Starts [PassCodeActivity]
     */
    private fun showPassCodeActivity(clientName: String?) {
        showToast(getString(R.string.toast_welcome, clientName))
        startPassCodeActivity()
    }

    /**
     * It is called whenever any error occurs while executing a request
     *
     * @param errorMessage Error message that tells the user about the problem.
     */
    private fun showMessage(errorMessage: String?) {
        showToast(errorMessage!!, Toast.LENGTH_LONG)
        binding.llLogin.visibility = View.VISIBLE
    }

    private fun showUsernameError(error: String?) {
        binding.tilUsername.error = error
    }

    private fun showPasswordError(error: String?) {
        binding.tilPassword.error = error
    }

    private fun clearUsernameError() {
        binding.tilUsername.isErrorEnabled = false
    }

    private fun clearPasswordError() {
        binding.tilPassword.isErrorEnabled = false
    }

    /**
     * Called when Login Button is clicked, used for logging in the user
     */

    private fun onLoginClicked() {
        val username = binding.tilUsername.editText?.editableText.toString()
        val password = binding.tilPassword.editText?.editableText.toString()
        if (Network.isConnected(this)) {
            if (isCredentialsValid(username, password))
                viewModel.login(username, password)
        } else {
            Toaster.show(binding.llLogin, getString(R.string.no_internet_connection))
        }
    }

    private fun isCredentialsValid(username: String, password: String): Boolean {
        var credentialValid = true
        val resources = context?.resources
        when {
            viewModel.isFieldEmpty(username) -> {
                showUsernameError(
                    context?.getString(
                        R.string.error_validation_blank,
                        context?.getString(R.string.username),
                    ),
                )
                credentialValid = false
            }

            viewModel.isUsernameLengthInadequate(username) -> {
                showUsernameError(
                    context?.getString(
                        R.string.error_validation_minimum_chars,
                        resources?.getString(R.string.username),
                        resources?.getInteger(R.integer.username_minimum_length),
                    ),
                )
                credentialValid = false
            }

            viewModel.usernameHasSpaces(username) -> {
                showUsernameError(
                    context?.getString(
                        R.string.error_validation_cannot_contain_spaces,
                        resources?.getString(R.string.username),
                        context?.getString(R.string.not_contain_username),
                    ),
                )
                credentialValid = false
            }

            else -> {
                clearUsernameError()
            }
        }

        when {
            viewModel.isFieldEmpty(password) -> {
                showPasswordError(
                    context?.getString(
                        R.string.error_validation_blank,
                        context?.getString(R.string.password),
                    ),
                )
                credentialValid = false
            }

            viewModel.isPasswordLengthInadequate(password) -> {
                showPasswordError(
                    context?.getString(
                        R.string.error_validation_minimum_chars,
                        resources?.getString(R.string.password),
                        resources?.getInteger(R.integer.password_minimum_length),
                    ),
                )
                credentialValid = false
            }

            else -> {
                clearPasswordError()
            }
        }
        return credentialValid
    }

    private fun onRegisterClicked() {
        startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
    }

    /**
     * Starts [PassCodeActivity] with `Constans.INTIAL_LOGIN` as true
     */

    private fun startPassCodeActivity() {
        val intent = Intent(this@LoginActivity, PassCodeActivity::class.java)
        intent.putExtra(Constants.INTIAL_LOGIN, true)
        startActivity(intent)
        finish()
    }
}
