package org.mifos.selfserviceapp.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.Transaction;
import org.mifos.selfserviceapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Vishwajeet
 * @since 10/08/16
 */
public class RecentTransactionListAdapter extends
        RecyclerView.Adapter<RecentTransactionListAdapter.ViewHolder> {

    private List<Transaction> transactions;

    @Inject
    public RecentTransactionListAdapter() {
        transactions = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_recent_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Transaction transaction = getItem(position);

        holder.tvAmount.setText(String.valueOf(transaction.getAmount() +
                transaction.getCurrency().getCode()));
        holder.tvTypeValue.setText(transaction.getType().getValue());
        holder.tvTransactionsDate.setText(
                transaction.getSubmittedOnDate().get(2).toString() +
                        Constants.BACK_SLASH + transaction.getSubmittedOnDate().get(
                        1).toString() +
                        Constants.BACK_SLASH + transaction.getSubmittedOnDate().get(
                        0).toString());
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }


    public Transaction getItem(int position) {
        return transactions.get(position);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_transactionDate)
        TextView tvTransactionsDate;

        @BindView(R.id.tv_amount)
        TextView tvAmount;

        @BindView(R.id.tv_value)
        TextView tvTypeValue;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}