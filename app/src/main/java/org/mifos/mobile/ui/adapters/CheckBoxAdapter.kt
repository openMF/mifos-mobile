package org.mifos.mobile.ui.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.CompoundButtonCompat
import androidx.recyclerview.widget.RecyclerView
import org.mifos.mobile.core.model.entity.CheckboxStatus
import org.mifos.mobile.databinding.RowCheckboxBinding
import javax.inject.Inject

/**
 * Created by dilpreet on 3/7/17.
 */
class CheckBoxAdapter @Inject constructor() :
    RecyclerView.Adapter<CheckBoxAdapter.ViewHolder>() {

    var statusList: List<CheckboxStatus?>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowCheckboxBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (status, color, isChecked) = statusList?.get(position) ?: return
        val states = arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf())
        val colors = intArrayOf(color, color)

        holder.bind(status, isChecked, states, colors)
    }

    override fun getItemCount(): Int {
        return statusList?.size ?: 0
    }

    inner class ViewHolder(private val binding: RowCheckboxBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(status: String?, isChecked: Boolean, states: Array<IntArray>, colors: IntArray) {
            with(binding) {
                cbStatusSelect.isChecked = isChecked
                cbStatusSelect.setOnClickListener {
                    statusList?.get(bindingAdapterPosition)?.isChecked = !isChecked
                }
                CompoundButtonCompat.setButtonTintList(
                    cbStatusSelect,
                    ColorStateList(states, colors),
                )
                cbStatusSelect.text = status
                cbStatusSelect.setOnCheckedChangeListener { _, _ ->
                    statusList?.get(bindingAdapterPosition)?.isChecked = cbStatusSelect.isChecked
                }
            }
        }
    }
}
