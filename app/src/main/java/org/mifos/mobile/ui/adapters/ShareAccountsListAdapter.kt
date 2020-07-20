package org.mifos.mobile.ui.adapters

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import butterknife.BindView
import butterknife.ButterKnife

import org.mifos.mobile.R
import org.mifos.mobile.injection.ActivityContext
import org.mifos.mobile.models.accounts.share.ShareAccount

import java.util.*
import javax.inject.Inject

class ShareAccountsListAdapter @Inject constructor(@param:ActivityContext private val context: Context) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var shareAccountsList: List<ShareAccount?>? = ArrayList()
    fun setShareAccountsList(shareAccountsList: List<ShareAccount?>?) {
        if (shareAccountsList != null)
            this.shareAccountsList = shareAccountsList
        notifyDataSetChanged()
    }

    fun getItem(position: Int): ShareAccount? {
        return shareAccountsList?.get(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
                R.layout.row_share_account, parent, false)
        vh = ViewHolder(v)
        return vh
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val shareAccount = getItem(position)
            holder.tvClientShareAccountsNumber?.text = shareAccount?.accountNo
            holder.tvShareAccountsProductName?.text = shareAccount?.productName
            holder.llAccountDetail?.visibility = View.GONE
            when {
                shareAccount?.status?.active == true -> {
                    holder.ivStatusIndicator?.setColorFilter(ContextCompat.getColor(context, R.color.deposit_green))
                    setSharingAccountDetail(holder, shareAccount)
                }
                shareAccount?.status?.approved == true -> {
                    holder.ivStatusIndicator?.setColorFilter(ContextCompat.getColor(context, R.color.light_green))
                }
                shareAccount?.status?.submittedAndPendingApproval == true -> {
                    holder.ivStatusIndicator?.setColorFilter(ContextCompat.getColor(context, R.color.light_yellow))
                }
                else -> {
                    holder.ivStatusIndicator?.setColorFilter(ContextCompat.getColor(context, R.color.light_blue))
                }
            }
        }
    }

    private fun setSharingAccountDetail(viewHolder: ViewHolder, shareAccount: ShareAccount) {
        viewHolder.llAccountDetail?.visibility = View.VISIBLE
        viewHolder.tvSharesPending?.text = shareAccount
                .totalPendingForApprovalShares.toString()
        viewHolder.tvSharesApproved?.text = shareAccount
                .totalApprovedShares.toString()
    }

    override fun getItemCount(): Int {
        if (shareAccountsList != null)
            return shareAccountsList!!.size
        return 0
    }

    private fun setCircularBackground(colorId: Int): LayerDrawable {
        val color: Drawable = ColorDrawable(ContextCompat.getColor(context, colorId))
        val image = ContextCompat.getDrawable(context, R.drawable.circular_background)
        return LayerDrawable(arrayOf(image, color))
    }

    class ViewHolder(v: View?) : RecyclerView.ViewHolder(v!!) {
        @JvmField
        @BindView(R.id.tv_clientSharingAccountNumber)
        var tvClientShareAccountsNumber: TextView? = null

        @JvmField
        @BindView(R.id.tv_shareAccountProductName)
        var tvShareAccountsProductName: TextView? = null

        @JvmField
        @BindView(R.id.iv_status_indicator)
        var ivStatusIndicator: AppCompatImageView? = null

        @JvmField
        @BindView(R.id.ll_account_detail)
        var llAccountDetail: LinearLayout? = null

        @JvmField
        @BindView(R.id.tv_shares_pending)
        var tvSharesPending: TextView? = null

        @JvmField
        @BindView(R.id.tv_shares_approved)
        var tvSharesApproved: TextView? = null

        init {
            ButterKnife.bind(this, v!!)
        }
    }
}