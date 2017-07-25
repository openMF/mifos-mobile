package org.mifos.selfserviceapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.beneficary.Beneficiary;
import org.mifos.selfserviceapp.presenters.BeneficiaryListPresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.adapters.BeneficiaryListAdapter;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;
import org.mifos.selfserviceapp.ui.views.BeneficiariesView;
import org.mifos.selfserviceapp.utils.RecyclerItemClickListener;

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
        beneficiaryListPresenter.loadBeneficiaries();

        return rootView;
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
        rvBeneficiaries.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), this));
        rvBeneficiaries.setAdapter(beneficiaryListAdapter);

        swipeRefreshLayout.setColorSchemeColors(getActivity()
                .getResources().getIntArray(R.array.swipeRefreshColors));
        swipeRefreshLayout.setOnRefreshListener(this);

        fabAddBeneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) getActivity()).replaceFragment(BeneficiaryAddOptionsFragment.
                                newInstance(), true, R.id.container);
            }
        });
    }

    /**
     * Refreshes {@code beneficiaryList} by calling {@code loadBeneficiaries()}
     */
    @Override
    public void onRefresh() {
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
