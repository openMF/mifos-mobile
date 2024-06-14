package org.mifos.mobile.ui.transfer_process

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.models.payload.TransferPayload
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.enums.TransferType
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.ParcelableAndSerializableUtils.getCheckedParcelable
import org.mifos.mobile.utils.ParcelableAndSerializableUtils.getCheckedSerializable

/**
 * Created by dilpreet on 1/7/17.
 */
@AndroidEntryPoint
class TransferProcessComposeFragment : BaseFragment() {

    private val viewModel: TransferProcessViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity != null) {
            arguments?.getCheckedParcelable(TransferPayload::class.java, Constants.PAYLOAD)?.let {
                viewModel.setContent(it)
            }
            (arguments?.getCheckedSerializable(TransferType::class.java, Constants.TRANSFER_TYPE) as TransferType).let {
                viewModel.setTransferType(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? BaseActivity)?.hideToolbar()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return mifosComposeView(requireContext()) {
            TransferProcessScreen(
                navigateBack = { activity?.supportFragmentManager?.popBackStack() },
            )
        }
    }

    companion object {
        /**
         * Used for TPT Transfer and own Account Transfer.<br></br>
         * Use `type` as TransferType.TPT for TPT and TransferType.SELF for self Account Transfer
         *
         * @param payload Transfer Information
         * @param type    enum of [TransferType]
         * @return Instance of [TransferProcessComposeFragment]
         */
        fun newInstance(
            payload: TransferPayload?,
            type: TransferType?
        ): TransferProcessComposeFragment {
            val fragment = TransferProcessComposeFragment()
            val args = Bundle()
            args.putParcelable(Constants.PAYLOAD, payload)
            args.putSerializable(Constants.TRANSFER_TYPE, type)
            fragment.arguments = args
            return fragment
        }
    }
}
