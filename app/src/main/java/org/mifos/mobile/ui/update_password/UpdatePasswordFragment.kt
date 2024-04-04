package org.mifos.mobile.ui.update_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import org.mifos.mobile.ui.fragments.SettingsFragment
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.RegistrationUiState
import org.mifos.mobile.utils.Toaster

/*
* Created by saksham on 13/July/2018
*/
@AndroidEntryPoint
class UpdatePasswordFragment : BaseFragment() {

    private lateinit var viewModel: UpdatePasswordViewModel

    private lateinit var newPasswordContent: String
    private lateinit var confirmPasswordContent: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(this)[UpdatePasswordViewModel::class.java]
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MifosMobileTheme {
                    UpdatePasswordScreen(
                        changePassword = { newPassword, confirmPassword ->
                            newPasswordContent = newPassword
                            confirmPasswordContent = confirmPassword
                            updatePassword()
                        },
                        getNewPasswordError = { newPassword ->
                            newPasswordContent = newPassword
                            showNewPasswordError()
                        },
                        getConfirmPasswordError = { confirmPassword ->
                            confirmPasswordContent = confirmPassword
                            showConfirmPasswordError()
                        },
                        getBackToPreviousScreen = {
                            navigateBack()
                        }
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.updatePasswordUiState.collect { state ->
                    when (state) {
                        RegistrationUiState.Loading -> showProgress()

                        RegistrationUiState.Success -> {
                            hideProgress()
                            showPasswordUpdatedSuccessfully()
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

    private fun updatePassword() {
        val newPassword = newPasswordContent
        val confirmPassword = confirmPasswordContent

        if (Network.isConnected(activity)) {
            if (passwordMatches()) {
                viewModel.updateAccountPassword(newPassword, confirmPassword)
            } else {
                showError(getString(R.string.error_password_not_match))
            }
        } else {
            Toaster.show(view, getString(R.string.no_internet_connection))
        }
    }

    private fun passwordMatches(): Boolean {
        return viewModel.validatePasswordMatch(newPasswordContent, confirmPasswordContent)
    }

    private fun showNewPasswordError(): String {
        var passwordError = ""
        when {
            viewModel.isInputFieldEmpty(newPasswordContent) -> {
                passwordError = getString(
                    R.string.error_validation_blank,
                    getString(R.string.new_password),
                )
            }

            viewModel.isInputLengthInadequate(newPasswordContent) -> {
                passwordError = getString(
                    R.string.error_validation_minimum_chars,
                    getString(R.string.new_password),
                    resources.getInteger(R.integer.password_minimum_length),
                )
            }
        }
        return passwordError
    }

    private fun showConfirmPasswordError(): String {
        var passwordError = ""
        when {
            viewModel.isInputFieldEmpty(confirmPasswordContent) -> {
                passwordError = getString(
                    R.string.error_validation_blank,
                    getString(R.string.confirm_password),
                )
            }

            viewModel.isInputLengthInadequate(confirmPasswordContent) -> {
                passwordError = getString(
                    R.string.error_validation_minimum_chars,
                    getString(R.string.confirm_password),
                    resources.getInteger(R.integer.password_minimum_length),
                )
            }
        }
        return passwordError
    }

    fun showError(message: String?) {
        var errorMessage = message
        Toaster.show(view, errorMessage)
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

    private fun navigateBack() {
        /*
        If we navigate to update password from user profile there is nothing in backStackEntry
        But when we migrate from settings to update password there is one backStackEntry
        Since, we need a code to work for both we used this condition here.
         */
        if (fragmentManager?.backStackEntryCount!! > 0) {
            fragmentManager?.popBackStack()
        } else {
            activity?.finish()
        }
    }

    fun showProgress() {
        showMifosProgressDialog(getString(R.string.progress_message_loading))
    }

    fun hideProgress() {
        hideMifosProgressDialog()
    }

    override fun onResume() {
        super.onResume()
        (activity as? BaseActivity)?.hideToolbar()
    }

    companion object {
        fun newInstance(): UpdatePasswordFragment {
            return UpdatePasswordFragment()
        }
    }
}
