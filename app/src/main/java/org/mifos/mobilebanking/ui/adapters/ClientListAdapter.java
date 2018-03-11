package org.mifos.mobilebanking.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.models.client.Client;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Vishwajeet
 * @since 20/06/16
 */

public class ClientListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    LayoutInflater layoutInflater;
    List<Client> listItems;
    private Context mContext;

    public ClientListAdapter(Context context, List<Client> listItems) {

        layoutInflater = LayoutInflater.from(context);
        this.listItems = listItems;
        this.mContext = context;
    }

    public Client getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_client_name, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerView.ViewHolder) {

            Client client = getItem(position);
            ((ViewHolder) holder).tv_clientName.setText(client.getFirstname() + " " + client
                    .getLastname());
            ((ViewHolder) holder).tv_clientAccountNumber.setText(client.getAccountNo());

        }
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_clientName)
        TextView tv_clientName;
        @BindView(R.id.tv_clientAccountNumber)
        TextView tv_clientAccountNumber;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}