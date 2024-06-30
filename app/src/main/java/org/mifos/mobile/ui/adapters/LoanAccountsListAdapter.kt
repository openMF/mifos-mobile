package org.mifos.mobile.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.mifos.mobile.R
import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount
import org.mifos.mobile.databinding.RowLoanAccountBinding
import org.mifos.mobile.core.common.utils.CurrencyUtil.formatCurrency
import org.mifos.mobile.core.common.utils.DateHelper.getDateAsString

/**
 * @author Vishwajeet
 * @since 22/6/16.
 */
class LoanAccountsListAdapter(
    val onItemClick: (itemPosition: Int) -> Unit,
) : RecyclerView.Adapter<LoanAccountsListAdapter.ViewHolder>() {

    private var loanAccountsList: List<LoanAccount?>? = ArrayList()

    fun setLoanAccountsList(loanAccountsList: List<LoanAccount?>?) {
        this.loanAccountsList = loanAccountsList
        notifyDataSetChanged()
    }

    fun getLoanAccountsList(): List<LoanAccount?>? {
        return loanAccountsList
    }

    fun getItem(position: Int): LoanAccount? {
        return loanAccountsList?.get(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RowLoanAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val loanAccount = getItem(position)
        holder.bind(loanAccount)
    }

    override fun getItemCount(): Int {
        return if (loanAccountsList != null) {
            loanAccountsList!!.size
        } else {
            0
        }
    }

    inner class ViewHolder(private val binding: RowLoanAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick(bindingAdapterPosition)
            }
        }

        fun bind(loanAccount: LoanAccount?) {
            with(binding) {
                tvClientLoanAccountNumber.text = loanAccount?.accountNo
                tvLoanAccountProductName.text = loanAccount?.productName
                tvAccountBalance.visibility = View.GONE
                val context = itemView.context
                if (loanAccount?.status?.active == true && loanAccount.inArrears == true) {
                    setLoanAccountsGeneralDetails(
                        R.color.red,
                        context.getString(
                            R.string.string_and_string,
                            context.getString(R.string.disbursement),
                            getDateAsString(loanAccount.timeline?.actualDisbursementDate),
                        ),
                    )
                    setLoanAccountsDetails(loanAccount, R.color.red)
                } else if (loanAccount?.status?.active == true) {
                    setLoanAccountsGeneralDetails(
                        R.color.deposit_green,
                        context.getString(
                            R.string.string_and_string,
                            context.getString(R.string.disbursement),
                            getDateAsString(loanAccount.timeline?.actualDisbursementDate),
                        ),
                    )
                    setLoanAccountsDetails(loanAccount, R.color.deposit_green)
                } else if (loanAccount?.status?.waitingForDisbursal == true) {
                    setLoanAccountsGeneralDetails(
                        R.color.blue,
                        context.getString(
                            R.string.string_and_string,
                            context.getString(R.string.approved),
                            getDateAsString(loanAccount.timeline?.approvedOnDate),
                        ),
                    )
                } else if (loanAccount?.status?.pendingApproval == true) {
                    setLoanAccountsGeneralDetails(
                        R.color.light_yellow,
                        context.getString(
                            R.string.string_and_string,
                            context.getString(R.string.submitted),
                            getDateAsString(loanAccount.timeline?.submittedOnDate),
                        ),
                    )
                } else if (loanAccount?.status?.overpaid == true) {
                    setLoanAccountsDetails(loanAccount, R.color.purple)
                    setLoanAccountsGeneralDetails(
                        R.color.purple,
                        context.getString(
                            R.string.string_and_string,
                            context.getString(R.string.disbursement),
                            getDateAsString(loanAccount.timeline?.actualDisbursementDate),
                        ),
                    )
                } else if (loanAccount?.status?.closed == true) {
                    setLoanAccountsGeneralDetails(
                        R.color.black,
                        context.getString(
                            R.string.string_and_string,
                            context.getString(R.string.closed),
                            getDateAsString(loanAccount.timeline?.closedOnDate),
                        ),
                    )
                } else {
                    setLoanAccountsGeneralDetails(
                        R.color.gray_dark,
                        context.getString(
                            R.string.string_and_string,
                            context.getString(R.string.withdrawn),
                            getDateAsString(loanAccount?.timeline?.withdrawnOnDate),
                        ),
                    )
                }
            }
        }

        private fun setLoanAccountsDetails(loanAccount: LoanAccount, color: Int) {
            with(binding) {
                val amountBalance: Double =
                    if (loanAccount.loanBalance != 0.0) loanAccount.loanBalance else 0.0
                tvAccountBalance.visibility = View.VISIBLE
                tvAccountBalance.text = formatCurrency(itemView.context, amountBalance)
                tvAccountBalance.setTextColor(ContextCompat.getColor(itemView.context, color))
            }
        }

        private fun setLoanAccountsGeneralDetails(colorId: Int, dateStr: String) {
            with(binding) {
                ivStatusIndicator.setColorFilter(ContextCompat.getColor(itemView.context, colorId))
                tvDate.text = dateStr
            }
        }
    }
}
