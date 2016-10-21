package org.mifos.selfserviceapp.models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vishwajeet
 * @since 16/8/16.
 */
public class Charge {
    private int id;
    private int chargeId;
    private String name;
    private double amount;
    private List<Long> dueDate = new ArrayList<>();
    private Currency currency;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChargeId() {
        return chargeId;
    }

    public void setChargeId(int chargeId) {
        this.chargeId = chargeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List<Long> getDueDate() {
        return dueDate;
    }

    public void setDueDate(List<Long> dueDate) {
        this.dueDate = dueDate;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public static class Currency {
        private String code;
        private String name;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
