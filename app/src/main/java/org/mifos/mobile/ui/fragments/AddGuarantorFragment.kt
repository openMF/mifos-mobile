package org.mifos.mobile.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.textfield.TextInputLayout
import org.mifos.mobile.R
import org.mifos.mobile.models.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.models.guarantor.GuarantorTemplatePayload
import org.mifos.mobile.presenters.AddGuarantorPresenter
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.enums.GuarantorState
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.views.AddGuarantorView
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.RxBus.publish
import org.mifos.mobile.utils.RxEvent.AddGuarantorEvent
import org.mifos.mobile.utils.RxEvent.UpdateGuarantorEvent
import org.mifos.mobile.utils.Toaster
import java.util.*
import javax.inject.Inject

/*
* Created by saksham on 23/July/2018
*/   class AddGuarantorFragment : BaseFragment(), AddGuarantorView {
    @kotlin.jvm.JvmField
    @BindView(R.id.sp_guarantor_type)
    var spGuarantorType: Spinner? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.til_first_name)
    var tilFirstName: TextInputLayout? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.til_last_name)
    var tilLastName: TextInputLayout? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.til_office_name)
    var tilOfficeName: TextInputLayout? = null

    @kotlin.jvm.JvmField
    @Inject
    var presenter: AddGuarantorPresenter? = null
    var guarantorTypeAdapter: ArrayAdapter<String?>? = null
    var template: GuarantorTemplatePayload? = null
    var payload: GuarantorPayload? = null
    var guarantorState: GuarantorState? = null
    var guarantorApplicationPayload: GuarantorApplicationPayload? = null
    var rootView: View? = null
    var loanId: Long = 0
    var index = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            loanId = arguments!!.getLong(Constants.LOAN_ID)
            guarantorState = arguments!!
                    .getSerializable(Constants.GUARANTOR_STATE) as GuarantorState
            payload = arguments!!.getParcelable(Constants.PAYLOAD)
            index = arguments!!.getInt(Constants.INDEX)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_add_guarantor, container, false)
        (activity as BaseActivity?)!!.activityComponent!!.inject(this)
        ButterKnife.bind(this, rootView!!)
        presenter!!.attachView(this)
        if (guarantorState == GuarantorState.CREATE) {
            setToolbarTitle(getString(R.string.add_guarantor))
        } else if (guarantorState == GuarantorState.UPDATE) {
            setToolbarTitle(getString(R.string.update_guarantor))
        }
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter!!.getGuarantorTemplate(guarantorState, loanId)
    }

    @OnClick(R.id.btn_submit_guarantor)
    fun onSubmit() {
        tilFirstName!!.isErrorEnabled = false
        tilLastName!!.isErrorEnabled = false
        tilOfficeName!!.isErrorEnabled = false
        if (isFieldsCompleted) {
            guarantorApplicationPayload = generatePayload()
            if (guarantorState == GuarantorState.CREATE) {
                presenter!!.createGuarantor(loanId, guarantorApplicationPayload)
            } else if (guarantorState == GuarantorState.UPDATE) {
                presenter!!.updateGuarantor(guarantorApplicationPayload, loanId, payload!!.id)
            }
        }
    }

    private fun generatePayload(): GuarantorApplicationPayload {
        return GuarantorApplicationPayload(
                template!!.guarantorTypeOptions!!
                        .get(getGuarantorTypeIndex(spGuarantorType!!.selectedItem.toString())),
                tilFirstName!!.editText!!.text.toString().trim { it <= ' ' },
                tilLastName!!.editText!!.text.toString().trim { it <= ' ' },
                tilOfficeName!!.editText!!.text.toString().trim { it <= ' ' }
        )
    }

    private val isFieldsCompleted: Boolean
        private get() {
            var rv = true
            if (tilFirstName!!.editText!!.text.toString().trim { it <= ' ' }.length == 0) {
                tilFirstName!!.error = getString(R.string.error_validation_blank,
                        getString(R.string.first_name))
                rv = false
            }
            if (tilLastName!!.editText!!.text.toString().trim { it <= ' ' }.length == 0) {
                tilLastName!!.error = getString(R.string.error_validation_blank,
                        getString(R.string.last_name))
                rv = false
            }
            if (tilOfficeName!!.editText!!.text.toString().trim { it <= ' ' }.length == 0) {
                tilOfficeName!!.error = getString(R.string.error_validation_blank,
                        getString(R.string.office_name))
                rv = false
            }
            return rv
        }

    private fun getGuarantorTypeIndex(optionSelected: String): Int {
        var rv = 0
        for (option in template!!.guarantorTypeOptions!!) {
            if (option.value == optionSelected) {
                return rv
            }
            rv++
        }
        return rv
    }

    override fun showProgress() {
        showProgressBar()
    }

    override fun hideProgress() {
        hideProgressBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter!!.detachView()
        hideProgressBar()
    }

    override fun showGuarantorApplication(template: GuarantorTemplatePayload?) {
        this.template = template
        setUpSpinner()
    }

    override fun showGuarantorUpdation(template: GuarantorTemplatePayload?) {
        this.template = template
        setUpSpinner()
        tilFirstName!!.editText!!.setText(payload!!.firstname)
        tilLastName!!.editText!!.setText(payload!!.lastname)
        tilOfficeName!!.editText!!.setText(payload!!.officeName)
        spGuarantorType!!.setSelection(findPreviouslySelectedGuarantorType(payload!!.guarantorType
                ?.value))
    }

    private fun findPreviouslySelectedGuarantorType(value: String?): Int {
        var rv = 0
        var counter = 0
        for (option in template!!.guarantorTypeOptions!!) {
            if (option.value == value) {
                rv = counter
            }
            counter++
        }
        return rv
    }

    private fun setUpSpinner() {
        val options: MutableList<String?> = ArrayList()
        for (option in template!!.guarantorTypeOptions!!) {
            options.add(option.value)
        }
        guarantorTypeAdapter = ArrayAdapter(context,
                android.R.layout.simple_spinner_dropdown_item, options)
        spGuarantorType!!.adapter = guarantorTypeAdapter
    }

    override fun updatedSuccessfully(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        publish(UpdateGuarantorEvent(guarantorApplicationPayload!!, index))
        activity!!.supportFragmentManager.popBackStack()
    }

    override fun submittedSuccessfully(message: String?, payload: GuarantorApplicationPayload?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        publish(AddGuarantorEvent(payload!!, index))
        activity!!.supportFragmentManager.popBackStack()
    }

    override fun showError(message: String?) {
        Toaster.show(rootView, message)
    }

    companion object {
        fun newInstance(index: Int, guarantorState: GuarantorState?,
                        payload: GuarantorPayload?, loanId: Long): AddGuarantorFragment {
            val fragment = AddGuarantorFragment()
            val bundle = Bundle()
            bundle.putLong(Constants.LOAN_ID, loanId)
            bundle.putSerializable(Constants.GUARANTOR_STATE, guarantorState)
            bundle.putParcelable(Constants.PAYLOAD, payload)
            bundle.putInt(Constants.INDEX, index)
            fragment.arguments = bundle
            return fragment
        }
    }
}