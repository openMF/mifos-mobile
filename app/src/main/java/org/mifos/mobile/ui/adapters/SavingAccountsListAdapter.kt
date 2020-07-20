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
import org.mifos.mobile.models.accounts.savings.SavingAccount
import org.mifos.mobile.utils.CurrencyUtil.formatCurrency
import org.mifos.mobile.utils.DateHelper.getDateAsString
import java.util.*
import javax.inject.Inject

/**
 * @author Vishwajeet
 * @since 22/6/16.
 */
class SavingAccountsListAdapter @Inject constructor(@param:ActivityContext private val context: Context) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
                R.layout.row_saving_account, parent, false)
        vh = ViewHolder(v)
        return vh
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val savingAccount = getItem(position)
            holder.tvClientSavingAccountNumber?.text = savingAccount?.accountNo
            holder.tvSavingAccountProductName?.text = savingAccount?.productName
            holder.tvAccountBalance?.visibility = View.GONE
            if (savingAccount?.status?.active!!) {
                setSavingAccountsDetails(holder, savingAccount,
                        R.color.deposit_green)
                setSavingAccountsGeneralDetails(holder, R.color.deposit_green, getDateAsString(savingAccount.lastActiveTransactionDate))
            } else if (savingAccount.status!!.approved!!) {
                setSavingAccountsGeneralDetails(holder, R.color.light_green, context.getString(R.string.string_and_string, context.getString(R.string.approved), getDateAsString(savingAccount.timeLine!!.approvedOnDate)))
            } else if (savingAccount.status!!.submittedAndPendingApproval!!) {
                setSavingAccountsGeneralDetails(holder, R.color.light_yellow, context.getString(R.string.string_and_string, context.getString(R.string.submitted), getDateAsString(savingAccount.timeLine!!.submittedOnDate)))
            } else if (savingAccount.status!!.matured!!) {
                setSavingAccountsDetails(holder, savingAccount, R.color.red_light)
                setSavingAccountsGeneralDetails(holder, R.color.red_light, getDateAsString(savingAccount.lastActiveTransactionDate))
            } else {
                setSavingAccountsGeneralDetails(holder, R.color.black, context.getString(R.string.string_and_string, context.getString(R.string.closed), getDateAsString(savingAccount.timeLine!!.closedOnDate)))
            }
        }
    }

    private fun setSavingAccountsDetails(
            viewHolder: ViewHolder, savingAccount: SavingAccount,
            colorId: Int
    ) {
        viewHolder.tvAccountBalance?.visibility = View.VISIBLE
        viewHolder.tvAccountBalance?.setTextColor(ContextCompat.getColor(context,
                colorId))
        viewHolder.tvAccountBalance?.text = context.getString(R.string.string_and_string,
                savingAccount.currency!!.displaySymbol, formatCurrency(context,
                savingAccount.accountBalance))
    }

    private fun setSavingAccountsGeneralDetails(
            holder: RecyclerView.ViewHolder, colorId: Int,
            dateStr: String
    ) {
        (holder as ViewHolder).ivStatusIndicator?.setColorFilter(ContextCompat.getColor(context, colorId))
        holder.tvLastActive?.text = dateStr
    }

    override fun getItemCount(): Int {
        return if (savingAccountsList != null)
            savingAccountsList!!.size
        else 0
    }

    class ViewHolder(v: View?) : RecyclerView.ViewHolder(v!!) {
        @JvmField
        @BindView(R.id.tv_clientSavingAccountNumber)
        var tvClientSavingAccountNumber: TextView? = null

        @JvmField
        @BindView(R.id.tv_savingAccountProductName)
        var tvSavingAccountProductName: TextView? = null

        @JvmField
        @BindView(R.id.iv_status_indicator)
        var ivStatusIndicator: AppCompatImageView? = null

        @JvmField
        @BindView(R.id.tv_last_active)
        var tvLastActive: TextView? = null

        @JvmField
        @BindView(R.id.tv_account_balance)
        var tvAccountBalance: TextView? = null

        init {
            ButterKnife.bind(this, v!!)
        }
    }
}