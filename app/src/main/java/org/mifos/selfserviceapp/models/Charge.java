package org.mifos.selfserviceapp.models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vishwajeet
 * @since 16/8/16.
 */
public class Charge {

    private int id;
    private int clientId;
    private int chargeId;
    private String name;

    private List<Long> dueDate = new ArrayList<>();
    private ChargeTimeType chargeTimeType;
    private ChargeCalculationType chargeCalculationType;
    private Currency currency;

    private double amount = 0.0;
    private double amountPaid = 0.0;
    private double amountWaived = 0.0;
    private double amountWrittenOff = 0.0;
    private double amountOutstanding = 0.0;

    private boolean penalty = false;
    private boolean isActive = true;
    private boolean isPaid = false;
    private boolean isWaived = false;

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

    public int getClientId() {
        return chargeId;
    }

    public void setClientId(int chargeId) {
        this.clientId = clientId;
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

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public double getAmountWaived() {
        return amountWaived;
    }

    public void setAmountWaived(double amountWaived) {
        this.amountWaived = amountWaived;
    }

    public double getAmountWrittenOff() {
        return amountWrittenOff;
    }

    public void setAmountWrittenOff(double amountWrittenOff) {
        this.amountWrittenOff = amountWrittenOff;
    }

    public double getAmountOutstanding() {
        return amountOutstanding;
    }

    public void setAmountOutstanding(double amountOutstanding) {
        this.amountOutstanding = amountOutstanding;
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

    public ChargeTimeType getChargeTimeType() {
        return chargeTimeType;
    }

    public void setChargeTimeType(ChargeTimeType chargeTimeType) {
        this.chargeTimeType = chargeTimeType;
    }

    public ChargeCalculationType getChargeCalculationType() {
        return chargeCalculationType;
    }

    public void setChargeCalculationType(ChargeCalculationType chargeCalculationType) {
        this.chargeCalculationType = chargeCalculationType;
    }

    public boolean getPenalty() {
        return penalty;
    }

    public void setPenalty(boolean penalty) {
        this.penalty = penalty;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public boolean getIsWaived() {
        return isWaived;
    }

    public void setIsWaived(boolean isWaived) {
        this.isWaived = isWaived;
    }


    public static class Currency {
        private String code;
        private String name;
        private int decimalPlaces;
        private String displaySymbol;
        private String nameCode;
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

        public int getDecimalPlaces() {
            return decimalPlaces;
        }

        public void setDecimalPlaces(int decimalPlaces) {
            this.decimalPlaces = decimalPlaces;
        }

        public String getDisplaySymbol() {
            return displaySymbol;
        }

        public void setDisplaySymbol(String displaySymbol) {
            this.displaySymbol = displaySymbol;
        }

        public String getNameCode() {
            return nameCode;
        }

        public void setNameCode(String nameCode) {
            this.nameCode = nameCode;
        }

        public String getDisplayLabel() {
            return displayLabel;
        }

        public void setDisplayLabel(String displayLabel) {
            this.displayLabel = displayLabel;
        }

    }


    public static class ChargeTimeType {
        private int id;
        private String code;
        private String value;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


    public static class ChargeCalculationType {
        private int id;
        private String code; // example "chargeCalculationType.flat"
        private String value;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
