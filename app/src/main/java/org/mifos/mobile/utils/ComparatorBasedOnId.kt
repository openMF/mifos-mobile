package org.mifos.mobile.utils

import org.mifos.mobile.models.accounts.Account

/**
 * Created by dilpreet on 14/6/17.
 */
open class ComparatorBasedOnId : Comparator<Account?> {
    /**
     * Compares [Account] based on their Id
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     * first argument is less than, equal to, or greater than the
     * second.
     */
    override fun compare(o1: Account?, o2: Account?): Int {
        return if (o1 != null && o2 != null)
            o2.id.toFloat().compareTo(o1.id.toFloat())
        else 0
    }
}