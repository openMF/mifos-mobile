package org.mifos.mobile.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import org.mifos.mobile.R
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.presenters.BeneficiaryDetailPresenter
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.enums.BeneficiaryState
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.views.BeneficiaryDetailView
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.CurrencyUtil
import org.mifos.mobile.utils.MaterialDialog
import org.mifos.mobile.utils.Toaster
import javax.inject.Inject

/**
 * Created by dilpreet on 15/6/17.
 */
class BeneficiaryDetailFragment : BaseFragment(), BeneficiaryDetailView {
    @kotlin.jvm.JvmField
    @BindView(R.id.tv_beneficiary_name)
    var tvName: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_account_number)
    var tvAccountNumber: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_client_name)
    var tvClientName: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_account_type)
    var tvAccountType: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_transfer_limit)
    var tvTransferLimit: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_office_name)
    var tvOfficeName: TextView? = null

    @kotlin.jvm.JvmField
    @Inject
    var presenter: BeneficiaryDetailPresenter? = null
    private var beneficiary: Beneficiary? = null
    private var rootView: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            beneficiary = arguments!!.getParcelable(Constants.BENEFICIARY)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_beneficiary_detail, container, false)
        (activity as BaseActivity?)!!.activityComponent!!.inject(this)
        setToolbarTitle(getString(R.string.beneficiary_detail))
        ButterKnife.bind(this, rootView!!)
        presenter!!.attachView(this)
        showUserInterface()
        return rootView
    }

    /**
     * Used for setting up of User Interface
     */
    override fun showUserInterface() {
        tvName!!.text = beneficiary!!.name
        tvAccountNumber!!.text = beneficiary!!.accountNumber
        tvClientName!!.text = beneficiary!!.clientName
        tvAccountType!!.text = beneficiary!!.accountType!!.value
        tvTransferLimit!!.text = CurrencyUtil.formatCurrency(activity, beneficiary!!.transferLimit!!)
        tvOfficeName!!.text = beneficiary!!.officeName
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_beneficiary, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_update_beneficiary -> (activity as BaseActivity?)!!.replaceFragment(BeneficiaryApplicationFragment.Companion.newInstance(BeneficiaryState.UPDATE, beneficiary), true, R.id.container)
            R.id.item_delete_beneficiary -> MaterialDialog.Builder().init(activity)
                    .setTitle(getString(R.string.delete_beneficiary))
                    .setMessage(getString(R.string.delete_beneficiary_confirmation))
                    .setPositiveButton(getString(R.string.delete)
                    ) { dialog, which ->
                        dialog.dismiss()
                        presenter!!.deleteBeneficiary(beneficiary!!.id!!.toLong())
                    }
                    .setNegativeButton(getString(R.string.cancel)
                    ) { dialog, which -> dialog.dismiss() }
                    .createMaterialDialog()
                    .show()
        }
        return true
    }

    /**
     * Shows a [Snackbar] on successfull deletion of a
     * Beneficiary and then pops current fragment
     */
    override fun showBeneficiaryDeletedSuccessfully() {
        Toaster.show(rootView, getString(R.string.beneficiary_deleted_successfully))
        activity!!.supportFragmentManager.popBackStack()
    }

    /**
     * It is called whenever any error occurs while executing a request
     *
     * @param msg Error message that tells the user about the problem.
     */
    override fun showError(msg: String?) {
        Toaster.show(rootView, msg)
    }

    /**
     * Shows [org.mifos.mobile.utils.ProgressBarHandler]
     */
    override fun showProgress() {
        showProgressBar()
    }

    /**
     * Hides [org.mifos.mobile.utils.ProgressBarHandler]
     */
    override fun hideProgress() {
        hideProgressBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideProgress()
        presenter!!.detachView()
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