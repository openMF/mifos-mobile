package org.mifos.selfserviceapp.ui.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.accounts.savings.TransactionType;
import org.mifos.selfserviceapp.models.accounts.savings.Transactions;
import org.mifos.selfserviceapp.utils.DateHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dilpreet on 6/3/17.
 */

public class SavingAccountsTransactionListAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Transactions> savingAccountsTransactionList;
    private Context context;

    @Inject
    public SavingAccountsTransactionListAdapter() {
        savingAccountsTransactionList = new ArrayList<>();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Transactions getItem(int position) {
        return savingAccountsTransactionList.get(position);
    }

    public void setSavingAccountsTransactionList(List<Transactions> savingAccountsTransactionList) {
        this.savingAccountsTransactionList = savingAccountsTransactionList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_saving_account_transaction, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    public String separator(String inputText){
        String textToSeparate = inputText;
        String textToLeaveAsItIs = "";
        if(inputText.contains(".")){
            int indexOfPoint = inputText.indexOf(".");
            textToSeparate = inputText.substring(0,indexOfPoint);
            textToLeaveAsItIs = inputText.substring(indexOfPoint+1);
        }
        String resultText = "";
        int coefficientOfNeededGroups = textToSeparate.length()/3;
        if (coefficientOfNeededGroups > 0){
            for (int i=0; i<textToSeparate.length(); i++){
                resultText += String.valueOf(textToSeparate.charAt(i));
                for (int k=1; k< coefficientOfNeededGroups+1; k++){
                    if(i == textToSeparate.length()-k*4+(k-1)){
                        resultText += ",";
                    }
                }
            }
        }
        else {
            resultText = textToSeparate;
        }
        if (textToLeaveAsItIs.equals("")){
            return resultText;
        }
        else{
            return resultText + "." + textToLeaveAsItIs;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        Transactions transaction = getItem(position);

        ((ViewHolder) holder).tvSavingAccountAmount.setText(
                 separator(String.valueOf(transaction.getAmount()))+" "+transaction.getCurrency().getDisplaySymbol());

        ((ViewHolder) holder).tvSavingAccountRunningBalance.setText(
                separator(String.valueOf(transaction.getRunningBalance()))+" "+transaction.getCurrency().getDisplaySymbol());

        ((ViewHolder) holder).tvTransactionType.setText(transaction.
                getTransactionType().getValue());

        if (transaction.getPaymentDetailData() != null) {
            ((ViewHolder) holder).tvTransactionDetailData.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).tvTransactionDetailData.setText(transaction.
                    getPaymentDetailData().getPaymentType().getName());
        }

        ((ViewHolder) holder).tvTransactionDate.setText(DateHelper.
                getDateAsString(transaction.getDate()));

        ColorSelect color = getColor(transaction.getTransactionType());
        if (color == ColorSelect.RED) {
            ((ViewHolder) holder).vIndicator.setRotation(180);
            ((ViewHolder) holder).vIndicator.setBackgroundDrawable(
                    ContextCompat.getDrawable(context, R.drawable.triangular_red_view));
        } else {
            ((ViewHolder) holder).vIndicator.setRotation(0);
            ((ViewHolder) holder).vIndicator.setBackgroundDrawable(
                    ContextCompat.getDrawable(context, R.drawable.triangular_green_view));
        }

    }

    private enum ColorSelect {
        RED ,
        GREEN
    }

    @Override
    public int getItemCount() {
        return savingAccountsTransactionList.size();
    }

    private ColorSelect getColor(TransactionType transactionType) {
        if (transactionType.getDeposit())
            return ColorSelect.GREEN;
        if (transactionType.getDividendPayout())
            return ColorSelect.RED;
        if (transactionType.getWithdrawal())
            return ColorSelect.RED;
        if (transactionType.getInterestPosting())
            return ColorSelect.GREEN;
        if (transactionType.getFeeDeduction())
            return ColorSelect.RED;
        if (transactionType.getInitiateTransfer())
            return ColorSelect.RED;
        if (transactionType.getApproveTransfer())
            return ColorSelect.RED;
        if (transactionType.getWithdrawTransfer())
            return ColorSelect.RED;
        if (transactionType.getRejectTransfer())
            return ColorSelect.GREEN;
        if (transactionType.getOverdraftFee())
            return ColorSelect.RED;

        return ColorSelect.GREEN;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_transaction_date)
        TextView tvTransactionDate;

        @BindView(R.id.tv_transaction_type)
        TextView tvTransactionType;

        @BindView(R.id.tv_transaction_detail_data)
        TextView tvTransactionDetailData;

        @BindView(R.id.tv_savingAccountAmount)
        TextView tvSavingAccountAmount;

        @BindView(R.id.tv_saving_account_running_balance)
        TextView tvSavingAccountRunningBalance;

        @BindView(R.id.v_indicator)
        View vIndicator;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}