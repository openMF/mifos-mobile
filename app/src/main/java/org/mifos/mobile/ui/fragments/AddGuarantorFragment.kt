package org.mifos.mobile.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentAddGuarantorBinding
import org.mifos.mobile.models.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.models.guarantor.GuarantorTemplatePayload
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.enums.GuarantorState
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.GuarantorUiState
import org.mifos.mobile.utils.ParcelableAndSerializableUtils.getCheckedParcelable
import org.mifos.mobile.utils.ParcelableAndSerializableUtils.getCheckedSerializable
import org.mifos.mobile.utils.RxBus.publish
import org.mifos.mobile.utils.RxEvent.AddGuarantorEvent
import org.mifos.mobile.utils.RxEvent.UpdateGuarantorEvent
import org.mifos.mobile.utils.Toaster
import org.mifos.mobile.viewModels.AddGuarantorViewModel

/*
* Created by saksham on 23/July/2018
*/
@AndroidEntryPoint
class AddGuarantorFragment : BaseFragment() {

    private var _binding: FragmentAddGuarantorBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddGuarantorViewModel by viewModels()

    private var guarantorTypeAdapter: ArrayAdapter<String?>? = null
    var template: GuarantorTemplatePayload? = null
    var payload: GuarantorPayload? = null
    private var guarantorState: GuarantorState? = null
    private var guarantorApplicationPayload: GuarantorApplicationPayload? = null
    var loanId: Long? = 0
    private var index: Int? = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            loanId = arguments?.getLong(Constants.LOAN_ID)
            guarantorState = requireArguments().getCheckedSerializable(
                GuarantorState::class.java,
                Constants.GUARANTOR_STATE
            ) as GuarantorState
            payload =
                arguments?.getCheckedParcelable(GuarantorPayload::class.java, Constants.PAYLOAD)
            index = arguments?.getInt(Constants.INDEX)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddGuarantorBinding.inflate(inflater, container, false)
        if (guarantorState == GuarantorState.CREATE) {
            setToolbarTitle(getString(R.string.add_guarantor))
        } else if (guarantorState == GuarantorState.UPDATE) {
            setToolbarTitle(getString(R.string.update_guarantor))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.guarantorUiState.collect {
                    when (it) {
                        is GuarantorUiState.Loading -> showProgress()
                        is GuarantorUiState.ShowError -> {
                            hideProgress()
                            showError(it.message)
                        }

                        is GuarantorUiState.ShowGuarantorApplication -> {
                            hideProgress()
                            showGuarantorApplication(it.template)
                        }

                        is GuarantorUiState.ShowGuarantorUpdation -> {
                            hideProgress()
                            showGuarantorUpdation(it.template)
                        }

                        is GuarantorUiState.SubmittedSuccessfully -> {
                            hideProgress()
                            submittedSuccessfully(it.message, it.payload)
                        }

                        is GuarantorUiState.GuarantorUpdatedSuccessfully -> {
                            hideProgress()
                            updatedSuccessfully(it.message)
                        }

                        else -> throw IllegalStateException("Undesired $it")
                    }
                }
            }
        }

        binding.btnSubmitGuarantor.setOnClickListener {
            onSubmit()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getGuarantorTemplate(guarantorState, loanId)
    }

    private fun onSubmit() {
        with(binding) {
            tilFirstName.isErrorEnabled = false
            tilLastName.isErrorEnabled = false
            tilOfficeName.isErrorEnabled = false
        }
        if (isFieldsCompleted) {
            guarantorApplicationPayload = generatePayload()
            if (guarantorState == GuarantorState.CREATE) {
                viewModel.createGuarantor(loanId, guarantorApplicationPayload)
            } else if (guarantorState == GuarantorState.UPDATE) {
                viewModel.updateGuarantor(guarantorApplicationPayload, loanId, payload?.id)
            }
        }
    }

    private fun generatePayload(): GuarantorApplicationPayload {
        with(binding) {
            return GuarantorApplicationPayload(
                template?.guarantorTypeOptions
                    ?.get(getGuarantorTypeIndex(guarantorTypeField.editText?.text.toString())),
                tilFirstName.editText?.text.toString().trim { it <= ' ' },
                tilLastName.editText?.text.toString().trim { it <= ' ' },
                tilOfficeName.editText?.text.toString().trim { it <= ' ' },
            )
        }
    }

    private val isFieldsCompleted: Boolean
        get() {
            var rv = true
            if (binding.tilFirstName.editText?.text.toString().trim { it <= ' ' }.isEmpty()) {
                binding.tilFirstName.error = getString(
                    R.string.error_validation_blank,
                    getString(R.string.first_name),
                )
                rv = false
            }
            if (binding.tilLastName.editText?.text.toString().trim { it <= ' ' }.isEmpty()) {
                binding.tilLastName.error = getString(
                    R.string.error_validation_blank,
                    getString(R.string.last_name),
                )
                rv = false
            }
            if (binding.tilOfficeName.editText?.text.toString().trim { it <= ' ' }.isEmpty()) {
                binding.tilOfficeName.error = getString(
                    R.string.error_validation_blank,
                    getString(R.string.office_name),
                )
                rv = false
            }
            return rv
        }

    private fun getGuarantorTypeIndex(optionSelected: String): Int {
        var rv = 0
        for (option in template?.guarantorTypeOptions!!) {
            if (option.value == optionSelected) {
                return rv
            }
            rv++
        }
        return rv
    }

    fun showProgress() {
        showProgressBar()
    }

    fun hideProgress() {
        hideProgressBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        hideProgressBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showGuarantorApplication(template: GuarantorTemplatePayload?) {
        this.template = template
        setUpSpinner()
    }

    private fun showGuarantorUpdation(template: GuarantorTemplatePayload?) {
        this.template = template
        setUpSpinner()
        with(binding) {
            tilFirstName.editText?.setText(payload?.firstname)
            tilLastName.editText?.setText(payload?.lastname)
            tilOfficeName.editText?.setText(payload?.officeName)
            (guarantorTypeField.editText as? MaterialAutoCompleteTextView)?.setText(payload?.guarantorType?.value!!)
        }
    }

    private fun setUpSpinner() {
        val options: MutableList<String?> = ArrayList()
        for (option in template?.guarantorTypeOptions!!) {
            options.add(option.value)
        }
        guarantorTypeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            options,
        )
        binding.guarantorTypeField.let {
            (it.editText as MaterialAutoCompleteTextView).setSimpleItems(options.toTypedArray())
        }
    }

    private fun updatedSuccessfully(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        publish(UpdateGuarantorEvent(guarantorApplicationPayload, index))
        activity?.supportFragmentManager?.popBackStack()
    }

    private fun submittedSuccessfully(message: String?, payload: GuarantorApplicationPayload?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        publish(AddGuarantorEvent(payload, index))
        activity?.supportFragmentManager?.popBackStack()
    }

    fun showError(message: String?) {
        Toaster.show(binding.root, message)
    }

    override fun onResume() {
        super.onResume()
        (activity as? BaseActivity)?.showToolbar()
    }

    companion object {
        fun newInstance(
            index: Int?,
            guarantorState: GuarantorState?,
            payload: GuarantorPayload?,
            loanId: Long?,
        ): AddGuarantorFragment {
            val fragment = AddGuarantorFragment()
            val bundle = Bundle()
            if (loanId != null) bundle.putLong(Constants.LOAN_ID, loanId)
            bundle.putSerializable(Constants.GUARANTOR_STATE, guarantorState)
            if (index != null) bundle.putInt(Constants.INDEX, index)
            bundle.putParcelable(Constants.PAYLOAD, payload)
            fragment.arguments = bundle
            return fragment
        }
    }
}