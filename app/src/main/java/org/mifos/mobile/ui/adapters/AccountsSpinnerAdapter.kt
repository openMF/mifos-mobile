package org.mifos.mobile.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

import androidx.annotation.LayoutRes

import butterknife.BindView
import butterknife.ButterKnife
import org.mifos.mobile.R
import org.mifos.mobile.models.payload.AccountDetail


/**
 * Created by dilpreet on 19/03/18.
 */
class AccountsSpinnerAdapter(context: Context, @LayoutRes resource: Int, objects: MutableList<AccountDetail?>) :
        ArrayAdapter<String?>(context, resource, 0, objects as List<String?>) {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val accountDetails: MutableList<AccountDetail?> = objects
    private val mResource: Int = resource

    @JvmField
    @BindView(R.id.tv_account_number)
    var tvAccountNumber: TextView? = null

    @JvmField
    @BindView(R.id.tv_account_type)
    var tvAccountType: TextView? = null
    override fun getDropDownView(
            position: Int, convertView: View?,
            parent: ViewGroup
    ): View {
        return createItemView(position, convertView, parent)
    }

    override fun getView(
            position: Int, convertView: View?,
            parent: ViewGroup
    ): View {
        return createItemView(position, convertView, parent)
    }

    private fun createItemView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = mInflater.inflate(mResource, parent, false)
        ButterKnife.bind(this, view)
        val (accountNumber: String?, accountType) = accountDetails[position]!!
        tvAccountNumber?.text = accountNumber
        tvAccountType?.text = accountType
        return view
    }

}