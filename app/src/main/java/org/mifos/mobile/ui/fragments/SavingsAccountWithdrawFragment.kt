package org.mifos.mobile.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentSavingsAccountWithdrawFragmentBinding
import org.mifos.mobile.models.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.models.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.ui.activities.SavingsAccountContainerActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.ParcelableAndSerializableUtils.getCheckedParcelable
import org.mifos.mobile.utils.SavingsAccountUiState
import org.mifos.mobile.utils.Toaster
import org.mifos.mobile.utils.getTodayFormatted
import org.mifos.mobile.viewModels.SavingsAccountWithdrawViewModel

/*
* Created by saksham on 02/July/2018
*/
@AndroidEntryPoint
class SavingsAccountWithdrawFragment : BaseFragment() {

    private var _binding: FragmentSavingsAccountWithdrawFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SavingsAccountWithdrawViewModel by viewModels()
    private var savingsWithAssociations: SavingsWithAssociations? = null
    private var payload: SavingsAccountWithdrawPayload? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? SavingsAccountContainerActivity)?.showToolbar()
        if (arguments != null) {
            savingsWithAssociations = arguments?.getCheckedParcelable(
                SavingsWithAssociations::class.java,
                Constants.SAVINGS_ACCOUNTS
            )        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSavingsAccountWithdrawFragmentBinding.inflate(inflater, container, false)
        showUserInterface()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnWithdrawSavingsAccount.setOnClickListener {
            onWithdrawSavingsAccount()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.savingsAccountWithdrawUiState.collect { state ->
                    when (state) {
                        SavingsAccountUiState.Loading -> showProgress()

                        is SavingsAccountUiState.ErrorMessage -> {
                            hideProgress()
                            showError(state.error.message ?: "")
                        }

                        SavingsAccountUiState.SavingsAccountWithdrawSuccess -> {
                            hideProgress()
                            showSavingsAccountWithdrawSuccessfully()
                        }

                        is SavingsAccountUiState.Initial -> {}

                        else -> throw IllegalStateException("Unexpected state : $state")
                    }
                }
            }
        }
    }

    private fun onWithdrawSavingsAccount() {
        submitWithdrawSavingsAccount()
    }

    fun showUserInterface() {
        activity?.title = getString(R.string.withdraw_savings_account)
        binding.tvAccountNumber.text = savingsWithAssociations?.accountNo
        binding.tvClientName.text = savingsWithAssociations?.clientName
        binding.tvWithdrawalDate.text = getTodayFormatted()
    }

    private val isFormIncomplete: Boolean
        get() {
            var rv = false
            if (binding.tilRemark.editText?.text.toString().trim { it <= ' ' }.isEmpty()) {
                rv = true
                binding.tilRemark.error = getString(
                    R.string.error_validation_blank,
                    getString(R.string.remark),
                )
            }
            return rv
        }

    private fun submitWithdrawSavingsAccount() {
        binding.tilRemark.isErrorEnabled = false
        if (!isFormIncomplete) {
            payload = SavingsAccountWithdrawPayload()
            payload?.note = binding.tilRemark.editText?.text.toString()
            payload?.withdrawnOnDate = getTodayFormatted()
            viewModel.submitWithdrawSavingsAccount(savingsWithAssociations?.accountNo, payload)
        }
    }

    private fun showSavingsAccountWithdrawSuccessfully() {
        showMessage(getString(R.string.savings_account_withdraw_successful))
        activity?.supportFragmentManager?.popBackStack()
    }

    private fun showMessage(message: String?) {
        Toaster.show(binding.root, message)
    }

    fun showError(error: String?) {
        Toaster.show(binding.root, error)
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

    companion object {
        fun newInstance(
            savingsWithAssociations: SavingsWithAssociations?,
        ): SavingsAccountWithdrawFragment {
            val fragment = SavingsAccountWithdrawFragment()
            val bundle = Bundle()
            bundle.putParcelable(Constants.SAVINGS_ACCOUNTS, savingsWithAssociations)
            fragment.arguments = bundle
            return fragment
        }
    }
}
