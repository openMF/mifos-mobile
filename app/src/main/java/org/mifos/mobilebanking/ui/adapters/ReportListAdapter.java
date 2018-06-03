package org.mifos.mobilebanking.ui.adapters;

/*
 * Created by saksham on 01/June/2018
 */

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.injection.ActivityContext;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportListAdapter extends
        RecyclerView.Adapter<ReportListAdapter.ReportListViewHolder> {

    List<String> reports;
    Context context;
    OnItemClickListener onItemClickListener;

    @Inject
    public ReportListAdapter(@ActivityContext Context context) {
        this.context = context;
    }

    @Override
    public ReportListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new ReportListViewHolder(inflater.inflate(R.layout.row_report, parent, false));
    }

    @Override
    public void onBindViewHolder(ReportListViewHolder holder, int position) {
        holder.tvReportName.setText(reports.get(position));
        holder.bind(holder.cvReportItemContainer, position);
    }

    @Override
    public int getItemCount() {
        return (reports != null) ? reports.size() : 0;
    }

    public void setReports(List<String> reports) {
        this.reports = reports;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ReportListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_report_name)
        TextView tvReportName;

        @BindView(R.id.cv_report_item_container)
        CardView cvReportItemContainer;

        public ReportListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(CardView item, final int position) {
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.setOnItemClickListener(reports.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void setOnItemClickListener(String reportName);
    }
}
