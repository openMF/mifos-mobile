package org.mifos.mobile.ui.third_party_transfer

/*

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentThirdPartyTransferBinding
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.models.beneficiary.BeneficiaryDetail
import org.mifos.mobile.models.payload.AccountDetail
import org.mifos.mobile.models.payload.TransferPayload
import org.mifos.mobile.models.templates.account.AccountOption
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.adapters.AccountsSpinnerAdapter
import org.mifos.mobile.ui.adapters.BeneficiarySpinnerAdapter
import org.mifos.mobile.ui.beneficiary.presentation.BeneficiaryAddOptionsFragment
import org.mifos.mobile.ui.enums.TransferType
import org.mifos.mobile.ui.fragments.TransferProcessFragment
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.transfer_process.TransferProcessComposeFragment
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.DateHelper
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.ParcelableAndSerializableUtils.getCheckedArrayListFromParcelable
import org.mifos.mobile.utils.ParcelableAndSerializableUtils.getCheckedParcelable
import org.mifos.mobile.utils.ThirdPartyTransferUiState
import org.mifos.mobile.utils.Toaster
import org.mifos.mobile.utils.Utils
import org.mifos.mobile.utils.getTodayFormatted

/**
 * Created by dilpreet on 21/6/17.
 */
@AndroidEntryPoint
class ThirdPartyTransferFragment : BaseFragment(), OnItemSelectedListener {

    private var _binding: FragmentThirdPartyTransferBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ThirdPartyTransferViewModel by viewModels()

