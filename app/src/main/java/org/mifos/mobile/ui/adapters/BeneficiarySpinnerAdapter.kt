package org.mifos.mobile.ui.adapters

/*
* Created by saksham on 18/June/2018
*/
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryDetail
import org.mifos.mobile.databinding.BeneficiarySpinnerLayoutBinding

class BeneficiarySpinnerAdapter(
    context: Context,
    private var resource: Int,
    private var list: MutableList<BeneficiaryDetail?>,
) : ArrayAdapter<String?>(context, resource, 0, list as List<String?>) {

    private var layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private lateinit var binding: BeneficiarySpinnerLayoutBinding

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    private fun createItemView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = BeneficiarySpinnerLayoutBinding.inflate(layoutInflater, parent, false)
        val view = binding.root
        this.binding = binding

        binding.tvAccountNumber.text = list[position]?.accountNumber
        binding.tvBeneficiaryName.text = list[position]?.beneficiaryName

        return view
    }

    override fun getItem(position: Int): String? {
        return list[position]?.accountNumber
    }
}
