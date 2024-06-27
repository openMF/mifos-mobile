package org.mifos.mobile.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import org.mifos.mobile.R
import org.mifos.mobile.core.model.entity.payload.AccountDetail

/**
 * Created by dilpreet on 19/03/18.
 */
class AccountsSpinnerAdapter(
    context: Context,
    private val accountDetails: List<AccountDetail>,
) : ArrayAdapter<AccountDetail>(context, 0, accountDetails) {

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup,
    ): View {
        val view = convertView ?: LayoutInflater.from(parent.context)
            .inflate(R.layout.account_spinner_layout, parent, false)
        getItem(position)?.let {
            view.findViewById<TextView>(R.id.tv_account_number).text = it.accountNumber
            view.findViewById<TextView>(R.id.tv_account_type).text = it.accountType
        }

        return view
    }

    override fun getFilter() = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val suggestions =
                if (constraint.isBlank()) {
                    accountDetails
                } else {
                    accountDetails.filterIndexed { _, item ->
                        item.accountNumber!!.contains(
                            constraint.trim(),
                        )
                    }
                }

            return FilterResults().apply {
                values = suggestions
                count = suggestions.size
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            clear()
            addAll(results.values as List<AccountDetail>)
            notifyDataSetChanged()
        }

        override fun convertResultToString(resultValue: Any) =
            (resultValue as AccountDetail).accountNumber ?: ""
    }
}
