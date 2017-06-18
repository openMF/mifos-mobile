package org.mifos.selfserviceapp.utils;

import org.mifos.selfserviceapp.models.accounts.Account;

import java.util.Comparator;

/**
 * Created by dilpreet on 14/6/17.
 */

public class ComparatorBasedOnId implements Comparator<Account> {
    @Override
    public int compare(Account o1, Account o2) {
        return Float.compare(o2.getId(), o1.getId());
    }
}

