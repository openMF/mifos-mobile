package org.mifos.mobile.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.TextView

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener

import butterknife.BindView
import butterknife.ButterKnife

import org.mifos.mobile.R
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
class AccountOverviewFragment : BaseFragment(), AccountOverviewMvpView, OnRefreshListener {
    @kotlin.jvm.JvmField
    @BindView(R.id.tv_total_savings)
    var tvTotalSavings: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_total_loan)
    var tvTotalLoan: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.swipe_container)
    var swipeRefreshLayout: SwipeRefreshLayout? = null

    @kotlin.jvm.JvmField
    @Inject
    var accountOverviewPresenter: AccountOverviewPresenter? = null
    var rootView: View? = null
    private var totalLoanBalance = 0.0
    private var totalSavingsBalance = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_account_overview, container, false)
        (activity as BaseActivity?)?.activityComponent?.inject(this)
        ButterKnife.bind(this, rootView!!)
        accountOverviewPresenter?.attachView(this)
        setToolbarTitle(getString(R.string.accounts_overview))
        swipeRefreshLayout?.setColorSchemeResources(R.color.blue_light, R.color.green_light, R.color.orange_light, R.color.red_light)
        swipeRefreshLayout?.setOnRefreshListener(this)
        if (savedInstanceState == null) {
            accountOverviewPresenter?.loadClientAccountDetails()
        }
        return rootView
    }

    override fun onRefresh() {
        accountOverviewPresenter?.loadClientAccountDetails()
    }

    override fun showTotalLoanSavings(totalLoan: Double?, totalSavings: Double?) {
        totalLoanBalance = totalLoan!!
        totalSavingsBalance = totalSavings!!
        tvTotalLoan?.text = CurrencyUtil.formatCurrency(context!!, totalLoan)
        tvTotalSavings?.text = CurrencyUtil.formatCurrency(context!!, totalSavings)
    }

    override fun showError(message: String?) {
        Toaster.show(rootView, message)
    }

    override fun showProgress() {
        swipeRefreshLayout?.isRefreshing = true
    }

    override fun hideProgress() {
        swipeRefreshLayout?.isRefreshing = false
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
    }

    companion object {
        @kotlin.jvm.JvmStatic
        fun newInstance(): AccountOverviewFragment {
            val fragment = AccountOverviewFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}