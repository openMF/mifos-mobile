package org.mifos.mobile.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentLoanAccountSummaryBinding
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.CurrencyUtil

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/ /**
 * Created by dilpreet on 25/2/17.
 */
class LoanAccountSummaryFragment : BaseFragment() {
    private var _binding: FragmentLoanAccountSummaryBinding? = null
    private val binding get() = _binding!!
    private var loanWithAssociations: LoanWithAssociations? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //(activity as BaseActivity?)?.activityComponent?.inject(this)
        if (arguments != null) {
            loanWithAssociations = arguments?.getParcelable(Constants.LOAN_ACCOUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoanAccountSummaryBinding.inflate(inflater, container, false)
        setToolbarTitle(getString(R.string.loan_summary))
        showLoanAccountsDetail(loanWithAssociations)
        return binding.root
    }

    /**
     * Sets basic information about a Loan Account
     *
     * @param loanWithAssociations object containing details of each loan account,
     */
    private fun showLoanAccountsDetail(loanWithAssociations: LoanWithAssociations?) {
        with(binding) {
            llLoanSummary.visibility = View.VISIBLE
            var currencySymbol = loanWithAssociations?.currency?.displaySymbol
            if (currencySymbol == null) {
                currencySymbol = loanWithAssociations?.currency?.code
            }
            tvLoanProductName.text = loanWithAssociations?.loanProductName
            tvPrincipal.text = getString(
                R.string.string_and_double,
                currencySymbol,
                loanWithAssociations?.principal,
            )
            tvInterestCharged.text = getString(
                R.string.string_and_double,
                currencySymbol,
                loanWithAssociations?.summary?.interestCharged,
            )
            tvFees.text = getString(
                R.string.string_and_double,
                currencySymbol,
                loanWithAssociations?.summary?.feeChargesCharged,
            )
            tvPenalties.text = getString(
                R.string.string_and_double,
                currencySymbol,
                loanWithAssociations?.summary?.penaltyChargesCharged,
            )
            tvTotalRepayment.text = getString(
                R.string.string_and_double,
                currencySymbol,
                loanWithAssociations?.summary?.totalExpectedRepayment,
            )
            tvTotalPaid.text = getString(
                R.string.string_and_double,
                currencySymbol,
                loanWithAssociations?.summary?.totalRepayment,
            )
            tvInterestWaived.text = getString(
                R.string.string_and_double,
                currencySymbol,
                loanWithAssociations?.summary?.interestWaived,
            )
            tvPenaltiesWaived.text = getString(
                R.string.string_and_double,
                currencySymbol,
                loanWithAssociations?.summary?.penaltyChargesWaived,
            )
            tvFeesWaived.text = getString(
                R.string.string_and_double,
                currencySymbol,
                loanWithAssociations?.summary?.feeChargesWaived,
            )
            tvOutstandingBalance.text = resources.getString(
                R.string.string_and_string,
                currencySymbol,
                CurrencyUtil.formatCurrency(
                    activity,
                    loanWithAssociations?.summary?.totalOutstanding,
                ),
            )
            tvLoanAccountNumber.text = loanWithAssociations?.accountNo
            if (loanWithAssociations?.loanPurposeName != null) {
                tvLoanPurpose.visibility = View.VISIBLE
                tvLoanPurpose.text = loanWithAssociations.loanPurposeName
            }
            if (loanWithAssociations?.status?.active == true) {
                tvAccountStatus.setText(R.string.active_uc)
                ivAccountStatus.setImageResource(
                    R.drawable.ic_check_circle_green_24px,
                )
            } else {
                tvAccountStatus.setText(R.string.inactive_uc)
                ivAccountStatus.setImageResource(R.drawable.ic_report_problem_red_24px)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideProgressBar()
        _binding = null
    }

    companion object {
        fun newInstance(
            loanWithAssociations: LoanWithAssociations?,
        ): LoanAccountSummaryFragment {
            val loanAccountSummaryFragment = LoanAccountSummaryFragment()
            val args = Bundle()
            args.putParcelable(Constants.LOAN_ACCOUNT, loanWithAssociations)
            loanAccountSummaryFragment.arguments = args
            return loanAccountSummaryFragment
        }
    }
}
