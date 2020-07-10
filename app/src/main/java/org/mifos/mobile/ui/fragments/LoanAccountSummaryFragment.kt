package org.mifos.mobile.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import org.mifos.mobile.R
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
    @kotlin.jvm.JvmField
    @BindView(R.id.tv_loan_product_name)
    var tvLoanProductName: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_principal)
    var tvPrincipalName: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_interest_charged)
    var tvInterestChargedName: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_fees)
    var tvFeesName: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_penalties)
    var tvPenaltiesName: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_total_repayment)
    var tvTotalRepaymentName: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_total_paid)
    var tvTotalPaidName: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_interest_waived)
    var tvInterestWaivedName: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_penalties_waived)
    var tvPenaltiesWaivedName: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_fees_waived)
    var tvFeesWaivedName: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_outstanding_balance)
    var tvOutstandingBalanceName: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.ll_error)
    var layoutError: View? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_account_status)
    var tvAccountStatus: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_status)
    var tvStatus: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.ll_loan_summary)
    var llLoanSummary: LinearLayout? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.ll_loan_purpose)
    var llLoanPurpose: LinearLayout? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_loan_account_number)
    var tvLoanAccountNumber: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_loan_purpose)
    var tvLoanPurpose: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.iv_account_status)
    var ivAccountStatus: ImageView? = null
    private var loanWithAssociations: LoanWithAssociations? = null
    private var rootView: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as BaseActivity?)!!.activityComponent!!.inject(this)
        if (arguments != null) {
            loanWithAssociations = arguments!!.getParcelable(Constants.LOAN_ACCOUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_loan_account_summary, container, false)
        setToolbarTitle(getString(R.string.loan_summary))
        ButterKnife.bind(this, rootView!!)
        showLoanAccountsDetail(loanWithAssociations)
        return rootView
    }

    /**
     * Sets basic information about a Loan Account
     *
     * @param loanWithAssociations object containing details of each loan account,
     */
    fun showLoanAccountsDetail(loanWithAssociations: LoanWithAssociations?) {
        llLoanSummary!!.visibility = View.VISIBLE
        var currencySymbol = loanWithAssociations!!.currency!!.displaySymbol
        if (currencySymbol == null) {
            currencySymbol = loanWithAssociations.currency!!.code
        }
        tvLoanProductName!!.text = loanWithAssociations.loanProductName
        tvPrincipalName!!.text = getString(R.string.string_and_double,
                currencySymbol,
                loanWithAssociations.principal)
        tvInterestChargedName!!.text = getString(R.string.string_and_double,
                currencySymbol,
                loanWithAssociations.summary!!.interestCharged)
        tvFeesName!!.text = getString(R.string.string_and_double,
                currencySymbol,
                loanWithAssociations.summary!!.feeChargesCharged)
        tvPenaltiesName!!.text = getString(R.string.string_and_double,
                currencySymbol,
                loanWithAssociations.summary!!.penaltyChargesCharged)
        tvTotalRepaymentName!!.text = getString(R.string.string_and_double,
                currencySymbol,
                loanWithAssociations.summary!!.totalExpectedRepayment)
        tvTotalPaidName!!.text = getString(R.string.string_and_double,
                currencySymbol,
                loanWithAssociations.summary!!.totalRepayment)
        tvInterestWaivedName!!.text = getString(R.string.string_and_double,
                currencySymbol,
                loanWithAssociations.summary!!.interestWaived)
        tvPenaltiesWaivedName!!.text = getString(R.string.string_and_double,
                currencySymbol,
                loanWithAssociations.summary!!.penaltyChargesWaived)
        tvFeesWaivedName!!.text = getString(R.string.string_and_double,
                currencySymbol,
                loanWithAssociations.summary!!.feeChargesWaived)
        tvOutstandingBalanceName!!.text = resources.getString(R.string.string_and_string,
                currencySymbol, CurrencyUtil.formatCurrency(activity,
                loanWithAssociations.summary!!.totalOutstanding))
        tvLoanAccountNumber!!.text = loanWithAssociations.accountNo
        if (loanWithAssociations.loanPurposeName != null) {
            llLoanPurpose!!.visibility = View.VISIBLE
            tvLoanPurpose!!.text = loanWithAssociations.loanPurposeName
        }
        if (loanWithAssociations.status!!.active!!) {
            tvAccountStatus!!.setText(R.string.active_uc)
            ivAccountStatus!!.setImageResource(R.drawable.ic_check_circle_green_24px
            )
        } else {
            tvAccountStatus!!.setText(R.string.inactive_uc)
            ivAccountStatus!!.setImageResource(R.drawable.ic_report_problem_red_24px)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideProgressBar()
    }

    companion object {
        fun newInstance(
                loanWithAssociations: LoanWithAssociations?): LoanAccountSummaryFragment {
            val loanAccountSummaryFragment = LoanAccountSummaryFragment()
            val args = Bundle()
            args.putParcelable(Constants.LOAN_ACCOUNT, loanWithAssociations)
            loanAccountSummaryFragment.arguments = args
            return loanAccountSummaryFragment
        }
    }
}