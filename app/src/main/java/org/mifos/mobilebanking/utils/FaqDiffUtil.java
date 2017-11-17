package org.mifos.mobilebanking.utils;

import android.support.v7.util.DiffUtil;

import org.mifos.mobilebanking.models.FAQ;

import java.util.List;

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
