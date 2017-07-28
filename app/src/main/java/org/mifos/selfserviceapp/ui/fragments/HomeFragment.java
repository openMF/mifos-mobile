package org.mifos.selfserviceapp.ui.fragments;

import android.content.Intent;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.local.PreferencesHelper;
import org.mifos.selfserviceapp.models.client.Client;
import org.mifos.selfserviceapp.presenters.HomePresenter;
import org.mifos.selfserviceapp.ui.activities.HomeActivity;
import org.mifos.selfserviceapp.ui.activities.UserProfileActivity;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.enums.AccountType;
import org.mifos.selfserviceapp.ui.enums.ChargeType;
import org.mifos.selfserviceapp.ui.enums.LoanState;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;
import org.mifos.selfserviceapp.ui.views.HomeView;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.MaterialDialog;
import org.mifos.selfserviceapp.utils.Toaster;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by michaelsosnick on 1/1/17.
 */

public class HomeFragment extends BaseFragment implements HomeView {

    public static final String LOG_TAG = HomeFragment.class.getSimpleName();

    @BindView(R.id.tv_saving_total_amount)
    TextView tvSavingTotalAmount;

    @BindView(R.id.tv_loan_total_amount)
    TextView tvLoanTotalAmount;

    @BindView(R.id.ll_account_detail)
    LinearLayout llAccountDetail;

    @BindView(R.id.iv_visibility)
    ImageView ivVisibility;

    @Inject
    HomePresenter presenter;

    @Inject
    PreferencesHelper preferencesHelper;

    View rootView;
    private long clientId;
    private View toolbarView;
    private boolean isDetailVisible;
    private View toolbarCustomView;
    private LinearLayout.LayoutParams toolBarLayoutParams;
    private int toolbarDefaultHeight, insetStartWidth;

    public static HomeFragment newInstance(Long clientId) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.CLIENT_ID, clientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clientId = getArguments().getLong(Constants.CLIENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ((HomeActivity) getActivity()).getActivityComponent().inject(this);

        ButterKnife.bind(this, rootView);

        presenter.attachView(this);
        presenter.loadClientAccountDetails();

        showUserInterface();
        return rootView;
    }
    @Override
    public void showUserInterface() {
        toolbarView = ((HomeActivity) getActivity()).getToolbar().getRootView();
        isDetailVisible = preferencesHelper.overviewState();
        if (isDetailVisible) {
            showOverviewState();
        } else {
            hideOverviewState();
        }
    }

    public void openAccount(AccountType accountType) {
        ((BaseActivity) getActivity()).replaceFragment(
                ClientAccountsFragment.newInstance(clientId, accountType), true, R.id.container);
    }

    @Override
    public void showLoanAccountDetails(double totalLoanAmount) {
        tvLoanTotalAmount.setText(getString(R.string.double_amount, totalLoanAmount));
    }

    @Override
    public void showSavingAccountDetails(double totalSavingAmount) {
        tvSavingTotalAmount.setText(getString(R.string.double_amount, totalSavingAmount));
    }

    @Override
    public void showUserDetails(Client client) {
        ((TextView) toolbarView.findViewById(R.id.tv_user_name)).setText(
                getString(R.string.hello_client, client.getDisplayName()));
    }

