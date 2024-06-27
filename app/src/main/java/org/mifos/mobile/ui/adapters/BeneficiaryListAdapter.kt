package org.mifos.mobile.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.databinding.RowBeneficiaryBinding

/**
 * Created by dilpreet on 15/6/17.
 */
class BeneficiaryListAdapter constructor(
    val onItemClick: (itemPosition: Int) -> Unit,
) :
    RecyclerView.Adapter<BeneficiaryListAdapter.ViewHolder>() {

    private var beneficiaryList: List<Beneficiary?>? = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowBeneficiaryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val beneficiaryItem = beneficiaryList?.get(position)
        holder.bind(beneficiaryItem)
    }

    override fun getItemCount(): Int {
        return beneficiaryList?.size ?: 0
    }

    fun setBeneficiaryList(beneficiaryList: List<Beneficiary?>?) {
        this.beneficiaryList = beneficiaryList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: RowBeneficiaryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick(bindingAdapterPosition)
            }
        }

        fun bind(beneficiary: Beneficiary?) {
            with(binding) {
                tvAccountNumber.text = beneficiary?.accountNumber
                tvBeneficiaryName.text = beneficiary?.name
                tvOfficeName.text = beneficiary?.officeName
            }
        }
    }
}
