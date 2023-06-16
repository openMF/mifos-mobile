package org.mifos.mobile.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import org.mifos.mobile.R
import org.mifos.mobile.databinding.ActivityLoginBinding
import org.mifos.mobile.models.payload.LoginPayload
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.Toaster
import org.mifos.mobile.viewModels.LoginViewModel
import org.mifos.mobile.viewModels.LoginViewModelFactory
import javax.inject.Inject

/**
 * @author Vishwajeet
 * @since 05/06/16
 */
class LoginActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: LoginViewModelFactory

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        activityComponent?.inject(this)
        setContentView(binding.root)
        dismissSoftKeyboardOnBkgTap(binding.nsvBackground)
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[LoginViewModel::class.java]
        viewModel.success.observe(this) {
            if (it.equals("success")) {
                hideProgressDialog()
                viewModel.navigateToPassCodeActivity.observe(this) { clientName ->
                    showToast(getString(R.string.toast_welcome, clientName))
                    startPassCodeActivity()
                }
                viewModel.loadClient()
            }
        }
        viewModel.error.observe(this) {
            hideProgressDialog()
            if (it.equals("error_validation_blank")) {
                viewModel.userName.observe(this) {
                    if (it.equals("Username cannot be blank")) {
                        binding.tilUsername.error = showUsernameError(
                            getString(
                                R.string.error_validation_blank,
                                getString(R.string.username)
                            )
                        )
                    } else binding.tilUsername.isErrorEnabled = false
                }
                viewModel.Password.observe(this) {
                    if (it.equals("Password cannot be blank")) {
                        binding.tilPassword.error = showPasswordError(
                            getString(
                                R.string.error_validation_blank,
                                getString(R.string.password)
                            )
                        )
                    } else binding.tilPassword.isErrorEnabled = false
                }
            }
            if (it.equals("error_validation_cannot_contain_spaces")) {
                binding.tilUsername.error =
                    showUsernameError(
                        getString(
                            R.string.error_validation_cannot_contain_spaces,
                            resources?.getString(R.string.username),
                            getString(R.string.not_contain_username)
                        )
                    )
            }
            if (it.equals("error_validation_minimum_chars")) {
                viewModel.userName.observe(this) {
                    if (it.equals("Username is less than 5")) {
                        binding.tilUsername.error = showUsernameError(
                            getString(
                                R.string.error_validation_minimum_chars,
                                resources?.getString(R.string.username),
                                resources?.getInteger(R.integer.username_minimum_length),
                            )
                        )
                    } else binding.tilUsername.isErrorEnabled = false
                }
                viewModel.Password.observe(this) {
                    if (it.equals("Password is less than 6")) {
                        binding.tilPassword.error = showUsernameError(
                            getString(
                                R.string.error_validation_minimum_chars,
                                resources?.getString(R.string.password),
                                resources?.getInteger(R.integer.password_minimum_length),
                            )
                        )
                    } else binding.tilPassword.isErrorEnabled = false
                }
            }
            if (it.equals("server down")) {
                showMessage(getString(R.string.error_server_down))
            }
            if (it.equals("error during sign in")) {
                showMessage(getString(R.string.err_during_login))
            }
            if (it.equals("not authorized")) {
                showMessage(getString(R.string.unauthorized_client))
            }
            if (it.equals("error_fetching_client")) {
                showMessage(getString(R.string.error_fetching_client))
            }
            if (it.equals("error_client_not_found")) {
                showMessage(getString(R.string.error_client_not_found))
            }
        }

        with(binding) {
            btnLogin.setOnClickListener {
                onLoginClicked()
            }
            btnRegister.setOnClickListener {
                onRegisterClicked()
            }
            etUsername.setOnTouchListener { view, event ->
                onTouch(view)
            }
            etPassword.setOnTouchListener { view, event ->
                onTouch(view)
            }

        }

    }

    private fun dismissSoftKeyboardOnBkgTap(view: View) {
        if (view !is EditText) {
            view.setOnTouchListener { view, event ->
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

    fun onTouch(v: View): Boolean {
        when (v) {
            binding.etUsername -> binding.tilUsername.isErrorEnabled = false
            binding.etPassword -> binding.tilPassword.isErrorEnabled = false
        }
        return false
    }

    fun showMessage(errorMessage: String?) {
        showToast(errorMessage!!, Toast.LENGTH_LONG)
        binding.llLogin.visibility = View.VISIBLE
    }

    fun showUsernameError(error: String): String {
        return error
    }

    fun showPasswordError(error: String): String {
        return error
    }

    /**
     * Called when Login Button is clicked, used for logging in the user
     */

    fun onLoginClicked() {
        val username = binding.tilUsername.editText?.editableText.toString()
        val password = binding.tilPassword.editText?.editableText.toString()
        if (Network.isConnected(this)) {
            val payload = LoginPayload()
            payload.username = username
            payload.password = password
            showProgressDialog(getString(R.string.progress_message_login))
            viewModel.login(payload)
        } else {
            Toaster.show(binding.llLogin, getString(R.string.no_internet_connection))
        }
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
