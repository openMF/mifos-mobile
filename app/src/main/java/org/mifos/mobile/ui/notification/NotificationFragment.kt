package org.mifos.mobile.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.mifos.mobile.BuildConfig
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.databinding.FragmentNotificationBinding
import org.mifos.mobile.models.notification.MifosNotification
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.adapters.NotificationAdapter
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.DividerItemDecoration
import org.mifos.mobile.utils.Network
import javax.inject.Inject

/**
 * Created by dilpreet on 13/9/17.
 */
@AndroidEntryPoint
class NotificationFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        (activity as? BaseActivity)?.hideToolbar()
        return mifosComposeView(requireContext()) {
            NotificationScreen(
                navigateBack = { activity?.onBackPressed() }
            )
        }
    }

    companion object {
        fun newInstance(): NotificationFragment {
            return NotificationFragment()
        }
    }
}
