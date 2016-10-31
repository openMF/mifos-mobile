package org.mifos.selfserviceapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;

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
    private final LayoutInflater layoutInflater;
    private List<LoanAccount> loanAccountsList = new ArrayList<>();

    public LoanAccountsListAdapter(Context context, List<LoanAccount> loanAccountsList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
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
        if (holder instanceof RecyclerView.ViewHolder) {

            LoanAccount loanAccount = getItem(position);
            ((ViewHolder) holder).tv_clientLoanAccountNumber.setText(loanAccount.getAccountNo());
            ((ViewHolder) holder).tv_loanAccountProductName.setText(loanAccount.getProductName());
            if (loanAccount.getStatus().getPendingApproval()) {
                ((ViewHolder) holder).vw_loanStatus.
                        setBackgroundResource(R.drawable.pending_approval);
            } else if (loanAccount.getStatus().getWaitingForDisbursal()) {
                ((ViewHolder) holder).vw_loanStatus.
                        setBackgroundResource(R.drawable.waiting_for_disbursal);
            } else if (loanAccount.getStatus().getActive()) {
                ((ViewHolder) holder).vw_loanStatus.
                        setBackgroundResource(R.drawable.active);
            } else if (loanAccount.getStatus().getClosedObligationsMet()) {
                ((ViewHolder) holder).vw_loanStatus.
                        setBackgroundResource(R.drawable.closed_obligationns_met);
            } else if (loanAccount.getStatus().getClosedWrittenOff()) {
                ((ViewHolder) holder).vw_loanStatus.
                        setBackgroundResource(R.drawable.closed_written_of);
            } else if (loanAccount.getStatus().getClosedRescheduled()) {
                ((ViewHolder) holder).vw_loanStatus.
                        setBackgroundResource(R.drawable.closed_resheduled);
            } else if (loanAccount.getStatus().getClosed()) {
                ((ViewHolder) holder).vw_loanStatus.
                        setBackgroundResource(R.drawable.closed);
            } else if (loanAccount.getStatus().getOverpaid()) {
                ((ViewHolder) holder).vw_loanStatus.
                        setBackgroundResource(R.drawable.overpaid);
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
        @BindView(R.id.vw_loanStatus)
        View vw_loanStatus;


        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
