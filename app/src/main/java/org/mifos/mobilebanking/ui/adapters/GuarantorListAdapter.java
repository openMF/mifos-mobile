package org.mifos.mobilebanking.ui.adapters;

/*
 * Created by saksham on 24/July/2018
 */

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.models.guarantor.GuarantorPayload;
import org.mifos.mobilebanking.utils.DateHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GuarantorListAdapter extends RecyclerView.Adapter<GuarantorListAdapter.ViewHolder> {


    List<GuarantorPayload> list;
    Context context;
    OnClickListener listener;

    public interface OnClickListener {
        void setOnClickListener(int position);
    }

    public GuarantorListAdapter(Context context,
                                OnClickListener listener) {
        this.context = context;
        list = new ArrayList<>();
        this.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ViewHolder(inflater.inflate(R.layout.row_guarantor, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvGuarantorName.setText(list.get(position).getFirstname() + " "
                + list.get(position).getLastname());
        holder.tvJoinedDate.setText(DateHelper.getDateAsString(list.get(position)
                .getJoinedDate()));
        holder.cvContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setOnClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setGuarantorList(List<GuarantorPayload> payload) {
        this.list = payload;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_guarantor_name)
        TextView tvGuarantorName;

        @BindView(R.id.tv_joined_date)
        TextView tvJoinedDate;

        @BindView(R.id.cv_container)
        CardView cvContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
