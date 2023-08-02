package org.mifos.mobile.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.BuildConfig
import org.mifos.mobile.utils.NotificationUiState
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentNotificationBinding
import org.mifos.mobile.models.notification.MifosNotification
import org.mifos.mobile.ui.adapters.NotificationAdapter
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.DividerItemDecoration
import org.mifos.mobile.utils.Network
import org.mifos.mobile.viewModels.NotificationViewModel
import javax.inject.Inject

/**
 * Created by dilpreet on 13/9/17.
 */
@AndroidEntryPoint
class NotificationFragment : BaseFragment(), OnRefreshListener {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : NotificationViewModel

    @JvmField
    @Inject
    var adapter: NotificationAdapter? = null
    private var sweetUIErrorHandler: SweetUIErrorHandler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarTitle(getString(R.string.notification))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        val rootView = binding.root
        viewModel = ViewModelProvider(this)[NotificationViewModel::class.java]
        sweetUIErrorHandler = SweetUIErrorHandler(activity, rootView)
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvNotifications.layoutManager = layoutManager
        binding.rvNotifications.addItemDecoration(
            DividerItemDecoration(
                activity,
                layoutManager.orientation,
            ),
        )
        binding.rvNotifications.adapter = adapter
        binding.swipeNotificationContainer.setColorSchemeResources(
            R.color.blue_light,
            R.color.green_light,
            R.color.orange_light,
            R.color.red_light,
        )
        binding.swipeNotificationContainer.setOnRefreshListener(this)
        loadNotifications()
        return rootView
    }

    private fun showNotifications(notifications: List<MifosNotification?>?) {
        if (BuildConfig.DEBUG && notifications == null) {
            error("Assertion failed")
        }
        if (notifications?.isNotEmpty() == true) {
            adapter?.setNotificationList(notifications as List<MifosNotification?>?)
        } else {
            sweetUIErrorHandler?.showSweetEmptyUI(
                getString(R.string.notification),
                R.drawable.ic_notifications,
                binding.rvNotifications,
                binding.layoutError.root,
            )
        }
    }

    fun showError(msg: String?) {
        if (!Network.isConnected(activity)) {
            sweetUIErrorHandler?.showSweetNoInternetUI(
                binding.rvNotifications,
                binding.layoutError.root,
            )
        } else {
            sweetUIErrorHandler?.showSweetErrorUI(
                msg,
                binding.rvNotifications,
                binding.layoutError.root,
            )
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.notificationUiState.observe(viewLifecycleOwner) { state ->
            when(state) {
                NotificationUiState.Loading -> showProgress()
                NotificationUiState.Error -> {
                    hideProgress()
                    showError(context?.getString(R.string.notification))
                }
                is NotificationUiState.LoadNotificationsSuccessful -> {
                    hideProgress()
                    showNotifications(state.notifications)
                }
            }
        }

        binding.layoutError.btnTryAgain.setOnClickListener {
            retryClicked()
        }
    }

    private fun loadNotifications() {
        viewModel.loadNotifications()
    }

    private fun retryClicked() {
        if (Network.isConnected(context)) {
            sweetUIErrorHandler?.hideSweetErrorLayoutUI(
                binding.rvNotifications,
                binding.layoutError.root,
            )
            loadNotifications()
        } else {
            Toast.makeText(
                context,
                getString(R.string.internet_not_connected),
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    fun showProgress() {
        binding.swipeNotificationContainer.isRefreshing = true
    }

    fun hideProgress() {
        binding.swipeNotificationContainer.isRefreshing = false
    }

    override fun onRefresh() {
        sweetUIErrorHandler?.hideSweetErrorLayoutUI(
            binding.rvNotifications,
            binding.layoutError.root,
        )
        loadNotifications()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance(): NotificationFragment {
            return NotificationFragment()
        }
    }
}
