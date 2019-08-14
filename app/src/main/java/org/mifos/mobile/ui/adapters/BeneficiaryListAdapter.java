package org.mifos.mobile.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mifos.mobile.R;
import org.mifos.mobile.injection.ActivityContext;
import org.mifos.mobile.models.beneficiary.Beneficiary;
import org.mifos.mobile.models.beneficiary.ThirdPartyBeneficiary;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dilpreet on 15/6/17.
 */

public class BeneficiaryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    private List<Beneficiary> beneficiaryList;

    private List<ThirdPartyBeneficiary> thirdPartyBeneficiaryList;

    @Inject
    public BeneficiaryListAdapter(@ActivityContext Context context) {
        this.context = context;
        beneficiaryList = new ArrayList<>();
        thirdPartyBeneficiaryList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_beneficiary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < beneficiaryList.size()) {
            Beneficiary beneficiary = beneficiaryList.get(position);
            ((ViewHolder) holder).tvAccountNumber.setText(beneficiary.getAccountNumber());
            ((ViewHolder) holder).tvName.setText(beneficiary.getName());
            ((ViewHolder) holder).tvOfficeName.setText(beneficiary.getOfficeName());
        } else {
            ThirdPartyBeneficiary thirdPartyBeneficiary = thirdPartyBeneficiaryList
                    .get(position - beneficiaryList.size());
            ((ViewHolder) holder).tvAccountNumber.setText(
                    thirdPartyBeneficiary.getPartyIdInfo().getPartyIdentifier());
            ((ViewHolder) holder).tvName.setText(thirdPartyBeneficiary.getFirstName()
                                                     + " " + thirdPartyBeneficiary.getLastName());
            ((ViewHolder) holder).tvOfficeName.setText(
                    thirdPartyBeneficiary.getPartyIdInfo().getPartyIdType()
            );
        }
    }

    @Override
    public int getItemCount() {
        return beneficiaryList.size() + thirdPartyBeneficiaryList.size();
    }

    public void setBeneficiaryList(List<Beneficiary> beneficiaryList) {
        this.beneficiaryList = beneficiaryList;
        notifyDataSetChanged();
    }

    public void setThirdParyBeneficiaryList(List<ThirdPartyBeneficiary> thirdParyBeneficiaryList) {
        this.thirdPartyBeneficiaryList = thirdParyBeneficiaryList;
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
