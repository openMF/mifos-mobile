
package org.mifos.mobile.ui.fragments

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.content.*
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.android.synthetic.main.fragment_home_old.*
import org.mifos.mobile.R
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.presenters.HomeOldPresenter
import org.mifos.mobile.ui.activities.HomeActivity
import org.mifos.mobile.ui.activities.LoanApplicationActivity
import org.mifos.mobile.ui.activities.NotificationActivity
import org.mifos.mobile.ui.activities.UserProfileActivity
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.enums.AccountType
import org.mifos.mobile.ui.enums.ChargeType
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.getThemeAttributeColor
import org.mifos.mobile.ui.views.HomeOldView
import org.mifos.mobile.utils.*
import javax.inject.Inject

/**
 * Created by michaelsosnick on 1/1/17.
 */
class HomeOldFragment : BaseFragment(), HomeOldView, OnRefreshListener {
    @kotlin.jvm.JvmField
    @BindView(R.id.tv_saving_total_amount)
    var tvSavingTotalAmount: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_loan_total_amount)
    var tvLoanTotalAmount: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.ll_account_detail)
    var llAccountDetail: LinearLayout? = null



    @kotlin.jvm.JvmField
    @BindView(R.id.iv_circular_user_image)
    var ivCircularUserImage: ShapeableImageView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_user_name)
    var tvUserName: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.swipe_home_container)
    var slHomeContainer: SwipeRefreshLayout? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.ll_container)
    var llContainer: LinearLayout? = null

    @kotlin.jvm.JvmField
    @Inject
    var presenter: HomeOldPresenter? = null

    @kotlin.jvm.JvmField
    @Inject
    var preferencesHelper: PreferencesHelper? = null
    var rootView: View? = null
    private var totalLoanAmount = 0.0
    private var totalSavingAmount = 0.0
    private var client: Client? = null
    private var clientId: Long? = 0
    private var toolbarView: View? = null
    private var isDetailVisible: Boolean? = false
    private var isReceiverRegistered = false
    private var tvNotificationCount: TextView? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_home_old, container, false)
        (activity as HomeActivity?)?.activityComponent?.inject(this)
        ButterKnife.bind(this, rootView!!)
        clientId = preferencesHelper?.clientId
        presenter?.attachView(this)
        setHasOptionsMenu(true)
        slHomeContainer?.setColorSchemeResources(R.color.blue_light, R.color.green_light, R.color.orange_light, R.color.red_light)
        slHomeContainer?.setOnRefreshListener(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            llContainer?.layoutTransition
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
        presenter?.unreadNotificationsCount
        count?.setOnClickListener { startActivity(Intent(context, NotificationActivity::class.java)) }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver()
        activity?.invalidateOptionsMenu()
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(notificationReceiver)
        isReceiverRegistered = false
        super.onPause()
    }

    private fun registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(notificationReceiver,
                    IntentFilter(Constants.NOTIFY_HOME_FRAGMENT))
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
            presenter?.setUserProfile(preferencesHelper?.userProfileImage)
            showLoanAccountDetails(savedInstanceState.getDouble(Constants.TOTAL_LOAN))
            showSavingAccountDetails(savedInstanceState.getDouble(Constants.TOTAL_SAVINGS))
        }
    }

    override fun onRefresh() {
        loadClientData()
    }

    private fun loadClientData() {
        presenter?.loadClientAccountDetails()
        presenter?.userDetails
        presenter?.userImage
    }

    override fun showUserInterface() {
        toolbarView = (activity as HomeActivity?)?.toolbar?.rootView
    }

    /**
     * Opens [ClientAccountsFragment] according to the `accountType` provided
     *
     * @param accountType Enum of [AccountType]
     */
    fun openAccount(accountType: AccountType?) {
        (activity as BaseActivity?)?.replaceFragment(
                ClientAccountsFragment.newInstance(accountType), true, R.id.container)
    }

    /**
     * Provides `totalLoanAmount` fetched from server
     *
     * @param totalLoanAmount Total Loan amount
     */
    override fun showLoanAccountDetails(totalLoanAmount: Double) {
        this.totalLoanAmount = totalLoanAmount
        tvLoanTotalAmount?.text = CurrencyUtil.formatCurrency(context, totalLoanAmount)
    }

    /**
     * Open LOAN tab under ClientAccountsFragment
     */
    @OnClick(R.id.ll_total_loan)
    fun onClickLoan() {
        openAccount(AccountType.LOAN)
        (activity as HomeActivity?)?.setNavigationViewSelectedItem(R.id.item_accounts)
    }

    /**
     * Provides `totalSavingAmount` fetched from server
     *
     * @param totalSavingAmount Total Saving amount
     */
    override fun showSavingAccountDetails(totalSavingAmount: Double) {
        this.totalSavingAmount = totalSavingAmount
        tvSavingTotalAmount?.text = CurrencyUtil.formatCurrency(context, totalSavingAmount)
    }

    /**
     * Open SAVINGS tab under ClientAccountsFragment
     */
    @OnClick(R.id.ll_total_savings)
    fun onClickSavings() {
        openAccount(AccountType.SAVINGS)
        (activity as HomeActivity?)?.setNavigationViewSelectedItem(R.id.item_accounts)
    }

    /**
     * Fetches Client details and display clientName
     *
     * @param client Details about client
     */
    override fun showUserDetails(client: Client?) {
        this.client = client
        tvUserName?.text = getString(R.string.hello_client, client?.displayName)
    }

    /**
     * Provides with Client image fetched from server
     *
     * @param bitmap Client Image
     */
    override fun showUserImage(bitmap: Bitmap?) {
        activity?.runOnUiThread {
            if (bitmap != null) {
                ivCircularUserImage?.visibility = View.VISIBLE
                ivCircularUserImage?.setImageBitmap(bitmap)
            } else {
                val userName = preferencesHelper?.clientName.let { savedName ->
                    if(savedName.isNullOrBlank()) getString(R.string.app_name)
                    else savedName
                }

                val drawable = TextDrawable.builder()
                        .beginConfig()
                        .toUpperCase()
                        .endConfig()
                        .buildRound(userName.substring(0, 1),requireContext().getThemeAttributeColor(R.attr.colorPrimary))
                ivCircularUserImage?.setImageDrawable(drawable)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)
        toggleVisibilityButton(
            btn_saving_total_amount_visibility,
            tv_saving_total_amount,
            tv_saving_total_amount_hidden
        )
        toggleVisibilityButton(
            btn_loan_amount_visibility,
            tv_loan_total_amount,
            tv_loan_total_amount_hidden
        )
    }

    private fun toggleVisibilityButton(
        button: ImageButton,
        visibleView: View,
        hiddenView: View
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

    override fun showNotificationCount(count: Int) {
        if (count > 0) {
            tvNotificationCount?.visibility = View.VISIBLE
            tvNotificationCount?.text = count.toString()
        } else {
            tvNotificationCount?.visibility = View.GONE
        }
    }

    @OnClick(R.id.iv_circular_user_image)
    fun userImageClicked() {
        startActivity(Intent(activity, UserProfileActivity::class.java))
    }

    /**
     * Calls `openAccount()` for opening [ClientAccountsFragment]
     */
    @OnClick(R.id.ll_accounts)
    fun accountsClicked() {
        openAccount(AccountType.SAVINGS)
        (activity as HomeActivity?)?.setNavigationViewSelectedItem(R.id.item_accounts)
    }

    /**
     * Shows a dialog with options: Normal Transfer and Third Party Transfer
     */
    @OnClick(R.id.ll_transfer)
    fun transferClicked() {
        val transferTypes = arrayOf(getString(R.string.transfer), getString(R.string.third_party_transfer))
        MaterialDialog.Builder().init(activity)
                .setTitle(R.string.choose_transfer_type)
                .setItems(transferTypes,
                        DialogInterface.OnClickListener { _, which ->
                            if (which == 0) {
                                (activity as HomeActivity?)?.replaceFragment(
                                        SavingsMakeTransferFragment.newInstance(1, ""), true,
                                        R.id.container)
                            } else {
                                (activity as HomeActivity?)?.replaceFragment(
                                        ThirdPartyTransferFragment.newInstance(), true, R.id.container)
                            }
                        })
                .createMaterialDialog()
                .show()
    }

    /**
     * Opens [ClientChargeFragment] to display all Charges associated with client's account
     */
    @OnClick(R.id.ll_charges)
    fun chargesClicked() {
        (activity as HomeActivity?)?.replaceFragment(ClientChargeFragment.newInstance(clientId,
                ChargeType.CLIENT), true, R.id.container)
    }

    /**
     * Opens [LoanApplicationFragment] to apply for a loan
     */
    @OnClick(R.id.ll_apply_for_loan)
    fun applyForLoan() {
        startActivity(Intent(activity, LoanApplicationActivity::class.java))
    }

    /**
     * Opens [BeneficiaryListFragment] which contains list of Beneficiaries associated with
     * Client's account
     */
    @OnClick(R.id.ll_beneficiaries)
    fun beneficiaries() {
        (activity as HomeActivity?)?.replaceFragment(BeneficiaryListFragment.newInstance(), true, R.id.container)
    }

    @OnClick(R.id.ll_surveys)
    fun surveys() {
    }

    /**
     * It is called whenever any error occurs while executing a request
     *
     * @param errorMessage Error message that tells the user about the problem.
     */
    override fun showError(errorMessage: String?) {
        val checkedItem = (activity as HomeActivity?)?.checkedItem
        if (checkedItem == R.id.item_about_us || checkedItem == R.id.item_help || checkedItem == R.id.item_settings) {
            return
        }
        Toaster.show(rootView, errorMessage)
    }

    /**
     * Shows [SwipeRefreshLayout]
     */
    override fun showProgress() {
        slHomeContainer?.isRefreshing = true
    }

    /**
     * Hides [SwipeRefreshLayout]
     */
    override fun hideProgress() {
        slHomeContainer?.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (slHomeContainer?.isRefreshing == true) {
            slHomeContainer?.isRefreshing = false
            slHomeContainer?.removeAllViews()
        }
        presenter?.detachView()
    }

    companion object {
        val LOG_TAG: String? = HomeFragment::class.java.simpleName
        fun newInstance(): HomeOldFragment {
            return HomeOldFragment()
        }
    }
}