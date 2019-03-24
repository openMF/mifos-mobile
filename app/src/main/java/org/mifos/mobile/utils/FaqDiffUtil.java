package org.mifos.mobile.utils;

import org.mifos.mobile.models.FAQ;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;

/**
 * Created by dilpreet on 12/8/17.
 */

public class FaqDiffUtil extends DiffUtil.Callback {

    private List<FAQ> oldFaq;
    private List<FAQ> newFaq;

    public FaqDiffUtil(List<FAQ> oldFaq, List<FAQ> newFaq) {
        this.oldFaq = oldFaq;
        this.newFaq = newFaq;
    }

    @Override
    public int getOldListSize() {
        return oldFaq.size();
    }

    @Override
    public int getNewListSize() {
        return newFaq.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldFaq.get(oldItemPosition).getQuestion().
                compareTo(newFaq.get(newItemPosition).getAnswer()) == 0;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldFaq.get(oldItemPosition).equals(newFaq.get(newItemPosition));
    }
}
