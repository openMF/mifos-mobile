package org.mifos.mobile.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.qualifiers.ActivityContext
import org.mifos.mobile.R
import org.mifos.mobile.core.datastore.model.MifosNotification
import org.mifos.mobile.databinding.RowNotificationBinding
import org.mifos.mobile.ui.getThemeAttributeColor
import org.mifos.mobile.core.common.utils.DateHelper.getDateAndTimeAsStringFromLong
import javax.inject.Inject

/**
 * Created by dilpreet on 13/9/17.
 */
class NotificationAdapter @Inject constructor(@param:ActivityContext private val context: Context) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    private var notificationList: List<MifosNotification?>?
    fun setNotificationList(notificationList: List<MifosNotification?>?) {
        this.notificationList = notificationList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RowNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = notificationList?.get(position)
        holder.bind(notification)
    }

    override fun getItemCount(): Int {
        return if (notificationList != null) {
            notificationList!!.size
        } else {
            0
        }
    }

    inner class ViewHolder(private val binding: RowNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private fun dismissNotification() {
            notificationList?.get(bindingAdapterPosition)?.setRead(true)
            notificationList?.get(bindingAdapterPosition)?.save()
            notifyItemChanged(bindingAdapterPosition)
        }

        fun bind(notification: MifosNotification?) {
            with(binding) {
                tvNotificationText.text = notification?.msg
                tvNotificationTime.text = getDateAndTimeAsStringFromLong(notification?.timeStamp)
                if (notification?.isRead() == true) {
                    ivNotificationIcon.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.gray_dark,
                        ),
                    )
                    btnDismissNotification.visibility = View.GONE
                } else {
                    ivNotificationIcon.setColorFilter(context.getThemeAttributeColor(R.attr.colorPrimary))
                    btnDismissNotification.visibility = View.VISIBLE
                }

                btnDismissNotification.setOnClickListener {
                    dismissNotification()
                }
            }
        }
    }

    init {
        notificationList = ArrayList()
    }
}