    @Override
    public void showUserImage(final Bitmap bitmap) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((ImageView) toolbarView.findViewById(R.id.iv_user_image)).
                        setImageBitmap(bitmap);
                toolbarView.findViewById(R.id.iv_user_image).
                        setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(getActivity(), UserProfileActivity.class));
                            }
                        });
            }
        });
    }

    @OnClick(R.id.iv_visibility)
    public void reverseDetailState() {
        if (isDetailVisible) {
            isDetailVisible = false;
            preferencesHelper.setOverviewState(false);
            hideOverviewState();
        } else {
            isDetailVisible = true;
            preferencesHelper.setOverviewState(true);
            showOverviewState();
        }
    }

    private void showOverviewState() {
        ivVisibility.setColorFilter(ContextCompat.getColor(getActivity(), R.color.gray_dark));
        llAccountDetail.setVisibility(View.VISIBLE);
    }

    private void hideOverviewState() {
        ivVisibility.setColorFilter(ContextCompat.getColor(getActivity(), R.color.light_grey));
        llAccountDetail.setVisibility(View.GONE);
    }

    @OnClick(R.id.ll_accounts)
    public void accountsClicked() {
        openAccount(AccountType.SAVINGS);
    }

    @OnClick(R.id.ll_transfer)
    public void transferClicked() {
        String[] transferTypes = {getString(R.string.transfer), getString(R.string.
                third_party_transfer)};
        new MaterialDialog.Builder().init(getActivity())
                .setTitle(R.string.choose_transfer_type)
                .setItems(transferTypes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            ((HomeActivity) getActivity()).replaceFragment(
                                    SavingsMakeTransferFragment.newInstance(1, ""), true,
                                    R.id.container);
                        } else {
                            ((HomeActivity) getActivity()).replaceFragment(
                                    ThirdPartyTransferFragment.newInstance(), true, R.id.container);
                        }
                    }
                })
                .createMaterialDialog()
                .show();
    }

    @OnClick(R.id.ll_charges)
    public void chargesClicked() {
        ((HomeActivity) getActivity()).replaceFragment(ClientChargeFragment.newInstance(clientId,
                ChargeType.CLIENT), true,  R.id.container);
    }

    @OnClick(R.id.ll_apply_for_loan)
    public void applyForLoan() {
        ((HomeActivity) getActivity()).replaceFragment(LoanApplicationFragment.
                        newInstance(LoanState.CREATE), true,  R.id.container);
    }

    @OnClick(R.id.ll_beneficiaries)
    public void beneficiaries() {
        ((HomeActivity) getActivity()).replaceFragment(BeneficiaryListFragment.
                newInstance(), true,  R.id.container);
    }

    @OnClick(R.id.ll_surveys)
    public void surveys() {

    }

    @Override
    public void showError(String errorMessage) {
        Toaster.show(rootView, errorMessage);
    }

    @Override
    public void showProgress() {
        showProgressBar();
    }

    @Override
    public void hideProgress() {
        hideProgressBar();
    }

    @Override
    public void onResume() {
        super.onResume();
        setToolbarLayoutForHome();
        presenter.getUserDetails();
        presenter.getUserImage();
    }

    @Override
    public void onPause() {
        super.onPause();
        setToolBarLayoutForOthers();
    }

    public void setToolbarLayoutForHome() {
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        toolbarCustomView = mInflater.inflate(R.layout.toolbar_home, null);

        toolBarLayoutParams = (LinearLayout.LayoutParams) ((HomeActivity) getActivity()).
                getToolbar().getLayoutParams();
        toolbarDefaultHeight = toolBarLayoutParams.height;
        insetStartWidth = ((HomeActivity) getActivity()).getToolbar().getContentInsetStart();

        toolBarLayoutParams.height = 350;

        ((HomeActivity) getActivity()).getToolbar().setLayoutParams(toolBarLayoutParams);
        ((HomeActivity) getActivity()).getToolbar().setContentInsetStartWithNavigation(0);
        ((HomeActivity) getActivity()).getToolbar().addView(toolbarCustomView);

        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void setToolBarLayoutForOthers() {

        toolBarLayoutParams.height = toolbarDefaultHeight;

        ((HomeActivity) getActivity()).getToolbar().removeView(toolbarCustomView);
        ((HomeActivity) getActivity()).getToolbar().setLayoutParams(toolBarLayoutParams);
        ((HomeActivity) getActivity()).getToolbar().
                setContentInsetStartWithNavigation(insetStartWidth);

        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
    }
}


