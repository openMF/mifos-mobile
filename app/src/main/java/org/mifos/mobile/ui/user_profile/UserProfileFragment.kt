package org.mifos.mobile.ui.user_profile

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.models.client.Group
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.getThemeAttributeColor
import org.mifos.mobile.ui.update_password.UpdatePasswordFragment
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.DateHelper
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.ParcelableAndSerializableUtils.getCheckedParcelable
import org.mifos.mobile.utils.TextDrawable
import org.mifos.mobile.utils.Toaster
import org.mifos.mobile.utils.UserDetailUiState
import javax.inject.Inject

/**
 * Created by dilpreet on 10/7/17.
 */
@AndroidEntryPoint
class UserProfileFragment : BaseFragment() {

    private val viewModel: UserDetailViewModel by viewModels()
    private var userBitmap: Bitmap? = null

    @JvmField
    @Inject
    var preferencesHelper: PreferencesHelper? = null
    private var client: Client? = null

    private var userDetails by mutableStateOf(UserDetails())
    private var isOnline by mutableStateOf(false)

    private var bitmap by mutableStateOf<Bitmap?>(null)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        (activity as BaseActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState == null) {
            viewModel.userDetails
            viewModel.userImage
        }

        isOnline = Network.isConnected(context)

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MifosMobileTheme {
                    bitmap?.let {
                        UserProfileScreen(
                            userDetails = userDetails,
                            isOnline = isOnline,
                            bitmap = it,
                            changePassword = { changePassword() },
                            home = { backToHome() }
                        )
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.userDetailUiState.collect {
                when (it) {
                    is UserDetailUiState.Loading -> showProgress()
                    is UserDetailUiState.ShowUserDetails -> {
                        hideProgress()
                        showUserDetails(it.client)
                    }

                    is UserDetailUiState.ShowUserImage -> {
                        hideProgress()
                        showUserImage(it.image)
                    }

                    is UserDetailUiState.ShowError -> {
                        hideProgress()
                        showError(getString(it.message))
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(Constants.USER_DETAILS, client)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            client = savedInstanceState.getCheckedParcelable(
                Client::class.java,
                Constants.USER_DETAILS
            )
            viewModel.setUserProfile(preferencesHelper?.userProfileImage)
            showUserDetails(client)
        }
    }

    /**
     * Sets client basic details which are fetched from server
     *
     * @param client instance of [Client] which contains information about client
     */
    private fun showUserDetails(client: Client?): UserDetails {
        this.client = client
        val userName = nullFieldCheck(getString(R.string.username), client?.displayName)
        val accountNumber = nullFieldCheck(getString(R.string.account_number), client?.accountNo)
        val activationDate = nullFieldCheck(
            getString(R.string.activation_date),
            DateHelper.getDateAsString(client?.activationDate)
        )
        val officeName = nullFieldCheck(getString(R.string.office_name), client?.officeName)
        val clientType = nullFieldCheck(getString(R.string.client_type), client?.clientType?.name)
        val groups = nullFieldCheck(getString(R.string.groups), getGroups(client?.groups))
        val clientClassification = client?.clientClassification?.name ?: "-"
        val phoneNumber = nullFieldCheck(getString(R.string.phone_number), client?.mobileNo)
        val dob = if (client?.dobDate?.size != 3) {
            getString(R.string.no_dob_found)
        } else {
            DateHelper.getDateAsString(client.dobDate)
        }
        val gender = nullFieldCheck(getString(R.string.gender), client?.gender?.name)
        userDetails = UserDetails(
            userName,
            accountNumber,
            activationDate,
            officeName,
            clientType,
            groups,
            clientClassification,
            phoneNumber,
            dob,
            gender
        )
        return userDetails
    }

    private fun nullFieldCheck(field: String, value: String?): String {
        return value
            ?: getString(R.string.no) + getString(R.string.blank) + field +
            getString(R.string.blank) + getString(R.string.found)
    }

    /**
     * Generate a string for groups which the client is part of.
     *
     * @param groups [List] of [Group] which client is a part of.
     * @return Returns String of groups
     */
    private fun getGroups(groups: List<Group>?): String {
        if (groups?.isEmpty() == true) {
            return getString(
                R.string.not_assigned_with_any_group,
            ) // no groups entry in database for the
            // client
        }
        val builder = StringBuilder()
        if (groups != null) {
            for ((_, _, name) in groups) {
                builder.append(getString(R.string.string_and_string, name, " | "))
            }
        }
        return builder.toString().substring(0, builder.toString().length - 2)
    }

    /**
     * Provides with client Image fetched from the server in `bitmap`
     *
     * @param bitmap User Image
     */
    private fun showUserImage(bitmap: Bitmap?): Bitmap? {
        activity?.runOnUiThread {
            userBitmap = bitmap
            if (userBitmap == null) {
                val textDrawable = TextDrawable.builder()
                    .beginConfig()
                    .width(100)
                    .height(100)
                    .toUpperCase()
                    .endConfig()
                    .buildRound(
                        (
                                if (preferencesHelper?.clientName.isNullOrEmpty()) {
                                    preferencesHelper
                                        ?.userName
                                } else {
                                    preferencesHelper
                                        ?.clientName
                                }
                                )
                            ?.substring(0, 1),
                        requireContext().getThemeAttributeColor(R.attr.colorPrimaryVariant),
                    )
                this.bitmap = textDrawable.toBitmap()
            } else {
                this.bitmap = bitmap
            }
        }
        return this.bitmap
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

    /**
     * It is called whenever any error occurs while executing a request
     *
     * @param message Error message that tells the user about the problem.
     */
    fun showError(message: String?) {
        Toaster.show(view, message)
    }

    fun showProgress() {
        showProgressBar()
    }

    fun hideProgress() {
        hideProgressBar()
    }

    companion object {
        @JvmStatic
        fun newInstance(): UserProfileFragment {
            return UserProfileFragment()
        }
    }
}
