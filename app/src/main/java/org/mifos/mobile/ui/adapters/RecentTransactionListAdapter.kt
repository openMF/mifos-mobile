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
import org.mifos.mobile.injection.ActivityContext
import org.mifos.mobile.models.Transaction
import org.mifos.mobile.utils.CurrencyUtil.formatCurrency
import org.mifos.mobile.utils.DateHelper.getDateAsString
import org.mifos.mobile.utils.Utils.formatTransactionType

import java.util.*
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
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_recent_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (_, _, _, type, _, currency, amount, submittedOnDate) = getItem(position)
        var currencyRepresentation = currency.displaySymbol
        if (currencyRepresentation == null) {
            currencyRepresentation = currency.code
        }
        holder.tvAmount!!.text = context.getString(R.string.string_and_string,
                currencyRepresentation, formatCurrency(context,
                amount!!))
        holder.tvTypeValue!!.text = formatTransactionType(type.value)
        holder.tvTransactionsDate!!.text = getDateAsString(submittedOnDate)
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

    class ViewHolder(v: View?) : RecyclerView.ViewHolder(v!!) {
        @JvmField
        @BindView(R.id.tv_transactionDate)
        var tvTransactionsDate: TextView? = null

        @JvmField
        @BindView(R.id.tv_amount)
        var tvAmount: TextView? = null

        @JvmField
        @BindView(R.id.tv_value)
        var tvTypeValue: TextView? = null

        init {
            ButterKnife.bind(this, v!!)
        }
    }

    init {
        transactions = ArrayList()
        this.context = context
    }
}