package org.mifos.mobile.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.reactivex.disposables.Disposable
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentGuarantorDetailBinding
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
*/ class GuarantorDetailFragment : BaseFragment(), GuarantorDetailView {

    private var _binding: FragmentGuarantorDetailBinding? = null
    private val binding get() = _binding!!

    @JvmField
    @Inject
    var presenter: GuarantorDetailPresenter? = null
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGuarantorDetailBinding.inflate(inflater, container, false)
        setToolbarTitle(getString(R.string.guarantor_details))
        setHasOptionsMenu(true)
        (activity as BaseActivity?)?.activityComponent?.inject(this)
        if (isFirstTime) {
            isFirstTime = false
            setUpRxBus()
        }
        presenter?.attachView(this)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.tvFirstName.text = payload?.firstname
        binding.tvLastName.text = payload?.lastname
        binding.tvGuarantorType.text = payload?.guarantorType?.value
        binding.tvJoinedDate.text = DateHelper.getDateAsString(payload?.joinedDate)
        binding.tvOfficeName.text = payload?.officeName
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
                .setMessage(
                    getString(
                        R.string.dialog_are_you_sure_that_you_want_to_string,
                        getString(R.string.delete_guarantor),
                    ),
                )
                .setPositiveButton(
                    getString(R.string.yes),
                ) { _, _ ->
                    presenter?.deleteGuarantor(loanId, guarantorId)
                }
                .setNegativeButton(R.string.cancel)
                .createMaterialDialog()
                .show()

            R.id.menu_update_guarantor -> (activity as BaseActivity?)?.replaceFragment(
                AddGuarantorFragment.newInstance(index, GuarantorState.UPDATE, payload, loanId),
                true,
                R.id.container,
            )
        }
        return super.onOptionsItemSelected(item)
    }

    override fun guarantorDeletedSuccessfully(message: String?) {
        activity?.supportFragmentManager?.popBackStack()
        publish(DeleteGuarantorEvent(index))
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: String?) {
        Toaster.show(binding.root, message)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(
            index: Int,
            loanId: Long,
            payload: GuarantorPayload?,
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
