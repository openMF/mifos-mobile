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

    public String separator(String inputText) {
        String textToSeparate = inputText;
        String textToLeaveAsItIs = "";
        if (inputText.contains(".")) {
            int indexOfPoint = inputText.indexOf('.');
            textToSeparate = inputText.substring(0, indexOfPoint);
            textToLeaveAsItIs = inputText.substring(indexOfPoint + 1);
        }
        String resultText = "";
        int coefficientOfNeededGroups = textToSeparate.length() / 3;
        if (coefficientOfNeededGroups > 0) {
            for (int i = 0; i < textToSeparate.length(); i++) {
                resultText += String.valueOf(textToSeparate.charAt(i));
                for (int k = 1; k < coefficientOfNeededGroups + 1; k++) {
                    if (i == textToSeparate.length() - k * 4 + (k - 1)) {
                        resultText += ",";
                    }
                }
            }
        } else {
            resultText = textToSeparate;
        }
        if (textToLeaveAsItIs.equals("")) {
            return resultText;
        } else {
            return resultText + "." + textToLeaveAsItIs;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Periods period = periodses.get(position);

        holder.tvLoanBalance.setText(
                separator(String.valueOf(period.getPrincipalOriginalDue()))
                        + " " + currency);

        holder.tvOutStandingBalance.setText(
                separator(String.valueOf(period.getPrincipalLoanBalanceOutstanding()))
                        + " " + currency);

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