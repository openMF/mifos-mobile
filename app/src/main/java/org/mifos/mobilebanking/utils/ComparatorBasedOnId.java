package org.mifos.mobilebanking.utils;

import org.mifos.mobilebanking.models.accounts.Account;

import java.util.Comparator;

/**
 * Created by dilpreet on 14/6/17.
 */

public class ComparatorBasedOnId implements Comparator<Account> {
    /**
     * Compares {@link Account} based on their Id
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     *         first argument is less than, equal to, or greater than the
     *         second.
     */
    @Override
    public int compare(Account o1, Account o2) {
        return Float.compare(o2.getId(), o1.getId());
    }
}

