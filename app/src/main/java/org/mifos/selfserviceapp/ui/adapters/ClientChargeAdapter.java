package org.mifos.selfserviceapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.Charge;
import org.mifos.selfserviceapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Vishwajeet
 * @since 17/8/16.
 */

public class ClientChargeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final LayoutInflater layoutInflater;
    private List<Charge> clientChargeList = new ArrayList<>();

    public ClientChargeAdapter(Context context, List<Charge> clientChargeList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.clientChargeList = clientChargeList;
    }

    public Charge getItem(int position) {
        return clientChargeList.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_client_charge, parent, false);
        vh = new ClientChargeAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerView.ViewHolder) {

            Charge charge = getItem(position);
            ((ClientChargeAdapter.ViewHolder) holder).tvAmount.setText(
                    String.valueOf(charge.getAmount() + charge.getCurrency().getCode()));
            ((ViewHolder) holder).tvClientName.setText(charge.getName());
            ((ViewHolder) holder).tvDueDate.setText(charge.getDueDate().get(2).toString() +
                    Constants.BACK_SLASH + charge.getDueDate().get(1).toString() +
                    Constants.BACK_SLASH + charge.getDueDate().get(0).toString());
        }

    }

    @Override
    public int getItemCount() {
        return clientChargeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_clientName)
        TextView tvClientName;
        @BindView(R.id.tv_due_date)
        TextView tvDueDate;
        @BindView(R.id.tv_amount)
        TextView tvAmount;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
