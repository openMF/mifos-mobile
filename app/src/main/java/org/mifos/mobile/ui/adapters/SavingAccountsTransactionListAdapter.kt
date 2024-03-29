package org.mifos.mobile.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.mifos.mobile.R
import org.mifos.mobile.databinding.RowSavingAccountTransactionBinding
import org.mifos.mobile.models.accounts.savings.TransactionType
import org.mifos.mobile.models.accounts.savings.Transactions
import org.mifos.mobile.utils.CurrencyUtil.formatCurrency
import org.mifos.mobile.utils.DateHelper.getDateAsString
import javax.inject.Inject

/**
 * Created by dilpreet on 6/3/17.
 */
class SavingAccountsTransactionListAdapter @Inject constructor() :
    RecyclerView.Adapter<SavingAccountsTransactionListAdapter.ViewHolder>() {

    private var savingAccountsTransactionList: List<Transactions?>?
    private var context: Context? = null

    init {
        savingAccountsTransactionList = ArrayList()
    }

    fun setContext(context: Context?) {
        this.context = context
    }

    fun getItem(position: Int?): Transactions? {
        return position?.let { savingAccountsTransactionList?.get(it) }
    }

    fun setSavingAccountsTransactionList(savingAccountsTransactionList: List<Transactions?>?) {
        if (savingAccountsTransactionList != null) {
            this.savingAccountsTransactionList =
                savingAccountsTransactionList
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowSavingAccountTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = getItem(position)
        holder.bind(transaction)
    }

    private enum class ColorSelect {
        RED, GREEN
    }

    override fun getItemCount(): Int {
        return savingAccountsTransactionList?.size ?: 0
    }

    private fun getColor(transactionType: TransactionType?): ColorSelect {
        if (transactionType?.deposit == true) {
            return ColorSelect.GREEN
        }
        if (transactionType?.dividendPayout == true) {
            return ColorSelect.RED
        }
        if (transactionType?.withdrawal == true) {
            return ColorSelect.RED
        }
        if (transactionType?.interestPosting == true) {
            return ColorSelect.GREEN
        }
        if (transactionType?.feeDeduction == true) {
            return ColorSelect.RED
        }
        if (transactionType?.initiateTransfer == true) {
            return ColorSelect.RED
        }
        if (transactionType?.approveTransfer == true) {
            return ColorSelect.RED
        }
        if (transactionType?.withdrawTransfer == true) {
            return ColorSelect.RED
        }
        if (transactionType?.rejectTransfer == true) {
            return ColorSelect.GREEN
        }
        return if (transactionType?.overdraftFee == true) {
            ColorSelect.RED
        } else {
            ColorSelect.GREEN
        }
    }

    inner class ViewHolder(private val binding: RowSavingAccountTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transactions?) {
            with(binding) {
                val (_, transactionType, _, _, date, currency, paymentDetailData, amount, runningBalance) = transaction!!
                val displayedSymbol = currency?.displaySymbol ?: currency?.code
                tvSavingAccountAmount.text = context?.getString(
                    R.string.string_and_string,
                    displayedSymbol,
                    formatCurrency(context, amount!!),
                )
                tvSavingAccountRunningBalance.text = context?.getString(
                    R.string.string_and_string,
                    displayedSymbol,
                    formatCurrency(context, runningBalance!!),
                )
                tvTransactionType.text = transactionType?.value

                if (paymentDetailData != null) {
                    tvTransactionDetailData.visibility = View.VISIBLE
                    tvTransactionDetailData.text = paymentDetailData.paymentType.name
                }

                tvTransactionDate.text = getDateAsString(date)
                val color = getColor(transactionType)

                if (color == ColorSelect.RED) {
                    vIndicator.background =
                        ContextCompat.getDrawable(itemView.context, R.drawable.triangular_red_view)
                } else {
                    vIndicator.background = ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.triangular_green_view,
                    )
                }
            }
        }
    }
}
