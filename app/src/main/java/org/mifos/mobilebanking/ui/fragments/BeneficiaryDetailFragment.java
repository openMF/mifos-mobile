package org.mifos.mobilebanking.ui.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.models.beneficiary.Beneficiary;
import org.mifos.mobilebanking.presenters.BeneficiaryDetailPresenter;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.enums.BeneficiaryState;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;
import org.mifos.mobilebanking.ui.views.BeneficiaryDetailView;
import org.mifos.mobilebanking.utils.Constants;
import org.mifos.mobilebanking.utils.CurrencyUtil;
import org.mifos.mobilebanking.utils.MaterialDialog;
import org.mifos.mobilebanking.utils.Toaster;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dilpreet on 15/6/17.
 */

public class BeneficiaryDetailFragment extends BaseFragment implements BeneficiaryDetailView {

    @BindView(R.id.tv_beneficiary_name)
    TextView tvName;

    @BindView(R.id.tv_account_number)
    TextView tvAccountNumber;

    @BindView(R.id.tv_client_name)
    TextView tvClientName;

    @BindView(R.id.tv_account_type)
    TextView tvAccountType;

    @BindView(R.id.tv_transfer_limit)
    TextView tvTransferLimit;

    @BindView(R.id.tv_office_name)
    TextView tvOfficeName;

    @Inject
    BeneficiaryDetailPresenter presenter;

    private Beneficiary beneficiary;
    private View rootView;

    public static BeneficiaryDetailFragment newInstance(Beneficiary beneficiary) {
        BeneficiaryDetailFragment fragment = new BeneficiaryDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.BENEFICIARY, beneficiary);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            beneficiary = getArguments().getParcelable(Constants.BENEFICIARY);
        }
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_beneficiary_detail, container, false);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        setToolbarTitle(getString(R.string.beneficiary_detail));

        ButterKnife.bind(this, rootView);
        presenter.attachView(this);

        showUserInterface();

        return rootView;
    }

    /**
     * Used for setting up of User Interface
     */
    @Override
    public void showUserInterface() {
        tvName.setText(beneficiary.getName());
        tvAccountNumber.setText(beneficiary.getAccountNumber());
        tvClientName.setText(beneficiary.getClientName());
        tvAccountType.setText(beneficiary.getAccountType().getValue());
        tvTransferLimit.setText(CurrencyUtil.formatCurrency(getActivity(), beneficiary.
                getTransferLimit()));
        tvOfficeName.setText(beneficiary.getOfficeName());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_beneficiary, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_update_beneficiary :
                ((BaseActivity) getActivity()).replaceFragment(BeneficiaryApplicationFragment.
                        newInstance(BeneficiaryState.UPDATE, beneficiary), true, R.id.container);
                break;
            case R.id.item_delete_beneficiary :
                new MaterialDialog.Builder().init(getActivity())
                        .setTitle(getString(R.string.delete_beneficiary))
                        .setMessage(getString(R.string.delete_beneficiary_confirmation))
                        .setPositiveButton(getString(R.string.delete),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        presenter.deleteBeneficiary(beneficiary.getId());
                                    }
                                })
                        .setNegativeButton(getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                        .createMaterialDialog()
                        .show();
                break;
        }
        return true;
    }

    /**
     * Shows a {@link android.support.design.widget.Snackbar} on successfull deletion of a
     * Beneficiary and then pops current fragment
     */
    @Override
    public void showBeneficiaryDeletedSuccessfully() {
        Toaster.show(rootView, getString(R.string.beneficiary_deleted_successfully));
        getActivity().getSupportFragmentManager().popBackStack();
    }

    /**
     * It is called whenever any error occurs while executing a request
     * @param msg Error message that tells the user about the problem.
     */
    @Override
    public void showError(String msg) {
        Toaster.show(rootView, msg);
    }

    /**
     * Shows {@link org.mifos.mobilebanking.utils.ProgressBarHandler}
     */
    @Override
    public void showProgress() {
        showProgressBar();
    }

    /**
     * Hides {@link org.mifos.mobilebanking.utils.ProgressBarHandler}
     */
    @Override
    public void hideProgress() {
        hideProgressBar();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideProgress();
        presenter.detachView();
    }
}
