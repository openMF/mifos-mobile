package org.mifos.mobile.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentLoanWithdrawBinding
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.models.accounts.loan.LoanWithdraw
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.DateHelper
import org.mifos.mobile.utils.LoanUiState
import org.mifos.mobile.utils.Toaster
import org.mifos.mobile.viewModels.LoanAccountWithdrawViewModel

/**
 * Created by dilpreet on 7/6/17.
 */
@AndroidEntryPoint
class LoanAccountWithdrawFragment : BaseFragment() {

    private var _binding: FragmentLoanWithdrawBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: LoanAccountWithdrawViewModel

    private var loanWithAssociations: LoanWithAssociations? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            loanWithAssociations = arguments?.getParcelable(Constants.LOAN_ACCOUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoanWithdrawBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[LoanAccountWithdrawViewModel::class.java]
        setToolbarTitle(getString(R.string.withdraw_loan))
        showUserInterface()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loanUiState.observe(viewLifecycleOwner) {
            when (it) {
                is LoanUiState.Loading -> showProgress()
                is LoanUiState.ShowError -> {
                    hideProgress()
                    showLoanAccountWithdrawError(getString(it.message))
                }
                is LoanUiState.WithdrawSuccess -> {
                    hideProgress()
                    showLoanAccountWithdrawSuccess()
                }
                else -> throw IllegalStateException("Unexpected state: $it")
            }
        }

        binding.btnWithdrawLoan.setOnClickListener {
            onLoanWithdraw()
        }
    }

    /**
     * Sets Basic Information about that Loan Application
     */
    private fun showUserInterface() {
        binding.tvClientName.text = loanWithAssociations?.clientName
        binding.tvAccountNumber.text = loanWithAssociations?.accountNo
    }

    /**
     * Sends a request to server to withdraw that Loan Account
     */
    fun onLoanWithdraw() {
        val loanWithdraw = LoanWithdraw()
        loanWithdraw.note = binding.etWithdrawReason.text.toString()
        loanWithdraw.withdrawnOnDate = DateHelper
            .getDateAsStringFromLong(System.currentTimeMillis())
        viewModel.withdrawLoanAccount(
            loanWithAssociations?.id?.toLong(),
            loanWithdraw,
        )
    }

    /**
     * Receives A confirmation after successfull withdrawing of Loan Application.
     */
    fun showLoanAccountWithdrawSuccess() {
        Toaster.show(binding.root, R.string.loan_application_withdrawn_successfully)
        activity?.supportFragmentManager?.popBackStack()
    }

    /**
     * Shows an error in {@link Snackbar} if any error occurs during
     * withdrawing of Loan
     *
     * @param message Error Message displayed
     */
    fun showLoanAccountWithdrawError(message: String?) {
        Toaster.show(binding.root, message)
    }

    fun showProgress() {
        showProgressBar()
    }

    fun hideProgress() {
        hideProgressBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideProgress()
        _binding = null
    }

    companion object {
        fun newInstance(
            loanWithAssociations: LoanWithAssociations?,
        ): LoanAccountWithdrawFragment {
            val fragment = LoanAccountWithdrawFragment()
            val args = Bundle()
            args.putParcelable(Constants.LOAN_ACCOUNT, loanWithAssociations)
            fragment.arguments = args
            return fragment
        }
    }
}
