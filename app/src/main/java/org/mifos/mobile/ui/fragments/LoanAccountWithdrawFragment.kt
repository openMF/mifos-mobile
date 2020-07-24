package org.mifos.mobile.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick

import org.mifos.mobile.R
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.models.accounts.loan.LoanWithdraw
import org.mifos.mobile.presenters.LoanAccountWithdrawPresenter
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.views.LoanAccountWithdrawView
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.DateHelper
import org.mifos.mobile.utils.Toaster

import javax.inject.Inject

/**
 * Created by dilpreet on 7/6/17.
 */
class LoanAccountWithdrawFragment : BaseFragment(), LoanAccountWithdrawView {

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_client_name)
    var tvClientName: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_account_number)
    var tvAccountNumber: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.et_withdraw_reason)
    var etWithdrawReason: EditText? = null

    @kotlin.jvm.JvmField
    @Inject
    var loanAccountWithdrawPresenter: LoanAccountWithdrawPresenter? = null
    var rootView: View? = null
    private var loanWithAssociations: LoanWithAssociations? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as BaseActivity?)?.activityComponent?.inject(this)
        if (arguments != null) {
            loanWithAssociations = arguments?.getParcelable(Constants.LOAN_ACCOUNT)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_loan_withdraw, container, false)
        setToolbarTitle(getString(R.string.withdraw_loan))
        ButterKnife.bind(this, rootView!!)
        showUserInterface()
        loanAccountWithdrawPresenter?.attachView(this)
        return rootView
    }

    /**
     * Sets Basic Information about that Loan Application
     */
    private fun showUserInterface() {
        tvClientName?.text = loanWithAssociations?.clientName
        tvAccountNumber?.text = loanWithAssociations?.accountNo
    }

    /**
     * Sends a request to server to withdraw that Loan Account
     */
    @OnClick(R.id.btn_withdraw_loan)
    fun onLoanWithdraw() {
        val loanWithdraw = LoanWithdraw()
        loanWithdraw.note = etWithdrawReason?.text.toString()
        loanWithdraw.withdrawnOnDate = DateHelper
                .getDateAsStringFromLong(System.currentTimeMillis())
        loanAccountWithdrawPresenter?.withdrawLoanAccount(loanWithAssociations?.id?.toLong(),
                loanWithdraw)
    }

    /**
     * Receives A confirmation after successfull withdrawing of Loan Application.
     */
    override fun showLoanAccountWithdrawSuccess() {
        Toaster.show(rootView, R.string.loan_application_withdrawn_successfully)
        activity?.supportFragmentManager?.popBackStack()
    }

    /**
     * Shows an error in [Snackbar] if any error occurs during
     * withdrawing of Loan
     *
     * @param message Error Message displayed
     */
    override fun showLoanAccountWithdrawError(message: String?) {
        Toaster.show(rootView, message)
    }

    override fun showProgress() {
        showProgressBar()
    }

    override fun hideProgress() {
        hideProgressBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideProgress()
        loanAccountWithdrawPresenter?.detachView()
    }

    companion object {
        fun newInstance(
                loanWithAssociations: LoanWithAssociations?
        ): LoanAccountWithdrawFragment {
            val fragment = LoanAccountWithdrawFragment()
            val args = Bundle()
            args.putParcelable(Constants.LOAN_ACCOUNT, loanWithAssociations)
            fragment.arguments = args
            return fragment
        }
    }
}