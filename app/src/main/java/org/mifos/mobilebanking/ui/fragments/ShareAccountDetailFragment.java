package org.mifos.mobilebanking.ui.fragments;

/*
 * Created by saksham on 04/July/2018
 */

import android.animation.LayoutTransition;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.models.accounts.share.ShareAccount;
import org.mifos.mobilebanking.models.accounts.share.Status;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;
import org.mifos.mobilebanking.utils.CircularImageView;
import org.mifos.mobilebanking.utils.Constants;
import org.mifos.mobilebanking.utils.DateHelper;
import org.mifos.mobilebanking.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareAccountDetailFragment extends BaseFragment {

    @BindView(R.id.tv_account_number)
    TextView tvAccountNumber;

    @BindView(R.id.tv_product_name)
    TextView tvProductName;

    @BindView(R.id.tv_status)
    TextView tvStatus;

    @BindView(R.id.tv_currency)
    TextView tvCurrency;

    @BindView(R.id.ll_submission_detail)
    LinearLayout llSubmissionDetail;

    @BindView(R.id.ll_approval_detail)
    LinearLayout llApprovalDetails;

    @BindView(R.id.ll_activation_detail)
    LinearLayout llActivationDetails;

    @BindView(R.id.tv_submission_date)
    TextView tvSubmissionDate;

    @BindView(R.id.tv_submission_first_name)
    TextView tvSubmissionFirstName;

    @BindView(R.id.tv_submission_last_name)
    TextView tvSubmissionLastName;

    @BindView(R.id.tv_submission_username)
    TextView tvSubmissionUsername;

    @BindView(R.id.tv_approval_date)
    TextView tvApprovalDate;

    @BindView(R.id.tv_approval_first_name)
    TextView tvApprovalFirstName;

    @BindView(R.id.tv_approval_last_name)
    TextView tvApprovalLastName;

    @BindView(R.id.tv_approval_username)
    TextView tvApprovalUsername;

    @BindView(R.id.tv_activation_date)
    TextView tvActivationDate;

    @BindView(R.id.tv_activation_first_name)
    TextView tvActivationFirstName;

    @BindView(R.id.tv_activation_last_name)
    TextView tvActivationLastName;

    @BindView(R.id.tv_activation_username)
    TextView tvActivationUsername;

    @BindView(R.id.ll_container)
    LinearLayout llContainer;

    @BindView(R.id.iv_status)
    CircularImageView ivStatus;

    @BindView(R.id.pc_shares_detail)
    PieChart mChart;

    View rootView;
    ShareAccount shareAccount;

    public static ShareAccountDetailFragment newInstance(ShareAccount shareAccount) {
        ShareAccountDetailFragment fragment = new ShareAccountDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.SHARE_ACCOUNTS, shareAccount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shareAccount = getArguments().getParcelable(Constants.SHARE_ACCOUNTS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_share_account_detail, container, false);
        ButterKnife.bind(this, rootView);
        setToolbarTitle(getString(R.string.share_account_detail));
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showShareAccountDetail();
    }

    public void showShareAccountDetail() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            llContainer.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        }

        addStatusColorLabel(shareAccount.getStatus());
        tvAccountNumber.setText(shareAccount.getAccountNo());
        tvProductName.setText(shareAccount.getProductName());
        tvStatus.setText(shareAccount.getStatus().getValue());
        tvCurrency.setText(shareAccount.getCurrency().getName());

        tvSubmissionDate.setText(DateHelper.getDateAsString(shareAccount.getTimeline()
                .getSubmittedOnDate()));
        tvSubmissionFirstName.setText(shareAccount.getTimeline().getSubmittedByFirstname());
        tvSubmissionLastName.setText(shareAccount.getTimeline().getSubmittedByLastname());
        tvSubmissionUsername.setText(shareAccount.getTimeline().getSubmittedByUsername());

        if (shareAccount.getTimeline().getApprovedDate().size() == 0) {
            tvApprovalDate.setText(getString(R.string.nil));
            tvApprovalFirstName.setText(getString(R.string.nil));
            tvApprovalLastName.setText(getString(R.string.nil));
            tvApprovalUsername.setText(getString(R.string.nil));
        } else {
            tvApprovalDate.setText(DateHelper.getDateAsString(shareAccount.getTimeline()
                    .getApprovedDate()));
            tvApprovalFirstName.setText(shareAccount.getTimeline().getApprovedByFirstname());
            tvApprovalLastName.setText(shareAccount.getTimeline().getApprovedByLastname());
            tvApprovalUsername.setText(shareAccount.getTimeline().getApprovedByUsername());
        }

        if (shareAccount.getTimeline().getActivatedDate().size() == 0) {
            tvActivationDate.setText(getString(R.string.nil));
            tvActivationUsername.setText(getString(R.string.nil));
            tvActivationFirstName.setText(getString(R.string.nil));
            tvActivationLastName.setText(getString(R.string.nil));
        } else {
            tvActivationDate.setText(DateHelper.getDateAsString(shareAccount.getTimeline()
                    .getActivatedDate()));
            tvActivationUsername.setText(shareAccount.getTimeline().getActivatedByUsername());
            tvActivationFirstName.setText(shareAccount.getTimeline().getActivatedByFirstname());
            tvActivationLastName.setText(shareAccount.getTimeline().getActivatedByLastname());
        }

        setSharesDetailChart();
    }

    public void addStatusColorLabel(Status status) {
        if (status.getActive()) {
            ivStatus.setImageDrawable(Utils.setCircularBackground(R.color.green_dark,
                    getActivity()));
        }
        if (status.getApproved()) {
            ivStatus.setImageDrawable(Utils.setCircularBackground(R.color.green_light,
                    getActivity()));
        }
        if (status.getRejected()) {
            ivStatus.setImageDrawable(Utils.setCircularBackground(R.color.blue_light,
                    getActivity()));
        }
        if (status.getClosed()) {
            ivStatus.setImageDrawable(Utils.setCircularBackground(R.color.black,
                    getActivity()));
        }
        if (status.getSubmittedAndPendingApproval()) {
            ivStatus.setImageDrawable(Utils.setCircularBackground(R.color.light_yellow,
                    getActivity()));
        }
    }

    public void setSharesDetailChart() {

        Legend legend = mChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);

        // entry label styling
        mChart.setEntryLabelColor(R.color.light_black);
        mChart.setEntryLabelTextSize(12f);

        List<PieEntry> yVals = new ArrayList<>();
        yVals.add(new PieEntry(shareAccount.getTotalApprovedShares(),
                getString(R.string.approved)));
        yVals.add(new PieEntry(shareAccount.getTotalPendingForApprovalShares(),
                getString(R.string.approval_pending)));

        PieDataSet dataSet = new PieDataSet(yVals, null);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData data = new PieData(dataSet);
        mChart.setData(data);
        mChart.setDescription(null);
        mChart.animateY(1000);
    }

    @OnClick(R.id.iv_submission_detail)
    void onClickSubmissionDetail() {
        int toggleVisibility =
                (llSubmissionDetail.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
        llSubmissionDetail.setVisibility(toggleVisibility);
    }

    @OnClick(R.id.iv_approval_detail)
    void onClickApprovalDetail() {
        int toggleVisibility =
                (llApprovalDetails.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
        llApprovalDetails.setVisibility(toggleVisibility);
    }

    @OnClick(R.id.iv_activation_detail)
    void onClickActivationDetail() {
        int toggleVisibility =
                (llActivationDetails.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
        llActivationDetails.setVisibility(toggleVisibility);
    }
}
