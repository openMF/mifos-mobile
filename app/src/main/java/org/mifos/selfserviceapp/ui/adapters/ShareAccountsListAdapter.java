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
            ((ViewHolder) holder).tv_clientshareAccountsNumber
                    .setText(shareAccount.getAccountNo());
            ((ViewHolder) holder).tv_shareAccountsProductName
                    .setText(shareAccount.getProductName());
            if (shareAccount.getStatus().getActive()) {

                ((ViewHolder) holder).iv_status_indicator
                        .setImageDrawable(setCircularBackground(R.color.deposit_green));

            } else if (shareAccount.getStatus().getApproved()) {

                ((ViewHolder) holder).iv_status_indicator
                        .setImageDrawable(setCircularBackground(R.color.light_green));

            } else if (shareAccount.getStatus().getSubmittedAndPendingApproval()) {

                ((ViewHolder) holder).iv_status_indicator
                        .setImageDrawable(setCircularBackground(R.color.light_yellow));

            } else {

                ((ViewHolder) holder).iv_status_indicator
                        .setImageDrawable(setCircularBackground(R.color.light_blue));

            }
        }
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
        TextView tv_clientshareAccountsNumber;

        @BindView(R.id.tv_shareAccountProductName)
        TextView tv_shareAccountsProductName;

        @BindView(R.id.iv_status_indicator)
        CircularImageView iv_status_indicator;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
