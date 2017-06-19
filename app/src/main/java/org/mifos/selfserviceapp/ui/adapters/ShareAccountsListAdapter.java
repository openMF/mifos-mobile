package org.mifos.selfserviceapp.ui.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.accounts.share.ShareAccount;
import org.mifos.selfserviceapp.utils.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareAccountsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private List<ShareAccount> shareAccountssList = new ArrayList<>();

    public ShareAccountsListAdapter(Context context, List<ShareAccount> shareAccountssList) {
        this.context = context;
        this.shareAccountssList = shareAccountssList;
    }

    public ShareAccount getItem(int position) {
        return shareAccountssList.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_share_account, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {

            ShareAccount shareAccount = getItem(position);
            ((ViewHolder) holder).tvClientShareAccountsNumber
                    .setText(shareAccount.getAccountNo());
            ((ViewHolder) holder).tvShareAccountsProductName
                    .setText(shareAccount.getProductName());
            ((ViewHolder) holder).llAccountDetail.setVisibility(View.GONE);

            if (shareAccount.getStatus().getActive()) {

                ((ViewHolder) holder).ivStatusIndicator
                        .setImageDrawable(setCircularBackground(R.color.deposit_green));
                setSharingAccountDetail(((ViewHolder) holder), shareAccount);

            } else if (shareAccount.getStatus().getApproved()) {

                ((ViewHolder) holder).ivStatusIndicator
                        .setImageDrawable(setCircularBackground(R.color.light_green));

            } else if (shareAccount.getStatus().getSubmittedAndPendingApproval()) {

                ((ViewHolder) holder).ivStatusIndicator
                        .setImageDrawable(setCircularBackground(R.color.light_yellow));

            } else {

                ((ViewHolder) holder).ivStatusIndicator
                        .setImageDrawable(setCircularBackground(R.color.light_blue));

            }
        }
    }

    private void setSharingAccountDetail(ViewHolder viewHolder, ShareAccount shareAccount) {
        viewHolder.llAccountDetail.setVisibility(View.VISIBLE);
        viewHolder.tvSharesPending.setText(String.valueOf(shareAccount
                .getTotalPendingForApprovalShares()));
        viewHolder.tvSharesApproved.setText(String.valueOf(shareAccount
                .getTotalApprovedShares()));
    }
    @Override
    public int getItemCount() {
        return shareAccountssList.size();
    }

    private LayerDrawable setCircularBackground(int colorId) {
        Drawable color = new ColorDrawable(ContextCompat.getColor(context, colorId));
        Drawable image = ContextCompat.getDrawable(context, R.drawable.circular_background);
        LayerDrawable ld = new LayerDrawable(new Drawable[]{image, color});
        return ld;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_clientSharingAccountNumber)
        TextView tvClientShareAccountsNumber;

        @BindView(R.id.tv_shareAccountProductName)
        TextView tvShareAccountsProductName;

        @BindView(R.id.iv_status_indicator)
        CircularImageView ivStatusIndicator;

        @BindView(R.id.ll_account_detail)
        LinearLayout llAccountDetail;

        @BindView(R.id.tv_shares_pending)
        TextView tvSharesPending;

        @BindView(R.id.tv_shares_approved)
        TextView tvSharesApproved;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
