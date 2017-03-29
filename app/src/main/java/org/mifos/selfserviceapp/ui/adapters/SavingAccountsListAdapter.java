package org.mifos.selfserviceapp.ui.adapters;

import android.content.Context;
import org.mifos.selfserviceapp.utils.Utils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.accounts.savings.SavingAccount;
import org.mifos.selfserviceapp.utils.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Vishwajeet
 * @since 22/6/16.
 */
public class SavingAccountsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;

    private List<SavingAccount> savingAccountsList = new ArrayList<>();

    public SavingAccountsListAdapter(Context context, List<SavingAccount> savingAccountsList) {
        this.context = context;
        this.savingAccountsList = savingAccountsList;
    }

    public SavingAccount getItem(int position) {
        return savingAccountsList.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_saving_account, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {

            SavingAccount savingAccount = getItem(position);

            ((ViewHolder) holder).tv_clientSavingAccountNumber.setText(
                    savingAccount.getAccountNo());

            ((ViewHolder) holder).tv_savingAccountProductName.setText(
                    savingAccount.getProductName());


            if (savingAccount.getStatus().getActive()) {

                ((ViewHolder) holder).iv_status_indicator.setImageDrawable(
                        Utils.setCircularBackground(R.color.deposit_green, context));

            } else if (savingAccount.getStatus().getApproved()) {

                ((ViewHolder) holder).iv_status_indicator.setImageDrawable(
                        Utils.setCircularBackground(R.color.light_green, context));

            } else if (savingAccount.getStatus().getSubmittedAndPendingApproval()) {

                ((ViewHolder) holder).iv_status_indicator.setImageDrawable(
                        Utils.setCircularBackground(R.color.light_yellow, context));

            } else {

                ((ViewHolder) holder).iv_status_indicator.setImageDrawable(
                        Utils.setCircularBackground(R.color.light_blue, context));

            }

        }

    }

    @Override
    public int getItemCount() {
        return savingAccountsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_clientSavingAccountNumber)
        TextView tv_clientSavingAccountNumber;

        @BindView(R.id.tv_savingAccountProductName)
        TextView tv_savingAccountProductName;

        @BindView(R.id.iv_status_indicator)
        CircularImageView iv_status_indicator;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}