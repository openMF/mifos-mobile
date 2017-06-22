package org.mifos.selfserviceapp.ui.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.injection.ActivityContext;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Vishwajeet
 * @since 22/6/16.
 */
public class LoanAccountsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private List<LoanAccount> loanAccountsList = new ArrayList<>();

    @Inject
    public LoanAccountsListAdapter(@ActivityContext Context context) {
        this.context = context;
    }

    public void setLoanAccountsList(List<LoanAccount> loanAccountsList) {
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
            ((ViewHolder) holder).tvClientLoanAccountNumber.setText(loanAccount.getAccountNo());
            ((ViewHolder) holder).tvLoanAccountProductName.setText(loanAccount.getProductName());
            ((ViewHolder) holder).llAccountDetail.setVisibility(View.GONE);

            if (loanAccount.getStatus().getActive() && loanAccount.getInArrears()) {
                ((ViewHolder) holder).ivStatusIndicator.setBackgroundColor(ContextCompat.
                        getColor(context, R.color.red));
                setLoanAccountsDetails(((ViewHolder) holder), loanAccount);
            } else if (loanAccount.getStatus().getActive()) {
                ((ViewHolder) holder).ivStatusIndicator.setBackgroundColor(ContextCompat.
                        getColor(context, R.color.deposit_green));
                setLoanAccountsDetails(((ViewHolder) holder), loanAccount);
            } else if (loanAccount.getStatus().getWaitingForDisbursal()) {
                ((ViewHolder) holder).ivStatusIndicator.setBackgroundColor(ContextCompat.
                        getColor(context, R.color.blue));
            } else if (loanAccount.getStatus().getPendingApproval()) {
                ((ViewHolder) holder).ivStatusIndicator.setBackgroundColor(ContextCompat.
                        getColor(context, R.color.light_yellow));
            }  else if (loanAccount.getStatus().getOverpaid()) {
                ((ViewHolder) holder).ivStatusIndicator.setBackgroundColor(ContextCompat.
                        getColor(context, R.color.purple));
                setLoanAccountsDetails(((ViewHolder) holder), loanAccount);
            } else {
                ((ViewHolder) holder).ivStatusIndicator.setBackgroundColor(ContextCompat.
                        getColor(context, R.color.black));
            }
        }

    }

    private void setLoanAccountsDetails(ViewHolder viewHolder, LoanAccount loanAccount) {

        double amountPaid = loanAccount.getAmountPaid() != 0 ? loanAccount.getAmountPaid() : 0;
        double amountBalance = loanAccount.getLoanBalance() != 0 ? loanAccount.getLoanBalance() : 0;
        viewHolder.llAccountDetail.setVisibility(View.VISIBLE);
        viewHolder.tvAccountBalance.setText(String.valueOf(amountBalance));
        viewHolder.tvAccountPaid.setText(String.valueOf(amountPaid));
    }

    @Override
    public int getItemCount() {
        return loanAccountsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_clientLoanAccountNumber)
        TextView tvClientLoanAccountNumber;

        @BindView(R.id.tv_loanAccountProductName)
        TextView tvLoanAccountProductName;

        @BindView(R.id.iv_status_indicator)
        View ivStatusIndicator;

        @BindView(R.id.ll_account_detail)
        LinearLayout llAccountDetail;

        @BindView(R.id.tv_account_balance)
        TextView tvAccountBalance;

        @BindView(R.id.tv_account_paid)
        TextView tvAccountPaid;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
