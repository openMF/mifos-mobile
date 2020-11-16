package org.mifos.mobile.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick

import org.mifos.mobile.R
import org.mifos.mobile.injection.ActivityContext
import org.mifos.mobile.models.notification.MifosNotification
import org.mifos.mobile.utils.DateHelper.getDateAndTimeAsStringFromLong

import java.util.*
import javax.inject.Inject

/**
 * Created by dilpreet on 13/9/17.
 */
class NotificationAdapter @Inject constructor(@param:ActivityContext private val context: Context) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var notificationList: List<MifosNotification?>?
    fun setNotificationList(notificationList: List<MifosNotification?>?) {
        this.notificationList = notificationList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.row_notification, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val notification = notificationList?.get(position)
        (holder as ViewHolder).tvNotificationText?.text = notification?.msg
        holder.tvNotificationTime?.text = getDateAndTimeAsStringFromLong(notification?.timeStamp)
        if (notification?.isRead() == true) {
            holder.ivNotificationIcon?.setColorFilter(ContextCompat.getColor(context, R.color.gray_dark))
            holder.btnDismissNotification?.visibility = View.GONE
        } else {
            holder.ivNotificationIcon?.setColorFilter(ContextCompat.getColor(context, R.color.primary))
            holder.btnDismissNotification?.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return if (notificationList != null) notificationList!!.size
        else 0
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        @JvmField
        @BindView(R.id.tv_notification_text)
        var tvNotificationText: TextView? = null

        @JvmField
        @BindView(R.id.tv_notification_time)
        var tvNotificationTime: TextView? = null

        @JvmField
        @BindView(R.id.iv_notification_icon)
        var ivNotificationIcon: ImageView? = null

        @JvmField
        @BindView(R.id.btn_dismiss_notification)
        var btnDismissNotification: Button? = null

        @OnClick(R.id.btn_dismiss_notification)
        fun dismissNotification() {
            notificationList?.get(adapterPosition)?.setRead(true)
            notificationList?.get(adapterPosition)?.save()
            notifyItemChanged(adapterPosition)
        }

        init {
            ButterKnife.bind(this, itemView!!)
        }
    }

    init {
        notificationList = ArrayList()
    }
}