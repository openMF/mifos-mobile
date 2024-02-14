package org.mifos.mobile.ui.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentBeneficiaryDetailBinding
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.enums.BeneficiaryState
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.BeneficiaryUiState
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.CurrencyUtil
import org.mifos.mobile.utils.MaterialDialog
import org.mifos.mobile.utils.ParcelableAndSerializableUtils.getCheckedParcelable
import org.mifos.mobile.utils.Toaster
import org.mifos.mobile.viewModels.BeneficiaryDetailViewModel

/**
 * Created by dilpreet on 15/6/17.
 */
@AndroidEntryPoint
class BeneficiaryDetailFragment : BaseFragment() {

    private var _binding: FragmentBeneficiaryDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BeneficiaryDetailViewModel by viewModels()

    private var beneficiary: Beneficiary? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            beneficiary =
                arguments?.getCheckedParcelable(Beneficiary::class.java, Constants.BENEFICIARY)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBeneficiaryDetailBinding.inflate(inflater, container, false)
        setToolbarTitle(getString(R.string.beneficiary_detail))
        showUserInterface()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.beneficiaryUiState.collect {
                    when (it) {
                        is BeneficiaryUiState.Loading -> showProgress()
                        is BeneficiaryUiState.ShowError -> {
                            hideProgress()
                            showError(getString(it.message))
                        }

                        is BeneficiaryUiState.DeletedSuccessfully -> {
                            hideProgress()
                            showBeneficiaryDeletedSuccessfully()
                        }

                        is BeneficiaryUiState.Initial -> {}

                        else -> throw IllegalStateException("Undesired $it")
                    }
                }
            }
        }
    }

    /**
     * Used for setting up of User Interface
     */
    fun showUserInterface() {
        with(binding) {
            tvBeneficiaryName.text = beneficiary?.name
            tvAccountNumber.text = beneficiary?.accountNumber
            tvClientName.text = beneficiary?.clientName
            tvAccountType.text = beneficiary?.accountType?.value
            tvTransferLimit.text =
                CurrencyUtil.formatCurrency(requireActivity(), beneficiary?.transferLimit!!)
            tvOfficeName.text = beneficiary?.officeName
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_beneficiary, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_update_beneficiary -> (activity as BaseActivity?)?.replaceFragment(
                BeneficiaryApplicationFragment.newInstance(BeneficiaryState.UPDATE, beneficiary),
                true,
                R.id.container,
            )

            R.id.item_delete_beneficiary -> MaterialDialog.Builder().init(activity)
                .setTitle(getString(R.string.delete_beneficiary))
                .setMessage(getString(R.string.delete_beneficiary_confirmation))
                .setPositiveButton(
                    getString(R.string.delete),
                    DialogInterface.OnClickListener { dialog, _ ->
                        dialog.dismiss()
                        viewModel.deleteBeneficiary(beneficiary?.id?.toLong())
                    },
                )
                .setNegativeButton(
                    getString(R.string.cancel),
                    DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() },
                )
                .createMaterialDialog()
                .show()
        }
        return true
    }

    /**
     * Shows a {@link Snackbar} on successfull deletion of a
     * Beneficiary and then pops current fragment
     */
    private fun showBeneficiaryDeletedSuccessfully() {
        Toaster.show(binding.root, getString(R.string.beneficiary_deleted_successfully))
        activity?.supportFragmentManager?.popBackStack()
    }

    /**
     * It is called whenever any error occurs while executing a request
     *
     * @param msg Error message that tells the user about the problem.
     */
    fun showError(msg: String?) {
        Toaster.show(binding.root, msg)
    }

    /**
     * Shows [org.mifos.mobile.utils.ProgressBarHandler]
     */
    fun showProgress() {
        showProgressBar()
    }

    /**
     * Hides [org.mifos.mobile.utils.ProgressBarHandler]
     */
    fun hideProgress() {
        hideProgressBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideProgress()
        _binding = null
    }

    companion object {
        fun newInstance(beneficiary: Beneficiary?): BeneficiaryDetailFragment {
            val fragment = BeneficiaryDetailFragment()
            val args = Bundle()
            args.putParcelable(Constants.BENEFICIARY, beneficiary)
            fragment.arguments = args
            return fragment
        }
    }
}
