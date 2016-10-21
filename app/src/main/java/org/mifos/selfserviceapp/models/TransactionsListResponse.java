package org.mifos.selfserviceapp.models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vishwajeet
 * @since 17/8/16.
 */
public class TransactionsListResponse {
    private List<Transaction> pageItems = new ArrayList<>();

    public List<Transaction> getPageItems() {
        return pageItems;
    }

    public void setPageItems(List<Transaction> pageItems) {
        this.pageItems = pageItems;
    }
}
