package org.mifos.mobile.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import butterknife.BindView
import butterknife.ButterKnife

import org.mifos.mobile.R
import org.mifos.mobile.injection.ActivityContext
import org.mifos.mobile.models.accounts.loan.LoanAccount
import org.mifos.mobile.utils.CurrencyUtil.formatCurrency
import org.mifos.mobile.utils.DateHelper.getDateAsString

import java.util.*
import javax.inject.Inject

/**
 * @author Vishwajeet
 * @since 22/6/16.
 */
class LoanAccountsListAdapter @Inject constructor(@param:ActivityContext private val context: Context) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
                R.layout.row_loan_account, parent, false)
        vh = ViewHolder(v)
        return vh
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val loanAccount = getItem(position)
            holder.tvClientLoanAccountNumber!!.text = loanAccount?.accountNo
            holder.tvLoanAccountProductName!!.text = loanAccount?.productName
            holder.tvAccountBalance!!.visibility = View.GONE
            if (loanAccount?.status!!.active!! && loanAccount.inArrears!!) {
                setLoanAccountsGeneralDetails(holder, R.color.red, context.getString(R.string.string_and_string, context.getString(R.string.disbursement), getDateAsString(loanAccount.timeline.actualDisbursementDate)))
                setLoanAccountsDetails(holder, loanAccount, R.color.red)
            } else if (loanAccount.status!!.active!!) {
                setLoanAccountsGeneralDetails(holder, R.color.deposit_green, context.getString(R.string.string_and_string, context.getString(R.string.disbursement),
                        getDateAsString(loanAccount.timeline.actualDisbursementDate)))
                setLoanAccountsDetails(holder, loanAccount, R.color.deposit_green)
            } else if (loanAccount.status!!.waitingForDisbursal!!) {
                setLoanAccountsGeneralDetails(holder, R.color.blue, context.getString(R.string.string_and_string, context.getString(R.string.approved), getDateAsString(loanAccount.timeline.approvedOnDate)))
            } else if (loanAccount.status!!.pendingApproval!!) {
                setLoanAccountsGeneralDetails(holder, R.color.light_yellow, context.getString(R.string.string_and_string, context.getString(R.string.submitted), getDateAsString(loanAccount.timeline.submittedOnDate)))
            } else if (loanAccount.status!!.overpaid!!) {
                setLoanAccountsDetails(holder, loanAccount, R.color.purple)
                setLoanAccountsGeneralDetails(holder, R.color.purple, context.getString(R.string.string_and_string, context.getString(R.string.disbursement), getDateAsString(loanAccount.timeline.actualDisbursementDate)))
            } else if (loanAccount.status!!.closed!!) {
                setLoanAccountsGeneralDetails(holder, R.color.black, context.getString(R.string.string_and_string, context.getString(R.string.closed), getDateAsString(loanAccount.timeline.closedOnDate)))
            } else {
                setLoanAccountsGeneralDetails(holder, R.color.gray_dark, context.getString(R.string.string_and_string, context.getString(R.string.withdrawn), getDateAsString(loanAccount.timeline.withdrawnOnDate)))
            }
        }
    }

    private fun setLoanAccountsDetails(viewHolder: ViewHolder, loanAccount: LoanAccount, color: Int) {
        val amountBalance: Double = if (loanAccount.loanBalance != 0.0) loanAccount.loanBalance else 0.0
        viewHolder.tvAccountBalance!!.visibility = View.VISIBLE
        viewHolder.tvAccountBalance!!.text = formatCurrency(context, amountBalance)
        viewHolder.tvAccountBalance!!.setTextColor(ContextCompat.getColor(context, color))
    }

    private fun setLoanAccountsGeneralDetails(
            holder: RecyclerView.ViewHolder, colorId: Int,
            dateStr: String
    ) {
        (holder as ViewHolder).ivStatusIndicator!!.setColorFilter(ContextCompat.getColor(context, colorId))
        holder.tvDate!!.text = dateStr
    }

    override fun getItemCount(): Int {
        return loanAccountsList!!.size
    }

    class ViewHolder(v: View?) : RecyclerView.ViewHolder(v!!) {
        @JvmField
        @BindView(R.id.tv_clientLoanAccountNumber)
        var tvClientLoanAccountNumber: TextView? = null

        @JvmField
        @BindView(R.id.tv_loanAccountProductName)
        var tvLoanAccountProductName: TextView? = null

        @JvmField
        @BindView(R.id.iv_status_indicator)
        var ivStatusIndicator: AppCompatImageView? = null

        @JvmField
        @BindView(R.id.tv_date)
        var tvDate: TextView? = null

        @JvmField
        @BindView(R.id.tv_account_balance)
        var tvAccountBalance: TextView? = null

        init {
            ButterKnife.bind(this, v!!)
        }
    }
}