package org.mifos.mobile.ui.user_profile

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.core.common.Constants.USER_DETAILS
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.update_password.UpdatePasswordFragment
import org.mifos.mobile.core.datastore.PreferencesHelper
import org.mifos.mobile.core.model.entity.client.Client
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.core.common.utils.ParcelableAndSerializableUtils.getCheckedParcelable
import org.mifos.mobile.feature.user_profile.screens.UserProfileScreen
import org.mifos.mobile.feature.user_profile.viewmodel.UserDetailViewModel
import javax.inject.Inject

/**
 * Created by dilpreet on 10/7/17.
 */
@AndroidEntryPoint
class UserProfileComposeFragment : BaseFragment() {

    private val viewModel: UserDetailViewModel by viewModels()

    @JvmField
    @Inject
    var preferencesHelper: PreferencesHelper? = null
    private var client: Client? = null
    private var userBitmap : Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        (activity as BaseActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MifosMobileTheme {
                    UserProfileScreen(
                        navigateBack = { backToHome() },
                        changePassword = { changePassword() },
                    )
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(USER_DETAILS, client)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            client = savedInstanceState.getCheckedParcelable(
                Client::class.java,
                USER_DETAILS
            )
            userBitmap = viewModel.getUserProfile(preferencesHelper?.userProfileImage)
        }
    }

    private fun changePassword() {
        (activity as BaseActivity?)?.replaceFragment(
            UpdatePasswordFragment.newInstance(),
            true,
            R.id.container,
        )
    }

    private fun backToHome() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    companion object {
        @JvmStatic
        fun newInstance(): UserProfileComposeFragment {
            return UserProfileComposeFragment()
        }
    }
}