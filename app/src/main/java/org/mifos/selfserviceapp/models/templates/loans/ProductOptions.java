package org.mifos.selfserviceapp.models.templates.loans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajan Maurya on 16/07/16.
 */
public class ProductOptions implements Parcelable {

    @SerializedName("id")
    Integer id;

    @SerializedName("name")
    String name;

    @SerializedName("includeInBorrowerCycle")
    Boolean includeInBorrowerCycle;

    @SerializedName("useBorrowerCycle")
    Boolean useBorrowerCycle;

    @SerializedName("isLinkedToFloatingInterestRates")
    Boolean isLinkedToFloatingInterestRates;

    @SerializedName("isFloatingInterestRateCalculationAllowed")
    Boolean isFloatingInterestRateCalculationAllowed;

    @SerializedName("allowVariableInstallments")
    Boolean allowVariableInstallments;

    @SerializedName("isInterestRecalculationEnabled")
    Boolean isInterestRecalculationEnabled;

    @SerializedName("canDefineInstallmentAmount")
    Boolean canDefineInstallmentAmount;

    @SerializedName("holdGuaranteeFunds")
    Boolean holdGuaranteeFunds;

    @SerializedName("accountMovesOutOfNPAOnlyOnArrearsCompletion")
    Boolean accountMovesOutOfNPAOnlyOnArrearsCompletion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIncludeInBorrowerCycle() {
        return includeInBorrowerCycle;
    }

    public void setIncludeInBorrowerCycle(Boolean includeInBorrowerCycle) {
        this.includeInBorrowerCycle = includeInBorrowerCycle;
    }

    public Boolean getUseBorrowerCycle() {
        return useBorrowerCycle;
    }

    public void setUseBorrowerCycle(Boolean useBorrowerCycle) {
        this.useBorrowerCycle = useBorrowerCycle;
    }

    public Boolean getLinkedToFloatingInterestRates() {
        return isLinkedToFloatingInterestRates;
    }

    public void setLinkedToFloatingInterestRates(Boolean linkedToFloatingInterestRates) {
        isLinkedToFloatingInterestRates = linkedToFloatingInterestRates;
    }

    public Boolean getFloatingInterestRateCalculationAllowed() {
        return isFloatingInterestRateCalculationAllowed;
    }

    public void setFloatingInterestRateCalculationAllowed(
            Boolean floatingInterestRateCalculationAllowed) {
        isFloatingInterestRateCalculationAllowed = floatingInterestRateCalculationAllowed;
    }

    public Boolean getAllowVariableInstallments() {
        return allowVariableInstallments;
    }

    public void setAllowVariableInstallments(Boolean allowVariableInstallments) {
        this.allowVariableInstallments = allowVariableInstallments;
    }

    public Boolean getInterestRecalculationEnabled() {
        return isInterestRecalculationEnabled;
    }

    public void setInterestRecalculationEnabled(Boolean interestRecalculationEnabled) {
        isInterestRecalculationEnabled = interestRecalculationEnabled;
    }

    public Boolean getCanDefineInstallmentAmount() {
        return canDefineInstallmentAmount;
    }

    public void setCanDefineInstallmentAmount(Boolean canDefineInstallmentAmount) {
        this.canDefineInstallmentAmount = canDefineInstallmentAmount;
    }

    public Boolean getHoldGuaranteeFunds() {
        return holdGuaranteeFunds;
    }

    public void setHoldGuaranteeFunds(Boolean holdGuaranteeFunds) {
        this.holdGuaranteeFunds = holdGuaranteeFunds;
    }

    public Boolean getAccountMovesOutOfNPAOnlyOnArrearsCompletion() {
        return accountMovesOutOfNPAOnlyOnArrearsCompletion;
    }

    public void setAccountMovesOutOfNPAOnlyOnArrearsCompletion(
            Boolean accountMovesOutOfNPAOnlyOnArrearsCompletion) {
        this.accountMovesOutOfNPAOnlyOnArrearsCompletion =
                accountMovesOutOfNPAOnlyOnArrearsCompletion;
    }

    @Override
    public String toString() {
        return "ProductOptions{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", includeInBorrowerCycle=" + includeInBorrowerCycle +
                ", useBorrowerCycle=" + useBorrowerCycle +
                ", isLinkedToFloatingInterestRates=" + isLinkedToFloatingInterestRates +
                ", isFloatingInterestRateCalculationAllowed=" +
                isFloatingInterestRateCalculationAllowed +
                ", allowVariableInstallments=" + allowVariableInstallments +
                ", isInterestRecalculationEnabled=" + isInterestRecalculationEnabled +
                ", canDefineInstallmentAmount=" + canDefineInstallmentAmount +
                ", holdGuaranteeFunds=" + holdGuaranteeFunds +
                ", accountMovesOutOfNPAOnlyOnArrearsCompletion=" +
                accountMovesOutOfNPAOnlyOnArrearsCompletion +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeValue(this.includeInBorrowerCycle);
        dest.writeValue(this.useBorrowerCycle);
        dest.writeValue(this.isLinkedToFloatingInterestRates);
        dest.writeValue(this.isFloatingInterestRateCalculationAllowed);
        dest.writeValue(this.allowVariableInstallments);
        dest.writeValue(this.isInterestRecalculationEnabled);
        dest.writeValue(this.canDefineInstallmentAmount);
        dest.writeValue(this.holdGuaranteeFunds);
        dest.writeValue(this.accountMovesOutOfNPAOnlyOnArrearsCompletion);
    }

    public ProductOptions() {
    }

    protected ProductOptions(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.includeInBorrowerCycle = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.useBorrowerCycle = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isLinkedToFloatingInterestRates = (Boolean) in.readValue(Boolean.class
                .getClassLoader());
        this.isFloatingInterestRateCalculationAllowed = (Boolean) in.readValue(Boolean.class
                .getClassLoader());
        this.allowVariableInstallments = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isInterestRecalculationEnabled = (Boolean) in.readValue(Boolean.class.getClassLoader
                ());
        this.canDefineInstallmentAmount = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.holdGuaranteeFunds = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.accountMovesOutOfNPAOnlyOnArrearsCompletion = (Boolean) in.readValue(Boolean.class
                .getClassLoader());
    }

    public static final Creator<ProductOptions> CREATOR =
            new Creator<ProductOptions>() {
        @Override
        public ProductOptions createFromParcel(Parcel source) {
            return new ProductOptions(source);
        }

        @Override
        public ProductOptions[] newArray(int size) {
            return new ProductOptions[size];
        }
    };
}
