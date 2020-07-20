package org.mifos.mobile.ui.fragments

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import org.mifos.mobile.R
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

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_amount)
    var tvAmount: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_pay_to)
    var tvPayTo: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_pay_from)
    var tvPayFrom: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_date)
    var tvDate: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_remark)
    var tvRemark: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.iv_success)
    var ivSuccess: ImageView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.ll_transfer)
    var llTransfer: LinearLayout? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.btn_close)
    var btnClose: AppCompatButton? = null

    @kotlin.jvm.JvmField
    @Inject
    var presenter: TransferProcessPresenter? = null
    private var rootView: View? = null
    private var payload: TransferPayload? = null
    private var transferType: TransferType? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity != null) {
            payload = arguments!!.getParcelable(Constants.PAYLOAD)
            transferType = arguments!!.getSerializable(Constants.TRANSFER_TYPE) as TransferType
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_transfer_process, container, false)
        (activity as BaseActivity?)!!.activityComponent!!.inject(this)
        setToolbarTitle(getString(R.string.transfer))
        ButterKnife.bind(this, rootView!!)
        presenter!!.attachView(this)
        tvAmount!!.text = CurrencyUtil.formatCurrency(activity, payload!!.transferAmount!!)
        tvPayFrom!!.text = payload!!.fromAccountNumber.toString()
        tvPayTo!!.text = payload!!.toAccountNumber.toString()
        tvDate!!.text = payload!!.transferDate
        tvRemark!!.text = payload!!.transferDescription
        return rootView
    }

    /**
     * Initiates a transfer depending upon `transferType`
     */
    @OnClick(R.id.btn_start_transfer)
    fun startTransfer() {
        if (!Network.isConnected(activity)) {
            Toaster.show(rootView, getString(R.string.internet_not_connected))
            return
        }
        if (transferType == TransferType.SELF) {
            presenter!!.makeSavingsTransfer(payload)
        } else if (transferType == TransferType.TPT) {
            presenter!!.makeTPTTransfer(payload)
        }
    }

    /**
     * Cancels the Transfer and pops fragment
     */
    @OnClick(R.id.btn_cancel_transfer)
    open fun cancelTransfer() {
        Toaster.cancelTransfer(rootView, getString(R.string.cancel_transfer),
                getString(R.string.yes), View.OnClickListener {
            activity!!.supportFragmentManager.popBackStack()
            activity!!.supportFragmentManager.popBackStack()
        })
    }

    /**
     * Closes the transfer fragment
     */
    @OnClick(R.id.btn_close)
    fun closeClicked() {
        activity!!.supportFragmentManager.popBackStack()
        activity!!.supportFragmentManager.popBackStack()
    }

    /**
     * Shows a [Snackbar] on succesfull transfer of money
     */
    override fun showTransferredSuccessfully() {
        Toaster.show(rootView, getString(R.string.transferred_successfully))
        ivSuccess!!.visibility = View.VISIBLE
        (ivSuccess!!.drawable as Animatable).start()
        btnClose!!.visibility = View.VISIBLE
        llTransfer!!.visibility = View.GONE
        SavingsAccountContainerActivity.transferSuccess = true
    }

    /**
     * It is called whenever any error occurs while executing a request
     *
     * @param msg Error message that tells the user about the problem.
     */
    override fun showError(msg: String?) {
        Toaster.show(rootView, msg)
    }

    override fun showProgress() {
        showMifosProgressDialog(getString(R.string.please_wait))
    }

    override fun hideProgress() {
        hideMifosProgressDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter!!.detachView()
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