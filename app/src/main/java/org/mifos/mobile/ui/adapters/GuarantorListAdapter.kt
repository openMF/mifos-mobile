package org.mifos.mobile.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.mifos.mobile.core.model.entity.guarantor.GuarantorPayload
import org.mifos.mobile.databinding.RowGuarantorBinding
import org.mifos.mobile.utils.DateHelper.getDateAsString

/*
* Created by saksham on 24/July/2018
*/
class GuarantorListAdapter(
    var context: Context?,
    listener: OnClickListener,
) : RecyclerView.Adapter<GuarantorListAdapter.ViewHolder>() {

    var list: MutableList<GuarantorPayload?>?
    var listener: OnClickListener

    interface OnClickListener {
        fun setOnClickListener(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RowGuarantorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val guarantorJoinedDate = getDateAsString(list?.get(position)?.joinedDate)
        val guarantorFullName = list?.get(position)?.firstname + " " + list?.get(position)?.lastname
        holder.bind(guarantorJoinedDate, guarantorFullName)
    }

    override fun getItemCount(): Int {
        return if (list?.size != null) {
            list?.size!!
        } else {
            0
        }
    }

    fun setGuarantorList(payload: MutableList<GuarantorPayload?>?) {
        list = payload
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: RowGuarantorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(joinedDate: String, fullName: String) {
            with(binding) {
                tvGuarantorName.text = fullName
                tvJoinedDate.text = joinedDate

                cvContainer.setOnClickListener {
                    listener.setOnClickListener(bindingAdapterPosition)
                }
            }
        }
    }

    init {
        list = ArrayList()
        this.listener = listener
    }
}
