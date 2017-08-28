package org.mifos.selfserviceapp.ui.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.client.Client;
import org.mifos.selfserviceapp.models.client.Group;
import org.mifos.selfserviceapp.presenters.UserDetailsPresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;
import org.mifos.selfserviceapp.ui.views.UserDetailsView;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.DateHelper;
import org.mifos.selfserviceapp.utils.Toaster;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dilpreet on 10/7/17.
 */

public class UserProfileFragment extends BaseFragment implements UserDetailsView {


    @BindView(R.id.iv_profile)
    ImageView ivProfile;

    @BindView(R.id.tv_account_number)
    TextView tvAccountNumber;

    @BindView(R.id.tv_activation_date)
    TextView tvActivationDate;

    @BindView(R.id.tv_office_name)
    TextView tvOfficeName;

    @BindView(R.id.tv_groups)
    TextView tvGroups;

    @BindView(R.id.tv_client_type)
    TextView tvCLientType;

    @BindView(R.id.tv_client_classification)
    TextView tvClientClassification;

    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;

    @BindView(R.id.tv_dob)
    TextView tvDOB;

    @BindView(R.id.tv_gender)
    TextView tvGender;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rl_error)
    RelativeLayout rlError;

    @Inject
    UserDetailsPresenter presenter;

    private View rootView;
    private Bitmap userBitmap;
    private Client client;

    public static UserProfileFragment newInstance() {
        UserProfileFragment fragment = new UserProfileFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        presenter.attachView(this);

        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
        ((BaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(getActivity(),
                R.color.white));
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(getActivity(),
                R.color.white));
        if (savedInstanceState == null) {
            presenter.getUserDetails();
            presenter.getUserImage();
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.USER_PROFILE, userBitmap);
        outState.putParcelable(Constants.USER_DETAILS, client);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            client = savedInstanceState.getParcelable(Constants.USER_DETAILS);
            userBitmap = savedInstanceState.getParcelable(Constants.USER_PROFILE);
            showUserImage(userBitmap);
            showUserDetails(client);
        }
    }

    /**
     * Sets client basic details which are fetched from server
     * @param client instance of {@link Client} which contains information about client
     */
    @Override
    public void showUserDetails(Client client) {
        this.client = client;
        collapsingToolbarLayout.setTitle(client.getDisplayName());
        tvAccountNumber.setText(client.getAccountNo());
        tvActivationDate.setText(DateHelper.getDateAsString(client.getActivationDate()));
        tvOfficeName.setText(client.getOfficeName());
        tvCLientType.setText(client.getClientType().getName());
        tvGroups.setText(getGroups(client.getGroups()));
        tvClientClassification.setText(client.getClientClassification().getName());
        tvPhoneNumber.setText(client.getMobileNo());
        tvDOB.setText(DateHelper.getDateAsString(client.getDobDate()));
        tvGender.setText(client.getGender().getName());
    }

    /**
     * Generate a string for groups which the client is part of.
     * @param groups {@link List} of {@link Group} which client is a part of.
     * @return Returns String of groups
     */
    private String getGroups(List<Group> groups) {
        StringBuilder builder = new StringBuilder();
        for (Group group : groups) {
            builder.append(getString(R.string.string_and_string, group.getName(), " | "));
        }
        return builder.toString().substring(0, builder.toString().length() - 2);
    }

    /**
     * Provides with client Image fetched from the server in {@code bitmap}
     * @param bitmap User Image
     */
    @Override
    public void showUserImage(final Bitmap bitmap) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                userBitmap = bitmap;
                ivProfile.setImageBitmap(bitmap);
            }
        });
    }

    /**
     * It is called whenever any error occurs while executing a request
     * @param message Error message that tells the user about the problem.
     */
    @Override
    public void showError(String message) {
        Toaster.show(rootView, message);
        appBarLayout.setVisibility(View.GONE);
        rlError.setVisibility(View.VISIBLE);
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
