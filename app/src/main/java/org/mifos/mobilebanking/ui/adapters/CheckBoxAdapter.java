package org.mifos.mobilebanking.ui.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.injection.ActivityContext;
import org.mifos.mobilebanking.models.CheckboxStatus;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by dilpreet on 3/7/17.
 */

public class CheckBoxAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<CheckboxStatus> statusList;

    @Inject
    public CheckBoxAdapter(@ActivityContext Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_checkbox, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        CheckboxStatus statusModel = statusList.get(position);

        int[][] states = {{android.R.attr.state_checked}, {}};
        int[] colors = {statusModel.getColor(), statusModel.getColor()};

        ((ViewHolder) holder).cbStatusSelect.setChecked(statusModel.isChecked());
        ((ViewHolder) holder).cbStatusSelect.setSupportButtonTintList(new ColorStateList(states,
                colors));
        ((ViewHolder) holder).tvStatus.setText(statusModel.getStatus());
    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    public void setStatusList(List<CheckboxStatus> statusList) {
        this.statusList = statusList;
    }

    public List<CheckboxStatus> getStatusList() {
        return statusList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cb_status_select)
        AppCompatCheckBox cbStatusSelect;

        @BindView(R.id.tv_status)
        TextView tvStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.ll_row_checkbox)
        public void rowClicked() {
            cbStatusSelect.setChecked(!cbStatusSelect.isChecked());
        }

        @OnCheckedChanged(R.id.cb_status_select)
        public void checkChanges() {
            statusList.get(getAdapterPosition()).setChecked(cbStatusSelect.isChecked());
        }
    }
}
