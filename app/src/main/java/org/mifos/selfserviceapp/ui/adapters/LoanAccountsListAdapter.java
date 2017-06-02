package org.mifos.selfserviceapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.utils.CircularImageView;
import org.mifos.selfserviceapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Vishwajeet
 * @since 22/6/16.
 */
public class LoanAccountsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private List<LoanAccount> loanAccountsList = new ArrayList<>();

    public LoanAccountsListAdapter(Context context, List<LoanAccount> loanAccountsList) {
        this.context = context;
        this.loanAccountsList = loanAccountsList;
    }

    public LoanAccount getItem(int position) {
        return loanAccountsList.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_loan_account, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {

            LoanAccount loanAccount = getItem(position);
            ((ViewHolder) holder).tv_clientLoanAccountNumber.setText(loanAccount.getAccountNo());
            ((ViewHolder) holder).tv_loanAccountProductName.setText(loanAccount.getProductName());

            if (loanAccount.getStatus().getActive() && loanAccount.getInArrears()) {
                ((ViewHolder) holder).iv_status_indicator.setImageDrawable(
                        Utils.setCircularBackground(R.color.red, context));
            } else if (loanAccount.getStatus().getActive()) {
                ((ViewHolder) holder).iv_status_indicator.setImageDrawable(
                        Utils.setCircularBackground(R.color.deposit_green, context));
            } else if (loanAccount.getStatus().getWaitingForDisbursal()) {
                ((ViewHolder) holder).iv_status_indicator.setImageDrawable(
                        Utils.setCircularBackground(R.color.blue, context));
            } else if (loanAccount.getStatus().getPendingApproval()) {
                ((ViewHolder) holder).iv_status_indicator.setImageDrawable(
                        Utils.setCircularBackground(R.color.light_yellow, context));
            }  else if (loanAccount.getStatus().getOverpaid()) {
                ((ViewHolder) holder).iv_status_indicator.setImageDrawable(
                        Utils.setCircularBackground(R.color.purple, context));
            } else {
                ((ViewHolder) holder).iv_status_indicator.setImageDrawable(
                        Utils.setCircularBackground(R.color.black, context));
            }
        }

    }

    @Override
    public int getItemCount() {
        return loanAccountsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_clientLoanAccountNumber)
        TextView tv_clientLoanAccountNumber;

        @BindView(R.id.tv_loanAccountProductName)
        TextView tv_loanAccountProductName;

        @BindView(R.id.iv_status_indicator)
        CircularImageView iv_status_indicator;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
