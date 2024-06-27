package org.mifos.mobile.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.mifos.mobile.MifosSelfServiceApp.Companion.context
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.ui.activities.PassCodeActivity
import org.mifos.mobile.ui.registration.RegistrationActivity
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.utils.LoginUiState
import org.mifos.mobile.core.common.Network

/**
 * @author Vishwajeet
 * @since 05/06/16
 */
@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var usernameContent: String
    private lateinit var passwordContent: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MifosMobileTheme {
                LoginScreen(
                    login = { username, password ->
                        usernameContent = username
                        passwordContent = password
                        onLoginClicked()
                    },
                    createAccount = { onRegisterClicked() },
                    getUsernameError = { validateUsername(it) },
                    getPasswordError = { validatePassword(it) }
                )
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginUiState.collect { state ->
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

                        LoginUiState.Initial -> {}
                    }
                }
            }
        }
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
    }

    /**
     * Called when Login Button is clicked, used for logging in the user
     */

    private fun onLoginClicked() {
        if (Network.isConnected(this)) {
            if (isCredentialsValid(usernameContent, passwordContent))
                viewModel.login(usernameContent, passwordContent)
        } else {
            showMessage(context?.getString(R.string.no_internet_connection))
        }
    }

    private fun isCredentialsValid(username: String, password: String): Boolean {
        var credentialValid = true
        when {
            viewModel.isFieldEmpty(username) -> {
                credentialValid = false
            }

            viewModel.isUsernameLengthInadequate(username) -> {
                credentialValid = false
            }

            viewModel.usernameHasSpaces(username) -> {
                credentialValid = false
            }
        }

        when {
            viewModel.isFieldEmpty(password) -> {
                credentialValid = false
            }

            viewModel.isPasswordLengthInadequate(password) -> {
                credentialValid = false
            }
        }
        return credentialValid
    }

    private fun validateUsername(username: String): String {
        var usernameError = ""
        when {
            viewModel.isFieldEmpty(username) -> {
                usernameError = context?.getString(
                    R.string.error_validation_blank,
                    context?.getString(R.string.username),
                ).toString()
            }

            viewModel.isUsernameLengthInadequate(username) -> {
                usernameError = context?.getString(
                    R.string.error_validation_minimum_chars,
                    resources?.getString(R.string.username),
                    resources?.getInteger(R.integer.username_minimum_length),
                ).toString()
            }

            viewModel.usernameHasSpaces(username) -> {
                usernameError = context?.getString(
                    R.string.error_validation_cannot_contain_spaces,
                    resources?.getString(R.string.username),
                    context?.getString(R.string.not_contain_username),
                ).toString()
            }
        }
        return usernameError
    }

    private fun validatePassword(password: String): String {
        var passwordError = ""
        when {
            viewModel.isFieldEmpty(password) -> {
                passwordError = context?.getString(
                    R.string.error_validation_blank,
                    context?.getString(R.string.password),
                ).toString()

            }

            viewModel.isPasswordLengthInadequate(password) -> {
                passwordError = context?.getString(
                    R.string.error_validation_minimum_chars,
                    resources?.getString(R.string.password),
                    resources?.getInteger(R.integer.password_minimum_length),
                ).toString()
            }
        }
        return passwordError
    }

    private fun onRegisterClicked() {
        startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
    }

    /**
     * Starts [PassCodeActivity] with `Constans.INTIAL_LOGIN` as true
     */

    private fun startPassCodeActivity() {
        val intent = Intent(this@LoginActivity, PassCodeActivity::class.java)
        intent.putExtra(org.mifos.mobile.core.common.Constants.INTIAL_LOGIN, true)
        startActivity(intent)
        finish()
    }
}
