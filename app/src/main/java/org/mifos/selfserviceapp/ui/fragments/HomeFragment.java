package org.mifos.selfserviceapp.ui.fragments;

import android.content.Intent;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import org.mifos.selfserviceapp.utils.CurrencyUtil;
import org.mifos.selfserviceapp.utils.MaterialDialog;
import org.mifos.selfserviceapp.utils.Toaster;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by michaelsosnick on 1/1/17.
 */

public class HomeFragment extends BaseFragment implements HomeView,
        SwipeRefreshLayout.OnRefreshListener {

    public static final String LOG_TAG = HomeFragment.class.getSimpleName();

    @BindView(R.id.tv_saving_total_amount)
    TextView tvSavingTotalAmount;

    @BindView(R.id.tv_loan_total_amount)
    TextView tvLoanTotalAmount;

    @BindView(R.id.ll_account_detail)
    LinearLayout llAccountDetail;

    @BindView(R.id.iv_visibility)
    ImageView ivVisibility;

    @BindView(R.id.swipe_home_container)
    SwipeRefreshLayout slHomeContainer;

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

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ((HomeActivity) getActivity()).getActivityComponent().inject(this);

        ButterKnife.bind(this, rootView);
        clientId = preferencesHelper.getClientId();

        presenter.attachView(this);
        slHomeContainer.setColorSchemeResources(R.color.blue_light, R.color.green_light, R
                .color.orange_light, R.color.red_light);
        slHomeContainer.setOnRefreshListener(this);
        loadClientData();

        showUserInterface();
        return rootView;
    }

    @Override
    public void onRefresh() {
        loadClientData();
    }

    private void loadClientData() {
        presenter.loadClientAccountDetails();
        presenter.getUserDetails();
        presenter.getUserImage();
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

    /**
     * Opens {@link ClientAccountsFragment} according to the {@code accountType} provided
     * @param accountType Enum of {@link AccountType}
     */
    public void openAccount(AccountType accountType) {
        ((BaseActivity) getActivity()).replaceFragment(
                ClientAccountsFragment.newInstance(accountType), true, R.id.container);
    }

    /**
     * Provides {@code totalLoanAmount} fetched from server
     * @param totalLoanAmount Total Loan amount
     */
    @Override
    public void showLoanAccountDetails(double totalLoanAmount) {
        tvLoanTotalAmount.setText(CurrencyUtil.formatCurrency(getContext(), totalLoanAmount));
    }

    /**
     * Provides {@code totalSavingAmount} fetched from server
     * @param totalSavingAmount Total Saving amount
     */
    @Override
    public void showSavingAccountDetails(double totalSavingAmount) {
        tvSavingTotalAmount.setText(CurrencyUtil.formatCurrency(getContext(), totalSavingAmount));
    }

    /**
     * Fetches Client details and display clientName
     * @param client Details about client
     */
    @Override
    public void showUserDetails(Client client) {
        ((TextView) toolbarView.findViewById(R.id.tv_user_name)).setText(
                getString(R.string.hello_client, client.getDisplayName()));
    }

    /**
     * Provides with Client image fetched from server
     * @param bitmap Client Image
     */
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

    /**
     * Reverses the state of Account Overview section i.e. visible to hidden or vice a versa
     */
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

    /**
     * Makes Overview state visible
     */
    private void showOverviewState() {
        ivVisibility.setColorFilter(ContextCompat.getColor(getActivity(), R.color.gray_dark));
        llAccountDetail.setVisibility(View.VISIBLE);
    }

    /**
     * Hides Overview state
     */
    private void hideOverviewState() {
        ivVisibility.setColorFilter(ContextCompat.getColor(getActivity(), R.color.light_grey));
        llAccountDetail.setVisibility(View.GONE);
    }


    /**
     * Calls {@code openAccount()} for opening {@link ClientAccountsFragment}
     */
    @OnClick(R.id.ll_accounts)
    public void accountsClicked() {
        openAccount(AccountType.SAVINGS);
        ((HomeActivity) getActivity()).setNavigationViewSelectedItem(R.id.item_accounts);
    }

    /**
     * Shows a dialog with options: Normal Transfer and Third Party Transfer
     */
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

    /**
     * Opens {@link ClientChargeFragment} to display all Charges associated with client's account
     */
    @OnClick(R.id.ll_charges)
    public void chargesClicked() {
        ((HomeActivity) getActivity()).replaceFragment(ClientChargeFragment.newInstance(clientId,
                ChargeType.CLIENT), true,  R.id.container);
    }

    /**
     * Opens {@link LoanApplicationFragment} to apply for a loan
     */
    @OnClick(R.id.ll_apply_for_loan)
    public void applyForLoan() {
        ((HomeActivity) getActivity()).replaceFragment(LoanApplicationFragment.
                        newInstance(LoanState.CREATE), true,  R.id.container);
    }

    /**
     * Opens {@link BeneficiaryListFragment} which contains list of Beneficiaries associated with
     * Client's account
     */
    @OnClick(R.id.ll_beneficiaries)
    public void beneficiaries() {
        ((HomeActivity) getActivity()).replaceFragment(BeneficiaryListFragment.
                newInstance(), true,  R.id.container);
    }

    @OnClick(R.id.ll_surveys)
    public void surveys() {

    }

    /**
     * It is called whenever any error occurs while executing a request
     * @param errorMessage Error message that tells the user about the problem.
     */
    @Override
    public void showError(String errorMessage) {
        Toaster.show(rootView, errorMessage);
    }

    /**
     * Shows {@link SwipeRefreshLayout}
     */
    @Override
    public void showProgress() {
        slHomeContainer.setRefreshing(true);
    }

    /**
     * Hides {@link SwipeRefreshLayout}
     */
    @Override
    public void hideProgress() {
        slHomeContainer.setRefreshing(false);
    }

    /**
     * Add a custom layout in Toolbar and then fetching client's details and image to show using
     * {@code setToolbarLayoutForHome()}
     */
    @Override
    public void onResume() {
        super.onResume();
        setToolbarLayoutForHome();
        presenter.getUserDetails();
        presenter.getUserImage();
    }

    /**
     * Removing the custom layout from Toolbar and setting everything to default using
     * {@code setToolBarLayoutForOthers()}
     */
    @Override
    public void onPause() {
        super.onPause();
        setToolBarLayoutForOthers();
    }

    /**
     * Adding a Custom View in toolbar to display User's image and Name
     */
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

    /**
     * Removes the custom layout added in Toolbar and setting everything to default
     */
    public void setToolBarLayoutForOthers() {

        toolBarLayoutParams.height = toolbarDefaultHeight;

        ((HomeActivity) getActivity()).getToolbar().removeView(toolbarCustomView);
        ((HomeActivity) getActivity()).getToolbar().setLayoutParams(toolBarLayoutParams);
        ((HomeActivity) getActivity()).getToolbar().
                setContentInsetStartWithNavigation(insetStartWidth);

        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
    }
}


