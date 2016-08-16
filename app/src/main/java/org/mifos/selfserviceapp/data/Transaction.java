package org.mifos.selfserviceapp.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vishwajeet
 * @since 10/8/16.
 */
public class Transaction {
    private int id;
    private int officeId;
    private List<Long> submittedOnDate = new ArrayList<>();
    private float amount;
    private Type type;
    private Currency currency;

    public List<Long> getSubmittedOnDate() {
        return submittedOnDate;
    }

    public int getOfficeId() {
        return officeId;
    }

    public void setSubmittedOnDate(List<Long> submittedOnDate) {
        this.submittedOnDate = submittedOnDate;
    }

    public void setOfficeId(int officeId) {
        this.officeId = officeId;

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

    public void setType(Type type) {
        this.type = type;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public static class Type {
        private int id;
        private String code;
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class Currency {
        private String code;
        private String name;
        private String displayLabel;

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

        public String getDisplayLabel() {
            return displayLabel;
        }

        public void setDisplayLabel(String displayLabel) {
            this.displayLabel = displayLabel;
        }
    }
}