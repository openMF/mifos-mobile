package org.mifos.mobile.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.mifos.mobile.R
import org.mifos.mobile.databinding.RowSavingAccountBinding
import org.mifos.mobile.models.accounts.savings.SavingAccount
import org.mifos.mobile.utils.CurrencyUtil.formatCurrency
import org.mifos.mobile.utils.DateHelper.getDateAsString

/**
 * @author Vishwajeet
 * @since 22/6/16.
 */
class SavingAccountsListAdapter(
    private val onItemClick: (itemPosition: Int) -> Unit,
) : RecyclerView.Adapter<SavingAccountsListAdapter.ViewHolder>() {

    private var savingAccountsList: List<SavingAccount?>? = ArrayList()

    fun setSavingAccountsList(savingAccountsList: List<SavingAccount?>?) {
        this.savingAccountsList = savingAccountsList
        notifyDataSetChanged()
    }

    fun getSavingAccountsList(): List<SavingAccount?>? {
        return savingAccountsList
    }

    fun getItem(position: Int): SavingAccount? {
        return savingAccountsList?.get(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RowSavingAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val savingAccount = getItem(position)
        holder.bind(savingAccount)
    }

    override fun getItemCount(): Int {
        return savingAccountsList?.size ?: 0
    }

    inner class ViewHolder(private val binding: RowSavingAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick(bindingAdapterPosition)
            }
        }

        fun bind(savingAccount: SavingAccount?) {
            with(binding) {
                tvClientSavingAccountNumber.text = savingAccount?.accountNo
                tvSavingAccountProductName.text = savingAccount?.productName
                tvAccountBalance.visibility = View.GONE

                val context = itemView.context
                when {
                    savingAccount?.status?.active == true -> {
                        setSavingAccountsDetails(savingAccount, R.color.deposit_green)
                        setSavingAccountsGeneralDetails(
                            R.color.deposit_green,
                            getDateAsString(savingAccount.lastActiveTransactionDate),
                        )
                    }

                    savingAccount?.status?.approved == true -> {
                        setSavingAccountsGeneralDetails(
                            R.color.light_green,
                            context.getString(
                                R.string.string_and_string,
                                context.getString(R.string.approved),
                                getDateAsString(savingAccount.timeLine?.approvedOnDate),
                            ),
                        )
                    }

                    savingAccount?.status?.submittedAndPendingApproval == true -> {
                        setSavingAccountsGeneralDetails(
                            R.color.light_yellow,
                            context.getString(
                                R.string.string_and_string,
                                context.getString(R.string.submitted),
                                getDateAsString(savingAccount.timeLine?.submittedOnDate),
                            ),
                        )
                    }

                    savingAccount?.status?.matured == true -> {
                        setSavingAccountsDetails(savingAccount, R.color.red_light)
                        setSavingAccountsGeneralDetails(
                            R.color.red_light,
                            getDateAsString(savingAccount.lastActiveTransactionDate),
                        )
                    }

                    else -> {
                        setSavingAccountsGeneralDetails(
                            R.color.black,
                            context.getString(
                                R.string.string_and_string,
                                context.getString(R.string.closed),
                                getDateAsString(savingAccount?.timeLine?.closedOnDate),
                            ),
                        )
                    }
                }
            }
        }

        private fun setSavingAccountsDetails(savingAccount: SavingAccount, colorId: Int) {
            with(binding) {
                tvAccountBalance.visibility = View.VISIBLE
                tvAccountBalance.setTextColor(ContextCompat.getColor(itemView.context, colorId))
                tvAccountBalance.text = itemView.context.getString(
                    R.string.string_and_string,
                    savingAccount.currency?.displaySymbol ?: savingAccount.currency?.code,
                    formatCurrency(itemView.context, savingAccount.accountBalance),
                )
            }
        }

        private fun setSavingAccountsGeneralDetails(colorId: Int, dateStr: String) {
            with(binding) {
                ivStatusIndicator.setColorFilter(ContextCompat.getColor(itemView.context, colorId))
                tvLastActive.text = dateStr
            }
        }
    }
}
