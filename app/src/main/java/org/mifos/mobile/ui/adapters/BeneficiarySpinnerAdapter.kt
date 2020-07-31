package org.mifos.mobile.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

import butterknife.BindView
import butterknife.ButterKnife

import org.mifos.mobile.R
import org.mifos.mobile.models.beneficiary.BeneficiaryDetail

/*
* Created by saksham on 18/June/2018
*/
class BeneficiarySpinnerAdapter(context: Context, private var resource: Int, var list: MutableList<BeneficiaryDetail?>) :
        ArrayAdapter<String?>(context, resource, 0, list as List<String?>) {

    @JvmField
    @BindView(R.id.tv_account_number)
    var tvAccountNumber: TextView? = null

    @JvmField
    @BindView(R.id.tv_beneficiary_name)
    var tvBeneficiaryName: TextView? = null
    private var layoutInflater: LayoutInflater = LayoutInflater.from(context)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    override fun getDropDownView(
            position: Int, convertView: View?,
            parent: ViewGroup
    ): View {
        return createItemView(position, convertView, parent)
    }

    private fun createItemView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = layoutInflater.inflate(resource, parent, false)
        ButterKnife.bind(this, view)
        tvAccountNumber?.text = list[position]?.accountNumber
        tvBeneficiaryName?.text = list[position]?.beneficiaryName
        return view
    }

    override fun getItem(position: Int): String? {
        return list[position]?.accountNumber
    }

}