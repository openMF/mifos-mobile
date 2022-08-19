package org.mifos.mobile.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.card.MaterialCardView

import org.mifos.mobile.R
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.utils.DateHelper.getDateAsString

import java.util.*

/*
* Created by saksham on 24/July/2018
*/
class GuarantorListAdapter(
        var context: Context?,
        listener: OnClickListener
) : RecyclerView.Adapter<GuarantorListAdapter.ViewHolder>() {

    var list: MutableList<GuarantorPayload?>?
    var listener: OnClickListener

    interface OnClickListener {
        fun setOnClickListener(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        return ViewHolder(inflater.inflate(R.layout.row_guarantor, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvGuarantorName?.text = (list?.get(position)?.firstname + " "
                + list?.get(position)?.lastname)
        holder.tvJoinedDate?.text = getDateAsString(list?.get(position)
                ?.joinedDate)
        holder.cvContainer?.setOnClickListener { listener.setOnClickListener(position) }
    }

    override fun getItemCount(): Int {
        return if (list?.size != null) list?.size!!
        else 0
    }

    fun setGuarantorList(payload: MutableList<GuarantorPayload?>?) {
        list = payload
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        @JvmField
        @BindView(R.id.tv_guarantor_name)
        var tvGuarantorName: TextView? = null

        @JvmField
        @BindView(R.id.tv_joined_date)
        var tvJoinedDate: TextView? = null

        @JvmField
        @BindView(R.id.cv_container)
        var cvContainer: MaterialCardView? = null

        init {
            ButterKnife.bind(this, itemView!!)
        }
    }

    init {
        list = ArrayList()
        this.listener = listener
    }
}