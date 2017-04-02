package org.mifos.selfserviceapp.models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vishwajeet
 * @since 16/8/16.
 */
public class ChargeListResponse {
    private List<Charge> pageItems = new ArrayList<>();

    public List<Charge> getPageItems() {
        return pageItems;
    }

    public void setPageItems(List<Charge> pageItems) {
        this.pageItems = pageItems;
    }
}
