package org.mifos.mobile.ui.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mifos.mobile.R;
import org.mifos.mobile.injection.ActivityContext;
import org.mifos.mobile.models.CheckboxStatus;
import org.mifos.mobile.utils.MaterialDialog;

import java.util.List;

import javax.inject.Inject;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;
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
    private int checkIfShouldBEenabledVariable = 0;
    private boolean isEnabled;
    private MaterialDialog.Builder dialogBuilder;

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

        @OnClick(R.id.cb_status_select)
        public void changeValue() {
            if (!cbStatusSelect.isChecked()) {
                checkIfShouldBEenabledVariable--;
            } else {
                checkIfShouldBEenabledVariable++;
            }
            if (checkIfShouldBEenabledVariable > 0) {
                isEnabled = true;
            } else {
                isEnabled = false;
            }
            toogleStatus();
        }
        @OnClick(R.id.ll_row_checkbox)
        public void rowClicked() {
            if (cbStatusSelect.isChecked()) {
                checkIfShouldBEenabledVariable--;
            } else {
                checkIfShouldBEenabledVariable++;
            }
            if (checkIfShouldBEenabledVariable > 0) {
                isEnabled = true;
            } else {
                isEnabled = false;
            }
            toogleStatus();
            cbStatusSelect.setChecked(!cbStatusSelect.isChecked());
        }

        @OnCheckedChanged(R.id.cb_status_select)
        public void checkChanges() {
            statusList.get(getAdapterPosition()).setChecked(cbStatusSelect.isChecked());
        }
    }

    /**
     This function receives a MaterialDialog Builder to change the button visibility.
     @param dialogBuilder: Material Dialog Builder
    */
    public void setDialogBuilder(MaterialDialog.Builder dialogBuilder) {
        this.dialogBuilder = dialogBuilder;
    }
    /**
     This function enables or disables Positive Button.
     */
    void toogleStatus() {
        if (isEnabled) {
            dialogBuilder.enablePositiveButton();
        } else {
            dialogBuilder.disablePositiveButton();
        }
    }
}
