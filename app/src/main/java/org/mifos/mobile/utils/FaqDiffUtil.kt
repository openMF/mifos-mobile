package org.mifos.mobile.utils

import androidx.recyclerview.widget.DiffUtil
import org.mifos.mobile.models.FAQ

/**
 * Created by dilpreet on 12/8/17.
 */
class FaqDiffUtil(private val oldFaq: List<FAQ>, private val newFaq: List<FAQ>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldFaq.size
    }

    override fun getNewListSize(): Int {
        return newFaq.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFaq[oldItemPosition].question!!.compareTo(newFaq[newItemPosition].answer!!) == 0
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFaq[oldItemPosition] == newFaq[newItemPosition]
    }
}