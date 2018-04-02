package org.mifos.mobilebanking.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.models.beneficary.Beneficiary;
import org.mifos.mobilebanking.presenters.BeneficiaryListPresenter;
import org.mifos.mobilebanking.ui.activities.AddBeneficiary;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.adapters.BeneficiaryListAdapter;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;
import org.mifos.mobilebanking.ui.views.BeneficiariesView;
import org.mifos.mobilebanking.utils.Constants;
import org.mifos.mobilebanking.utils.DividerItemDecoration;
import org.mifos.mobilebanking.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dilpreet on 14/6/17.
 */

public class BeneficiaryListFragment extends BaseFragment implements RecyclerItemClickListener.
        OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, BeneficiariesView {

    @BindView(R.id.rv_beneficiaries)
    RecyclerView rvBeneficiaries;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.fab_add_beneficiary)
    FloatingActionButton fabAddBeneficiary;

    @BindView(R.id.view_error)
    View viewError;

    @BindView(R.id.tv_status)
    TextView tvStatus;

    @Inject
    BeneficiaryListPresenter beneficiaryListPresenter;

    @Inject
    BeneficiaryListAdapter beneficiaryListAdapter;

    private View rootView;
    private List<Beneficiary> beneficiaryList;

    public static BeneficiaryListFragment newInstance() {
        BeneficiaryListFragment fragment = new BeneficiaryListFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_beneficiary_list, container, false);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        setToolbarTitle(getString(R.string.beneficiaries));

        showUserInterface();

        beneficiaryListPresenter.attachView(this);
        if (savedInstanceState == null) {
            beneficiaryListPresenter.loadBeneficiaries();
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Constants.BENEFICIARY, new ArrayList<Parcelable>(
                beneficiaryList));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            List<Beneficiary> beneficiaries = savedInstanceState.getParcelableArrayList(Constants.
                    BENEFICIARY);
            showBeneficiaryList(beneficiaries);
        }
    }

    /**
     * Setup Initial User Interface
     */
    @Override
    public void showUserInterface() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvBeneficiaries.setLayoutManager(layoutManager);
        rvBeneficiaries.setHasFixedSize(true);
        rvBeneficiaries.addItemDecoration(new DividerItemDecoration(getActivity(), layoutManager.
                getOrientation()));
        rvBeneficiaries.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), this));
        rvBeneficiaries.setAdapter(beneficiaryListAdapter);

        swipeRefreshLayout.setColorSchemeColors(getActivity()
                .getResources().getIntArray(R.array.swipeRefreshColors));
        swipeRefreshLayout.setOnRefreshListener(this);

        fabAddBeneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddBeneficiary.class));
            }
        });
    }

    /**
     * Refreshes {@code beneficiaryList} by calling {@code loadBeneficiaries()}
     */
    @Override
    public void onRefresh() {
        if (viewError.getVisibility() == View.VISIBLE) {
            viewError.setVisibility(View.GONE);
            rvBeneficiaries.setVisibility(View.VISIBLE);
        }
        beneficiaryListPresenter.loadBeneficiaries();
    }

    /**
     * Shows {@link SwipeRefreshLayout}
     */
    @Override
    public void showProgress() {
        showSwipeRefreshLayout(true);
    }

    /**
     * Hides {@link SwipeRefreshLayout}
     */
    @Override
    public void hideProgress() {
        showSwipeRefreshLayout(false);
    }

    /**
     * It is called whenever any error occurs while executing a request
     * @param msg Error message that tells the user about the problem.
     */
    @Override
    public void showError(String msg) {
        rvBeneficiaries.setVisibility(View.GONE);
        viewError.setVisibility(View.VISIBLE);
        tvStatus.setText(msg);
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Set the {@code beneficiaryList} fetched from server to {@code beneficiaryListAdapter}
     * @param beneficiaryList
     */
    @Override
    public void showBeneficiaryList(List<Beneficiary> beneficiaryList) {
        this.beneficiaryList = beneficiaryList;
        beneficiaryListAdapter.setBeneficiaryList(beneficiaryList);
    }

    @Override
    public void onItemClick(View childView, int position) {
        ((BaseActivity) getActivity()).replaceFragment(BeneficiaryDetailFragment
                .newInstance(beneficiaryList.get(position)), true, R.id.container);
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    public void showSwipeRefreshLayout(final boolean show) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(show);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        beneficiaryListPresenter.detachView();
    }
}
