package org.mifos.mobile.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.databinding.FragmentSavingsAccountApplicationBinding
import org.mifos.mobile.models.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.models.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.models.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.models.templates.savings.ProductOptions
import org.mifos.mobile.models.templates.savings.SavingsAccountTemplate
import org.mifos.mobile.ui.enums.SavingsAccountState
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.DateHelper
import org.mifos.mobile.utils.SavingsAccountUiState
import org.mifos.mobile.utils.Toaster
import org.mifos.mobile.utils.getTodayFormatted
import org.mifos.mobile.viewModels.SavingsAccountApplicationViewModel
import javax.inject.Inject

/*
* Created by saksham on 30/June/2018
*/
@AndroidEntryPoint
class SavingsAccountApplicationFragment : BaseFragment() {

    private var _binding: FragmentSavingsAccountApplicationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SavingsAccountApplicationViewModel by viewModels()


    @JvmField
    @Inject
    var preferencesHelper: PreferencesHelper? = null
    private var state: SavingsAccountState? = null
    private var savingsWithAssociations: SavingsWithAssociations? = null
    private var template: SavingsAccountTemplate? = null
    private var productOptions: List<ProductOptions>? = null
    private val productIdList: MutableList<String?> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            state = requireArguments()
                .getSerializable(Constants.SAVINGS_ACCOUNT_STATE) as SavingsAccountState
            savingsWithAssociations = arguments?.getParcelable(Constants.SAVINGS_ACCOUNTS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSavingsAccountApplicationBinding.inflate(inflater)
        viewModel.loadSavingsAccountApplicationTemplate(preferencesHelper?.clientId, state)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSubmit.setOnClickListener {
            onSubmit()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.savingsAccountApplicationUiState.collect { state ->
                    when (state) {
                        SavingsAccountUiState.Loading -> showProgress()

                        SavingsAccountUiState.HideProgress -> hideProgress()

                        is SavingsAccountUiState.ShowUserInterfaceSavingAccountApplication -> {
                            hideProgress()
                            showUserInterfaceSavingAccountApplication(state.template)
                        }

                        is SavingsAccountUiState.ShowUserInterfaceSavingAccountUpdate -> {
                            hideProgress()
                            showUserInterfaceSavingAccountUpdate(state.template)
                        }

                        is SavingsAccountUiState.ErrorMessage -> {
                            hideProgress()
                            showError(state.error.message ?: "")
                        }

                        SavingsAccountUiState.SavingsAccountApplicationSuccess -> {
                            hideProgress()
                            showSavingsAccountApplicationSuccessfully()
                        }

                        SavingsAccountUiState.SavingsAccountUpdateSuccess -> {
                            hideProgress()
                            showSavingsAccountUpdateSuccessfully()
                        }

                        is SavingsAccountUiState.Initial -> {}

                        else -> throw IllegalStateException("Unexpected state : $state")
                    }

                }
            }
        }
    }

    private fun showUserInterfaceSavingAccountApplication(template: SavingsAccountTemplate?) {
        showUserInterface(template)
    }

    private fun showSavingsAccountApplicationSuccessfully() {
        showMessage(getString(R.string.new_saving_account_created_successfully))
        activity?.finish()
    }

    private fun showUserInterfaceSavingAccountUpdate(template: SavingsAccountTemplate?) {
        showUserInterface(template)
        activity?.title = getString(
            R.string.string_savings_account,
            getString(R.string.update),
        )
        binding.productIdField.setText(savingsWithAssociations?.savingsProductName!!, false)
    }

    private fun showSavingsAccountUpdateSuccessfully() {
        showMessage(getString(R.string.saving_account_updated_successfully))
        activity?.supportFragmentManager?.popBackStack()
    }

    fun showUserInterface(template: SavingsAccountTemplate?) {
        this.template = template
        productOptions = template?.productOptions
        if (productOptions != null) {
            for ((_, name) in productOptions as ArrayList<ProductOptions>) {
                productIdList.add(name)
            }
        }
        binding.tvClientName.text = template?.clientName
        binding.productIdField.setSimpleItems(productIdList.toTypedArray())
        binding.tvSubmissionDate.text = getTodayFormatted()
    }

    private fun submitSavingsAccountApplication() {
        val payload = SavingsAccountApplicationPayload()
        payload.clientId = template?.clientId

        if (productIdList.indexOf(binding.productIdField.text.toString()) != -1) {
            payload.productId = productIdList.indexOf(binding.productIdField.text.toString())
                .let { productOptions?.get(it)?.id }
        } else {
            Toaster.show(binding.root, getString(R.string.select_product_id))
            return
        }
        payload.submittedOnDate = DateHelper.getSpecificFormat(
            DateHelper.FORMAT_dd_MMMM_yyyy,
            getTodayFormatted(),
        )
        viewModel.submitSavingsAccountApplication(payload)
    }

    private fun updateSavingAccount() {
        val payload = SavingsAccountUpdatePayload()
        payload.clientId = template?.clientId?.toLong()
        payload.productId = productIdList.indexOf(binding.productIdField.text.toString())
            .let { productOptions?.get(it)?.id }?.toLong()
        viewModel.updateSavingsAccount(savingsWithAssociations?.id, payload)
    }

    private fun onSubmit() {
        if (state == SavingsAccountState.CREATE) {
            submitSavingsAccountApplication()
        } else {
            updateSavingAccount()
        }
    }

    fun showError(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    private fun showMessage(showMessage: String?) {
        Toast.makeText(context, showMessage, Toast.LENGTH_SHORT).show()
    }

    fun showProgress() {
        showMifosProgressDialog(getString(R.string.progress_message_loading))
    }

    fun hideProgress() {
        hideMifosProgressDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        hideProgress()
    }

    companion object {
        @JvmStatic
        fun newInstance(
            state: SavingsAccountState?,
            savingsWithAssociations: SavingsWithAssociations?,
        ): SavingsAccountApplicationFragment {
            val fragment = SavingsAccountApplicationFragment()
            val bundle = Bundle()
            bundle.putSerializable(Constants.SAVINGS_ACCOUNT_STATE, state)
            bundle.putParcelable(Constants.SAVINGS_ACCOUNTS, savingsWithAssociations)
            fragment.arguments = bundle
            return fragment
        }
    }
}
