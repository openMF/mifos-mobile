package org.mifos.mobile.ui.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import io.reactivex.disposables.Disposable
import org.mifos.mobile.R
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.presenters.GuarantorDetailPresenter
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.enums.GuarantorState
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.views.GuarantorDetailView
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.DateHelper
import org.mifos.mobile.utils.MaterialDialog
import org.mifos.mobile.utils.RxBus.listen
import org.mifos.mobile.utils.RxBus.publish
import org.mifos.mobile.utils.RxEvent.DeleteGuarantorEvent
import org.mifos.mobile.utils.RxEvent.UpdateGuarantorEvent
import org.mifos.mobile.utils.Toaster
import javax.inject.Inject

/*
* Created by saksham on 24/July/2018
*/   class GuarantorDetailFragment : BaseFragment(), GuarantorDetailView {
    @kotlin.jvm.JvmField
    @BindView(R.id.tv_first_name)
    var tvFirstName: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_last_name)
    var tvLastName: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_joined_date)
    var tvJoinedDate: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_guarantor_type)
    var tvGuarantorType: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_office_name)
    var tvOfficeName: TextView? = null

    @kotlin.jvm.JvmField
    @Inject
    var presenter: GuarantorDetailPresenter? = null
    var rootView: View? = null
    var loanId: Long? = 0
    private var guarantorId: Long? = 0
    var index: Int? = 0
    var payload: GuarantorPayload? = null
    var disposableUpdateGuarantor: Disposable? = null
    var isFirstTime = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            loanId = arguments?.getLong(Constants.LOAN_ID)
            payload = arguments?.getParcelable(Constants.GUARANTOR_DETAILS)
            index = arguments?.getInt(Constants.INDEX)
            guarantorId = payload?.id
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_guarantor_detail, container, false)
        setToolbarTitle(getString(R.string.guarantor_details))
        setHasOptionsMenu(true)
        ButterKnife.bind(this, rootView!!)
        (activity as BaseActivity?)?.activityComponent?.inject(this)
        if (isFirstTime) {
            isFirstTime = false
            setUpRxBus()
        }
        presenter?.attachView(this)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvFirstName?.text = payload?.firstname
        tvLastName?.text = payload?.lastname
        tvGuarantorType?.text = payload?.guarantorType?.value
        tvJoinedDate?.text = DateHelper.getDateAsString(payload?.joinedDate)
        tvOfficeName?.text = payload?.officeName
    }

    private fun setUpRxBus() {
        disposableUpdateGuarantor = listen(UpdateGuarantorEvent::class.java)
                .subscribe { (payload1) ->
                    payload?.firstname = payload1?.firstName
                    payload?.lastname = payload1?.lastName
                    payload?.guarantorType = payload1
                            ?.guarantorType
                    payload?.officeName = payload1?.officeName
                }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_guarantor, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_guarantor -> MaterialDialog.Builder()
                    .init(context)
                    .setTitle(R.string.delete_guarantor)
                    .setMessage(getString(R.string.dialog_are_you_sure_that_you_want_to_string,
                            getString(R.string.delete_guarantor)))
                    .setPositiveButton(getString(R.string.yes),
                            DialogInterface.OnClickListener { _, _ ->
                                presenter?.deleteGuarantor(loanId, guarantorId)
                            })
                    .setNegativeButton(R.string.cancel)
                    .createMaterialDialog()
                    .show()
            R.id.menu_update_guarantor -> (activity as BaseActivity?)?.replaceFragment(AddGuarantorFragment.Companion.newInstance(index, GuarantorState.UPDATE, payload, loanId),
                    true, R.id.container)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun guarantorDeletedSuccessfully(message: String?) {
        activity?.supportFragmentManager?.popBackStack()
        publish(DeleteGuarantorEvent(index))
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: String?) {
        Toaster.show(rootView, message)
    }

    override fun showProgress() {
        showProgressBar()
    }

    override fun hideProgress() {
        hideProgressBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
        if (disposableUpdateGuarantor?.isDisposed == false) {
            disposableUpdateGuarantor?.dispose()
        }
        hideProgressBar()
    }

    companion object {
        fun newInstance(
                index: Int, loanId: Long,
                payload: GuarantorPayload?
        ): GuarantorDetailFragment {
            val fragment = GuarantorDetailFragment()
            val args = Bundle()
            args.putLong(Constants.LOAN_ID, loanId)
            args.putParcelable(Constants.GUARANTOR_DETAILS, payload)
            args.putInt(Constants.INDEX, index)
            fragment.arguments = args
            return fragment
        }
    }
}