package org.mifos.mobile.ui.fragments

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_client_charge.view.*
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentClientChargeBinding
import org.mifos.mobile.models.Charge
import org.mifos.mobile.ui.adapters.ClientChargeAdapter
import org.mifos.mobile.ui.enums.ChargeType
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.ClientChargeUiState
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.Toaster
import org.mifos.mobile.viewModels.ClientChargeViewModel
import java.util.*

/**
 * @author Vishwajeet
 * @since 17/8/16.
 */
@AndroidEntryPoint
class ClientChargeFragment : BaseFragment() {

    private var _binding: FragmentClientChargeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ClientChargeViewModel

    private var clientChargeAdapter: ClientChargeAdapter? = null
    private var id: Long? = 0
    private var chargeType: ChargeType? = null
    private var layoutManager: LinearLayoutManager? = null
    private var clientChargeList: List<Charge?>? = ArrayList()
    private var sweetUIErrorHandler: SweetUIErrorHandler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            id = arguments?.getLong(Constants.CLIENT_ID)
            chargeType = arguments?.getSerializable(Constants.CHARGE_TYPE) as ChargeType
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentClientChargeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ClientChargeViewModel::class.java]
        clientChargeAdapter = ClientChargeAdapter(::onItemClick)
        setToolbarTitle(getString(R.string.charges))
        sweetUIErrorHandler = SweetUIErrorHandler(activity, binding.root)
        layoutManager = LinearLayoutManager(activity)
        layoutManager?.orientation = LinearLayoutManager.VERTICAL
        binding.rvClientCharge.layoutManager = layoutManager
        binding.swipeChargeContainer.setColorSchemeResources(
            R.color.blue_light,
            R.color.green_light,
            R.color.orange_light,
            R.color.red_light,
        )
        binding.swipeChargeContainer.setOnRefreshListener { loadCharges() }
        if (savedInstanceState == null) {
            loadCharges()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.clientChargeUiState.observe(viewLifecycleOwner) {
            when (it) {
                is ClientChargeUiState.Loading -> showProgress()
                is ClientChargeUiState.ShowError -> {
                    hideProgress()
                    showErrorFetchingClientCharges(getString(it.message))
                }
                is ClientChargeUiState.ShowClientCharges -> {
                    hideProgress()
                    showClientCharges(it.charges)
                }
            }
        }

        binding.layoutError.btnTryAgain.setOnClickListener {
            retryClicked()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(
            Constants.CHARGES,
            ArrayList<Parcelable>(
                clientChargeList,
            ),
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            val charges: List<Charge?> =
                savedInstanceState.getParcelableArrayList(Constants.CHARGES) ?: listOf()
            showClientCharges(charges)
        }
    }

    /**
     * Fetches Charges for `id` according to `chargeType` provided.
     */
    private fun loadCharges() {
        if (binding.root.layout_error?.visibility == View.VISIBLE) {
            sweetUIErrorHandler?.hideSweetErrorLayoutUI(
                binding.rvClientCharge,
                binding.root.layout_error
            )
        }
        when (chargeType) {
            ChargeType.CLIENT -> id?.let { viewModel.loadClientCharges(it) }
            ChargeType.SAVINGS -> id?.let { viewModel.loadSavingsAccountCharges(it) }
            ChargeType.LOAN -> id?.let { viewModel.loadLoanAccountCharges(it) }
        }
    }

    /**
     * It is called whenever any error occurs while executing a request. If not connected to
     * internet then it shows display a message to user to connect to internet other it just
     * displays the `message` in a {@link Snackbar}
     *
     * @param message Error message that tells the user about the problem.
     */
    fun showErrorFetchingClientCharges(message: String?) {
        if (!Network.isConnected(activity)) {
            sweetUIErrorHandler?.showSweetNoInternetUI(
                binding.rvClientCharge,
                binding.root.layout_error
            )
        } else {
            sweetUIErrorHandler?.showSweetErrorUI(
                message,
                binding.rvClientCharge,
                binding.root.layout_error
            )
            Toaster.show(binding.root, message)
        }
    }

    /**
     * Tries to fetch charges again.
     */
    fun retryClicked() {
        if (Network.isConnected(context)) {
            sweetUIErrorHandler?.hideSweetErrorLayoutUI(
                binding.rvClientCharge,
                binding.root.layout_error
            )
            loadCharges()
        } else {
            Toast.makeText(
                context,
                getString(R.string.internet_not_connected),
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    fun showClientCharges(clientChargeList: List<Charge?>?) {
        this.clientChargeList = clientChargeList
        inflateClientChargeList()
        if (binding.swipeChargeContainer.isRefreshing) {
            binding.swipeChargeContainer.isRefreshing = false
        }
    }

    /**
     * Updates `clientChargeAdapter` with updated `clientChargeList` if
     * `clientChargeList` size if greater than 0 else shows the error layout
     */
    private fun inflateClientChargeList() {
        if (clientChargeList?.isNotEmpty() == true) {
            clientChargeAdapter?.setClientChargeList(clientChargeList)
            binding.rvClientCharge.adapter = clientChargeAdapter
        } else {
            sweetUIErrorHandler?.showSweetEmptyUI(
                getString(R.string.charges),
                R.drawable.ic_charges,
                binding.rvClientCharge,
                binding.root.layout_error,
            )
        }
    }

    fun showProgress() {
        binding.swipeChargeContainer?.isRefreshing = true
    }

    fun hideProgress() {
        binding.swipeChargeContainer.isRefreshing = false
    }

    fun onItemClick(position: Int) {}
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(clientId: Long?, chargeType: ChargeType?): ClientChargeFragment {
            val clientChargeFragment = ClientChargeFragment()
            val args = Bundle()
            if (clientId != null) {
                args.putLong(Constants.CLIENT_ID, clientId)
            }
            args.putSerializable(Constants.CHARGE_TYPE, chargeType)
            clientChargeFragment.arguments = args
            return clientChargeFragment
        }
    }
}
