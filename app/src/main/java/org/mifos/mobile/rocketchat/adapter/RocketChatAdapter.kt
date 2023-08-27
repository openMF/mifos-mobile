package org.mifos.mobile.rocketchat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_chat_support.view.tv_message_support
import kotlinx.android.synthetic.main.item_chat_user.view.tv_message_user
import org.mifos.mobile.R
import org.mifos.mobile.rocketchat.model.SupportChatMessage

class RocketChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val messages = mutableListOf<SupportChatMessage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SupportChatMessage.MessageType.USER.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_user, parent, false)

                UserMessageViewHolder(view)
            }

            SupportChatMessage.MessageType.SUPPORT.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_support, parent, false)
                SupportMessageViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is UserMessageViewHolder -> holder.bind(message)
            is SupportMessageViewHolder -> holder.bind(message)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return messages[position].messageType.ordinal
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun addNewMessage(supportChatMessage: SupportChatMessage) {
        messages.add(supportChatMessage)
        notifyDataSetChanged()
    }

    inner class UserMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: SupportChatMessage) {
            itemView.tv_message_user.tv_message_user.text = message.text
        }
    }

    inner class SupportMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: SupportChatMessage) {
            itemView.tv_message_support.text = message.text
        }
    }


}

class MessageDiffCallback : DiffUtil.ItemCallback<SupportChatMessage>() {
    override fun areItemsTheSame(
        oldItem: SupportChatMessage,
        newItem: SupportChatMessage
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: SupportChatMessage,
        newItem: SupportChatMessage
    ): Boolean {
        return oldItem == newItem
    }
}