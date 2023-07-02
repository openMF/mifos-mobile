package org.mifos.mobile.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentAccountOverviewBinding
import org.mifos.mobile.presenters.AccountOverviewPresenter
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.views.AccountOverviewMvpView
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.CurrencyUtil
import org.mifos.mobile.utils.Toaster
import org.mifos.mobile.utils.Utils
import javax.inject.Inject

/**
 * @author Rajan Maurya
 * On 16/10/17.
 */
@AndroidEntryPoint
class AccountOverviewFragment : BaseFragment(), AccountOverviewMvpView, OnRefreshListener {

    private var _binding: FragmentAccountOverviewBinding? = null
    private val binding get() = _binding!!

    @JvmField
    @Inject
    var accountOverviewPresenter: AccountOverviewPresenter? = null
    private var totalLoanBalance = 0.0
    private var totalSavingsBalance = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAccountOverviewBinding.inflate(inflater, container, false)
        //(activity as BaseActivity?)?.activityComponent?.inject(this)
        accountOverviewPresenter?.attachView(this)
        setToolbarTitle(getString(R.string.accounts_overview))
        binding.swipeContainer.setColorSchemeResources(
            R.color.blue_light,
            R.color.green_light,
            R.color.orange_light,
            R.color.red_light,
        )
        binding.swipeContainer.setOnRefreshListener(this)
        if (savedInstanceState == null) {
            accountOverviewPresenter?.loadClientAccountDetails()
        }
        return binding.root
    }

    override fun onRefresh() {
        accountOverviewPresenter?.loadClientAccountDetails()
    }

    override fun showTotalLoanSavings(totalLoan: Double?, totalSavings: Double?) {
        totalLoanBalance = totalLoan!!
        totalSavingsBalance = totalSavings!!
        binding.tvTotalLoan.text = CurrencyUtil.formatCurrency(requireContext(), totalLoan)
        binding.tvTotalSavings.text = CurrencyUtil.formatCurrency(requireContext(), totalSavings)
    }

    override fun showError(message: String?) {
        Toaster.show(binding.root, message)
    }

    override fun showProgress() {
        binding.swipeContainer.isRefreshing = true
    }

    override fun hideProgress() {
        binding.swipeContainer.isRefreshing = false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_account_overview, menu)
        Utils.setToolbarIconColor(activity, menu, R.color.white)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menu_refresh_account_overview) {
            accountOverviewPresenter?.loadClientAccountDetails()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putDouble(Constants.TOTAL_LOAN, totalLoanBalance)
        outState.putDouble(Constants.TOTAL_SAVINGS, totalSavingsBalance)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            totalLoanBalance = savedInstanceState.getDouble(Constants.TOTAL_LOAN)
            totalSavingsBalance = savedInstanceState.getDouble(Constants.TOTAL_SAVINGS)
            showTotalLoanSavings(totalLoanBalance, totalSavingsBalance)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        accountOverviewPresenter?.detachView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(): AccountOverviewFragment {
            val fragment = AccountOverviewFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
