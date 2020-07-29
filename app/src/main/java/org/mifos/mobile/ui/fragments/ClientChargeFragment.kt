package org.mifos.mobile.ui.fragments

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler
import org.mifos.mobile.R
import org.mifos.mobile.models.Charge
import org.mifos.mobile.presenters.ClientChargePresenter
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.adapters.ClientChargeAdapter
import org.mifos.mobile.ui.enums.ChargeType
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.views.ClientChargeView
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.RecyclerItemClickListener
import org.mifos.mobile.utils.Toaster
import java.util.*
import javax.inject.Inject


/**
 * @author Vishwajeet
 * @since 17/8/16.
 */
class ClientChargeFragment :
        BaseFragment(), RecyclerItemClickListener.OnItemClickListener, ClientChargeView {

    @kotlin.jvm.JvmField
    @BindView(R.id.rv_client_charge)
    var rvClientCharge: RecyclerView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.swipe_charge_container)
    var swipeChargeContainer: SwipeRefreshLayout? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.layout_error)
    var layoutError: View? = null

    @kotlin.jvm.JvmField
    @Inject
    var clientChargePresenter: ClientChargePresenter? = null

    @kotlin.jvm.JvmField
    @Inject
    var clientChargeAdapter: ClientChargeAdapter? = null
    private var id: Long? = 0
    private var chargeType: ChargeType? = null
    private var rootView: View? = null
    private var layoutManager: LinearLayoutManager? = null
    private var clientChargeList: List<Charge?>? = ArrayList()
    private var sweetUIErrorHandler: SweetUIErrorHandler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as BaseActivity?)?.activityComponent?.inject(this)
        if (arguments != null) {
            id = arguments?.getLong(Constants.CLIENT_ID)
            chargeType = arguments?.getSerializable(Constants.CHARGE_TYPE) as ChargeType
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_client_charge, container, false)
        ButterKnife.bind(this, rootView!!)
        clientChargePresenter?.attachView(this)
        setToolbarTitle(getString(R.string.charges))
        sweetUIErrorHandler = SweetUIErrorHandler(activity, rootView)
        layoutManager = LinearLayoutManager(activity)
        layoutManager?.orientation = LinearLayoutManager.VERTICAL
        rvClientCharge?.layoutManager = layoutManager
        rvClientCharge?.addOnItemTouchListener(RecyclerItemClickListener(activity, this))
        swipeChargeContainer?.setColorSchemeResources(R.color.blue_light, R.color.green_light, R.color.orange_light, R.color.red_light)
        swipeChargeContainer?.setOnRefreshListener { loadCharges() }
        if (savedInstanceState == null) {
            loadCharges()
        }
        return rootView
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(Constants.CHARGES, ArrayList<Parcelable>(
                clientChargeList))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            val charges: List<Charge?> = savedInstanceState.getParcelableArrayList(Constants.CHARGES)
            showClientCharges(charges)
        }
    }

    /**
     * Fetches Charges for `id` according to `chargeType` provided.
     */
    private fun loadCharges() {
        if (layoutError?.visibility == View.VISIBLE) {
            sweetUIErrorHandler?.hideSweetErrorLayoutUI(rvClientCharge, layoutError)
        }
        when (chargeType) {
            ChargeType.CLIENT -> id?.let { clientChargePresenter?.loadClientCharges(it) }
            ChargeType.SAVINGS -> id?.let { clientChargePresenter?.loadSavingsAccountCharges(it) }
            ChargeType.LOAN -> id?.let { clientChargePresenter?.loadLoanAccountCharges(it) }
        }
    }

    /**
     * It is called whenever any error occurs while executing a request. If not connected to
     * internet then it shows display a message to user to connect to internet other it just
     * displays the `message` in a [Snackbar]
     *
     * @param message Error message that tells the user about the problem.
     */
    override fun showErrorFetchingClientCharges(message: String?) {
        if (!Network.isConnected(activity)) {
            sweetUIErrorHandler?.showSweetNoInternetUI(rvClientCharge, layoutError)
        } else {
            sweetUIErrorHandler?.showSweetErrorUI(message, rvClientCharge, layoutError)
            Toaster.show(rootView, message)
        }
    }

    /**
     * Tries to fetch charges again.
     */
    @OnClick(R.id.btn_try_again)
    fun retryClicked() {
        if (Network.isConnected(context)) {
            sweetUIErrorHandler?.hideSweetErrorLayoutUI(rvClientCharge, layoutError)
            loadCharges()
        } else {
            Toast.makeText(context, getString(R.string.internet_not_connected),
                    Toast.LENGTH_SHORT).show()
        }
    }

    override fun showClientCharges(clientChargeList: List<Charge?>?) {
        this.clientChargeList = clientChargeList
        inflateClientChargeList()
        if (swipeChargeContainer!!.isRefreshing) {
            swipeChargeContainer!!.isRefreshing = false
        }
    }

    /**
     * Updates `clientChargeAdapter` with updated `clientChargeList` if
     * `clientChargeList` size if greater than 0 else shows the error layout
     */
    private fun inflateClientChargeList() {
        if (clientChargeList?.isNotEmpty() == true) {
            clientChargeAdapter?.setClientChargeList(clientChargeList)
            rvClientCharge?.adapter = clientChargeAdapter
        } else {
            sweetUIErrorHandler?.showSweetEmptyUI(getString(R.string.charges), R.drawable.ic_charges,
                    rvClientCharge, layoutError)
        }
    }

    override fun showProgress() {
        swipeChargeContainer?.isRefreshing = true
    }

    override fun hideProgress() {
        swipeChargeContainer?.isRefreshing = false
    }

    override fun onItemClick(childView: View?, position: Int) {}
    override fun onItemLongPress(childView: View?, position: Int) {}
    override fun onDestroyView() {
        super.onDestroyView()
        clientChargePresenter?.detachView()
    }

    companion object {
        fun newInstance(clientId: Long?, chargeType: ChargeType?): ClientChargeFragment {
            val clientChargeFragment = ClientChargeFragment()
            val args = Bundle()
            if (clientId != null)
                args.putLong(Constants.CLIENT_ID, clientId)
            args.putSerializable(Constants.CHARGE_TYPE, chargeType)
            clientChargeFragment.arguments = args
            return clientChargeFragment
        }
    }
}