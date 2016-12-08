package org.mifos.selfserviceapp.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.Charge;

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

            ImageView iv = ((ClientChargeAdapter.ViewHolder) holder).circle_color;
            GradientDrawable bgShape = (GradientDrawable) iv.getDrawable();
            if (charge.isActive()) {
                bgShape.setColor(Color.rgb(129, 209, 53));
            } else {
                bgShape.setColor(Color.rgb(255, 255, 255));
            }

            String day = charge.getDueDate().get(2).toString() + " ";
            String month = "";
            switch (charge.getDueDate().get(1).toString()) {
                case "1":  month = "January ";
                    break;
                case "2":  month = "February ";
                    break;
                case "3":  month = "March ";
                    break;
                case "4":  month = "April ";
                    break;
                case "5":  month = "May ";
                    break;
                case "6":  month = "June ";
                    break;
                case "7":  month = "July ";
                    break;
                case "8":  month = "August ";
                    break;
                case "9":  month = "September ";
                    break;
                case "10": month = "October ";
                    break;
                case "11": month = "November ";
                    break;
                case "12": month = "December ";
                    break;
                default: month = "Invalid month ";
                    break;
            }
            String year = charge.getDueDate().get(0).toString();

            ((ViewHolder) holder).tvDueDate.setText(day + month + year);
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

        @BindView(R.id.circle_color)
        ImageView circle_color;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
