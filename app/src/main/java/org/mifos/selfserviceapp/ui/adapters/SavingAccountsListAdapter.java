package org.mifos.selfserviceapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.accounts.savings.SavingAccount;

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
    private final LayoutInflater layoutInflater;
    private List<SavingAccount> savingAccountsList = new ArrayList<>();

    public SavingAccountsListAdapter(Context context, List<SavingAccount> savingAccountsList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
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
        if (holder instanceof RecyclerView.ViewHolder) {

            SavingAccount savingAccount = getItem(position);
            ((ViewHolder) holder).tv_clientSavingAccountNumber.setText(
                    savingAccount.getAccountNo().toString());
            ((ViewHolder) holder).tv_savingAccountProductName.setText(
                    savingAccount.getProductName());

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

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}