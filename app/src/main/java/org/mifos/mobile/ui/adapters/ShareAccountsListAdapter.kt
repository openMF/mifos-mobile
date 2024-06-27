package org.mifos.mobile.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.mifos.mobile.R
import org.mifos.mobile.core.model.entity.accounts.share.ShareAccount
import org.mifos.mobile.databinding.RowShareAccountBinding

class ShareAccountsListAdapter(
    private val onItemClick: (itemPosition: Int) -> Unit,
) : RecyclerView.Adapter<ShareAccountsListAdapter.ViewHolder>() {

    private var shareAccountsList: List<ShareAccount?>? = ArrayList()
    fun setShareAccountsList(shareAccountsList: List<ShareAccount?>?) {
        if (shareAccountsList != null) {
            this.shareAccountsList = shareAccountsList
        }
        notifyDataSetChanged()
    }

    fun getItem(position: Int): ShareAccount? {
        return shareAccountsList?.get(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RowShareAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shareAccount = getItem(position)
        holder.bind(shareAccount)
    }

    override fun getItemCount(): Int {
        if (shareAccountsList != null) {
            return shareAccountsList!!.size
        }
        return 0
    }

    inner class ViewHolder(private val binding: RowShareAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick(bindingAdapterPosition)
            }
        }

        fun bind(shareAccount: ShareAccount?) {
            with(binding) {
                tvClientSharingAccountNumber.text = shareAccount?.accountNo
                tvShareAccountProductName.text = shareAccount?.productName
                llAccountDetail.visibility = View.GONE
                val context = itemView.context
                when {
                    shareAccount?.status?.active == true -> {
                        ivStatusIndicator.setColorFilter(
                            ContextCompat.getColor(
                                context,
                                R.color.deposit_green,
                            ),
                        )
                        setSharingAccountDetail(shareAccount)
                    }

                    shareAccount?.status?.approved == true -> {
                        ivStatusIndicator.setColorFilter(
                            ContextCompat.getColor(
                                context,
                                R.color.light_green,
                            ),
                        )
                    }

                    shareAccount?.status?.submittedAndPendingApproval == true -> {
                        ivStatusIndicator.setColorFilter(
                            ContextCompat.getColor(
                                context,
                                R.color.light_yellow,
                            ),
                        )
                    }

                    else -> {
                        ivStatusIndicator.setColorFilter(
                            ContextCompat.getColor(
                                context,
                                R.color.light_blue,
                            ),
                        )
                    }
                }
            }
        }

        private fun setSharingAccountDetail(shareAccount: ShareAccount) {
            with(binding) {
                llAccountDetail.visibility = View.VISIBLE
                tvSharesPending.text = shareAccount
                    .totalPendingForApprovalShares.toString()
                tvSharesApproved.text = shareAccount
                    .totalApprovedShares.toString()
            }
        }
    }
}
