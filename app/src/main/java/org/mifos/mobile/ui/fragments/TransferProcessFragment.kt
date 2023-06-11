package org.mifos.mobile.ui.fragments

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentTransferProcessBinding
import org.mifos.mobile.models.payload.TransferPayload
import org.mifos.mobile.presenters.TransferProcessPresenter
import org.mifos.mobile.ui.activities.SavingsAccountContainerActivity
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.enums.TransferType
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.views.TransferProcessView
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.CurrencyUtil
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.Toaster
import javax.inject.Inject


/**
 * Created by dilpreet on 1/7/17.
 */
class TransferProcessFragment : BaseFragment(), TransferProcessView {

    private var _binding: FragmentTransferProcessBinding? = null
    private val binding get() = _binding!!

    @kotlin.jvm.JvmField
    @Inject
    var presenter: TransferProcessPresenter? = null
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransferProcessBinding.inflate(inflater,container,false)
        (activity as BaseActivity?)?.activityComponent?.inject(this)
        setToolbarTitle(getString(R.string.transfer))
        presenter?.attachView(this)
        binding.tvAmount.text = CurrencyUtil.formatCurrency(activity, payload?.transferAmount)
        binding.tvPayFrom.text = payload?.fromAccountNumber.toString()
        binding.tvPayTo.text = payload?.toAccountNumber.toString()
        binding.tvDate.text = payload?.transferDate
        binding.tvRemark.text = payload?.transferDescription

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStartTransfer.setOnClickListener {
            startTransfer()
        }
        binding.btnCancelTransfer.setOnClickListener {
            cancelTransferProcess()
        }
        binding.btnClose.setOnClickListener {
            closeClicked()
        }
    }

    /**
     * Initiates a transfer depending upon `transferType`
     */
    fun startTransfer() {
        if (!Network.isConnected(activity)) {
            Toaster.show(binding.root, getString(R.string.internet_not_connected))
            return
        }
        if (transferType == TransferType.SELF) {
            presenter?.makeSavingsTransfer(payload)
        } else if (transferType == TransferType.TPT) {
            presenter?.makeTPTTransfer(payload)
        }
    }

    /**
     * Cancels the Transfer and pops fragment
     */
    fun cancelTransferProcess() {
        Toaster.cancelTransfer(binding.root, getString(R.string.cancel_transfer),
            getString(R.string.yes), View.OnClickListener {
                activity?.supportFragmentManager?.popBackStack()
                activity?.supportFragmentManager?.popBackStack()
            })
    }

    /**
     * Closes the transfer fragment
     */
    fun closeClicked() {
        activity?.supportFragmentManager?.popBackStack()
        activity?.supportFragmentManager?.popBackStack()
    }

    /**
     * Shows a {@link Snackbar} on succesfull transfer of money
     */
    override fun showTransferredSuccessfully() {
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
    override fun showError(msg: String?) {
        Toaster.show(binding.root, msg)
    }

    override fun showProgress() {
        showMifosProgressDialog(getString(R.string.please_wait))
    }

    override fun hideProgress() {
        hideMifosProgressDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.detachView()
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