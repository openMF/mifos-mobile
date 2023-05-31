package org.mifos.mobile.ui.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler
import org.mifos.mobile.R
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.databinding.FragmentUserProfileBinding
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.models.client.Group
import org.mifos.mobile.presenters.UserDetailsPresenter
import org.mifos.mobile.ui.activities.EditUserDetailActivity
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.getThemeAttributeColor
import org.mifos.mobile.ui.views.UserDetailsView
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.DateHelper
import org.mifos.mobile.utils.TextDrawable
import org.mifos.mobile.utils.Toaster
import javax.inject.Inject

/**
 * Created by dilpreet on 10/7/17.
 */
class UserProfileFragment : BaseFragment(), UserDetailsView {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    @kotlin.jvm.JvmField
    @Inject
    var presenter: UserDetailsPresenter? = null

    @kotlin.jvm.JvmField
    @Inject
    var preferencesHelper: PreferencesHelper? = null
    private var userBitmap: Bitmap? = null
    private var client: Client? = null
    private var sweetUIErrorHandler: SweetUIErrorHandler? = null
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserProfileBinding.inflate(inflater,container,false)
        (activity as BaseActivity?)?.activityComponent?.inject(this)
        presenter?.attachView(this)
        (activity as BaseActivity?)?.setSupportActionBar(binding.toolbar) // check this part before pushing
        (activity as BaseActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        sweetUIErrorHandler = SweetUIErrorHandler(activity, binding.root)
        if (savedInstanceState == null) {
            presenter?.userDetails
            presenter?.userImage
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnChangePassword.setOnClickListener {
            changePassword()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(Constants.USER_DETAILS, client)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            client = savedInstanceState.getParcelable(Constants.USER_DETAILS)
            presenter?.setUserProfile(preferencesHelper?.userProfileImage)
            showUserDetails(client)
        }
    }

    /**
     * Sets client basic details which are fetched from server
     *
     * @param client instance of [Client] which contains information about client
     */
    override fun showUserDetails(client: Client?) {
        this.client = client
        binding.tvUserName.text = nullFieldCheck(getString(R.string.username), client?.displayName)
        binding.tvAccountNumber.text = nullFieldCheck(getString(R.string.account_number),
                client?.accountNo)
        binding.tvActivationDate.text = nullFieldCheck(getString(R.string.activation_date),
                DateHelper.getDateAsString(client?.activationDate))
        binding.tvOfficeName.text = nullFieldCheck(getString(R.string.office_name),
                client?.officeName)
        binding.tvClientType.text = nullFieldCheck(getString(R.string.client_type),
                client?.clientType?.name)
        binding.tvGroups.text = nullFieldCheck(getString(R.string.groups),
                getGroups(client?.groups))
        binding.tvClientClassification.text = client?.clientClassification?.name ?: "-"
        binding.tvPhoneNumber.text = nullFieldCheck(getString(R.string.phone_number),
                client?.mobileNo)
        if (client?.dobDate?.size != 3) {  // no data entry in database for the client
            binding.tvDob.text = getString(R.string.no_dob_found)
        } else {
            binding.tvDob.text = DateHelper.getDateAsString(client.dobDate)
        }
        binding.tvGender.text = nullFieldCheck(getString(R.string.gender), client?.gender?.name)
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
                    R.string.not_assigned_with_any_group) // no groups entry in database for the
            // client
        }
        val builder = StringBuilder()
        if (groups != null)
            for ((_, _, name) in groups) {
                builder.append(getString(R.string.string_and_string, name, " | "))
            }
        return builder.toString().substring(0, builder.toString().length - 2)
    }

    /**
     * Provides with client Image fetched from the server in `bitmap`
     *
     * @param bitmap User Image
     */
    override fun showUserImage(bitmap: Bitmap?) {
        activity?.runOnUiThread {
            userBitmap = bitmap
            if (userBitmap == null) {
                val textDrawable = TextDrawable.builder()
                        .beginConfig()
                        .toUpperCase()
                        .endConfig()
                        .buildRound(
                                (if (preferencesHelper?.clientName.isNullOrEmpty()) preferencesHelper
                                        ?.userName
                                else preferencesHelper
                                        ?.clientName)
                                        ?.substring(0, 1),
                                requireContext().getThemeAttributeColor(R.attr.colorPrimaryVariant))
                binding.ivProfile.setImageDrawable(textDrawable)
            } else {
                binding.ivProfile.setImageBitmap(bitmap)
            }
        }
    }

    fun changePassword() {
        startActivity(Intent(context, EditUserDetailActivity::class.java))
    }

    /**
     * It is called whenever any error occurs while executing a request
     *
     * @param message Error message that tells the user about the problem.
     */
    override fun showError(message: String?) {
        Toaster.show(binding.root, message)
        sweetUIErrorHandler?.showSweetCustomErrorUI(getString(R.string.error_fetching_user_profile),
                R.drawable.ic_error_black_24dp, binding.appBarLayout,
                binding.layoutError.root)
        binding.fabEdit.visibility = View.GONE
    }

    override fun showProgress() {
        showProgressBar()
    }

    override fun hideProgress() {
        hideProgressBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideProgress()
        presenter?.detachView()
        _binding = null
    }

    companion object {
        @kotlin.jvm.JvmStatic
        fun newInstance(): UserProfileFragment {
            return UserProfileFragment()
        }
    }
}