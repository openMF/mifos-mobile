package org.mifos.mobile.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.mifos.mobile.R
import org.mifos.mobile.databinding.RowFaqBinding
import org.mifos.mobile.injection.ActivityContext
import org.mifos.mobile.models.FAQ
import org.mifos.mobile.utils.FaqDiffUtil
import javax.inject.Inject

/**
 * Created by dilpreet on 12/8/17.
 */
class FAQAdapter @Inject constructor(@ActivityContext context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var faqArrayList: ArrayList<FAQ?>?
    private var alreadySelectedPosition = 0
    private val context: Context
    private lateinit var binding: RowFaqBinding

    init {
        faqArrayList = ArrayList()
        this.context = context
    }

    fun setFaqArrayList(faqArrayList: ArrayList<FAQ?>?) {
        this.faqArrayList = faqArrayList
        alreadySelectedPosition = -1
    }

    fun updateList(faqArrayList: java.util.ArrayList<FAQ?>?) {
        val diffResult = DiffUtil.calculateDiff(FaqDiffUtil(this.faqArrayList, faqArrayList))
        diffResult.dispatchUpdatesTo(this)
        setFaqArrayList(faqArrayList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = RowFaqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val (question, answer, isSelected) = faqArrayList?.get(position)!!
        (holder as ViewHolder).bind(question, answer, isSelected)
    }

    private fun updateView(position: Int) {
        if (alreadySelectedPosition == position) {
            faqArrayList?.get(alreadySelectedPosition)?.isSelected = false
            notifyItemChanged(alreadySelectedPosition)
            alreadySelectedPosition = -1
            return
        }
        if (alreadySelectedPosition != -1) {
            faqArrayList?.get(alreadySelectedPosition)?.isSelected = false
            notifyItemChanged(alreadySelectedPosition)
        }
        faqArrayList?.get(position)?.isSelected = true
        notifyItemChanged(position)
        alreadySelectedPosition = position
    }

    override fun getItemCount(): Int {
        return faqArrayList?.size ?: 0
    }

    inner class ViewHolder(private val binding: RowFaqBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(question: String?, answer: String?, isSelected: Boolean) {
            binding.tvQs.text = question
            binding.tvAns.text = answer

            if (isSelected) {
                binding.tvAns.visibility = View.VISIBLE
                binding.ivArrow.setImageResource(R.drawable.ic_arrow_up)
            } else {
                binding.tvAns.visibility = View.GONE
                binding.ivArrow.setImageResource(R.drawable.ic_arrow_drop_down)
            }
        }

        init {
            binding.llFaq.setOnClickListener {
                updateView(adapterPosition)
            }
        }
    }
}
