package org.mifos.mobile.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.mifos.mobile.R
import org.mifos.mobile.databinding.RowClientChargeBinding
import org.mifos.mobile.models.Charge
import org.mifos.mobile.ui.getThemeAttributeColor
import org.mifos.mobile.utils.CurrencyUtil.formatCurrency
import org.mifos.mobile.utils.DateHelper.getDateAsString

/**
 * @author Vishwajeet
 * @since 17/8/16.
 */
class ClientChargeAdapter(
    val onItemClick: (itemPosition: Int) -> Unit,
) : RecyclerView.Adapter<ClientChargeAdapter.ViewHolder>() {

    private var clientChargeList: List<Charge?>? = ArrayList()
    fun setClientChargeList(clientChargeList: List<Charge?>?) {
        this.clientChargeList = clientChargeList
    }

    fun getItem(position: Int): Charge? {
        return clientChargeList?.get(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RowClientChargeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val vh = ViewHolder(binding)
        binding.root.setOnClickListener {
            onItemClick(vh.bindingAdapterPosition)
        }
        return vh
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val charge = getItem(position)
        var currencyRepresentation = charge?.currency?.displaySymbol ?: charge?.currency?.code
        if (currencyRepresentation == null) {
            currencyRepresentation = charge?.currency?.code
        }
        val context = holder.itemView.context
        holder.bind(charge, currencyRepresentation, context)
    }

    override fun getItemCount(): Int {
        return if (clientChargeList != null) {
            clientChargeList!!.size
        } else {
            0
        }
    }

    class ViewHolder(private val binding: RowClientChargeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Binds the values for each of the stored variables to the view
        // Also changes the color of the circle depending on whether the charge is active or not
        fun bind(charge: Charge?, currencyRepresentation: String?, context: Context?) {
            with(binding) {
                tvAmount.text = context?.getString(
                    R.string.string_and_string,
                    currencyRepresentation,
                    formatCurrency(
                        context,
                        charge?.amount,
                    ),
                )
                tvAmountPaid.text = context?.getString(
                    R.string.string_and_string,
                    currencyRepresentation,
                    formatCurrency(
                        context,
                        charge?.amountPaid,
                    ),
                )
                tvAmountWaived.text = context?.getString(
                    R.string.string_and_string,
                    currencyRepresentation,
                    formatCurrency(context, charge?.amountWaived),
                )
                tvAmountOutstanding.text = context?.getString(
                    R.string.string_and_string,
                    currencyRepresentation,
                    formatCurrency(context, charge?.amountOutstanding),
                )
                tvClientName.text = charge?.name
                if (charge?.dueDate?.isNotEmpty() == true) {
                    tvDueDate.text = getDateAsString(charge.dueDate)
                }
                if (charge?.isPaid == true || charge?.isWaived == true || charge?.paid == true || charge?.waived == true) {
                    circleStatus.setBackgroundColor(itemView.context.getThemeAttributeColor(R.attr.colorError))
                } else {
                    context?.getThemeAttributeColor(R.attr.colorSuccess)
                        ?.let { circleStatus.setBackgroundColor(it) }
                }
            }
        }
    }
}
