package org.mifos.mobile.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentSavingsAccountWithdrawFragmentBinding
import org.mifos.mobile.models.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.models.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.presenters.SavingsAccountWithdrawPresenter
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.views.SavingsAccountWithdrawView
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.Toaster
import org.mifos.mobile.utils.getTodayFormatted

import javax.inject.Inject

/*
* Created by saksham on 02/July/2018
*/
class SavingsAccountWithdrawFragment : BaseFragment(), SavingsAccountWithdrawView {

    private var _binding: FragmentSavingsAccountWithdrawFragmentBinding? = null
    private val binding get() = _binding!!

    @kotlin.jvm.JvmField
    @Inject
    var presenter: SavingsAccountWithdrawPresenter? = null
    private var savingsWithAssociations: SavingsWithAssociations? = null
    private var payload: SavingsAccountWithdrawPayload? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            savingsWithAssociations = arguments?.getParcelable(Constants.SAVINGS_ACCOUNTS)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentSavingsAccountWithdrawFragmentBinding.inflate(inflater,container,false)
        (activity as BaseActivity?)?.activityComponent?.inject(this)
        presenter?.attachView(this)
        showUserInterface()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnWithdrawSavingsAccount.setOnClickListener {
            onWithdrawSavingsAccount()
        }
    }

    fun onWithdrawSavingsAccount() {
        submitWithdrawSavingsAccount()
    }

    override fun showUserInterface() {
        activity?.title = getString(R.string.withdraw_savings_account)
        binding.tvAccountNumber.text = savingsWithAssociations?.accountNo
        binding.tvClientName.text = savingsWithAssociations?.clientName
        binding.tvWithdrawalDate.text = getTodayFormatted()
    }

    private val isFormIncomplete: Boolean
        get() {
            var rv = false
            if (binding.tilRemark.editText?.text.toString().trim { it <= ' ' }.isEmpty()) {
                rv = true
                binding.tilRemark.error = getString(R.string.error_validation_blank,
                        getString(R.string.remark))
            }
            return rv
        }

    override fun submitWithdrawSavingsAccount() {
        binding.tilRemark.isErrorEnabled = false
        if (!isFormIncomplete) {
            payload = SavingsAccountWithdrawPayload()
            payload?.note = binding.tilRemark.editText?.text.toString()
            payload?.withdrawnOnDate = getTodayFormatted()
            presenter?.submitWithdrawSavingsAccount(savingsWithAssociations?.accountNo, payload)
        }
    }

    override fun showSavingsAccountWithdrawSuccessfully() {
        showMessage(getString(R.string.savings_account_withdraw_successful))
        activity?.supportFragmentManager?.popBackStack()
    }

    override fun showMessage(message: String?) {
        Toaster.show(binding.root, message)
    }

    override fun showError(error: String?) {
        Toaster.show(binding.root, error)
    }

    override fun showProgress() {
        showMifosProgressDialog(getString(R.string.progress_message_loading))
    }

    override fun hideProgress() {
        hideMifosProgressDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
    }

    companion object {
        fun newInstance(
                savingsWithAssociations: SavingsWithAssociations?): SavingsAccountWithdrawFragment {
            val fragment = SavingsAccountWithdrawFragment()
            val bundle = Bundle()
            bundle.putParcelable(Constants.SAVINGS_ACCOUNTS, savingsWithAssociations)
            fragment.arguments = bundle
            return fragment
        }
    }
}