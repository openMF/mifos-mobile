package org.mifos.mobile.ui.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.ui.activities.HomeActivity
import org.mifos.mobile.ui.activities.LoanApplicationActivity
import org.mifos.mobile.ui.activities.NotificationActivity
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.client_charge.ClientChargeComposeFragment
import org.mifos.mobile.ui.enums.AccountType
import org.mifos.mobile.ui.enums.ChargeType
import org.mifos.mobile.ui.fragments.BeneficiaryListFragment
import org.mifos.mobile.ui.fragments.ClientAccountsFragment
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.savings_make_transfer.SavingsMakeTransferComposeFragment
import org.mifos.mobile.ui.third_party_transfer.ThirdPartyTransferComposeFragment
import org.mifos.mobile.ui.user_profile.UserProfileActivity
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.MaterialDialog
import org.mifos.mobile.utils.Toaster
import javax.inject.Inject

/**
 * Created by michaelsosnick on 1/1/17.
 */
@AndroidEntryPoint
class HomeOldFragment : BaseFragment(), OnRefreshListener {

    private val viewModel: HomeViewModel by viewModels()

    @JvmField
    @Inject
    var preferencesHelper: PreferencesHelper? = null

    private var clientId: Long? = 0
    private var toolbarView: View? = null
    private var isReceiverRegistered = false
    private var tvNotificationCount: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        clientId = preferencesHelper?.clientId
        setHasOptionsMenu(true)
        setToolbarTitle(getString(R.string.home))
        showUserInterface()
        loadClientData()
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MifosMobileTheme {
                    HomeScreen(
                        homeUiState = viewModel.homeUiState.value,
                        homeCards = viewModel.getHomeCardItems(),
                        callHelpline = { callHelpline() },
                        mailHelpline = { mailHelpline() },
                        totalSavings = { onClickSavings() },
                        totalLoan = { onClickLoan() },
                        userProfile = { userImageClicked() },
                        homeCardClicked = { handleHomeCardClick(it) }
                    )
                }
            }
        }
    }

    private fun handleHomeCardClick(homeCardItem: HomeCardItem) {
        when (homeCardItem) {
            is HomeCardItem.AccountCard -> accountsClicked()
            is HomeCardItem.BeneficiariesCard -> beneficiaries()
            is HomeCardItem.ChargesCard -> chargesClicked()
            is HomeCardItem.LoanCard -> applyForLoan()
            is HomeCardItem.SurveyCard -> surveys()
            is HomeCardItem.TransferCard -> transferClicked()
        }
    }

    private val notificationReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            activity?.invalidateOptionsMenu()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        val menuItem = menu.findItem(R.id.menu_notifications)
        val count = menuItem.actionView
        tvNotificationCount = count?.findViewById(R.id.tv_notification_indicator)
        viewModel.unreadNotificationsCount
        count?.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    NotificationActivity::class.java,
                ),
            )
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver()
        activity?.invalidateOptionsMenu()
        (activity as? BaseActivity)?.showToolbar()
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(requireActivity())
            .unregisterReceiver(notificationReceiver)
        isReceiverRegistered = false
        super.onPause()
    }

    private fun registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
                notificationReceiver,
                IntentFilter(Constants.NOTIFY_HOME_FRAGMENT),
            )
            isReceiverRegistered = true
        }
    }

    override fun onRefresh() {
        loadClientData()
    }

    private fun loadClientData() {
        viewModel.loadClientAccountDetails()
        viewModel.getUserDetails()
        viewModel.getUserImage()
    }

    fun showUserInterface() {
        toolbarView = (activity as HomeActivity?)?.toolbar?.rootView
    }

    /**
     * Opens [ClientAccountsFragment] according to the `accountType` provided
     *
     * @param accountType Enum of [AccountType]
     */
    private fun openAccount(accountType: AccountType?) {
        (activity as BaseActivity?)?.replaceFragment(
            ClientAccountsFragment.newInstance(accountType),
            true,
            R.id.container,
        )
    }

    /**
     * Open LOAN tab under ClientAccountsFragment
     */
    private fun onClickLoan() {
        openAccount(AccountType.LOAN)
        (activity as HomeActivity?)?.setNavigationViewSelectedItem(R.id.item_accounts)
    }

    /**
     * Open SAVINGS tab under ClientAccountsFragment
     */
    private fun onClickSavings() {
        openAccount(AccountType.SAVINGS)
        (activity as HomeActivity?)?.setNavigationViewSelectedItem(R.id.item_accounts)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            viewModel.notificationsCount.collect { count ->
                showNotificationCount(count)
            }
        }
    }

    private fun callHelpline() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + getString(R.string.help_line_number))
        startActivity(intent)
    }

    private fun mailHelpline() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.contact_email)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.user_query))
        }
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                getString(R.string.no_app_to_support_action),
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    fun showNotificationCount(count: Int) {
        if (count > 0) {
            tvNotificationCount?.visibility = View.VISIBLE
            tvNotificationCount?.text = count.toString()
        } else {
            tvNotificationCount?.visibility = View.GONE
        }
    }

    private fun userImageClicked() {
        startActivity(Intent(activity, UserProfileActivity::class.java))
    }

    /**
     * Calls `openAccount()` for opening [ClientAccountsFragment]
     */
    private fun accountsClicked() {
        openAccount(AccountType.SAVINGS)
        (activity as HomeActivity?)?.setNavigationViewSelectedItem(R.id.item_accounts)
    }

    /**
     * Shows a dialog with options: Normal Transfer and Third Party Transfer
     */
    private fun transferClicked() {
        val transferTypes =
            arrayOf(getString(R.string.transfer), getString(R.string.third_party_transfer))
        MaterialDialog.Builder().init(activity)
            .setTitle(R.string.choose_transfer_type)
            .setItems(
                transferTypes,
                DialogInterface.OnClickListener { _, which ->
                    if (which == 0) {
                        (activity as HomeActivity?)?.replaceFragment(
                            SavingsMakeTransferComposeFragment.newInstance(1, ""),
                            true,
                            R.id.container,
                        )
                    } else {
                        (activity as HomeActivity?)?.replaceFragment(
                            ThirdPartyTransferComposeFragment.newInstance(),
                            true,
                            R.id.container,
                        )
                    }
                },
            )
            .createMaterialDialog()
            .show()
    }

    /**
     * Opens [ClientChargeFragment] to display all Charges associated with client's account
     */
    private fun chargesClicked() {
        (activity as HomeActivity?)?.replaceFragment(
            ClientChargeComposeFragment.newInstance(
                clientId,
                ChargeType.CLIENT,
            ),
            true,
            R.id.container,
        )
    }

    /**
     * Opens [LoanApplicationFragment] to apply for a loan
     */
    private fun applyForLoan() {
        startActivity(Intent(activity, LoanApplicationActivity::class.java))
    }

    /**
     * Opens [BeneficiaryListFragment] which contains list of Beneficiaries associated with
     * Client's account
     */
    fun beneficiaries() {
        (activity as HomeActivity?)?.replaceFragment(
            BeneficiaryListFragment.newInstance(),
            true,
            R.id.container,
        )
    }

    private fun surveys() {
    }

    /**
     * It is called whenever any error occurs while executing a request
     *
     * @param errorMessage Error message that tells the user about the problem.
     */
    fun showError(errorMessage: String?) {
        val checkedItem = (activity as HomeActivity?)?.checkedItem
        if (checkedItem == R.id.item_about_us || checkedItem == R.id.item_help || checkedItem == R.id.item_settings) {
            return
        }
        Toaster.show(view, errorMessage)
    }

    companion object {
        fun newInstance(): HomeOldFragment {
            return HomeOldFragment()
        }
    }
}
