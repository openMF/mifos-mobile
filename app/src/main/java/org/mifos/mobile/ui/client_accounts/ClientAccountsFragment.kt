package org.mifos.mobile.ui.client_accounts

/*
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentClientAccountsBinding
import org.mifos.mobile.ui.activities.HomeActivity
import org.mifos.mobile.ui.activities.LoanApplicationActivity
import org.mifos.mobile.ui.activities.SavingsAccountApplicationActivity
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.adapters.CheckBoxAdapter
import org.mifos.mobile.ui.adapters.ViewPagerAdapter
import org.mifos.mobile.ui.enums.AccountType
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.utils.ParcelableAndSerializableUtils.getCheckedSerializable
import org.mifos.mobile.utils.StatusUtils
import javax.inject.Inject

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/
@AndroidEntryPoint
class ClientAccountsFragment : BaseFragment() {
    private var _binding: FragmentClientAccountsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : AccountsViewModel

    @JvmField
    @Inject
    var checkBoxAdapter: CheckBoxAdapter? = null
    private var checkBoxRecyclerView: RecyclerView? = null
    private var accountType: AccountType? = null
    private var isDialogBoxSelected = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (arguments != null) {
            accountType = arguments?.getCheckedSerializable(
                AccountType::class.java,
                Constants.ACCOUNT_TYPE
            ) as AccountType
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentClientAccountsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AccountsViewModel::class.java]
        val rootView = binding.root
        setToolbarTitle(getString(R.string.accounts))
        setUpViewPagerAndTabLayout()
        if (savedInstanceState == null) {
            viewModel.loadClientAccounts()
        }
        return rootView
    }

    /**
     * Setting up [ViewPagerAdapter] and [TabLayout] for Savings, Loans and Share
     * accounts. `accountType` is used for setting the current Fragment
     */
    private fun setUpViewPagerAndTabLayout() {
        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        viewPagerAdapter.addFragment(
            AccountsFragment.newInstance(Constants.SAVINGS_ACCOUNTS),
            getString(R.string.savings),
        )
        viewPagerAdapter.addFragment(
            AccountsFragment.newInstance(Constants.LOAN_ACCOUNTS),
            getString(R.string.loan),
        )
        viewPagerAdapter.addFragment(
            AccountsFragment.newInstance(Constants.SHARE_ACCOUNTS),
            getString(R.string.share),
        )
        binding.viewpager.offscreenPageLimit = 2
        binding.viewpager.adapter = viewPagerAdapter
        when (accountType) {
            AccountType.SAVINGS -> binding.viewpager.currentItem = 0
            AccountType.LOAN -> binding.viewpager.currentItem = 1
            AccountType.SHARE -> binding.viewpager.currentItem = 2
            else -> {}
        }
        binding.tabs.setupWithViewPager(binding.viewpager)
        binding.viewpager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {
            }

            override fun onPageSelected(position: Int) {
                activity?.invalidateOptionsMenu()
                view?.let { (activity as HomeActivity?)?.hideKeyboard(it) }
                if (position == 0 || position == 1) {
                    binding.fabCreateLoan.show()
                } else {
                    binding.fabCreateLoan.hide()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    /**
     * Returns tag of Fragment present at `position`
     *
     * @param position position of Fragment
     * @return Tag of Fragment
     */
    private fun getFragmentTag(position: Int): String {
        return "android:switcher:" + R.id.viewpager + ":" + position
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabCreateLoan.setOnClickListener {
            createLoan()
        }
    }

    private fun createLoan() {
        when (binding.viewpager.currentItem) {
            0 -> startActivity(Intent(activity, SavingsAccountApplicationActivity::class.java))
            1 -> startActivity(Intent(activity, LoanApplicationActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_account, menu)
        when (binding.viewpager.currentItem) {
            0 -> {
                menu.findItem(R.id.menu_filter_savings).isVisible = true
                menu.findItem(R.id.menu_filter_loan).isVisible = false
                menu.findItem(R.id.menu_filter_shares).isVisible = false
                menu.findItem(R.id.menu_search_saving).isVisible = true
                menu.findItem(R.id.menu_search_loan).isVisible = false
                menu.findItem(R.id.menu_search_share).isVisible = false
                initSearch(menu, AccountType.SAVINGS)
            }
            1 -> {
                menu.findItem(R.id.menu_filter_savings).isVisible = false
                menu.findItem(R.id.menu_filter_loan).isVisible = true
                menu.findItem(R.id.menu_filter_shares).isVisible = false
                menu.findItem(R.id.menu_search_saving).isVisible = false
                menu.findItem(R.id.menu_search_loan).isVisible = true
                menu.findItem(R.id.menu_search_share).isVisible = false
                initSearch(menu, AccountType.LOAN)
            }
            2 -> {
                menu.findItem(R.id.menu_filter_savings).isVisible = false
                menu.findItem(R.id.menu_filter_loan).isVisible = false
                menu.findItem(R.id.menu_filter_shares).isVisible = true
                menu.findItem(R.id.menu_search_saving).isVisible = false
                menu.findItem(R.id.menu_search_loan).isVisible = false
                menu.findItem(R.id.menu_search_share).isVisible = true
                initSearch(menu, AccountType.SHARE)
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_filter_savings -> showFilterDialog(AccountType.SAVINGS)
            R.id.menu_filter_loan -> showFilterDialog(AccountType.LOAN)
            R.id.menu_filter_shares -> showFilterDialog(AccountType.SHARE)
        }
        return true
    }

    /**
     * Initializes the search option in [Menu] depending upon `account`
     *
     * @param menu    Interface for managing the items in a menu.
     * @param account An enum of [AccountType]
     */
    private fun initSearch(menu: Menu, account: AccountType) {
        val manager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        var search: SearchView? = null

        search = when (account) {
            AccountType.SAVINGS -> {
                menu.findItem(R.id.menu_search_saving).actionView as SearchView
            }

            AccountType.LOAN -> {
                menu.findItem(R.id.menu_search_loan).actionView as SearchView
            }

            AccountType.SHARE -> {
                menu.findItem(R.id.menu_search_share).actionView as SearchView
            }
        }
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        search.maxWidth = (0.75 * width).toInt()
        search.setSearchableInfo(manager.getSearchableInfo(activity?.componentName))
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                when (account) {
                    AccountType.SAVINGS -> {
                        (
                            childFragmentManager.findFragmentByTag(
                                getFragmentTag(0),
                            ) as AccountsFragment?
                            )?.searchSavingsAccount(newText)
                    }

                    AccountType.LOAN -> {
                        (
                            childFragmentManager.findFragmentByTag(
                                getFragmentTag(1),
                            ) as AccountsFragment?
                            )?.searchLoanAccount(newText)
                    }

                    AccountType.SHARE -> {
                        (
                            childFragmentManager.findFragmentByTag(
                                getFragmentTag(2),
                            ) as AccountsFragment?
                            )?.searchSharesAccount(newText)
                    }
                }
                return false
            }
        })
    }

    /**
     * Displays a filter dialog according to the `account` provided in the parameter
     *
     * @param account An enum of [AccountType]
     */
    private fun showFilterDialog(account: AccountType) {
        if (isDialogBoxSelected) {
            return
        }
        isDialogBoxSelected = true
        var title = ""
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = RecyclerView.VERTICAL
        checkBoxRecyclerView = RecyclerView(requireContext())
        checkBoxRecyclerView?.layoutManager = layoutManager
        checkBoxRecyclerView?.adapter = checkBoxAdapter
        val wrapper = LinearLayout(requireContext())
        resources.getDimension(R.dimen.Mifos_DesignSystem_Spacing_CardInnerPaddingLarge).toInt()
            .let { cardInnerPadding ->
                wrapper.setPadding(
                    cardInnerPadding,
                    cardInnerPadding,
                    cardInnerPadding,
                    cardInnerPadding,
                )
            }
        wrapper.addView(checkBoxRecyclerView)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            checkBoxRecyclerView?.layoutParams?.height = (getScreenHeight() / 3.5).toInt()
        }
        when (account) {
            AccountType.SAVINGS -> {
                if ((
                        childFragmentManager.findFragmentByTag(
                            getFragmentTag(0),
                        ) as AccountsFragment?
                        )?.getCurrentFilterList() == null
                ) {
                    checkBoxAdapter?.statusList = StatusUtils.getSavingsAccountStatusList(activity)
                } else {
                    checkBoxAdapter?.statusList = (
                        childFragmentManager
                            .findFragmentByTag(getFragmentTag(0)) as AccountsFragment?
                        )?.getCurrentFilterList()
                }
                title = getString(R.string.filter_savings)
            }

            AccountType.LOAN -> {
                if ((
                        childFragmentManager.findFragmentByTag(
                            getFragmentTag(1),
                        ) as AccountsFragment?
                        )?.getCurrentFilterList() == null
                ) {
                    checkBoxAdapter?.statusList = StatusUtils.getLoanAccountStatusList(activity)
                } else {
                    checkBoxAdapter?.statusList = (
                        childFragmentManager
                            .findFragmentByTag(getFragmentTag(1)) as AccountsFragment?
                        )?.getCurrentFilterList()
                }
                title = getString(R.string.filter_loan)
            }

            AccountType.SHARE -> {
                if ((
                        childFragmentManager.findFragmentByTag(
                            getFragmentTag(2),
                        ) as AccountsFragment?
                        )?.getCurrentFilterList() == null
                ) {
                    checkBoxAdapter?.statusList = StatusUtils.getShareAccountStatusList(activity)
                } else {
                    checkBoxAdapter?.statusList = (
                        childFragmentManager
                            .findFragmentByTag(getFragmentTag(2)) as AccountsFragment?
                        )?.getCurrentFilterList()
                }
                title = getString(R.string.filter_share)
            }
        }
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(title)
            .setCancelable(false)
            .setMessage(getString(R.string.select_you_want))
            .setView(wrapper)
            .setPositiveButton(getString(R.string.filter)) { _, _ ->
                isDialogBoxSelected = false
                when (account) {
                    AccountType.SAVINGS -> {
                        (
                            childFragmentManager.findFragmentByTag(
                                getFragmentTag(0),
                            ) as AccountsFragment?
                            )
                            ?.setCurrentFilterList(checkBoxAdapter?.statusList)
                        (
                            childFragmentManager.findFragmentByTag(
                                getFragmentTag(0),
                            ) as AccountsFragment?
                            )
                            ?.filterSavingsAccount(checkBoxAdapter?.statusList)
                    }

                    AccountType.LOAN -> {
                        (
                            childFragmentManager.findFragmentByTag(
                                getFragmentTag(1),
                            ) as AccountsFragment?
                            )
                            ?.setCurrentFilterList(checkBoxAdapter?.statusList)
                        (
                            childFragmentManager.findFragmentByTag(
                                getFragmentTag(1),
                            ) as AccountsFragment?
                            )
                            ?.filterLoanAccount(checkBoxAdapter?.statusList)
                    }

                    AccountType.SHARE -> {
                        (
                            childFragmentManager.findFragmentByTag(
                                getFragmentTag(2),
                            ) as AccountsFragment?
                            )
                            ?.setCurrentFilterList(checkBoxAdapter?.statusList)
                        (
                            childFragmentManager.findFragmentByTag(
                                getFragmentTag(2),
                            ) as AccountsFragment?
                            )
                            ?.filterShareAccount(checkBoxAdapter?.statusList)
                    }
                }
            }
            .setNeutralButton(R.string.clear_filters) { _, _ ->
                isDialogBoxSelected = false
                when (account) {
                    AccountType.SAVINGS -> {
                        (
                            childFragmentManager.findFragmentByTag(
                                getFragmentTag(0),
                            ) as AccountsFragment?
                            )?.clearFilter()
                        checkBoxAdapter?.statusList =
                            StatusUtils.getSavingsAccountStatusList(activity)
                        viewModel.loadAccounts(Constants.SAVINGS_ACCOUNTS)
                    }

                    AccountType.LOAN -> {
                        (
                            childFragmentManager.findFragmentByTag(
                                getFragmentTag(1),
                            ) as AccountsFragment?
                            )?.clearFilter()
                        checkBoxAdapter?.statusList = StatusUtils.getLoanAccountStatusList(activity)
                        viewModel.loadAccounts(Constants.LOAN_ACCOUNTS)
                    }

                    AccountType.SHARE -> {
                        (
                            childFragmentManager.findFragmentByTag(
                                getFragmentTag(2),
                            ) as AccountsFragment?
                            )?.clearFilter()
                        checkBoxAdapter?.statusList =
                            StatusUtils.getShareAccountStatusList(activity)
                        viewModel.loadAccounts(Constants.SHARE_ACCOUNTS)
                    }
                }
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> isDialogBoxSelected = false }
            .create()
            .show()
    }

    override fun onResume() {
        super.onResume()
        (activity as BaseActivity?)?.hideToolbarElevation()
    }

    override fun onPause() {
        super.onPause()
        (activity as BaseActivity?)?.setToolbarElevation()
    }

    private fun getScreenHeight(): Int {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    companion object {
        fun newInstance(accountType: AccountType?): ClientAccountsFragment {
            val clientAccountsFragment = ClientAccountsFragment()
            val args = Bundle()
            args.putSerializable(Constants.ACCOUNT_TYPE, accountType)
            clientAccountsFragment.arguments = args
            return clientAccountsFragment
        }
    }
}
*/