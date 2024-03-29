package org.mifos.mobile.ui.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentLoanRepaymentScheduleBinding
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.models.accounts.loan.Periods
import org.mifos.mobile.models.accounts.loan.tableview.Cell
import org.mifos.mobile.models.accounts.loan.tableview.ColumnHeader
import org.mifos.mobile.models.accounts.loan.tableview.RowHeader
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.adapters.LoanRepaymentScheduleAdapter
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.DateHelper
import org.mifos.mobile.utils.LoanUiState
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.ParcelableAndSerializableUtils.getCheckedParcelable
import org.mifos.mobile.viewModels.LoanRepaymentScheduleViewModel
import javax.inject.Inject

/**
 * Created by Rajan Maurya on 03/03/17.
 */
@AndroidEntryPoint
class LoanRepaymentScheduleFragment : BaseFragment() {

    private var _binding: FragmentLoanRepaymentScheduleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoanRepaymentScheduleViewModel by viewModels()

    @JvmField
    @Inject
    var loanRepaymentScheduleAdapter: LoanRepaymentScheduleAdapter? = null
    var sweetUIErrorHandler: SweetUIErrorHandler? = null
    private var loanId: Long? = 0
    private var loanWithAssociations: LoanWithAssociations? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? BaseActivity)?.showToolbar()
        setToolbarTitle(getString(R.string.loan_repayment_schedule))
        if (arguments != null) loanId = arguments?.getLong(Constants.LOAN_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoanRepaymentScheduleBinding.inflate(inflater, container, false)
        sweetUIErrorHandler = SweetUIErrorHandler(context, binding.root)
        showUserInterface()
        if (savedInstanceState == null) {
            viewModel.loanLoanWithAssociations(loanId)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loanUiState.collect {
                    when (it) {
                        is LoanUiState.Loading -> showProgress()
                        is LoanUiState.ShowError -> {
                            hideProgress()
                            showError(getString(it.message))
                        }

                        is LoanUiState.ShowLoan -> {
                            hideProgress()
                            showLoanRepaymentSchedule(it.loanWithAssociations)
                        }

                        is LoanUiState.ShowEmpty -> {
                            hideProgress()
                            showEmptyRepaymentsSchedule(loanWithAssociations)
                        }

                        else -> throw IllegalStateException("Unexpected state: $it")
                    }
                }
            }
        }

        binding.layoutError.btnTryAgain.setOnClickListener {
            retryClicked()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(Constants.LOAN_ACCOUNT, loanWithAssociations)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            showLoanRepaymentSchedule(
                savedInstanceState.getCheckedParcelable(
                    LoanWithAssociations::class.java,
                    Constants.LOAN_ACCOUNT
                )
            )
        }
    }

    /**
     * Initializes the layout
     */
    fun showUserInterface() {
        val columnWidth: Double
        binding.tvRepaymentSchedule.setHasFixedWidth(true)
        val orientation = resources.configuration.orientation
        binding.tvRepaymentSchedule.layoutParams?.width = resources.displayMetrics.widthPixels
        binding.tvRepaymentSchedule.setAdapter(loanRepaymentScheduleAdapter)
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            columnWidth = 2 * (resources.displayMetrics.widthPixels / 7.2)
        } else {
            columnWidth = 2 * (resources.displayMetrics.widthPixels / 6.6)
        }
        loanRepaymentScheduleAdapter?.setColumnWidth(columnWidth)
    }

    fun showProgress() {
        showProgressBar()
    }

    fun hideProgress() {
        hideProgressBar()
    }

    /**
     * Fetches [LoanWithAssociations] for a loan with `loanId`
     *
     * @param loanWithAssociations Contains details about Repayment Schedule
     */
    fun showLoanRepaymentSchedule(loanWithAssociations: LoanWithAssociations?) {
        this.loanWithAssociations = loanWithAssociations
        var currencyRepresentation =
            loanWithAssociations?.currency?.displaySymbol ?: loanWithAssociations?.currency?.code
        loanRepaymentScheduleAdapter
            ?.setCurrency(currencyRepresentation)
        setTableViewList(loanWithAssociations?.repaymentSchedule?.periods)
        binding.tvAccountNumber.text = loanWithAssociations?.accountNo
        binding.tvDisbursementDate.text =
            DateHelper.getDateAsString(loanWithAssociations?.timeline?.expectedDisbursementDate)
        binding.tvNumberOfPayments.text = loanWithAssociations?.numberOfRepayments.toString()
    }

    private fun setTableViewList(periods: List<Periods>?) {
        val mColumnHeaderList: MutableList<ColumnHeader?> = ArrayList()
        val mRowHeaders: MutableList<RowHeader?> = ArrayList()
        val mCellList: MutableList<List<Cell?>> = ArrayList()
        mColumnHeaderList.add(ColumnHeader(getString(R.string.date)))
        mColumnHeaderList.add(ColumnHeader(getString(R.string.loan_balance)))
        mColumnHeaderList.add(ColumnHeader(getString(R.string.repayment)))
        if (periods != null) {
            for ((i, period) in periods.withIndex()) {
                val cells: MutableList<Cell> = ArrayList()
                cells.add(Cell(period))
                cells.add(Cell(period))
                cells.add(Cell(period))
                mCellList.add(cells)
                mRowHeaders.add(RowHeader(i + 1))
            }
        }
        loanRepaymentScheduleAdapter?.setAllItems(mColumnHeaderList, mRowHeaders, mCellList)
    }

    /**
     * Shows an empty layout for a loan with `loanId` which has no Repayment Schedule
     *
     * @param loanWithAssociations Contains details about Repayment Schedule
     */
    fun showEmptyRepaymentsSchedule(loanWithAssociations: LoanWithAssociations?) {
        binding.tvAccountNumber.text = loanWithAssociations?.accountNo
        binding.tvDisbursementDate.text =
            DateHelper.getDateAsString(loanWithAssociations?.timeline?.expectedDisbursementDate)
        binding.tvNumberOfPayments.text = loanWithAssociations?.numberOfRepayments.toString()
        sweetUIErrorHandler?.showSweetEmptyUI(
            getString(R.string.repayment_schedule),
            R.drawable.ic_charges,
            binding.tvRepaymentSchedule,
            binding.layoutError.root,
        )
    }

    /**
     * It is called whenever any error occurs while executing a request
     *
     * @param message Error message that tells the user about the problem.
     */
    fun showError(message: String?) {
        if (!Network.isConnected(activity)) {
            sweetUIErrorHandler?.showSweetNoInternetUI(
                binding.tvRepaymentSchedule,
                binding.layoutError.root,
            )
        } else {
            sweetUIErrorHandler?.showSweetErrorUI(
                message,
                binding.tvRepaymentSchedule,
                binding.layoutError.root,
            )
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun retryClicked() {
        if (Network.isConnected(context)) {
            sweetUIErrorHandler?.hideSweetErrorLayoutUI(
                binding.tvRepaymentSchedule,
                binding.layoutError.root,
            )
            viewModel.loanLoanWithAssociations(loanId)
        } else {
            Toast.makeText(
                context,
                getString(R.string.internet_not_connected),
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideProgressBar()
        _binding = null
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        showUserInterface()
    }

    companion object {
        fun newInstance(loanId: Long?): LoanRepaymentScheduleFragment {
            val loanRepaymentScheduleFragment = LoanRepaymentScheduleFragment()
            val args = Bundle()
            if (loanId != null) args.putLong(Constants.LOAN_ID, loanId)
            loanRepaymentScheduleFragment.arguments = args
            return loanRepaymentScheduleFragment
        }
    }
}
