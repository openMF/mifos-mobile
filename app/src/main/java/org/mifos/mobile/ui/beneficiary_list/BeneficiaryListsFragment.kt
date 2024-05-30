package org.mifos.mobile.ui.beneficiary_list

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.BeneficiaryDetailFragment
import org.mifos.mobile.ui.fragments.BeneficiaryListFragment
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.ParcelableAndSerializableUtils.getCheckedArrayListFromParcelable
import org.mifos.mobile.viewModels.BeneficiaryListViewModel

@AndroidEntryPoint
class BeneficiaryListsFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
    private var sweetUIErrorHandler: SweetUIErrorHandler? = null
    private val viewModel: BeneficiaryListViewModel by viewModels()
    private var beneficiaryList: List<Beneficiary?>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MifosMobileTheme {
                    BeneficiaryListScreen(
                        beneficiaryList = beneficiaryList,
                        sweetUIErrorHandler = sweetUIErrorHandler,
                        navigateBack = { activity?.supportFragmentManager?.popBackStack() },
                        addBeneficiaryClicked = {}
                        )
                }
            }
        }
    }



    override fun onRefresh() {

        viewModel.loadBeneficiaries()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (beneficiaryList != null) {
            outState.putParcelableArrayList(
                Constants.BENEFICIARY,
                ArrayList<Parcelable?>(
                    beneficiaryList,
                ),
            )
        }
    }

    private fun onItemClick(position: Int) {
        (activity as BaseActivity?)?.replaceFragment(
            BeneficiaryDetailFragment.newInstance(
                beneficiaryList!![position],
            ),
            true,
            R.id.container,
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            val beneficiaries: List<Beneficiary?> =
                savedInstanceState.getCheckedArrayListFromParcelable(
                    Beneficiary::class.java,
                    Constants.BENEFICIARY
                ) ?: listOf()
//            showBeneficiaryList(beneficiaries)
        }
    }

//
//    private fun retryClicked() {
//        if (Network.isConnected((context?.applicationContext)!!)) {
//            sweetUIErrorHandler?.hideSweetErrorLayoutUI(
////                binding.rvBeneficiaries,
////                binding.layoutError.root,
//            )
//            viewModel.loadBeneficiaries()
//        } else {
//            Toast.makeText(
//                context,
//                getString(R.string.internet_not_connected),
//                Toast.LENGTH_SHORT,
//            ).show()
//        }
//    }

    override fun onResume() {
        super.onResume()
        (activity as? BaseActivity)?.hideToolbar()
        viewModel.loadBeneficiaries()
    }

    companion object {
        fun newInstance(): BeneficiaryListsFragment {
            return BeneficiaryListsFragment()
        }
    }
}