package org.mifos.mobile.ui.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnCheckedChanged
import butterknife.OnClick
import org.mifos.mobile.R
import org.mifos.mobile.models.CheckboxStatus
import javax.inject.Inject

/**
 * Created by dilpreet on 3/7/17.
 */
class CheckBoxAdapter @Inject constructor() :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var statusList: List<CheckboxStatus?>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
                R.layout.row_checkbox, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val (status, color, isChecked) = statusList?.get(position)!!
        val states = arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf())
        val colors = intArrayOf(color, color)
        (holder as ViewHolder).cbStatusSelect?.isChecked = isChecked
        holder.cbStatusSelect?.supportButtonTintList = ColorStateList(states,colors)
        holder.cbStatusSelect?.text = status
    }

    override fun getItemCount(): Int {
        return if (statusList != null) statusList!!.size
        else 0
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        @JvmField
        @BindView(R.id.cb_status_select)
        var cbStatusSelect: AppCompatCheckBox? = null


        @OnClick(R.id.cb_status_select)
        fun rowClicked() {
            cbStatusSelect?.isChecked = (cbStatusSelect?.isChecked == false)
        }

        @OnCheckedChanged(R.id.cb_status_select)
        fun checkChanges() {
            statusList!![adapterPosition]?.isChecked = (cbStatusSelect?.isChecked == true)
        }

        init {
            ButterKnife.bind(this, itemView!!)
        }
    }
}