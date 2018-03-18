package org.mifos.mobilebanking.ui.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.models.payload.AccountDetail;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dilpreet on 19/03/18.
 */

public class AccountsSpinnerAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;
    private final List<AccountDetail> accountDetails;
    private final int mResource;

    @BindView(R.id.tv_account_number)
    TextView tvAccountNumber;

    @BindView(R.id.tv_account_type)
    TextView tvAccountType;

    public AccountsSpinnerAdapter(Context context, @LayoutRes int resource, List objects) {
        super(context, resource, 0, objects);

        mInflater = LayoutInflater.from(context);
        mResource = resource;
        accountDetails = objects;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public View getView(int position, @Nullable View convertView,
                                 @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        final View view = mInflater.inflate(mResource, parent, false);
        ButterKnife.bind(this, view);
        AccountDetail accountDetail = accountDetails.get(position);
        tvAccountNumber.setText(accountDetail.getAccountNumber());
        tvAccountType.setText(accountDetail.getAccountType());
        return view;
    }
}
