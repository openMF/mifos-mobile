package org.mifos.mobile.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.mifos.mobile.MifosSelfServiceApp.Companion.context
import org.mifos.mobile.R
import org.mifos.mobile.databinding.RowRecentTransactionBinding
import dagger.hilt.android.qualifiers.ActivityContext


import org.mifos.mobile.models.Transaction
import org.mifos.mobile.models.client.Type
import org.mifos.mobile.utils.CurrencyUtil.formatCurrency
import org.mifos.mobile.utils.DateHelper.getDateAsString
import org.mifos.mobile.utils.Utils.formatTransactionType
import javax.inject.Inject

/**
 * @author Vishwajeet
 * @since 10/08/16
 */
class RecentTransactionListAdapter @Inject constructor(@ActivityContext context: Context) :
    RecyclerView.Adapter<RecentTransactionListAdapter.ViewHolder>() {

    private var transactions: MutableList<Transaction?>
    private val context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RowRecentTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (_, _, _, type, _, currency, amount, submittedOnDate) = getItem(position)
        var currencyRepresentation = currency?.displaySymbol ?: currency?.code
        if (currencyRepresentation == null) {
            currencyRepresentation = currency?.code
        }

        holder.bind(currencyRepresentation, type, submittedOnDate, amount)
    }

    fun setTransactions(transactions: MutableList<Transaction?>?) {
        if (transactions != null) {
            this.transactions = transactions
        }
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Transaction {
        return transactions[position]!!
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    class ViewHolder(private val binding: RowRecentTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            currencyRepresentation: String?,
            type: Type,
            submittedOnDate: List<Int>,
            amount: Double?,
        ) {
            with(binding) {
                tvAmount.text = context?.getString(
                    R.string.string_and_string,
                    currencyRepresentation,
                    formatCurrency(
                        context,
                        amount!!,
                    ),
                )
                tvValue.text = formatTransactionType(type.value)
                tvTransactionDate.text = getDateAsString(submittedOnDate)
            }
        }
    }

    init {
        transactions = ArrayList()
        this.context = context
    }
}
