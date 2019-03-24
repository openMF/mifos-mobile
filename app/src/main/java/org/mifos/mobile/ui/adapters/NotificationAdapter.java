package org.mifos.mobile.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.mifos.mobile.R;
import org.mifos.mobile.injection.ActivityContext;
import org.mifos.mobile.models.notification.MifosNotification;
import org.mifos.mobile.utils.DateHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dilpreet on 13/9/17.
 */

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<MifosNotification> notificationList;

    @Inject
    public NotificationAdapter(@ActivityContext Context context) {
        this.context = context;
        this.notificationList = new ArrayList<>();
    }

    public void setNotificationList(List<MifosNotification> notificationList) {
        this.notificationList = notificationList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_notification, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MifosNotification notification = notificationList.get(position);

        ((ViewHolder) holder).tvNotificationText.setText(notification.getMsg());
        ((ViewHolder) holder).tvNotificationTime.setText(DateHelper.
                getDateAndTimeAsStringFromLong(notification.getTimeStamp()));

        if (notification.isRead()) {
            ((ViewHolder) holder).ivNotificationIcon.setColorFilter(ContextCompat.
                    getColor(context, R.color.gray_dark));
            ((ViewHolder) holder).btnDismissNotification.setVisibility(View.GONE);
        } else {
            ((ViewHolder) holder).ivNotificationIcon.setColorFilter(ContextCompat.
                    getColor(context, R.color.primary));
            ((ViewHolder) holder).btnDismissNotification.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_notification_text)
        TextView tvNotificationText;

        @BindView(R.id.tv_notification_time)
        TextView tvNotificationTime;

        @BindView(R.id.iv_notification_icon)
        ImageView ivNotificationIcon;

        @BindView(R.id.btn_dismiss_notification)
        Button btnDismissNotification;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.btn_dismiss_notification)
        public void dismissNotification() {
            notificationList.get(getAdapterPosition()).setRead(true);
            notificationList.get(getAdapterPosition()).save();
            notifyItemChanged(getAdapterPosition());
        }
    }
}
