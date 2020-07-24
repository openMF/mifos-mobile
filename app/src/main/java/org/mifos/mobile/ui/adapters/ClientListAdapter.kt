package org.mifos.mobile.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import org.mifos.mobile.R
import org.mifos.mobile.models.client.Client

/**
 * @author Vishwajeet
 * @since 20/06/16
 */
class ClientListAdapter(context: Context, private var listItems: List<Client>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val mContext: Context = context
    fun getItem(position: Int): Client {
        return listItems[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
                R.layout.row_client_name, parent, false)
        vh = ViewHolder(v)
        return vh
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecyclerView.ViewHolder) {
            val client = getItem(position)
            (holder as ViewHolder).tv_clientName?.text = client.firstname + " " + client
                    .lastname
            holder.tv_clientAccountNumber?.text = client.accountNo
        }
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    class ViewHolder(v: View?) : RecyclerView.ViewHolder(v!!) {
        @JvmField
        @BindView(R.id.tv_clientName)
        var tv_clientName: TextView? = null

        @JvmField
        @BindView(R.id.tv_clientAccountNumber)
        var tv_clientAccountNumber: TextView? = null

        init {
            ButterKnife.bind(this, v!!)
        }
    }

}