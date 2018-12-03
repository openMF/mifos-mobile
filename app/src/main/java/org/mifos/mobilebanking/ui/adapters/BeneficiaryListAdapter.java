package org.mifos.mobilebanking.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.injection.ActivityContext;
import org.mifos.mobilebanking.models.beneficiary.Beneficiary;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dilpreet on 15/6/17.
 */

public class BeneficiaryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    private List<Beneficiary> beneficiaryList;

    @Inject
    public BeneficiaryListAdapter(@ActivityContext Context context) {
        this.context = context;
        beneficiaryList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_beneficiary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Beneficiary beneficiary = beneficiaryList.get(position);
        ((ViewHolder) holder).tvAccountNumber.setText(beneficiary.getAccountNumber());
        ((ViewHolder) holder).tvName.setText(beneficiary.getName());
        ((ViewHolder) holder).tvOfficeName.setText(beneficiary.getOfficeName());
    }

    @Override
    public int getItemCount() {
        return beneficiaryList.size();
    }

    public void setBeneficiaryList(List<Beneficiary> beneficiaryList) {
        this.beneficiaryList = beneficiaryList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_beneficiary_name)
        TextView tvName;

        @BindView(R.id.tv_account_number)
        TextView tvAccountNumber;

        @BindView(R.id.tv_office_name)
        TextView tvOfficeName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
