package org.mifos.mobile.ui.fragments

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler
import org.mifos.mobile.R
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.databinding.FragmentLoanAccountDetailsBinding
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.presenters.LoanAccountsDetailPresenter
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.enums.AccountType
import org.mifos.mobile.ui.enums.ChargeType
import org.mifos.mobile.ui.enums.LoanState
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.views.LoanAccountsDetailView
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.CurrencyUtil
import org.mifos.mobile.utils.DateHelper
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.QrCodeGenerator
import javax.inject.Inject

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/ /**
 * @author Vishwajeet
 * @since 19/08/16
 */
class LoanAccountsDetailFragment : BaseFragment(), LoanAccountsDetailView {
    private var _binding: FragmentLoanAccountDetailsBinding? = null
    private val binding get() = _binding!!

    @JvmField
    @Inject
    var loanAccountDetailsPresenter: LoanAccountsDetailPresenter? = null

    @JvmField
    @Inject
    var preferencesHelper: PreferencesHelper? = null
    private var loanWithAssociations: LoanWithAssociations? = null
    private var showLoanUpdateOption = false
    private var loanId: Long? = 0
    private var sweetUIErrorHandler: SweetUIErrorHandler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            loanId = arguments?.getLong(Constants.LOAN_ID)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        //(activity as BaseActivity?)?.activityComponent?.inject(this)
        _binding = FragmentLoanAccountDetailsBinding.inflate(inflater, container, false)
        val rootView = binding.root
        setToolbarTitle(getString(R.string.loan_account_details))
        loanAccountDetailsPresenter?.attachView(this)
        sweetUIErrorHandler = SweetUIErrorHandler(activity, rootView)
        if (savedInstanceState == null && this.loanWithAssociations == null) {
            loanAccountDetailsPresenter?.loadLoanAccountDetails(loanId)
        } else {
            showLoanAccountsDetail(this.loanWithAssociations)
        }
        return rootView
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(Constants.LOAN_ACCOUNT, loanWithAssociations)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            showLoanAccountsDetail(savedInstanceState.getParcelable<Parcelable>(Constants.LOAN_ACCOUNT) as LoanWithAssociations)
        }
    }

    /**
     * Shows details about loan account fetched from server is status is Active else shows and
     * error layout i.e. `layoutError` with a msg related to the status.
     *
     * @param loanWithAssociations object containing details of each loan account,
     */
    override fun showLoanAccountsDetail(loanWithAssociations: LoanWithAssociations?) {
        this.loanWithAssociations = loanWithAssociations
        with(binding) {
            llAccountDetail.visibility = View.VISIBLE
            if (loanWithAssociations?.status?.active!!) {
                val overdueSinceDate = loanWithAssociations.summary?.getOverdueSinceDate()
                if (overdueSinceDate == null) {
                    tvDueDate.setText(R.string.not_available)
                } else {
                    tvDueDate.text = DateHelper.getDateAsString(overdueSinceDate)
                }
                showDetails(loanWithAssociations)
            } else if (loanWithAssociations.status?.pendingApproval!!) {
                sweetUIErrorHandler?.showSweetCustomErrorUI(
                    getString(R.string.approval_pending),
                    R.drawable.ic_assignment_turned_in_black_24dp,
                    llAccountDetail,
                    layoutError.root,
                )
                showLoanUpdateOption = true
            } else if (loanWithAssociations.status?.waitingForDisbursal!!) {
                sweetUIErrorHandler?.showSweetCustomErrorUI(
                    getString(R.string.waiting_for_disburse),
                    R.drawable.ic_assignment_turned_in_black_24dp,
                    llAccountDetail,
                    layoutError.root,
                )
            } else {
                btnMakePayment.visibility = View.GONE
                tvDueDate.setText(R.string.not_available)
                showDetails(loanWithAssociations)
            }
        }
        activity?.invalidateOptionsMenu()
    }

    /**
     * Sets basic information about a loan
     *
     * @param loanWithAssociations object containing details of each loan account,
     */
    private fun showDetails(loanWithAssociations: LoanWithAssociations?) {
        with(binding) {
            tvOutstandingBalance.text = resources.getString(
                R.string.string_and_string,
                loanWithAssociations?.summary?.currency?.displaySymbol,
                CurrencyUtil.formatCurrency(
                    activity,
                    loanWithAssociations?.summary?.totalOutstanding,
                ),
            )
            if (loanWithAssociations?.repaymentSchedule?.periods != null) {
                for ((_, _, dueDate, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, totalDueForPeriod) in loanWithAssociations.repaymentSchedule?.periods!!) {
                    if (dueDate == loanWithAssociations.summary?.getOverdueSinceDate()) {
                        tvNextInstallment.text = resources.getString(
                            R.string.string_and_string,
                            loanWithAssociations.summary?.currency?.displaySymbol,
                            CurrencyUtil.formatCurrency(
                                activity,
                                totalDueForPeriod,
                            ),
                        )
                        break
                    } else if (loanWithAssociations.summary?.getOverdueSinceDate() == null) {
                        tvNextInstallment.setText(R.string.not_available)
                    }
                }
            }
            tvAccountNumber.text = loanWithAssociations?.accountNo
            tvLoanType.text = loanWithAssociations?.loanType?.value
            tvCurrency.text = loanWithAssociations?.summary?.currency?.code
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnMakePayment.setOnClickListener {
                onMakePaymentClicked()
            }

            llSummary.setOnClickListener {
                onLoanSummaryClicked()
            }

            llRepayment.setOnClickListener {
                onRepaymentScheduleClicked()
            }

            llLoanTransactions.setOnClickListener {
                onTransactionsClicked()
            }

            llLoanCharges.setOnClickListener {
                chargesClicked()
            }

            llLoanQrCode.setOnClickListener {
                onQrCodeClicked()
            }

            layoutError.btnTryAgain.setOnClickListener {
                retryClicked()
            }
        }
    }

    /**
     * Opens [SavingsMakeTransferFragment] to Make a payment for loan account with given
     * `loanId`
     */
    private fun onMakePaymentClicked() {
        (activity as BaseActivity?)?.replaceFragment(
            SavingsMakeTransferFragment.newInstance(
                loanId,
                loanWithAssociations?.summary?.totalOutstanding,
                Constants.TRANSFER_PAY_TO,
            ),
            true,
            R.id.container,
        )
    }

    /**
     * Opens [LoanAccountSummaryFragment]
     */
    private fun onLoanSummaryClicked() {
        (activity as BaseActivity?)?.replaceFragment(
            LoanAccountSummaryFragment.newInstance(
                loanWithAssociations,
            ),
            true,
            R.id.container,
        )
    }

    /**
     * Opens [LoanRepaymentScheduleFragment]
     */
    private fun onRepaymentScheduleClicked() {
        (activity as BaseActivity?)?.replaceFragment(
            LoanRepaymentScheduleFragment.newInstance(
                loanId,
            ),
            true,
            R.id.container,
        )
    }

    /**
     * Opens [LoanAccountTransactionFragment]
     */
    private fun onTransactionsClicked() {
        (activity as BaseActivity?)?.replaceFragment(
            LoanAccountTransactionFragment.newInstance(
                loanId,
            ),
            true,
            R.id.container,
        )
    }

    private fun chargesClicked() {
        (activity as BaseActivity?)?.replaceFragment(
            ClientChargeFragment.newInstance(
                loanWithAssociations?.id?.toLong(),
                ChargeType.LOAN,
            ),
            true,
            R.id.container,
        )
    }

    private fun onQrCodeClicked() {
        val accountDetailsInJson = QrCodeGenerator.getAccountDetailsInString(
            loanWithAssociations?.accountNo,
            preferencesHelper?.officeName,
            AccountType.LOAN,
        )
        (activity as BaseActivity?)?.replaceFragment(
            QrCodeDisplayFragment.newInstance(
                accountDetailsInJson,
            ),
            true,
            R.id.container,
        )
    }

    /**
     * It is called whenever any error occurs while executing a request
     *
     * @param message Error message that tells the user about the problem.
     */
    override fun showErrorFetchingLoanAccountsDetail(message: String?) {
        if (!Network.isConnected(activity)) {
            sweetUIErrorHandler?.showSweetNoInternetUI(
                binding.llAccountDetail,
                binding.layoutError.root,
            )
        } else {
            sweetUIErrorHandler?.showSweetErrorUI(
                message,
                binding.llAccountDetail,
                binding.layoutError.root,
            )
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun retryClicked() {
        if (Network.isConnected(context)) {
            sweetUIErrorHandler?.hideSweetErrorLayoutUI(
                binding.llAccountDetail,
                binding.layoutError.root,
            )
            loanAccountDetailsPresenter?.loadLoanAccountDetails(loanId)
        } else {
            Toast.makeText(
                context,
                getString(R.string.internet_not_connected),
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    override fun showProgress() {
        showProgressBar()
    }

    override fun hideProgress() {
        hideProgressBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideProgressBar()
        loanAccountDetailsPresenter?.detachView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_loan_details, menu)
        if (showLoanUpdateOption) {
            menu.findItem(R.id.menu_update_loan).isVisible = true
            menu.findItem(R.id.menu_withdraw_loan).isVisible = true
            menu.findItem(R.id.menu_view_guarantor).isVisible = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_update_loan -> {
                (activity as BaseActivity?)?.replaceFragment(
                    LoanApplicationFragment.newInstance(
                        LoanState.UPDATE,
                        loanWithAssociations,
                    ),
                    true,
                    R.id.container,
                )
                return true
            }

            R.id.menu_withdraw_loan -> {
                (activity as BaseActivity?)?.replaceFragment(
                    LoanAccountWithdrawFragment.newInstance(
                        loanWithAssociations,
                    ),
                    true,
                    R.id.container,
                )
            }

            R.id.menu_view_guarantor -> {
                (activity as BaseActivity?)?.replaceFragment(
                    GuarantorListFragment.newInstance(
                        loanId,
                    ),
                    true,
                    R.id.container,
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        @JvmStatic
        fun newInstance(loanId: Long): LoanAccountsDetailFragment {
            val fragment = LoanAccountsDetailFragment()
            val args = Bundle()
            args.putLong(Constants.LOAN_ID, loanId)
            fragment.arguments = args
            return fragment
        }
    }
}
