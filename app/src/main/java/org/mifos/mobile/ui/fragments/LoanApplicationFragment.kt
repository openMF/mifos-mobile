package org.mifos.mobile.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentAddLoanApplicationBinding
import org.mifos.mobile.models.accounts.loan.LoanAccount
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.models.payload.LoansPayload
import org.mifos.mobile.models.templates.loans.LoanTemplate
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.enums.LoanState
import org.mifos.mobile.ui.loan_review.ReviewLoanApplicationFragment.Companion.newInstance
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.*
import org.mifos.mobile.utils.ParcelableAndSerializableUtils.getCheckedParcelable
import org.mifos.mobile.utils.ParcelableAndSerializableUtils.getCheckedSerializable
import org.mifos.mobile.viewModels.LoanApplicationViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

/**
 * Created by Rajan Maurya on 06/03/17.
 */
@AndroidEntryPoint
class LoanApplicationFragment : BaseFragment() {
    private var _binding: FragmentAddLoanApplicationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoanApplicationViewModel by viewModels()

    private val listLoanProducts: MutableList<String?> = ArrayList()
    private val listLoanPurpose: MutableList<String?> = ArrayList()
    private var loanTemplate: LoanTemplate? = null
    private var selectedDisbursementDate: Instant = Instant.now()
    private val datePickerDialog by lazy {
        getDatePickerDialog(selectedDisbursementDate, DatePickerConstrainType.ONLY_FUTURE_DAYS) {
            val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(it)
            if (isDisbursementDate) {
                binding.tvExpectedDisbursementDate.text = formattedDate
                disbursementDate = formattedDate
                isDisbursementDate = false
            }
            setSubmissionDisburseDate()
        }
    }
    private var loanState: LoanState? = null
    private var loanWithAssociations: LoanWithAssociations? = null
    private var productId: Int? = 0
    private var purposeId: Int? = -1
    private var disbursementDate: String? = null
    private var submittedDate: String? = null
    private var isDisbursementDate = false
    private var isLoanUpdatePurposesInitialization = true
    private var sweetUIErrorHandler: SweetUIErrorHandler? = null
    /**
     * Used when we want to apply for a Loan
     *
     * @param loanState [LoanState] is set to `LoanState.CREATE`
     * @return Instance of [LoanApplicationFragment]
     */
    fun newInstance(loanState: LoanState): LoanApplicationFragment {
        val fragment = LoanApplicationFragment()
        val args = Bundle()
        args.putSerializable(Constants.LOAN_STATE, loanState)
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
        loanState: LoanState?,
        loanWithAssociations: LoanWithAssociations?,
    ): LoanApplicationFragment {
        val fragment = LoanApplicationFragment()
        val args = Bundle()
        args.putSerializable(Constants.LOAN_STATE, loanState)
        args.putParcelable(Constants.LOAN_ACCOUNT, loanWithAssociations)
        fragment.arguments = args
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            loanState = arguments?.getCheckedSerializable(
                LoanState::class.java,
                Constants.LOAN_STATE
            ) as LoanState
            if (loanState == LoanState.CREATE) {
                setToolbarTitle(getString(R.string.apply_for_loan))
            } else {
                setToolbarTitle(getString(R.string.update_loan))
                loanWithAssociations = arguments?.getCheckedParcelable(
                    LoanWithAssociations::class.java,
                    Constants.LOAN_ACCOUNT
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddLoanApplicationBinding.inflate(inflater, container, false)
        sweetUIErrorHandler = SweetUIErrorHandler(activity, binding.root)
        showUserInterface()
        if (savedInstanceState == null) {
            loadLoanTemplate()
        }
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(Constants.TEMPLATE, loanTemplate)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            val template: LoanTemplate? = savedInstanceState.getCheckedParcelable(
                LoanTemplate::class.java,
                Constants.TEMPLATE
            )
            if (loanState == LoanState.CREATE) {
                showLoanTemplate(template)
            } else {
                showUpdateLoanTemplate(template)
            }
        }
    }

    /**
     * Loads [LoanTemplate] according to the `loanState`
     */
    private fun loadLoanTemplate() {
        if (loanState == LoanState.CREATE) {
            viewModel.loadLoanApplicationTemplate(LoanState.CREATE)
        } else {
            viewModel.loadLoanApplicationTemplate(LoanState.UPDATE)
        }
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

                        is LoanUiState.ShowLoanTemplate -> {
                            hideProgress()
                            showLoanTemplateByProduct(it.template)
                        }

                        is LoanUiState.ShowUpdateLoanTemplate -> {
                            hideProgress()
                            showUpdateLoanTemplateByProduct(it.template)
                        }

                        is LoanUiState.ShowLoanTemplateByProduct -> {
                            hideProgress()
                            showLoanTemplate(it.template)
                        }

                        is LoanUiState.ShowUpdateLoanTemplateByProduct -> {
                            hideProgress()
                            showUpdateLoanTemplate(it.template)
                        }

                        else -> throw IllegalStateException("Unexpected state: $it")
                    }
                }
            }
        }

        with(binding) {
            btnLoanReview.setOnClickListener {
                onReviewLoanApplication()
            }

            expectedDisbursementDateEdit.setOnClickListener {
                setTvDisbursementOnDate()
            }

            binding.layoutError.btnTryAgain.setOnClickListener {
                onRetry()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? BaseActivity)?.showToolbar()
    }

    /**
     * Calls function which applies for a new Loan Application or updates a Loan Application
     * according to `loanState`
     */
    private fun onReviewLoanApplication() {
        with(binding) {
            if (loanProductsField.text.toString().isEmpty()) {
                Toast.makeText(
                    activity,
                    getString(R.string.select_loan_product_field),
                    Toast.LENGTH_SHORT,
                ).show()
                return
            }
            if (tilPrincipalAmount.editText?.text.toString() == "") {
                tilPrincipalAmount.error = getString(R.string.enter_amount)
                return
            }
            if (tilPrincipalAmount.editText?.text.toString() == ".") {
                tilPrincipalAmount.error = getString(R.string.invalid_amount)
                return
            }
            if (tilPrincipalAmount.editText?.text.toString().matches("^0*".toRegex())) {
                tilPrincipalAmount.error = getString(R.string.amount_greater_than_zero)
                return
            }
            tilPrincipalAmount.error = null
            if (loanState == LoanState.CREATE) {
                reviewNewLoanApplication()
            } else {
                submitUpdateLoanApplication()
            }
        }
    }

    /**
     * Submits a New Loan Application to the server
     */
    private fun reviewNewLoanApplication() {
        with(binding) {
            val loansPayload = LoansPayload()
            loansPayload.clientId = loanTemplate?.clientId
            loansPayload.loanPurpose = loanPurposeField.text.toString()
            loansPayload.productName = loanProductsField.text.toString()
            loansPayload.currency = tvCurrency.text.toString()
            if (purposeId != null && purposeId!! > 0) loansPayload.loanPurposeId = purposeId
            loansPayload.productId = productId
            loansPayload.principal = tilPrincipalAmount.editText?.text.toString().toDouble()
            loansPayload.loanTermFrequency = loanTemplate?.termFrequency
            loansPayload.loanTermFrequencyType = loanTemplate?.interestRateFrequencyType?.id
            loansPayload.loanType = "individual"
            loansPayload.numberOfRepayments = loanTemplate?.numberOfRepayments
            loansPayload.repaymentEvery = loanTemplate?.repaymentEvery
            loansPayload.repaymentFrequencyType = loanTemplate?.interestRateFrequencyType?.id
            loansPayload.interestRatePerPeriod = loanTemplate?.interestRatePerPeriod
            loansPayload.expectedDisbursementDate = disbursementDate
            loansPayload.submittedOnDate = submittedDate
            loansPayload.transactionProcessingStrategyId =
                loanTemplate?.transactionProcessingStrategyId
            loansPayload.amortizationType = loanTemplate?.amortizationType?.id
            loansPayload.interestCalculationPeriodType =
                loanTemplate?.interestCalculationPeriodType?.id
            loansPayload.interestType = loanTemplate?.interestType?.id
            (activity as BaseActivity?)?.replaceFragment(
                newInstance(
                    loanState!!,
                    loansPayload,
                    tvNewLoanApplication.text.toString(),
                    tvAccountNumber.text.toString(),
                ),
                true,
                R.id.container,
            )
        }
    }

    /**
     * Requests server to update the Loan Application with new values
     */
    private fun submitUpdateLoanApplication() {
        with(binding) {
            val loansPayload = LoansPayload()
            loansPayload.principal = tilPrincipalAmount.editText?.text.toString().toDouble()
            loansPayload.productId = productId
            loansPayload.loanPurpose = loanPurposeField.text.toString()
            loansPayload.productName = loanProductsField.text.toString()
            loansPayload.currency = tvCurrency.text.toString()
            if (purposeId != null && purposeId!! > 0) loansPayload.loanPurposeId = purposeId
            loansPayload.loanTermFrequency = loanTemplate?.termFrequency
            loansPayload.loanTermFrequencyType = loanTemplate?.interestRateFrequencyType?.id
            loansPayload.numberOfRepayments = loanTemplate?.numberOfRepayments
            loansPayload.repaymentEvery = loanTemplate?.repaymentEvery
            loansPayload.repaymentFrequencyType = loanTemplate?.interestRateFrequencyType?.id
            loansPayload.interestRatePerPeriod = loanTemplate?.interestRatePerPeriod
            loansPayload.interestType = loanTemplate?.interestType?.id
            loansPayload.interestCalculationPeriodType =
                loanTemplate?.interestCalculationPeriodType?.id
            loansPayload.amortizationType = loanTemplate?.amortizationType?.id
            loansPayload.transactionProcessingStrategyId =
                loanTemplate?.transactionProcessingStrategyId
            loansPayload.expectedDisbursementDate = disbursementDate
            (activity as BaseActivity?)?.replaceFragment(
                newInstance(
                    loanState,
                    loansPayload,
                    loanWithAssociations?.id?.toLong(),
                    tvNewLoanApplication.text.toString(),
                    tvAccountNumber.text.toString(),
                ),
                true,
                R.id.container,
            )
        }
    }

    /**
     * Retries to fetch [LoanTemplate] by calling `loadLoanTemplate()`
     */
    private fun onRetry() {
        if (Network.isConnected(context)) {
            sweetUIErrorHandler?.hideSweetErrorLayoutUI(
                binding.viewFlipper,
                binding.layoutError.root,
            )
            loadLoanTemplate()
        } else {
            Toaster.show(binding.viewFlipper, getString(R.string.internet_not_connected))
        }

    }

    /**
     * Initializes `tvSubmissionDate` with current Date
     */
    private fun inflateSubmissionDate() {
        binding.tvSubmissionDate.text = getTodayFormatted()
    }

    /**
     * Initializes `tvExpectedDisbursementDate` with current Date
     */
    private fun inflateDisbursementDate() {
        binding.tvExpectedDisbursementDate.text = getTodayFormatted()
    }

    /**
     * Sets `submittedDate` and `disbursementDate` in a specific format
     */
    private fun setSubmissionDisburseDate() {
        disbursementDate = binding.tvExpectedDisbursementDate.text.toString()
        submittedDate = binding.tvSubmissionDate.text.toString()
        submittedDate = DateHelper.getSpecificFormat(DateHelper.FORMAT_dd_MMMM_yyyy, submittedDate)
        disbursementDate = DateHelper.getSpecificFormat(
            DateHelper.FORMAT_dd_MMMM_yyyy,
            disbursementDate,
        )
    }

    /**
     * Shows a [DialogFragment] for selecting a Date for Disbursement
     */
    private fun setTvDisbursementOnDate() {
        isDisbursementDate = true
        datePickerDialog.show(requireActivity().supportFragmentManager, Constants.DFRAG_DATE_PICKER)
    }

    /**
     * Initializes the layout
     */
    fun showUserInterface() {
        with(binding) {
            loanProductsField.setSimpleItems(listLoanProducts.toTypedArray())
            loanPurposeField.setSimpleItems(listLoanPurpose.toTypedArray())

            loanProductsField.setOnItemClickListener { _, _, position, _ ->
                println("loan_products_field clicked")
                productId = loanTemplate?.productOptions?.get(position)?.id
                viewModel.loadLoanApplicationTemplateByProduct(productId, loanState)
                loanPurposeFieldParent.isEnabled = true
            }
            loanPurposeField.setOnItemClickListener { _, _, position, _ ->
                println("loan_purpose_field clicked")
                loanTemplate?.loanPurposeOptions?.let {
                    if (it.size > position) {
                        purposeId = it[position].id
                    }
                }
            }
        }
        inflateSubmissionDate()
        inflateDisbursementDate()
        setSubmissionDisburseDate()
    }

    /**
     * Fetches the [LoanTemplate] from server for `loanState` as CREATE
     *
     * @param loanTemplate Template for Loan Application
     */
    private fun showLoanTemplate(loanTemplate: LoanTemplate?) {
        this.loanTemplate = loanTemplate
        if (loanTemplate?.productOptions != null) {
            for ((_, name) in loanTemplate.productOptions) {
                if (!listLoanProducts.contains(name)) {
                    listLoanProducts.add(name)
                }
            }
        }
        binding.loanProductsField.setSimpleItems(listLoanProducts.toTypedArray())
    }

    /**
     * Fetches the [LoanTemplate] from server for `loanState` as UPDATE
     *
     * @param loanTemplate Template for Loan Application
     */
    private fun showUpdateLoanTemplate(loanTemplate: LoanTemplate?) {
        this.loanTemplate = loanTemplate
        if (loanTemplate?.productOptions != null) {
            for ((_, name) in loanTemplate.productOptions) {
                if (!listLoanProducts.contains(name)) {
                    listLoanProducts.add(name)
                }
            }
        }
        with(binding) {
            loanProductsField.setSimpleItems(listLoanProducts.toTypedArray())
            loanProductsField.setText(loanWithAssociations?.loanProductName!!, false)

            tvAccountNumber.text = getString(
                R.string.string_and_string,
                getString(R.string.account_number) + " ",
                loanWithAssociations?.accountNo,
            )
            tvNewLoanApplication.text = getString(
                R.string.string_and_string,
                getString(R.string.update_loan_application) + " ",
                loanWithAssociations?.clientName,
            )
            tilPrincipalAmount.editText?.setText(
                String.format(
                    Locale.getDefault(),
                    "%.2f",
                    loanWithAssociations?.principal,
                ),
            )
            tvCurrency.text = loanWithAssociations?.currency?.displayLabel
            tvSubmissionDate.text = DateHelper.getDateAsString(
                loanWithAssociations?.timeline?.submittedOnDate,
                "dd-MM-yyyy",
            )
            tvExpectedDisbursementDate.text = DateHelper.getDateAsString(
                loanWithAssociations?.timeline?.expectedDisbursementDate,
                "dd-MM-yyyy",
            )
        }
        setSubmissionDisburseDate()
    }

    /**
     * Fetches the [LoanTemplate] according to product from server for `loanState` as
     * CREATE
     *
     * @param loanTemplate Template for Loan Application
     */
    private fun showLoanTemplateByProduct(loanTemplate: LoanTemplate?) {
        this.loanTemplate = loanTemplate
        with(binding) {
            tvAccountNumber.text = getString(
                R.string.string_and_string,
                getString(R.string.account_number) + " ",
                loanTemplate?.clientAccountNo,
            )
            tvNewLoanApplication.text = getString(
                R.string.string_and_string,
                getString(R.string.new_loan_application) + " ",
                loanTemplate?.clientName,
            )
            tilPrincipalAmount.editText?.setText(
                String.format(
                    Locale.getDefault(),
                    "%.2f", loanTemplate?.principal
                ),
            )
            tvCurrency.text = loanTemplate?.currency?.displayLabel
            listLoanPurpose.clear()
            listLoanPurpose.add(activity?.getString(R.string.loan_purpose_not_provided))
            if (loanTemplate?.loanPurposeOptions != null) {
                for (loanPurposeOptions in loanTemplate.loanPurposeOptions) {
                    listLoanPurpose.add(loanPurposeOptions.name)
                }
            }
            loanPurposeField.setSimpleItems(listLoanPurpose.toTypedArray())
            loanPurposeField.setText(listLoanPurpose[0]!!, false)
        }
    }

    /**
     * Fetches the [LoanTemplate] according to product from server for `loanState` as
     * UPDATE
     *
     * @param loanTemplate Template for Loan Application
     */
    private fun showUpdateLoanTemplateByProduct(loanTemplate: LoanTemplate?) {
        this.loanTemplate = loanTemplate
        listLoanPurpose.clear()
        listLoanPurpose.add(activity?.getString(R.string.loan_purpose_not_provided))
        if (loanTemplate?.loanPurposeOptions != null) {
            for (loanPurposeOptions in loanTemplate.loanPurposeOptions) {
                listLoanPurpose.add(loanPurposeOptions.name)
            }
        }
        with(binding) {
            loanPurposeField.setSimpleItems(listLoanPurpose.toTypedArray())
            loanPurposeField.setText(listLoanPurpose[0]!!, false)
            if (isLoanUpdatePurposesInitialization &&
                loanWithAssociations?.loanPurposeName != null
            ) {
                loanPurposeField.setText(loanWithAssociations?.loanPurposeName!!, false)
                isLoanUpdatePurposesInitialization = false
            } else {
                tvAccountNumber.text = getString(
                    R.string.string_and_string,
                    getString(R.string.account_number) + " ",
                    loanTemplate?.clientAccountNo,
                )
                tvNewLoanApplication.text = getString(
                    R.string.string_and_string,
                    getString(R.string.new_loan_application) + " ",
                    loanTemplate?.clientName,
                )
                tilPrincipalAmount.editText?.setText(
                    String.format(
                        Locale.getDefault(),
                        "%.2f", loanTemplate?.principal
                    ),
                )
                tvCurrency.text = loanTemplate?.currency?.displayLabel
            }
        }
    }

    /**
     * It is called whenever any error occurs while executing a request
     *
     * @param message Error message that tells the user about the problem.
     */
    fun showError(message: String?) {
        if (!Network.isConnected(context)) {
            sweetUIErrorHandler?.showSweetNoInternetUI(binding.viewFlipper, binding.layoutError.root)
        } else {
            sweetUIErrorHandler?.showSweetErrorUI(
                message,
                binding.viewFlipper,
                binding.layoutError.root,
            )
            Toaster.show(binding.viewFlipper, message)
        }
        hideProgress()
    }

    fun showProgress() {
        binding.llAddLoan.visibility = View.GONE
        showProgressBar()
    }

    fun hideProgress() {
        binding.llAddLoan.visibility = View.VISIBLE
        hideProgressBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideProgressBar()
        _binding = null
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
            args.putSerializable(Constants.LOAN_STATE, loanState)
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
            loanState: LoanState?,
            loanWithAssociations: LoanWithAssociations?,
        ): LoanApplicationFragment {
            val fragment = LoanApplicationFragment()
            val args = Bundle()
            args.putSerializable(Constants.LOAN_STATE, loanState)
            args.putParcelable(Constants.LOAN_ACCOUNT, loanWithAssociations)
            fragment.arguments = args
            return fragment
        }
    }
}
