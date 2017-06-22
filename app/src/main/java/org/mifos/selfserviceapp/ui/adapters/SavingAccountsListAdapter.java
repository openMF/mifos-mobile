package org.mifos.selfserviceapp.ui.adapters;

import android.content.Context;

import org.mifos.selfserviceapp.injection.ActivityContext;
import org.mifos.selfserviceapp.utils.DateHelper;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.accounts.savings.SavingAccount;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Vishwajeet
 * @since 22/6/16.
 */
public class SavingAccountsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;

    private List<SavingAccount> savingAccountsList = new ArrayList<>();

    @Inject
    public SavingAccountsListAdapter(@ActivityContext Context context) {
        this.context = context;
    }

    public void setSavingAccountsList(List<SavingAccount> savingAccountsList) {
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

            ((ViewHolder) holder).tvClientSavingAccountNumber.setText(
                    savingAccount.getAccountNo());

            ((ViewHolder) holder).tvSavingAccountProductName.setText(
                    savingAccount.getProductName());
            ((ViewHolder) holder).llAccountDetail.setVisibility(View.GONE);
            ((ViewHolder) holder).tvLastActive.setVisibility(View.GONE);

            if (savingAccount.getStatus().getActive()) {

                ((ViewHolder) holder).ivStatusIndicator.setBackgroundColor(ContextCompat.
                        getColor(context, R.color.deposit_green));
                setSavingAccountsDetails(((ViewHolder) holder), savingAccount);
            } else if (savingAccount.getStatus().getApproved()) {

                ((ViewHolder) holder).ivStatusIndicator.setBackgroundColor(ContextCompat.
                        getColor(context, R.color.light_green));

            } else if (savingAccount.getStatus().getSubmittedAndPendingApproval()) {

                ((ViewHolder) holder).ivStatusIndicator.setBackgroundColor(ContextCompat.
                        getColor(context, R.color.light_yellow));

            } else if (savingAccount.getStatus().getMatured()) {
                ((ViewHolder) holder).ivStatusIndicator.setBackgroundColor(ContextCompat.
                        getColor(context, R.color.red_light));
                setSavingAccountsDetails(((ViewHolder) holder), savingAccount);
            } else {

                ((ViewHolder) holder).ivStatusIndicator.setBackgroundColor(ContextCompat.
                        getColor(context, R.color.black));

            }

        }

    }

    private void setSavingAccountsDetails(ViewHolder viewHolder, SavingAccount savingAccount) {
        viewHolder.llAccountDetail.setVisibility(View.VISIBLE);
        viewHolder.tvLastActive.setVisibility(View.VISIBLE);
        viewHolder.tvAccountBalance.setText( context.getString(R.string.double_and_String,
                savingAccount.getAccountBalance(), savingAccount.getCurrency().getDisplaySymbol()));

        viewHolder.tvLastActive.setText(DateHelper.getDateAsString(savingAccount
                .getLastActiveTransactionDate()));
    }

    @Override
    public int getItemCount() {
        return savingAccountsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_clientSavingAccountNumber)
        TextView tvClientSavingAccountNumber;

        @BindView(R.id.tv_savingAccountProductName)
        TextView tvSavingAccountProductName;

        @BindView(R.id.iv_status_indicator)
        View ivStatusIndicator;

        @BindView(R.id.ll_account_detail)
        LinearLayout llAccountDetail;

        @BindView(R.id.tv_last_active)
        TextView tvLastActive;

        @BindView(R.id.tv_account_balance)
        TextView tvAccountBalance;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}