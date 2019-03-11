package org.mifos.mobilebanking.ui.fragments;

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.models.FAQ;
import org.mifos.mobilebanking.presenters.HelpPresenter;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.adapters.FAQAdapter;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;
import org.mifos.mobilebanking.ui.views.HelpView;
import org.mifos.mobilebanking.utils.Constants;
import org.mifos.mobilebanking.utils.DividerItemDecoration;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HelpFragment extends BaseFragment implements HelpView, BottomNavigationView.
        OnNavigationItemSelectedListener {

    @BindView(R.id.rv_faq)
    RecyclerView rvFaq;

    @BindView(R.id.bnv_help)
    BottomNavigationView bnvHelp;

    @Inject
    FAQAdapter faqAdapter;

    @Inject
    HelpPresenter presenter;

    private View rootView;
    private ArrayList<FAQ> faqArrayList;

    public static HelpFragment newInstance() {
        HelpFragment fragment = new HelpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_help, container, false);
        setHasOptionsMenu(true);

        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        presenter.attachView(this);
        setToolbarTitle(getString(R.string.help));

        showUserInterface();
        if (savedInstanceState == null) {
            presenter.loadFaq();
        }

        return rootView;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Constants.HELP, new ArrayList<Parcelable>(faqArrayList));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            ArrayList<FAQ> faqs = savedInstanceState.getParcelableArrayList(Constants.HELP);
            showFaq(faqs);
        }
    }

    private void showUserInterface() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvFaq.setLayoutManager(layoutManager);
        rvFaq.addItemDecoration(new DividerItemDecoration(getActivity(),
                layoutManager.getOrientation()));

        bnvHelp.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void showFaq(ArrayList<FAQ> faqArrayList) {
        faqAdapter.setFaqArrayList(faqArrayList);
        rvFaq.setAdapter(faqAdapter);
        this.faqArrayList = faqArrayList;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_call:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + getString(R.string.help_line_number)));
                startActivity(intent);
                break;
            case R.id.menu_email:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + getString(R.string.contact_email)));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.user_query));
                if (emailIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(emailIntent);
                }
                break;
            case R.id.menu_locations:
                ((BaseActivity) getActivity()).replaceFragment(LocationsFragment.newInstance(),
                        true, R.id.container);
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_help, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.
                SEARCH_SERVICE);

        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search_faq).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().
                getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                faqAdapter.updateList(presenter.filterList(faqArrayList, newText));
                return false;
            }
        });
    }

    @Override
    public void showProgress() {
        showProgressBar();
    }

    @Override
    public void hideProgress() {
        hideProgressBar();
    }
}
