package org.mifos.mobile.ui.guarantor_details
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.databinding.FragmentGuarantorDetailBinding
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.enums.GuarantorState
import org.mifos.mobile.ui.fragments.AddGuarantorFragment
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.loan_review.ReviewLoanApplicationScreen
import org.mifos.mobile.utils.*
import org.mifos.mobile.utils.ParcelableAndSerializableUtils.getCheckedParcelable
import org.mifos.mobile.utils.RxBus.listen
import org.mifos.mobile.utils.RxBus.publish
import org.mifos.mobile.utils.RxEvent.DeleteGuarantorEvent
import org.mifos.mobile.utils.RxEvent.UpdateGuarantorEvent

/*
* Created by saksham on 24/July/2018
*/
@AndroidEntryPoint
class GuarantorDetailComposeFragment : BaseFragment() {

    private val viewModel: GuarantorDetailViewModel by viewModels()

    var disposableUpdateGuarantor: Disposable? = null
    var isFirstTime = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            viewModel.setGuarantorData(arguments?.getCheckedParcelable(GuarantorPayload::class.java, Constants.GUARANTOR_DETAILS))
            viewModel.setLoanId(arguments?.getLong(Constants.LOAN_ID))
            viewModel.setIndex(arguments?.getInt(Constants.INDEX))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (isFirstTime) {
            isFirstTime = false
            setUpRxBus()
        }
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MifosMobileTheme {
                    GuarantorDetailScreen(
                        navigateBack = { activity?.supportFragmentManager?.popBackStack() },
                        updateGuarantor = { updateGuarantor() }
                    )
                }
            }
        }
    }

    private fun updateGuarantor() {
        (activity as BaseActivity?)?.replaceFragment(
            AddGuarantorFragment.newInstance(viewModel.index, GuarantorState.UPDATE, viewModel.guarantorUiData.value, viewModel.loanId),
            true,
            R.id.container,
        )
    }

    private fun setUpRxBus() {
        disposableUpdateGuarantor = listen(UpdateGuarantorEvent::class.java)
            .subscribe { (payload1) ->
                val payload = viewModel.guarantorUiData.value.copy(
                    guarantorType = payload1?.guarantorType,
                    firstname = payload1?.firstName,
                    lastname = payload1?.lastName,
                    officeName = payload1?.officeName,
                )
                viewModel.setGuarantorData(payload)
            }
    }

    override fun onResume() {
        super.onResume()
        (activity as? BaseActivity)?.hideToolbar()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (disposableUpdateGuarantor?.isDisposed == false) {
            disposableUpdateGuarantor?.dispose()
        }
    }

    companion object {
        fun newInstance(
            index: Int,
            loanId: Long,
            payload: GuarantorPayload?,
        ): GuarantorDetailComposeFragment {
            val fragment = GuarantorDetailComposeFragment()
            val args = Bundle()
            args.putLong(Constants.LOAN_ID, loanId)
            args.putParcelable(Constants.GUARANTOR_DETAILS, payload)
            args.putInt(Constants.INDEX, index)
            fragment.arguments = args
            return fragment
        }
    }
}
