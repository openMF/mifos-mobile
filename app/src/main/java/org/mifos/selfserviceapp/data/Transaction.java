package org.mifos.selfserviceapp.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vishwajeet
 * @since 10/8/16.
 */
public class Transaction {
    private int id;
    private List<Transaction> pageItems = new ArrayList<Transaction>();
    private List<Integer> submittedOnDate = new ArrayList<Integer>();
    private float amount;
    private Type type;

    public List<Transaction> getPageItems() {
        return pageItems;
    }

    public void setPageItems(List<Transaction> pageItems) {
        this.pageItems = pageItems;
    }

    public List<Integer> getSubmittedOnDate() {
        return submittedOnDate;
    }

    public void setSubmittedOndate(List<Integer> submittedOnDate) {
        this.submittedOnDate = submittedOnDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType (Type type) {
        this.type = type;
    }

    public class Type {
        private int id;
        private String code;
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}