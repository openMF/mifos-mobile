package org.mifos.selfserviceapp.ui.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.injection.ActivityContext;
import org.mifos.selfserviceapp.models.Charge;
import org.mifos.selfserviceapp.utils.DateHelper;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Vishwajeet
 * @since 17/8/16.
 */

public class ClientChargeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private List<Charge> clientChargeList = new ArrayList<>();

    @Inject
    public ClientChargeAdapter(@ActivityContext Context context) {
        this.context = context;
    }

    public void setClientChargeList(List<Charge> clientChargeList) {
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
        vh = new ViewHolder(v);
        return vh;
    }

    // Binds the values for each of the stored variables to the view
    // Also changes the color of the circle depending on whether the charge is active or not
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerView.ViewHolder) {

            Charge charge = getItem(position);
            ((ViewHolder) holder).tvAmountDue.setText(context.getString(R.string.double_and_String,
                    charge.getAmount(), charge.getCurrency().getDisplaySymbol()));
            ((ViewHolder) holder).tvAmountPaid.setText(context.getString(R.string.double_and_String,
                    charge.getAmountPaid(), charge.getCurrency().getDisplaySymbol()));
            ((ViewHolder) holder).tvAmountWaived.setText(context.
                    getString(R.string.double_and_String, charge.getAmountWaived(),
                            charge.getCurrency().getDisplaySymbol()));
            ((ViewHolder) holder).tvAmountOutstanding.setText(context.
                    getString(R.string.double_and_String, charge.getAmountOutstanding(),
                            charge.getCurrency().getDisplaySymbol()));
            ((ViewHolder) holder).tvClientName.setText(charge.getName());
            if (charge.getDueDate().size() > 0) {
                ((ViewHolder) holder).tvDueDate.setText(DateHelper.getDateAsString(charge.
                        getDueDate()));
            }

            if (charge.isIsPaid() || charge.isIsWaived() || charge.getPaid() || charge.
                    getWaived()) {
                ((ViewHolder) holder).circle_status.setBackgroundColor(ContextCompat.
                        getColor(context, R.color.black));
            } else {
                ((ViewHolder) holder).circle_status.setBackgroundColor(ContextCompat.
                        getColor(context, R.color.deposit_green));
            }

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
        TextView tvAmountDue;

        @BindView(R.id.tv_amount_paid)
        TextView tvAmountPaid;

        @BindView(R.id.tv_amount_waived)
        TextView tvAmountWaived;

        @BindView(R.id.tv_amount_outstanding)
        TextView tvAmountOutstanding;

        @BindView(R.id.circle_status)
        View circle_status;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
