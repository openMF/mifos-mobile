package org.mifos.mobile.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentBeneficiaryListBinding
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.presenters.BeneficiaryListPresenter
import org.mifos.mobile.ui.activities.AddBeneficiaryActivity
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.adapters.BeneficiaryListAdapter
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.views.BeneficiariesView
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.DividerItemDecoration
import org.mifos.mobile.utils.Network
import javax.inject.Inject

/**
 * Created by dilpreet on 14/6/17.
 */
@AndroidEntryPoint
class BeneficiaryListFragment : BaseFragment(), OnRefreshListener, BeneficiariesView {

    private var _binding: FragmentBeneficiaryListBinding? = null
    private val binding get() = _binding!!

    @JvmField
    @Inject
    var beneficiaryListPresenter: BeneficiaryListPresenter? = null

    private var beneficiaryListAdapter: BeneficiaryListAdapter? = null

    private var beneficiaryList: List<Beneficiary?>? = null
    private var sweetUIErrorHandler: SweetUIErrorHandler? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBeneficiaryListBinding.inflate(inflater, container, false)
        beneficiaryListAdapter = BeneficiaryListAdapter(::onItemClick)
        ////(activity as BaseActivity?)?.activityComponent?.inject(this)
        setToolbarTitle(getString(R.string.beneficiaries))
        sweetUIErrorHandler = SweetUIErrorHandler(activity, binding.root)
        showUserInterface()
        beneficiaryListPresenter?.attachView(this)
        if (savedInstanceState == null) {
            beneficiaryListPresenter?.loadBeneficiaries()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutError.btnTryAgain.setOnClickListener {
            retryClicked()
        }
    }

    override fun onResume() {
        super.onResume()
        beneficiaryListPresenter?.loadBeneficiaries()
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            val beneficiaries: List<Beneficiary?> =
                savedInstanceState.getParcelableArrayList(Constants.BENEFICIARY) ?: listOf()
            showBeneficiaryList(beneficiaries)
        }
    }

    /**
     * Setup Initial User Interface
     */
    override fun showUserInterface() {
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        with(binding) {
            rvBeneficiaries.layoutManager = layoutManager
            rvBeneficiaries.setHasFixedSize(true)
            rvBeneficiaries.addItemDecoration(
                DividerItemDecoration(
                    (activity?.applicationContext)!!,
                    layoutManager.orientation,
                ),
            )
            rvBeneficiaries.adapter = beneficiaryListAdapter
            swipeContainer.setColorSchemeColors(
                *requireActivity()
                    .resources.getIntArray(R.array.swipeRefreshColors),
            )
            swipeContainer.setOnRefreshListener(this@BeneficiaryListFragment)
            fabAddBeneficiary.setOnClickListener {
                startActivity(
                    Intent(
                        activity,
                        AddBeneficiaryActivity::class.java,
                    ),
                )
            }
        }
    }

    fun retryClicked() {
        if (Network.isConnected((context?.applicationContext)!!)) {
            sweetUIErrorHandler?.hideSweetErrorLayoutUI(
                binding.rvBeneficiaries,
                binding.layoutError.root,
            )
            beneficiaryListPresenter?.loadBeneficiaries()
        } else {
            Toast.makeText(
                context,
                getString(R.string.internet_not_connected),
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    /**
     * Refreshes `beneficiaryList` by calling `loadBeneficiaries()`
     */
    override fun onRefresh() {
        if (binding.layoutError.root.visibility == View.VISIBLE) {
            sweetUIErrorHandler?.hideSweetErrorLayoutUI(
                binding.rvBeneficiaries,
                binding.layoutError.root,
            )
        }
        beneficiaryListPresenter?.loadBeneficiaries()
    }

    /**
     * Shows [SwipeRefreshLayout]
     */
    override fun showProgress() {
        showSwipeRefreshLayout(true)
    }

    /**
     * Hides [SwipeRefreshLayout]
     */
    override fun hideProgress() {
        showSwipeRefreshLayout(false)
    }

    /**
     * It is called whenever any error occurs while executing a request
     *
     * @param msg Error message that tells the user about the problem.
     */
    override fun showError(msg: String?) {
        if (!Network.isConnected((activity?.applicationContext)!!)) {
            sweetUIErrorHandler?.showSweetNoInternetUI(
                binding.rvBeneficiaries,
                binding.layoutError.root,
            )
        } else {
            sweetUIErrorHandler?.showSweetErrorUI(
                msg,
                binding.rvBeneficiaries,
                binding.layoutError.root,
            )
        }
    }

    /**
     * Set the `beneficiaryList` fetched from server to `beneficiaryListAdapter`
     */
    override fun showBeneficiaryList(beneficiaryList: List<Beneficiary?>?) {
        this.beneficiaryList = beneficiaryList
        if (beneficiaryList?.isNotEmpty() == true) {
            beneficiaryListAdapter?.setBeneficiaryList(beneficiaryList)
        } else {
            showEmptyBeneficiary()
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

    private fun showSwipeRefreshLayout(show: Boolean?) {
        binding.swipeContainer.post { binding.swipeContainer.isRefreshing = (show == true) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        beneficiaryListPresenter?.detachView()
        _binding = null
    }

    /**
     * Shows an error layout when this function is called.`
     */
    private fun showEmptyBeneficiary() {
        sweetUIErrorHandler?.showSweetEmptyUI(
            getString(R.string.beneficiary),
            getString(R.string.beneficiary),
            R.drawable.ic_beneficiaries_48px,
            binding.rvBeneficiaries,
            binding.layoutError.root,
        )
        binding.rvBeneficiaries.visibility = View.GONE
    }

    companion object {
        fun newInstance(): BeneficiaryListFragment {
            return BeneficiaryListFragment()
        }
    }
}
