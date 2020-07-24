package org.mifos.mobile.utils

import androidx.recyclerview.widget.DiffUtil
import org.mifos.mobile.models.FAQ

/**
 * Created by dilpreet on 12/8/17.
 */
class FaqDiffUtil(private val oldFaq: ArrayList<FAQ?>??, private val newFaq: ArrayList<FAQ?>?) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return if (oldFaq?.size != null) oldFaq.size
        else 0
    }

    override fun getNewListSize(): Int {
        return if (newFaq?.size != null) newFaq.size
        else 0
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newFaq?.get(newItemPosition)?.answer?.let { oldFaq?.get(oldItemPosition)?.question?.compareTo(it) } == 0
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFaq?.get(oldItemPosition) == newFaq?.get(newItemPosition)
    }
}