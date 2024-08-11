package org.mifos.mobile.ui.loan_account_application

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.core.common.Constants.LOAN_STATE
import org.mifos.mobile.core.common.utils.DateHelper
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.model.entity.payload.LoansPayload
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.core.model.enums.LoanState
import org.mifos.mobile.feature.loan.loan_account_application.LoanApplicationScreen
import org.mifos.mobile.feature.loan.loan_account_application.LoanApplicationViewModel
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.*
import org.mifos.mobile.core.common.utils.ParcelableAndSerializableUtils.getCheckedParcelable
import org.mifos.mobile.core.common.utils.ParcelableAndSerializableUtils.getCheckedSerializable
import org.mifos.mobile.ui.loan_review.ReviewLoanApplicationFragment

/**
 * Created by Rajan Maurya on 06/03/17.
 */
@AndroidEntryPoint
class LoanApplicationFragment : BaseFragment() {

    private val viewModel: LoanApplicationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as? BaseActivity)?.hideToolbar()
//        if (arguments != null) {
//            viewModel.loanState = arguments?.getCheckedSerializable(LoanState::class.java, LOAN_STATE) as? LoanState
//                ?: LoanState.CREATE
//            if (viewModel.loanState == org.mifos.mobile.core.model.enums.LoanState.UPDATE) {
//                viewModel.loanWithAssociations = arguments?.getCheckedParcelable(
//                    LoanWithAssociations::class.java, org.mifos.mobile.core.common.Constants.LOAN_ACCOUNT)
//            }
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return mifosComposeView(requireContext()) {

        }
    }


    private fun reviewNewLoanApplication() {
        val loansPayload = LoansPayload().apply {
            clientId = viewModel.loanTemplate.clientId
            loanPurpose = viewModel.loanApplicationScreenData.value.selectedLoanPurpose ?: getString(R.string.loan_purpose_not_provided)
            productName = viewModel.loanApplicationScreenData.value.selectedLoanProduct
            currency = viewModel.loanApplicationScreenData.value.currencyLabel
            if (viewModel.purposeId > 0) loanPurposeId = viewModel.purposeId
            productId = viewModel.productId
            principal = viewModel.loanApplicationScreenData.value.principalAmount?.toDoubleOrNull() ?: 0.0
            loanTermFrequency = viewModel.loanTemplate.termFrequency
            loanTermFrequencyType = viewModel.loanTemplate.interestRateFrequencyType?.id
            loanType = "individual"
            numberOfRepayments = viewModel.loanTemplate.numberOfRepayments
            repaymentEvery = viewModel.loanTemplate.repaymentEvery
            repaymentFrequencyType = viewModel.loanTemplate.interestRateFrequencyType?.id
            interestRatePerPeriod = viewModel.loanTemplate.interestRatePerPeriod
            expectedDisbursementDate = DateHelper.getSpecificFormat(DateHelper.FORMAT_dd_MMMM_yyyy, viewModel.loanApplicationScreenData.value.disbursementDate)
            submittedOnDate = DateHelper.getSpecificFormat(DateHelper.FORMAT_dd_MMMM_yyyy, viewModel.loanApplicationScreenData.value.submittedDate)
            transactionProcessingStrategyId = viewModel.loanTemplate.transactionProcessingStrategyId
            amortizationType = viewModel.loanTemplate.amortizationType?.id
            interestCalculationPeriodType = viewModel.loanTemplate.interestCalculationPeriodType?.id
            interestType = viewModel.loanTemplate.interestType?.id
        }

        (activity as BaseActivity?)?.replaceFragment(
            ReviewLoanApplicationFragment.newInstance(
                viewModel.loanState.value,
                loansPayload,
                getString(R.string.string_and_string, getString(R.string.new_loan_application) + " ", viewModel.loanApplicationScreenData.value.clientName ?: ""),
                getString(R.string.string_and_string, getString(R.string.account_number) + " ", viewModel.loanApplicationScreenData.value.accountNumber ?: "")
            ),
            true,
            R.id.container
        )
    }

    private fun submitUpdateLoanApplication() {
        val loansPayload = LoansPayload().apply {
            principal = viewModel.loanApplicationScreenData.value.principalAmount?.toDoubleOrNull() ?: 0.0
            productId = viewModel.productId
            loanPurpose = viewModel.loanApplicationScreenData.value.selectedLoanPurpose ?: getString(R.string.loan_purpose_not_provided)
            productName = viewModel.loanApplicationScreenData.value.selectedLoanProduct
            currency = viewModel.loanApplicationScreenData.value.currencyLabel
            if (viewModel.purposeId > 0) loanPurposeId = viewModel.purposeId
            loanTermFrequency = viewModel.loanTemplate.termFrequency
            loanTermFrequencyType = viewModel.loanTemplate.interestRateFrequencyType?.id
            numberOfRepayments = viewModel.loanTemplate.numberOfRepayments
            repaymentEvery = viewModel.loanTemplate.repaymentEvery
            repaymentFrequencyType = viewModel.loanTemplate.interestRateFrequencyType?.id
            interestRatePerPeriod = viewModel.loanTemplate.interestRatePerPeriod
            interestType = viewModel.loanTemplate.interestType?.id
            interestCalculationPeriodType = viewModel.loanTemplate.interestCalculationPeriodType?.id
            amortizationType = viewModel.loanTemplate.amortizationType?.id
            transactionProcessingStrategyId = viewModel.loanTemplate.transactionProcessingStrategyId
            expectedDisbursementDate = DateHelper.getSpecificFormat(DateHelper.FORMAT_dd_MMMM_yyyy, viewModel.loanApplicationScreenData.value.disbursementDate)
        }

        (activity as BaseActivity?)?.replaceFragment(
            ReviewLoanApplicationFragment.newInstance(
                viewModel.loanState.value,
                loansPayload,
                viewModel.loanWithAssociations.value?.id?.toLong(),
                getString(R.string.string_and_string, getString(R.string.new_loan_application) + " ", viewModel.loanApplicationScreenData.value.clientName ?: ""),
                getString(R.string.string_and_string, getString(R.string.account_number) + " ", viewModel.loanApplicationScreenData.value.accountNumber ?: "")
            ),
            true,
            R.id.container
        )
    }


    companion object {
        /**
         * Used when we want to apply for a Loan
         *
         * @param loanState [LoanState] is set to `LoanState.CREATE`
         * @return Instance of [LoanApplicationFragment]
         */
        fun newInstance(loanState: LoanState?): LoanApplicationFragment {
            val fragment = LoanApplicationFragment()
            val args = Bundle()
            args.putSerializable(org.mifos.mobile.core.common.Constants.LOAN_STATE, loanState)
            fragment.arguments = args
            return fragment
        }

        /**
         * Used when we want to update a Loan Application
         *
         * @param loanState            [LoanState] is set to `LoanState.UPDATE`
         * @param loanWithAssociations [LoanAccount] to modify
         * @return Instance of [LoanApplicationFragment]
         */
        fun newInstance(
            loanState: org.mifos.mobile.core.model.enums.LoanState?,
            loanWithAssociations: LoanWithAssociations?,
        ): LoanApplicationFragment {
            val fragment = LoanApplicationFragment()
            val args = Bundle()
            args.putSerializable(org.mifos.mobile.core.common.Constants.LOAN_STATE, loanState)
            args.putParcelable(org.mifos.mobile.core.common.Constants.LOAN_ACCOUNT, loanWithAssociations)
            fragment.arguments = args
            return fragment
        }
    }
}
