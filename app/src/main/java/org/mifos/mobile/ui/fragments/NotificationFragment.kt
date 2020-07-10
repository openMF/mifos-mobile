package org.mifos.mobile.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler
import org.mifos.mobile.R
import org.mifos.mobile.models.notification.MifosNotification
import org.mifos.mobile.presenters.NotificationPresenter
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.adapters.NotificationAdapter
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.views.NotificationView
import org.mifos.mobile.utils.DividerItemDecoration
import org.mifos.mobile.utils.Network
import javax.inject.Inject

/**
 * Created by dilpreet on 13/9/17.
 */
class NotificationFragment : BaseFragment(), NotificationView, OnRefreshListener {
    @kotlin.jvm.JvmField
    @BindView(R.id.rv_notifications)
    var rvNotification: RecyclerView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.swipe_notification_container)
    var swipeNotificationContainer: SwipeRefreshLayout? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.layout_error)
    var layoutError: View? = null

    @kotlin.jvm.JvmField
    @Inject
    var presenter: NotificationPresenter? = null

    @kotlin.jvm.JvmField
    @Inject
    var adapter: NotificationAdapter? = null
    private var rootView: View? = null
    private var sweetUIErrorHandler: SweetUIErrorHandler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarTitle(getString(R.string.notification))
        (activity as BaseActivity?)!!.activityComponent!!.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_notification, container, false)
        ButterKnife.bind(this, rootView!!)
        sweetUIErrorHandler = SweetUIErrorHandler(activity, rootView)
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvNotification!!.layoutManager = layoutManager
        rvNotification!!.addItemDecoration(DividerItemDecoration(activity,
                layoutManager.orientation))
        rvNotification!!.adapter = adapter
        swipeNotificationContainer!!.setColorSchemeResources(R.color.blue_light,
                R.color.green_light, R.color.orange_light, R.color.red_light)
        swipeNotificationContainer!!.setOnRefreshListener(this)
        presenter!!.attachView(this)
        presenter!!.loadNotifications()
        return rootView
    }

    override fun showNotifications(notifications: List<MifosNotification?>?) {
        assert(notifications != null)
        if (notifications!!.size != 0) {
            adapter!!.setNotificationList(notifications as List<MifosNotification?>?)
        } else {
            sweetUIErrorHandler!!.showSweetEmptyUI(getString(R.string.notification),
                    R.drawable.ic_notifications, rvNotification, layoutError)
        }
    }

    override fun showError(msg: String?) {
        if (!Network.isConnected(activity)) {
            sweetUIErrorHandler!!.showSweetNoInternetUI(rvNotification, layoutError)
        } else {
            sweetUIErrorHandler!!.showSweetErrorUI(msg,
                    rvNotification, layoutError)
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
        }
    }

    @OnClick(R.id.btn_try_again)
    fun retryClicked() {
        if (Network.isConnected(context)) {
            sweetUIErrorHandler!!.hideSweetErrorLayoutUI(rvNotification, layoutError)
            presenter!!.loadNotifications()
        } else {
            Toast.makeText(context, getString(R.string.internet_not_connected),
                    Toast.LENGTH_SHORT).show()
        }
    }

    override fun showProgress() {
        swipeNotificationContainer!!.isRefreshing = true
    }

    override fun hideProgress() {
        swipeNotificationContainer!!.isRefreshing = false
    }

    override fun onRefresh() {
        sweetUIErrorHandler!!.hideSweetErrorLayoutUI(rvNotification, layoutError)
        presenter!!.loadNotifications()
    }

    companion object {
        @kotlin.jvm.JvmStatic
        fun newInstance(): NotificationFragment {
            return NotificationFragment()
        }
    }
}