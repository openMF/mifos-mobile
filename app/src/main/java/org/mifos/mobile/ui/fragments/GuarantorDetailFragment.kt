package org.mifos.mobile.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentGuarantorDetailBinding
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.enums.GuarantorState
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.*
import org.mifos.mobile.utils.RxBus.listen
import org.mifos.mobile.utils.RxBus.publish
import org.mifos.mobile.utils.RxEvent.DeleteGuarantorEvent
import org.mifos.mobile.utils.RxEvent.UpdateGuarantorEvent
import org.mifos.mobile.viewModels.GuarantorDetailViewModel

/*
* Created by saksham on 24/July/2018
*/
@AndroidEntryPoint
class GuarantorDetailFragment : BaseFragment() {

    private var _binding: FragmentGuarantorDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: GuarantorDetailViewModel

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
        viewModel = ViewModelProvider(this)[GuarantorDetailViewModel::class.java]
        setToolbarTitle(getString(R.string.guarantor_details))
        setHasOptionsMenu(true)
        if (isFirstTime) {
            isFirstTime = false
            setUpRxBus()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.guarantorUiState.observe(viewLifecycleOwner) {
            when (it) {
                is GuarantorUiState.Loading -> showProgress()
                is GuarantorUiState.ShowError -> {
                    hideProgress()
                    showError(it.message)
                }
                is GuarantorUiState.GuarantorDeletedSuccessfully -> {
                    hideProgress()
                    guarantorDeletedSuccessfully(it.message)
                }
                else -> throw IllegalStateException("Undesired $it")
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(binding) {
            tvFirstName.text = payload?.firstname
            tvLastName.text = payload?.lastname
            tvGuarantorType.text = payload?.guarantorType?.value
            tvJoinedDate.text = DateHelper.getDateAsString(payload?.joinedDate)
            tvOfficeName.text = payload?.officeName
        }

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
                    viewModel.deleteGuarantor(loanId, guarantorId)
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

    fun guarantorDeletedSuccessfully(message: String?) {
        activity?.supportFragmentManager?.popBackStack()
        publish(DeleteGuarantorEvent(index))
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showError(message: String?) {
        Toaster.show(binding.root, message)
    }

    fun showProgress() {
        showProgressBar()
    }

    fun hideProgress() {
        hideProgressBar()
    }

    override fun onDestroy() {
        super.onDestroy()
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
