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
import org.mifos.mobile.databinding.FragmentReviewLoanApplicationBinding
import org.mifos.mobile.models.payload.LoansPayload
import org.mifos.mobile.ui.enums.LoanState
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.MFErrorParser
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.ReviewLoanApplicationUiState
import org.mifos.mobile.utils.Toaster
import org.mifos.mobile.viewModels.ReviewLoanApplicationViewModel
import java.util.Locale

@AndroidEntryPoint
class ReviewLoanApplicationFragment : BaseFragment() {

    private var _binding: FragmentReviewLoanApplicationBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val LOANS_PAYLOAD = "loans_payload"
        const val LOAN_NAME = "loan_name"
        const val ACCOUNT_NO = "account_no"
        const val LOAN_STATE = "loan_state"
        const val LOAN_ID = "loan_id"

        fun newInstance(
            loanState: LoanState,
            loansPayload: LoansPayload,
            loanName: String,
            accountNo: String,
        ): ReviewLoanApplicationFragment {
            val fragment = ReviewLoanApplicationFragment()
            val args = Bundle().apply {
                putSerializable(LOAN_STATE, loanState)
                putParcelable(LOANS_PAYLOAD, loansPayload)
                putString(LOAN_NAME, loanName)
                putString(ACCOUNT_NO, accountNo)
            }
            fragment.arguments = args
            return fragment
        }

        fun newInstance(
            loanState: LoanState?,
            loansPayload: LoansPayload?,
            loanId: Long?,
            loanName: String?,
            accountNo: String?,
        ): ReviewLoanApplicationFragment {
            val fragment = ReviewLoanApplicationFragment()
            val args = Bundle().apply {
                putSerializable(LOAN_STATE, loanState)
                if (loanId != null) putLong(LOAN_ID, loanId)
                putParcelable(LOANS_PAYLOAD, loansPayload)
                putString(LOAN_NAME, loanName)
                putString(ACCOUNT_NO, accountNo)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel by viewModels<ReviewLoanApplicationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentReviewLoanApplicationBinding.inflate(inflater, container, false)
        val loanState = arguments?.getSerializable(LOAN_STATE) as LoanState
        if (loanState == LoanState.CREATE) {
            viewModel.insertData(
                loanState,
                arguments?.getParcelable(LOANS_PAYLOAD)!!,
                arguments?.getString(LOAN_NAME)!!,
                arguments?.getString(ACCOUNT_NO)!!,
            )
        } else {
            viewModel.insertData(
                loanState,
                arguments?.getLong(LOAN_ID)!!,
                arguments?.getParcelable(LOANS_PAYLOAD)!!,
                arguments?.getString(LOAN_NAME)!!,
                arguments?.getString(ACCOUNT_NO)!!,
            )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            tvLoanProduct.text = viewModel.getLoanProduct()
            tvLoanPurpose.text = viewModel.getLoanPurpose()
            tvPrincipalAmount.text = String.format(
                Locale.getDefault(),
                "%.2f", viewModel.getPrincipal(),
            )
            tvExpectedDisbursementDate.text = viewModel.getDisbursementDate()
            tvSubmissionDate.text = viewModel.getSubmissionDate()
            tvCurrency.text = viewModel.getCurrency()
            tvNewLoanApplication.text = viewModel.getLoanName()
            tvAccountNumber.text = viewModel.getAccountNo()

            btnLoanSubmit.setOnClickListener {
                viewModel.submitLoan()
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.reviewLoanApplicationUiState.collect {
                        hideProgress()
                        when (it) {
                            is ReviewLoanApplicationUiState.Initial -> {}

                            is ReviewLoanApplicationUiState.Loading -> {
                                showProgress()
                            }

                            is ReviewLoanApplicationUiState.ReviewSuccess -> {
                                if (viewModel.getLoanState() == LoanState.CREATE) {
                                    showLoanAccountCreatedSuccessfully()
                                } else {
                                    showLoanAccountUpdatedSuccessfully()
                                }
                            }

                            is ReviewLoanApplicationUiState.Error -> {
                                showError(MFErrorParser.errorMessage(it.throwable))
                            }
                        }
                    }
                }
            }
        }

    }

    private fun showLoanAccountUpdatedSuccessfully() {
        Toaster.show(binding.root, R.string.loan_application_updated_successfully)
        activity?.supportFragmentManager?.popBackStack()
    }

    fun showError(message: String?) {
        if (!Network.isConnected(activity)) {
            binding.apply {
                llError.ivStatus.setImageResource(R.drawable.ic_error_black_24dp)
                llError.tvStatus.text = getString(R.string.internet_not_connected)
                llAddLoan.visibility = View.GONE
                llError.root.visibility = View.VISIBLE
            }
        } else {
            Toaster.show(binding.root, message)
        }
    }

    fun showProgress() {
        binding.llAddLoan.visibility = View.GONE
        showProgressBar()
    }

    fun hideProgress() {
        binding.llAddLoan.visibility = View.VISIBLE
        hideProgressBar()
    }

    private fun showLoanAccountCreatedSuccessfully() {
        Toaster.show(binding.root, R.string.loan_application_submitted_successfully)
        activity?.supportFragmentManager?.popBackStack()
    }
}
