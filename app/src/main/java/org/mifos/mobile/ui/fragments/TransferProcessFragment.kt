package org.mifos.mobile.ui.fragments

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentTransferProcessBinding
import org.mifos.mobile.models.payload.TransferPayload
import org.mifos.mobile.models.templates.account.AccountOption
import org.mifos.mobile.ui.activities.SavingsAccountContainerActivity
import org.mifos.mobile.ui.enums.TransferType
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.*
import org.mifos.mobile.viewModels.TransferProcessViewModel

/**
 * Created by dilpreet on 1/7/17.
 */
@AndroidEntryPoint
class TransferProcessFragment : BaseFragment() {

    private var _binding: FragmentTransferProcessBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TransferProcessViewModel

    private var toAccountOption: AccountOption? = null
    private var fromAccountOption: AccountOption? = null

    private var payload: TransferPayload? = null
    private var transferType: TransferType? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity != null) {
            payload = arguments?.getParcelable(Constants.PAYLOAD)
            transferType = arguments?.getSerializable(Constants.TRANSFER_TYPE) as TransferType
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTransferProcessBinding.inflate(inflater, container, false)
        setToolbarTitle(getString(R.string.transfer))
        viewModel = ViewModelProvider(this)[TransferProcessViewModel::class.java]
        with(binding) {
            tvAmount.text = CurrencyUtil.formatCurrency(activity, payload?.transferAmount)
            tvPayFrom.text = payload?.fromAccountNumber.toString()
            tvPayTo.text = payload?.toAccountNumber.toString()
            tvDate.text = payload?.transferDate
            tvRemark.text = payload?.transferDescription
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.transferUiState.observe(viewLifecycleOwner) {
            when (it) {
                is TransferUiState.Loading -> showProgress()
                is TransferUiState.TransferSuccess -> {
                    hideProgress()
                    showTransferredSuccessfully()
                }
                is TransferUiState.Error -> {
                    hideProgress()
                    showError(MFErrorParser.errorMessage(it.errorMessage))
                }
            }
        }

        with(binding) {
            btnStartTransfer.setOnClickListener {
                startTransfer()
            }
            btnCancelTransfer.setOnClickListener {
                cancelTransferProcess()
            }
            btnClose.setOnClickListener {
                closeClicked()
            }
        }
    }

    /**
     * Initiates a transfer depending upon `transferType`
     */
    private fun startTransfer() {
        if (!Network.isConnected(activity)) {
            Toaster.show(binding.root, getString(R.string.internet_not_connected))
            return
        }
        val fromOfficeId = fromAccountOption?.officeId
        val fromClientId = fromAccountOption?.clientId
        val fromAccountType = fromAccountOption?.accountType?.id
        val fromAccountId = fromAccountOption?.accountId
        val toOfficeId = toAccountOption?.officeId
        val toClientId = toAccountOption?.clientId
        val toAccountType = toAccountOption?.accountType?.id
        val toAccountId = toAccountOption?.accountId
        val transferDate = DateHelper.getSpecificFormat(
            DateHelper.FORMAT_dd_MMMM_yyyy,
            getTodayFormatted(),
        )
        val transferAmount = binding.tvAmount.text.toString().toDouble()
        val transferDescription = binding.tvRemark.text.toString()
        var dateFormat: String = "dd MMMM yyyy"
        var locale: String = "en"
        val fromAccountNumber = fromAccountOption?.accountNo
        val toAccountNumber = toAccountOption?.accountNo
        if (transferType == TransferType.SELF) {
            viewModel.makeSavingsTransfer(
                fromOfficeId,
                fromClientId,
                fromAccountType,
                fromAccountId,
                toOfficeId,
                toClientId,
                toAccountType,
                toAccountId,
                transferDate,
                transferAmount,
                transferDescription,
                dateFormat,
                locale,
                fromAccountNumber,
                toAccountNumber
            )
        } else if (transferType == TransferType.TPT) {
            val fromOfficeId = fromAccountOption?.officeId
            val fromClientId = fromAccountOption?.clientId
            val fromAccountType = fromAccountOption?.accountType?.id
            val fromAccountId = fromAccountOption?.accountId
            val toOfficeId = toAccountOption?.officeId
            val toClientId = toAccountOption?.clientId
            val toAccountType = toAccountOption?.accountType?.id
            val toAccountId = toAccountOption?.accountId
            val transferDate = DateHelper.getSpecificFormat(
                DateHelper.FORMAT_dd_MMMM_yyyy,
                getTodayFormatted(),
            )
            val transferAmount = binding.tvAmount.text.toString().toDouble()
            val transferDescription = binding.tvRemark.text.toString()
            var dateFormat: String = "dd MMMM yyyy"
            var locale: String = "en"
            val fromAccountNumber = fromAccountOption?.accountNo
            val toAccountNumber = toAccountOption?.accountNo
            viewModel.makeTPTTransfer(
                fromOfficeId,
                fromClientId,
                fromAccountType,
                fromAccountId,
                toOfficeId,
                toClientId,
                toAccountType,
                toAccountId,
                transferDate,
                transferAmount,
                transferDescription,
                dateFormat,
                locale,
                fromAccountNumber,
                toAccountNumber
            )
        }
    }

    /**
     * Cancels the Transfer and pops fragment
     */
    fun cancelTransferProcess() {
        Toaster.cancelTransfer(
            binding.root,
            getString(R.string.cancel_transfer),
            getString(R.string.yes),
            View.OnClickListener {
                activity?.supportFragmentManager?.popBackStack()
                activity?.supportFragmentManager?.popBackStack()
            },
        )
    }

    /**
     * Closes the transfer fragment
     */
    private fun closeClicked() {
        activity?.supportFragmentManager?.popBackStack()
        activity?.supportFragmentManager?.popBackStack()
    }

    /**
     * Shows a {@link Snackbar} on succesfull transfer of money
     */
    fun showTransferredSuccessfully() {
        Toaster.show(binding.root, getString(R.string.transferred_successfully))
        binding.ivSuccess.visibility = View.VISIBLE
        (binding.ivSuccess.drawable as Animatable).start()
        binding.btnClose.visibility = View.VISIBLE
        binding.llTransfer.visibility = View.GONE
        SavingsAccountContainerActivity.transferSuccess = true
    }

    /**
     * It is called whenever any error occurs while executing a request
     *
     * @param msg Error message that tells the user about the problem.
     */
    fun showError(msg: String?) {
        Toaster.show(binding.root, msg)
    }

    fun showProgress() {
        showMifosProgressDialog(getString(R.string.please_wait))
    }

    fun hideProgress() {
        hideMifosProgressDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Used for TPT Transfer and own Account Transfer.<br></br>
         * Use `type` as TransferType.TPT for TPT and TransferType.SELF for self Account Transfer
         *
         * @param payload Transfer Information
         * @param type    enum of [TransferType]
         * @return Instance of [TransferProcessFragment]
         */
        fun newInstance(payload: TransferPayload?, type: TransferType?): TransferProcessFragment {
            val fragment = TransferProcessFragment()
            val args = Bundle()
            args.putParcelable(Constants.PAYLOAD, payload)
            args.putSerializable(Constants.TRANSFER_TYPE, type)
            fragment.arguments = args
            return fragment
        }
    }
}