    private val listBeneficiary: MutableList<BeneficiaryDetail?> = ArrayList()
    private val listPayFrom: MutableList<AccountDetail> = ArrayList()
    private var beneficiaries: List<Beneficiary?>? = null
    private var beneficiaryAdapter: BeneficiarySpinnerAdapter? = null
    private var payFromAdapter: AccountsSpinnerAdapter? = null
    private var fromAccountOption: AccountOption? = null
    private var beneficiaryAccountOption: AccountOption? = null
    private var accountOptionsTemplate: AccountOptionsTemplate? = null
    private var transferDate: String? = null
    private var sweetUIErrorHandler: SweetUIErrorHandler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentThirdPartyTransferBinding.inflate(inflater, container, false)
        setToolbarTitle(getString(R.string.third_party_transfer))
        sweetUIErrorHandler = SweetUIErrorHandler(activity, binding.root)
        showUserInterface()
        if (savedInstanceState == null) {
            viewModel.loadTransferTemplate()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.thirdPartyTransferUiState.collect {
                    when (it) {
                        is ThirdPartyTransferUiState.Loading -> showProgress()
                        is ThirdPartyTransferUiState.Error -> {
                            hideProgress()
                            getString(it.message)
                        }

                        is ThirdPartyTransferUiState.ShowThirdPartyTransferTemplate -> {
                            hideProgress()
                            showThirdPartyTransferTemplate(it.accountOptionsTemplate)
                        }

                        is ThirdPartyTransferUiState.ShowBeneficiaryList -> {
                            hideProgress()
                            showBeneficiaryList(it.beneficiaries)
                        }

                        ThirdPartyTransferUiState.Initial -> {}
                    }
                }
            }
        }

        with(binding) {
            btnReviewTransfer.setOnClickListener {
                reviewTransfer()
            }

            btnPayFrom.setOnClickListener {
                payFromSelected()
            }

            btnPayTo.setOnClickListener {
                payToSelected()
            }

            btnAmount.setOnClickListener {
                amountSet()
            }

            btnCancelTransfer.setOnClickListener {
                cancelTransfer()
            }

            btnAddBeneficiary.setOnClickListener {
                addBeneficiary()
            }
            layoutError.btnTryAgain.setOnClickListener {
                onRetry()
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        accountOptionsTemplate?.let {
            outState.putParcelable(Constants.TEMPLATE, it)
        }
        beneficiaries?.let {
            outState.putParcelableArrayList(
                Constants.BENEFICIARY,
                ArrayList<Parcelable?>(it),
            )
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            showThirdPartyTransferTemplate(
                savedInstanceState.getCheckedParcelable(
                    AccountOptionsTemplate::class.java,
                    Constants.TEMPLATE
                )
            )
            val tempBeneficiaries: List<Beneficiary?> =
                savedInstanceState.getCheckedArrayListFromParcelable(
                    Beneficiary::class.java,
                    Constants.BENEFICIARY,
                ) ?: listOf()
            showBeneficiaryList(tempBeneficiaries)
        }
    }

    /**
     * Setting up basic components
     */
    fun showUserInterface() {
        payFromAdapter = activity?.applicationContext?.let {
            AccountsSpinnerAdapter(it, listPayFrom)
        }
        payFromAdapter?.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        binding.spPayFrom.adapter = payFromAdapter
        binding.spPayFrom.onItemSelectedListener = this
        beneficiaryAdapter = activity?.applicationContext?.let {
            BeneficiarySpinnerAdapter(
                it,
                R.layout.beneficiary_spinner_layout,
                listBeneficiary,
            )
        }
        beneficiaryAdapter?.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        binding.spBeneficiary.adapter = beneficiaryAdapter
        binding.spBeneficiary.onItemSelectedListener = this
        transferDate = DateHelper.getSpecificFormat(
            DateHelper.FORMAT_dd_MMMM_yyyy,
            getTodayFormatted(),
        )
        binding.processOne.setCurrentActive()
    }

    /**
     * Checks validation of `etRemark` and then opens [TransferProcessComposeFragment] for
     * initiating the transfer
     */
    private fun reviewTransfer() {
        if (binding.etAmount.text.toString() == "") {
            Toaster.show(binding.root, getString(R.string.enter_amount))
            return
        }
        if (binding.etAmount.text.toString() == ".") {
            Toaster.show(binding.root, getString(R.string.invalid_amount))
            return
        }
        if (binding.etRemark.text.toString().trim { it <= ' ' } == "") {
            Toaster.show(binding.root, getString(R.string.remark_is_mandatory))
            return
        }
        if (binding.spBeneficiary.selectedItem.toString() == binding.spPayFrom.selectedItem.toString()) {
            Toaster.show(binding.root, getString(R.string.error_same_account_transfer))
            return
        }
        val transferPayload = TransferPayload()
        transferPayload.fromAccountId = fromAccountOption?.accountId
        transferPayload.fromClientId = fromAccountOption?.clientId
        transferPayload.fromAccountNumber = fromAccountOption?.accountNo
        transferPayload.fromAccountType = fromAccountOption?.accountType?.id
        transferPayload.fromOfficeId = fromAccountOption?.officeId
        transferPayload.toOfficeId = beneficiaryAccountOption?.officeId
        transferPayload.toAccountId = beneficiaryAccountOption?.accountId
        transferPayload.toClientId = beneficiaryAccountOption?.clientId
        transferPayload.toAccountNumber = beneficiaryAccountOption?.accountNo
        transferPayload.toAccountType = beneficiaryAccountOption?.accountType?.id
        transferPayload.transferDate = transferDate
        transferPayload.transferAmount = binding.etAmount.text.toString().toDouble()
        transferPayload.transferDescription = binding.etRemark.text.toString()
        transferPayload.fromAccountNumber = fromAccountOption?.accountNo
        transferPayload.toAccountNumber = beneficiaryAccountOption?.accountNo
        (activity as BaseActivity?)?.replaceFragment(
            TransferProcessComposeFragment.newInstance(
                transferPayload,
                TransferType.TPT,
            ),
            true,
            R.id.container,
        )
    }

    /**
     * Shows a {@link Snackbar} with `message`
     *
     * @param msg String to be shown
     */
    private fun showToaster(msg: String?) {
        Toaster.show(binding.root, msg)
    }

    /**
     * Provides with `accountOptionsTemplate` fetched from server which is used to update
     * `listPayFrom`
     *
     * @param accountOptionsTemplate Template for account transfer
     */
    private fun showThirdPartyTransferTemplate(accountOptionsTemplate: AccountOptionsTemplate?) {
        this.accountOptionsTemplate = accountOptionsTemplate
        listPayFrom.clear()
        viewModel.getAccountNumbersFromAccountOptions(
            accountOptionsTemplate?.fromAccountOptions,
            context?.getString(R.string.account_type_loan)
        )
            .let { listPayFrom.addAll(it) }
        payFromAdapter?.notifyDataSetChanged()
    }

    /**
     * Provides with `beneficiaries` fetched from server which is used to update
     * `listBeneficiary`
     *
     * @param beneficiaries List of [Beneficiary] linked with user's account
     */
    private fun showBeneficiaryList(beneficiaries: List<Beneficiary?>?) {
        this.beneficiaries = beneficiaries
        listBeneficiary.clear()
        viewModel.getAccountNumbersFromBeneficiaries(beneficiaries)
            .let { listBeneficiary.addAll(it) }
        beneficiaryAdapter?.notifyDataSetChanged()
    }

    /**
     * Disables `spPayFrom` [Spinner] and sets `pvOne` to completed and make
     * `pvThree` pvTwo
     */
    private fun payFromSelected() {
        with(binding) {
            processOne.setCurrentCompleted()
            processTwo.setCurrentActive()
            btnPayFrom.visibility = View.GONE
            tvSelectBeneficary.visibility = View.GONE
            if (listBeneficiary.isNotEmpty()) {
                btnPayTo.visibility = View.VISIBLE
                spBeneficiary.visibility = View.VISIBLE
                spPayFrom.isEnabled = false
            } else {
                tvAddBeneficiaryMsg.visibility = View.VISIBLE
                btnAddBeneficiary.visibility = View.VISIBLE
            }
        }

    }

    /**
     * Checks validation of `spBeneficiary` [Spinner].<br></br>
     * Disables `spBeneficiary` [Spinner] and sets `pvTwo` to completed and make
     * `pvThree` active
     */
    private fun payToSelected() {

        with(binding) {
            if (spBeneficiary.selectedItem.toString() == binding.spPayFrom.selectedItem.toString()) {
                showToaster(getString(R.string.error_same_account_transfer))
                return
            }
            processTwo.setCurrentCompleted()
            processThree.setCurrentActive()
            btnPayTo.visibility = View.GONE
            tvSelectAmount.visibility = View.GONE
            etAmount.visibility = View.VISIBLE
            btnAmount.visibility = View.VISIBLE
            spBeneficiary.isEnabled = false
        }

    }

    /**
     * Checks validation of `etAmount` [EditText].<br></br>
     * Disables `etAmount` and sets `pvThree` to completed and make
     * `pvFour` active
     */
    private fun amountSet() {
        with(binding) {
            if (etAmount.text.toString() == "") {
                showToaster(getString(R.string.enter_amount))
                return
            }
            if (etAmount.text.toString() == ".") {
                showToaster(getString(R.string.invalid_amount))
                return
            }
            if (etAmount.text.toString().toDouble() == 0.0) {
                showToaster(getString(R.string.amount_greater_than_zero))
                return
            }
            processThree.setCurrentCompleted()
            processFour.setCurrentActive()
            btnAmount.visibility = View.GONE
            tvEnterRemark.visibility = View.GONE
            etRemark.visibility = View.VISIBLE
            llReview.visibility = View.VISIBLE
            etAmount.isEnabled = false
        }

    }

    private fun cancelTransfer() {
        activity?.supportFragmentManager?.popBackStack()
    }

    private fun addBeneficiary() {
        (activity as BaseActivity?)?.replaceFragment(
            BeneficiaryAddOptionsFragment.newInstance(),
            true,
            R.id.container,
        )
    }

    private fun onRetry() {
        if (Network.isConnected(context)) {
            sweetUIErrorHandler?.hideSweetErrorLayoutUI(
                binding.llMakeTransfer,
                binding.layoutError.root,
            )
            viewModel.loadTransferTemplate()
        } else {
            Toaster.show(binding.root, getString(R.string.internet_not_connected))
        }
    }

    /**
     * It is called whenever any error occurs while executing a request
     *
     * @param msg Error message that tells the user about the problem.
     */
    fun showError(msg: String?) {
        if (!Network.isConnected(context)) {
            sweetUIErrorHandler?.showSweetNoInternetUI(
                binding.llMakeTransfer,
                binding.layoutError.root,
            )
        } else {
            sweetUIErrorHandler?.showSweetErrorUI(
                msg,
                binding.llMakeTransfer,
                binding.layoutError.root,
            )
        }
    }

    fun showProgress() {
        binding.llMakeTransfer.visibility = View.GONE
        showProgressBar()
    }

    fun hideProgress() {
        hideProgressBar()
        binding.llMakeTransfer.visibility = View.VISIBLE
    }

    /**
     * Callback for `spPayFrom` and `spBeneficiary`
     */
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.sp_beneficiary ->
                beneficiaryAccountOption =
                    viewModel.searchAccount(
                        accountOptionsTemplate?.toAccountOptions,
                        beneficiaryAdapter?.getItem(position),
                    )

            R.id.sp_pay_from ->
                fromAccountOption =
                    accountOptionsTemplate?.fromAccountOptions?.get(position)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_transfer, menu)
        Utils.setToolbarIconColor(activity, menu, R.color.white)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_refresh_transfer) {
            val transaction = fragmentManager?.beginTransaction()
            val currFragment = activity?.supportFragmentManager
                ?.findFragmentById(R.id.container)
            if (currFragment != null) {
                transaction?.detach(currFragment)
                transaction?.attach(currFragment)
            }
            transaction?.commit()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideProgress()
        _binding = null
    }

    companion object {
        fun newInstance(): ThirdPartyTransferFragment {
            return ThirdPartyTransferFragment()
        }
    }
}

 */
