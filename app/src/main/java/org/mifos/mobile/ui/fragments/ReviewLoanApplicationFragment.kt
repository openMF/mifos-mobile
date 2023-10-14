package org.mifos.mobile.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentReviewLoanApplicationBinding
import org.mifos.mobile.models.payload.LoansPayload
import org.mifos.mobile.ui.enums.LoanState
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.MFErrorParser
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.ParcelableAndSerializableUtils.getCheckedParcelable
import org.mifos.mobile.utils.ParcelableAndSerializableUtils.getCheckedSerializable
import org.mifos.mobile.utils.Toaster
import org.mifos.mobile.viewModels.ReviewLoanApplicationViewModel

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


    private lateinit var viewModel: ReviewLoanApplicationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentReviewLoanApplicationBinding.inflate(inflater, container, false)
        viewModel = ViewModelProviders.of(this)[ReviewLoanApplicationViewModel::class.java]
        val loanState = arguments?.getCheckedSerializable(LoanState::class.java, LOAN_STATE) as LoanState
        if (loanState == LoanState.CREATE) {
            viewModel.insertData(
                loanState,
                arguments?.getCheckedParcelable(LoansPayload::class.java, LOANS_PAYLOAD)!!,
                arguments?.getString(LOAN_NAME)!!,
                arguments?.getString(ACCOUNT_NO)!!,
            )
        } else {
            viewModel.insertData(
                loanState,
                arguments?.getLong(LOAN_ID)!!,
                arguments?.getCheckedParcelable(LoansPayload::class.java, LOANS_PAYLOAD)!!,
                arguments?.getString(LOAN_NAME)!!,
                arguments?.getString(ACCOUNT_NO)!!,
            )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvLoanProduct.text = viewModel.getLoanProduct()
        binding.tvLoanPurpose.text = viewModel.getLoanPurpose()
        binding.tvPrincipalAmount.text = viewModel.getPrincipal().toString()
        binding.tvExpectedDisbursementDate.text = viewModel.getDisbursementDate()
        binding.tvSubmissionDate.text = viewModel.getSubmissionDate()
        binding.tvCurrency.text = viewModel.getCurrency()
        binding.tvNewLoanApplication.text = viewModel.getLoanName()
        binding.tvAccountNumber.text = viewModel.getAccountNo()

        binding.btnLoanSubmit.setOnClickListener {
            showProgress()
            viewModel.submitLoan()
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<ResponseBody>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: ResponseBody) {
                        hideProgress()
                        if (viewModel.getLoanState() == LoanState.CREATE) {
                            showLoanAccountCreatedSuccessfully()
                        } else {
                            showLoanAccountUpdatedSuccessfully()
                        }
                    }

                    override fun onError(e: Throwable) {
                        hideProgress()
                        showError(MFErrorParser.errorMessage(e))
                    }
                })
        }
    }

    fun showLoanAccountUpdatedSuccessfully() {
        Toaster.show(binding.root, R.string.loan_application_updated_successfully)
        activity?.supportFragmentManager?.popBackStack()
    }

    fun showError(message: String?) = if (!Network.isConnected(activity)) {
        binding.llError.ivStatus.setImageResource(R.drawable.ic_error_black_24dp)
        binding.llError.tvStatus.text = getString(R.string.internet_not_connected)
        binding.llAddLoan.visibility = View.GONE
        binding.llError.root.visibility = View.VISIBLE
    } else {
        Toaster.show(binding.root, message)
    }

    fun showProgress() {
        binding.llAddLoan.visibility = View.GONE
        showProgressBar()
    }

    fun hideProgress() {
        binding.llAddLoan.visibility = View.VISIBLE
        hideProgressBar()
    }

    fun showLoanAccountCreatedSuccessfully() {
        Toaster.show(binding.root, R.string.loan_application_submitted_successfully)
        activity?.supportFragmentManager?.popBackStack()
    }
}
