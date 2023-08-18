package org.mifos.mobile.ui.fragments

import android.animation.LayoutTransition
import android.content.*
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.databinding.FragmentHomeOldBinding
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.ui.activities.HomeActivity
import org.mifos.mobile.ui.activities.LoanApplicationActivity
import org.mifos.mobile.ui.activities.NotificationActivity
import org.mifos.mobile.ui.activities.UserProfileActivity
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.enums.AccountType
import org.mifos.mobile.ui.enums.ChargeType
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.getThemeAttributeColor
import org.mifos.mobile.utils.*
import org.mifos.mobile.viewModels.HomeViewModel
import javax.inject.Inject

/**
 * Created by michaelsosnick on 1/1/17.
 */
@AndroidEntryPoint
class HomeOldFragment : BaseFragment(), OnRefreshListener {
    private var _binding: FragmentHomeOldBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    @JvmField
    @Inject
    var preferencesHelper: PreferencesHelper? = null
    private var totalLoanAmount = 0.0
    private var totalSavingAmount = 0.0
    private var client: Client? = null
    private var clientId: Long? = 0
    private var toolbarView: View? = null
    private var isDetailVisible: Boolean? = false
    private var isReceiverRegistered = false
    private var tvNotificationCount: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeOldBinding.inflate(inflater, container, false)
        val rootView = binding.root
        clientId = preferencesHelper?.clientId
        setHasOptionsMenu(true)
        binding.swipeHomeContainer.setColorSchemeResources(
            R.color.blue_light,
            R.color.green_light,
            R.color.orange_light,
            R.color.red_light,
        )
        binding.swipeHomeContainer.setOnRefreshListener(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            binding.llContainer.layoutTransition
                ?.enableTransitionType(LayoutTransition.CHANGING)
        }
        if (savedInstanceState == null) {
            loadClientData()
        }
        setToolbarTitle(getString(R.string.home))
        showUserInterface()
        return rootView
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putDouble(Constants.TOTAL_LOAN, totalLoanAmount)
        outState.putDouble(Constants.TOTAL_SAVINGS, totalSavingAmount)
        outState.putParcelable(Constants.USER_DETAILS, client)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            showUserDetails(savedInstanceState.getParcelable<Parcelable>(Constants.USER_DETAILS) as? Client)
            viewModel.setUserProfile(preferencesHelper?.userProfileImage)
            showLoanAccountDetails(savedInstanceState.getDouble(Constants.TOTAL_LOAN))
            showSavingAccountDetails(savedInstanceState.getDouble(Constants.TOTAL_SAVINGS))
        }
    }

    override fun onRefresh() {
        loadClientData()
    }

    private fun loadClientData() {
        viewModel.loadClientAccountDetails()
        viewModel.userDetails
        viewModel.userImage
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
     * Provides `totalLoanAmount` fetched from server
     *
     * @param totalLoanAmount Total Loan amount
     */
    fun showLoanAccountDetails(totalLoanAmount: Double) {
        this.totalLoanAmount = totalLoanAmount
        binding.tvLoanTotalAmount.text = CurrencyUtil.formatCurrency(context, totalLoanAmount)
    }

    /**
     * Open LOAN tab under ClientAccountsFragment
     */
    private fun onClickLoan() {
        openAccount(AccountType.LOAN)
        (activity as HomeActivity?)?.setNavigationViewSelectedItem(R.id.item_accounts)
    }

    /**
     * Provides `totalSavingAmount` fetched from server
     *
     * @param totalSavingAmount Total Saving amount
     */
    fun showSavingAccountDetails(totalSavingAmount: Double) {
        this.totalSavingAmount = totalSavingAmount
        binding.tvSavingTotalAmount.text = CurrencyUtil.formatCurrency(context, totalSavingAmount)
    }

    /**
     * Open SAVINGS tab under ClientAccountsFragment
     */
    private fun onClickSavings() {
        openAccount(AccountType.SAVINGS)
        (activity as HomeActivity?)?.setNavigationViewSelectedItem(R.id.item_accounts)
    }

    /**
     * Fetches Client details and display clientName
     *
     * @param client Details about client
     */
    fun showUserDetails(client: Client?) {
        this.client = client
        binding.tvUserName.text = getString(R.string.hello_client, client?.displayName)
    }

    /**
     * Provides with Client image fetched from server
     *
     * @param bitmap Client Image
     */
    fun showUserImage(bitmap: Bitmap?) {
        activity?.runOnUiThread {
            if (bitmap != null) {
                binding.ivCircularUserImage.visibility = View.VISIBLE
                binding.ivCircularUserImage.setImageBitmap(bitmap)
            } else {
                val userName = preferencesHelper?.clientName.let { savedName ->
                    if (savedName.isNullOrBlank()) {
                        getString(R.string.app_name)
                    } else {
                        savedName
                    }
                }

                val drawable = TextDrawable.builder()
                    .beginConfig()
                    .toUpperCase()
                    .endConfig()
                    .buildRound(
                        userName.substring(0, 1),
                        requireContext().getThemeAttributeColor(R.attr.colorPrimary),
                    )
                binding.ivCircularUserImage.setImageDrawable(drawable)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.homeUiState.collect {
                when (it) {
                    is HomeUiState.Loading -> showProgress()
                    is HomeUiState.UserImage -> {
                        hideProgress()
                        showUserImage(it.image)
                    }
                    is HomeUiState.ClientAccountDetails -> {
                        hideProgress()
                        showLoanAccountDetails(it.loanAccounts)
                        showSavingAccountDetails(it.savingsAccounts)
                    }
                    is HomeUiState.Error -> {
                        hideProgress()
                        showError(getString(it.errorMessage))
                    }
                    is HomeUiState.UserDetails -> {
                        hideProgress()
                        showUserDetails(it.client)
                    }
                    is HomeUiState.UnreadNotificationsCount -> {
                        hideProgress()
                        showNotificationCount(it.count)
                    }
                }
            }
        }

        toggleVisibilityButton(
            binding.btnSavingTotalAmountVisibility,
            binding.tvSavingTotalAmount,
            binding.tvSavingTotalAmountHidden,
        )
        toggleVisibilityButton(
            binding.btnLoanAmountVisibility,
            binding.tvLoanTotalAmount,
            binding.tvLoanTotalAmountHidden,
        )

        binding.llTotalLoan.setOnClickListener {
            onClickLoan()
        }

        binding.llTotalSavings.setOnClickListener {
            onClickSavings()
        }

        binding.ivCircularUserImage.setOnClickListener {
            userImageClicked()
        }

        binding.llAccounts.setOnClickListener {
            accountsClicked()
        }

        binding.llTransfer.setOnClickListener {
            transferClicked()
        }

        binding.llCharges.setOnClickListener {
            chargesClicked()
        }

        binding.llApplyForLoan.setOnClickListener {
            applyForLoan()
        }

        binding.llBeneficiaries.setOnClickListener {
            beneficiaries()
        }

        binding.llSurveys.setOnClickListener {
            surveys()
        }
    }

    private fun toggleVisibilityButton(
        button: ImageButton,
        visibleView: View,
        hiddenView: View,
    ) {
        button.setOnClickListener {
            if (visibleView.visibility == View.VISIBLE) {
                visibleView.visibility = View.GONE
                hiddenView.visibility = View.VISIBLE
                button.setImageResource(R.drawable.ic_visibility_24px)
            } else {
                visibleView.visibility = View.VISIBLE
                hiddenView.visibility = View.GONE
                button.setImageResource(R.drawable.ic_visibility_off_24px)
            }
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
                            SavingsMakeTransferFragment.newInstance(1, ""),
                            true,
                            R.id.container,
                        )
                    } else {
                        (activity as HomeActivity?)?.replaceFragment(
                            ThirdPartyTransferFragment.newInstance(),
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
            ClientChargeFragment.newInstance(
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
        Toaster.show(binding.root, errorMessage)
    }

    /**
     * Shows [SwipeRefreshLayout]
     */
    fun showProgress() {
        binding.swipeHomeContainer.isRefreshing = true
    }

    /**
     * Hides [SwipeRefreshLayout]
     */
    fun hideProgress() {
        binding.swipeHomeContainer.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (binding.swipeHomeContainer.isRefreshing) {
            binding.swipeHomeContainer.isRefreshing = false
            binding.swipeHomeContainer.removeAllViews()
        }
        _binding = null
    }

    companion object {
        fun newInstance(): HomeOldFragment {
            return HomeOldFragment()
        }
    }
}
