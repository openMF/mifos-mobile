package org.mifos.mobile.ui.fragments

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
import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentThirdPartyTransferBinding
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.models.beneficiary.BeneficiaryDetail
import org.mifos.mobile.models.payload.AccountDetail
import org.mifos.mobile.models.payload.TransferPayload
import org.mifos.mobile.models.templates.account.AccountOption
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
import org.mifos.mobile.presenters.ThirdPartyTransferPresenter
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.adapters.AccountsSpinnerAdapter
import org.mifos.mobile.ui.adapters.BeneficiarySpinnerAdapter
import org.mifos.mobile.ui.enums.TransferType
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.views.ThirdPartyTransferView
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.DateHelper
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.Toaster
import org.mifos.mobile.utils.Utils
import org.mifos.mobile.utils.getTodayFormatted
import javax.inject.Inject

/**
 * Created by dilpreet on 21/6/17.
 */
class ThirdPartyTransferFragment : BaseFragment(), ThirdPartyTransferView, OnItemSelectedListener {

    private var _binding: FragmentThirdPartyTransferBinding? = null
    private val binding get() = _binding!!

    @JvmField
    @Inject
    var presenter: ThirdPartyTransferPresenter? = null
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
        (activity as BaseActivity?)?.activityComponent?.inject(this)
        _binding = FragmentThirdPartyTransferBinding.inflate(inflater, container, false)
        setToolbarTitle(getString(R.string.third_party_transfer))
        sweetUIErrorHandler = SweetUIErrorHandler(activity, binding.root)
        showUserInterface()
        presenter?.attachView(this)
        if (savedInstanceState == null) {
            presenter?.loadTransferTemplate()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnReviewTransfer.setOnClickListener {
            reviewTransfer()
        }

        binding.btnPayFrom.setOnClickListener {
            payFromSelected()
        }

        binding.btnPayTo.setOnClickListener {
            payToSelected()
        }

        binding.btnAmount.setOnClickListener {
            amountSet()
        }

        binding.btnCancelTransfer.setOnClickListener {
            cancelTransfer()
        }

        binding.btnAddBeneficiary.setOnClickListener {
            addBeneficiary()
        }
        binding.layoutError.btnTryAgain.setOnClickListener {
            onRetry()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(Constants.TEMPLATE, accountOptionsTemplate)
        outState.putParcelableArrayList(
            Constants.BENEFICIARY,
            ArrayList<Parcelable?>(
                beneficiaries,
            ),
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            showThirdPartyTransferTemplate(savedInstanceState.getParcelable<Parcelable>(Constants.TEMPLATE) as AccountOptionsTemplate)
            val tempBeneficiaries: List<Beneficiary?> = savedInstanceState.getParcelableArrayList(
                Constants.BENEFICIARY,
            ) ?: listOf()
            showBeneficiaryList(tempBeneficiaries)
        }
    }

    /**
     * Setting up basic components
     */
    override fun showUserInterface() {
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
     * Checks validation of `etRemark` and then opens [TransferProcessFragment] for
     * initiating the transfer
     */
    fun reviewTransfer() {
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
            TransferProcessFragment.newInstance(
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
    override fun showToaster(msg: String?) {
        Toaster.show(binding.root, msg)
    }

    /**
     * Provides with `accountOptionsTemplate` fetched from server which is used to update
     * `listPayFrom`
     *
     * @param accountOptionsTemplate Template for account transfer
     */
    override fun showThirdPartyTransferTemplate(accountOptionsTemplate: AccountOptionsTemplate?) {
        this.accountOptionsTemplate = accountOptionsTemplate
        listPayFrom.clear()
        presenter?.getAccountNumbersFromAccountOptions(accountOptionsTemplate?.fromAccountOptions)
            ?.let { listPayFrom.addAll(it) }
        payFromAdapter?.notifyDataSetChanged()
    }

    /**
     * Provides with `beneficiaries` fetched from server which is used to update
     * `listBeneficiary`
     *
     * @param beneficiaries List of [Beneficiary] linked with user's account
     */
    override fun showBeneficiaryList(beneficiaries: List<Beneficiary?>?) {
        this.beneficiaries = beneficiaries
        listBeneficiary.clear()
        presenter?.getAccountNumbersFromBeneficiaries(beneficiaries)
            ?.let { listBeneficiary.addAll(it) }
        beneficiaryAdapter?.notifyDataSetChanged()
    }

    /**
     * Disables `spPayFrom` [Spinner] and sets `pvOne` to completed and make
     * `pvThree` pvTwo
     */
    fun payFromSelected() {
        binding.processOne.setCurrentCompleted()
        binding.processTwo.setCurrentActive()
        binding.btnPayFrom.visibility = View.GONE
        binding.tvSelectBeneficary.visibility = View.GONE
        if (listBeneficiary.isNotEmpty()) {
            binding.btnPayTo.visibility = View.VISIBLE
            binding.spBeneficiary.visibility = View.VISIBLE
            binding.spPayFrom.isEnabled = false
        } else {
            binding.tvAddBeneficiaryMsg.visibility = View.VISIBLE
            binding.btnAddBeneficiary.visibility = View.VISIBLE
        }
    }

    /**
     * Checks validation of `spBeneficiary` [Spinner].<br></br>
     * Disables `spBeneficiary` [Spinner] and sets `pvTwo` to completed and make
     * `pvThree` active
     */
    fun payToSelected() {
        if (binding.spBeneficiary.selectedItem.toString() == binding.spPayFrom.selectedItem.toString()) {
            showToaster(getString(R.string.error_same_account_transfer))
            return
        }
        binding.processTwo.setCurrentCompleted()
        binding.processThree.setCurrentActive()
        binding.btnPayTo.visibility = View.GONE
        binding.tvSelectAmount.visibility = View.GONE
        binding.etAmount.visibility = View.VISIBLE
        binding.btnAmount.visibility = View.VISIBLE
        binding.spBeneficiary.isEnabled = false
    }

    /**
     * Checks validation of `etAmount` [EditText].<br></br>
     * Disables `etAmount` and sets `pvThree` to completed and make
     * `pvFour` active
     */
    fun amountSet() {
        if (binding.etAmount.text.toString() == "") {
            showToaster(getString(R.string.enter_amount))
            return
        }
        if (binding.etAmount.text.toString() == ".") {
            showToaster(getString(R.string.invalid_amount))
            return
        }
        if (binding.etAmount.text.toString().toDouble() == 0.0) {
            showToaster(getString(R.string.amount_greater_than_zero))
            return
        }
        binding.processThree.setCurrentCompleted()
        binding.processFour.setCurrentActive()
        binding.btnAmount.visibility = View.GONE
        binding.tvEnterRemark.visibility = View.GONE
        binding.etRemark.visibility = View.VISIBLE
        binding.llReview.visibility = View.VISIBLE
        binding.etAmount.isEnabled = false
    }

    fun cancelTransfer() {
        activity?.supportFragmentManager?.popBackStack()
    }

    fun addBeneficiary() {
        (activity as BaseActivity?)?.replaceFragment(
            BeneficiaryAddOptionsFragment.newInstance(),
            true,
            R.id.container,
        )
    }

    fun onRetry() {
        if (Network.isConnected(context)) {
            sweetUIErrorHandler?.hideSweetErrorLayoutUI(
                binding.llMakeTransfer,
                binding.layoutError.root,
            )
            presenter?.loadTransferTemplate()
        } else {
            Toaster.show(binding.root, getString(R.string.internet_not_connected))
        }
    }

    /**
     * It is called whenever any error occurs while executing a request
     *
     * @param msg Error message that tells the user about the problem.
     */
    override fun showError(msg: String?) {
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

    override fun showProgress() {
        binding.llMakeTransfer.visibility = View.GONE
        showProgressBar()
    }

    override fun hideProgress() {
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
                    presenter?.searchAccount(
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
        presenter?.detachView()
        _binding = null
    }

    companion object {
        fun newInstance(): ThirdPartyTransferFragment {
            return ThirdPartyTransferFragment()
        }
    }
}
