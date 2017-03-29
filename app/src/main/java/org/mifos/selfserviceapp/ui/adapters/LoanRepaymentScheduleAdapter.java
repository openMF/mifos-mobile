package org.mifos.selfserviceapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.accounts.loan.Periods;
import org.mifos.selfserviceapp.utils.DateHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rajan Maurya on 04/03/17.
 */

public class LoanRepaymentScheduleAdapter extends
        RecyclerView.Adapter<LoanRepaymentScheduleAdapter.ViewHolder> {

    private List<Periods> periodses;
    private String currency;
    private Context context;

    @Inject
    public LoanRepaymentScheduleAdapter() {
        periodses = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_loan_repayment_schedule, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Periods period = periodses.get(position);

        holder.tvLoanBalance.setText(context.getString(R.string.double_and_String,
                period.getPrincipalLoanBalanceOutstanding(), currency));

        holder.tvOutStandingBalance.setText(context.getString(R.string.double_and_String,
                        period.getPrincipalOriginalDue(), currency));

        holder.tvDate.setText(DateHelper.getDateAsString(period.getDueDate()));
    }

    public void setPeriods(List<Periods> periodses) {
        this.periodses = periodses;
        this.periodses.remove(0);
        notifyDataSetChanged();
    }

    public void setCurrency(String currency) {
        this.currency = currency;
        notifyDataSetChanged();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return periodses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_loan_balance)
        TextView tvLoanBalance;

        @BindView(R.id.tv_loan_outstanding)
        TextView tvOutStandingBalance;

        @BindView(R.id.tv_date)
        TextView tvDate;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}