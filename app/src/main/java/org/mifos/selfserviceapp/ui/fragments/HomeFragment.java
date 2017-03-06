package org.mifos.selfserviceapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.models.accounts.savings.SavingAccount;
import org.mifos.selfserviceapp.models.client.Client;
import org.mifos.selfserviceapp.presenters.HomePresenter;
import org.mifos.selfserviceapp.presenters.SavingAccountsDetailPresenter;
import org.mifos.selfserviceapp.presenters.LoanAccountsDetailPresenter;
import org.mifos.selfserviceapp.ui.activities.LoanAccountsDetailActivity;
import org.mifos.selfserviceapp.ui.activities.SavingAccountsDetailActivity;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.adapters.LoanAccountsListAdapter;
import org.mifos.selfserviceapp.ui.adapters.SavingAccountsListAdapter;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;
import org.mifos.selfserviceapp.ui.views.HomeView;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.PrefManager;

import org.mifos.selfserviceapp.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by michaelsosnick on 1/1/17.
 */

public class HomeFragment extends BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener, HomeView {

    public static final String LOG_TAG = HomeFragment.class.getSimpleName();

    @BindView(R.id.noAccountText)
    TextView noAccountText;

    @BindView(R.id.ll_error)
    LinearLayout ll_error;

    @BindView(R.id.noAccountIcon)
    ImageView noAccountIcon;

    @BindView(R.id.iv_profile_pic)
    ImageView iv_profile_pic;

    @Inject
    HomePresenter homePresenter;

    @Inject
    SavingAccountsDetailPresenter savingPresenter;

    View rootView;

    @BindView(R.id.tv_client_name)
    TextView tv_client_name;

    @BindView(R.id.tv_savings)
    TextView tv_savings;

    @BindView(R.id.tv_saving_one)
    TextView tv_saving_one;

    @BindView(R.id.tv_saving_two)
    TextView tv_saving_two;

    @BindView(R.id.tv_saving_three)
    TextView tv_saving_three;

    @BindView(R.id.tv_loans)
    TextView tv_loans;

    @BindView(R.id.tv_loan_one)
    TextView tv_loan_one;

    @BindView(R.id.tv_loan_two)
    TextView tv_loan_two;

    private List<LoanAccount> loanAccounts;
    private List<SavingAccount> savingAccounts;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        loanAccounts = new ArrayList<>();
        savingAccounts = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, rootView);
        homePresenter.attachView(this);
        homePresenter.loadInfo();
        homePresenter.loadClient();

//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//
//
//        rvSavingAccounts.setLayoutManager(layoutManager);
//        rvSavingAccounts.setHasFixedSize(true);

//        swipeRefreshLayout.setColorSchemeColors(getActivity()
//                .getResources().getIntArray(R.array.swipeRefreshColors));
//        swipeRefreshLayout.setOnRefreshListener(this);

        showProgress();

        return rootView;
    }

    @OnClick(R.id.noAccountIcon)
    void reloadOnError() {
        ll_error.setVisibility(View.GONE);
        homePresenter.loadInfo();
        homePresenter.loadClient();
    }

    @Override
    public void onRefresh() {
        ll_error.setVisibility(View.GONE);
        homePresenter.loadInfo();
        homePresenter.loadClient();
    }


    public void showEmptyAccounts(String emptyAccounts) {
        ll_error.setVisibility(View.VISIBLE);
        noAccountText.setText(emptyAccounts);
//        iv_userPicture.setImageResource(R.drawable.ic_clients);
    }

    @Override
    public void showError(String errorMessage) {
        ll_error.setVisibility(View.VISIBLE);
        noAccountText.setText(errorMessage);
//        iv_userPicture.setImageResource(R.drawable.ic_clients);
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void showClientInfo(Client client) {
        tv_client_name.setText("Hello, " + client.getFirstname());
//         lazy the  load profile picture
//        if (client.isImagePresent()) {

//            // make the image url
//            String url = PrefManager.getInstanceUrl()
//                    + "clients/"
//                    + client.getId()
//                    + "/images?maxHeight=120&maxWidth=120";
            String url = "" + client.getId();
            tv_saving_three.setText(url);
//            GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
//                    .addHeader(MifosInterceptor.HEADER_TENANT, PrefManager.getTenant())
//                    .addHeader(MifosInterceptor.HEADER_AUTH, PrefManager.getToken())
//                    .addHeader("Accept", "application/octet-stream")
//                    .build());
//
//            // download the image from the url
//            Glide.with(mContext)
//                    .load(glideUrl)
//                    .asBitmap()
//                    .placeholder(R.drawable.ic_dp_placeholder)
//                    .error(R.drawable.ic_dp_placeholder)
//                    .into(new BitmapImageViewTarget((View) iv_userPicture) {
//                        @Override
//                        protected void setResource(Bitmap result) {
//                            // check a valid bitmap is downloaded
//                            if (result == null || result.getWidth() == 0)
//                                return;
//
//                            // set to image view
//                            iv_userPicture.setImageResource(result);
//                        }
//                    });

            iv_profile_pic.setImageResource(R.drawable.ic_clients);

//        } else {
//            iv_profile_pic.setImageResource(R.drawable.ic_clients);
//            tv_saving_three.setText("not here");
//
//        }
    }

    @Override
    public void showInfo(List<LoanAccount> loanAccounts, List<SavingAccount> savingAccounts) {

        this.savingAccounts = savingAccounts;
        this.loanAccounts = loanAccounts;

        LoanAccount l0 = loanAccounts.get(0);
        homePresenter.loadLoanAccountDetails(l0.getId(), tv_loan_one);
        LoanAccount l1 = loanAccounts.get(1);
        homePresenter.loadLoanAccountDetails(l1.getId(), tv_loan_two);

        SavingAccount s0 = savingAccounts.get(0);
        homePresenter.loadSavingAccountDetails(s0.getId(), tv_saving_one);
        SavingAccount s1 = savingAccounts.get(1);
        homePresenter.loadSavingAccountDetails(s1.getId(), tv_saving_two);
        SavingAccount s2 = savingAccounts.get(2);
//        homePresenter.loadSavingAccountDetails(s2.getId(), tv_saving_three);

        if (savingAccounts.size() == 0) {
            showEmptyAccounts(getString(R.string.empty_savings_accounts));
        } else if (loanAccounts.size() == 0) {
            showEmptyAccounts(getString(R.string.empty_loan_accounts));
        }
    }

    @Override
    public void showLoanAccountsDetail(LoanAccount loanAccount, TextView tv) {
        tv.setText(String.valueOf(loanAccount.getPrincipal()));
    }

    @Override
    public void showErrorFetchingLoanAccountsDetail(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSavingAccountsDetail(SavingAccount savingAccount, TextView tv) {
        tv.setText(String.valueOf(savingAccount.getAccountBalance()));
    }

    @Override
    public void showErrorFetchingSavingAccountsDetail(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void hideProgress() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        homePresenter.detachView();
    }

}


