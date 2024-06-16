package org.mifos.mobile.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.PasswordStrength
import org.mifos.mobile.utils.RegistrationUiState
import org.mifos.mobile.utils.Toaster

/**
 * Created by dilpreet on 31/7/17.
 */
@AndroidEntryPoint
class RegistrationFragment : BaseFragment() {

    private lateinit var viewModel: RegistrationViewModel

    private lateinit var accountNumberContent: String
    private lateinit var usernameContent: String
    private lateinit var firstNameContent: String
    private lateinit var lastNameContent: String
    private lateinit var phoneNumberContent: String
    private lateinit var emailContent: String
    private lateinit var passwordContent: String
    private lateinit var authenticationModeContent: String
    private lateinit var countryCodeContent: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(this)[RegistrationViewModel::class.java]
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MifosMobileTheme {
                    RegistrationScreen(
                        register = { account, username, firstname, lastname, phoneNumber, email, password, authenticationMode, countryCode ->
                            accountNumberContent = account
                            usernameContent = username
                            firstNameContent = firstname
                            lastNameContent = lastname
                            phoneNumberContent = phoneNumber
                            emailContent = email
                            passwordContent = password
                            authenticationModeContent = authenticationMode
                            countryCodeContent = countryCode
                            registerClicked()
                        },
                        progress = { updatePasswordStrengthView(it) }
                    )
                }
            }
        }
    }

    private fun updatePasswordStrengthView(password: String): Float {
        val str = PasswordStrength.calculateStrength(password)
        return when (str.getText(context)) {
            getString(R.string.password_strength_weak) -> 0.25f
            getString(R.string.password_strength_medium) -> 0.5f
            getString(R.string.password_strength_strong) -> 0.75f
            else -> 1f
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registrationUiState.collect { state ->
                    when (state) {
                        RegistrationUiState.Loading -> showProgress()

                        RegistrationUiState.Success -> {
                            hideProgress()
                            showRegisteredSuccessfully()
                        }

                        is RegistrationUiState.Error -> {
                            hideProgress()
                            showError(getString(state.exception))
                        }

                        RegistrationUiState.Initial -> {}
                    }
                }
            }
        }

    }

    private fun registerClicked() {
        if (areFieldsValidated()) {
            if (Network.isConnected(context)) {
                viewModel.registerUser(
                    accountNumberContent,
                    authenticationModeContent,
                    emailContent,
                    firstNameContent,
                    lastNameContent,
                    phoneNumberContent,
                    passwordContent,
                    usernameContent
                )
            } else {
                Toaster.show(view, getString(R.string.no_internet_connection))
            }

        }
    }

    private fun areFieldsValidated(): Boolean {
        return when {
            viewModel.isInputFieldBlank(accountNumberContent) -> {
                Toaster.show(
                    view,
                    getString(
                        R.string.error_validation_blank, getString(R.string.account_number)
                    ),
                )
                false
            }

            viewModel.isInputFieldBlank(usernameContent) -> {
                Toaster.show(
                    view,
                    getString(R.string.error_validation_blank, getString(R.string.username)),
                )
                false
            }

            viewModel.isInputLengthInadequate(usernameContent) -> {
                Toaster.show(view, getString(R.string.error_username_greater_than_six))
                false
            }

            viewModel.inputHasSpaces(usernameContent) -> {
                Toaster.show(
                    view,
                    getString(
                        R.string.error_validation_cannot_contain_spaces,
                        getString(R.string.username),
                        getString(R.string.not_contain_username),
                    ),
                )
                false
            }

            viewModel.isInputFieldBlank(firstNameContent) -> {
                Toaster.show(
                    view,
                    getString(R.string.error_validation_blank, getString(R.string.first_name)),
                )
                false
            }

            viewModel.isPhoneNumberNotValid(phoneNumberContent) ->{
                Toaster.show(
                    view,
                    getString(R.string.invalid_phn_number),
                )
                false
            }

            viewModel.isInputFieldBlank(lastNameContent) -> {
                Toaster.show(
                    view,
                    getString(R.string.error_validation_blank, getString(R.string.last_name)),
                )
                false
            }

            viewModel.isInputFieldBlank(emailContent) -> {
                Toaster.show(
                    view,
                    getString(R.string.error_validation_blank, getString(R.string.email)),
                )
                false
            }

            viewModel.isInputFieldBlank(passwordContent) -> {
                Toaster.show(
                    view,
                    getString(R.string.error_validation_blank, getString(R.string.password)),
                )
                false
            }

            viewModel.hasLeadingTrailingSpaces(passwordContent) -> {
                Toaster.show(
                    view,
                    getString(
                        R.string.error_validation_cannot_contain_leading_or_trailing_spaces,
                        getString(R.string.password),
                    ),
                )
                false
            }

            viewModel.isEmailInvalid(emailContent) -> {
                Toaster.show(view, getString(R.string.error_invalid_email))
                false
            }

            viewModel.isInputLengthInadequate(passwordContent) -> {
                Toaster.show(
                    view,
                    getString(
                        R.string.error_validation_minimum_chars,
                        getString(R.string.password),
                        resources.getInteger(R.integer.password_minimum_length),
                    ),
                )
                return false
            }

            else -> true
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
        Toaster.show(view, msg)
    }

    fun showProgress() {
        showMifosProgressDialog(getString(R.string.sign_up))
    }

    fun hideProgress() {
        hideMifosProgressDialog()
    }

    companion object {
        fun newInstance(): RegistrationFragment {
            return RegistrationFragment()
        }
    }
}
