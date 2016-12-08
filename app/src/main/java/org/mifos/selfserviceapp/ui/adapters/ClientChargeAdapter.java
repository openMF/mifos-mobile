package org.mifos.selfserviceapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.Charge;
import org.mifos.selfserviceapp.utils.CircularImageView;
import org.mifos.selfserviceapp.utils.Utils;
import java.util.ArrayList;
import java.util.Currency;
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

    // Binds the values for each of the stored variables to the view
    // Also changes the color of the circle depending on whether the charge is active or not
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerView.ViewHolder) {

            Charge charge = getItem(position);
            Currency c  = Currency.getInstance("" + (charge.getCurrency()).getCode());
            ((ClientChargeAdapter.ViewHolder) holder).tvAmountDue.setText(
                    String.valueOf(charge.getAmount() + " " + c.getSymbol()));
            ((ClientChargeAdapter.ViewHolder) holder).tvAmountPaid.setText(
                    String.valueOf(charge.getAmountPaid() + " " + c.getSymbol()));
            ((ClientChargeAdapter.ViewHolder) holder).tvAmountWaived.setText(
                    String.valueOf(charge.getAmountWaived() + " " + c.getSymbol()));
            ((ClientChargeAdapter.ViewHolder) holder).tvAmountOutstanding.setText(
                    String.valueOf(charge.getAmountOutstanding() + " " + c.getSymbol()));
            ((ViewHolder) holder).tvClientName.setText(charge.getName());

            if (charge.isIsActive()) {
                ((ViewHolder) holder).circle_status.setImageDrawable(
                        Utils.setCircularBackground(R.color.deposit_green, context));
            } else if (charge.isIsPaid()) {
                ((ViewHolder) holder).circle_status.setImageDrawable(
                        Utils.setCircularBackground(R.color.light_yellow, context));
            } else {
                ((ViewHolder) holder).circle_status.setImageDrawable(
                        Utils.setCircularBackground(R.color.light_blue, context));
            }

            String day = charge.getDueDate().get(2).toString();
            String month = Utils.getMonth(Integer.parseInt(charge.getDueDate().get(1).toString()));
            String year = charge.getDueDate().get(0).toString();

            ((ViewHolder) holder).tvDueDate.setText(day + " " + month + " " + year);
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
        CircularImageView circle_status;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
