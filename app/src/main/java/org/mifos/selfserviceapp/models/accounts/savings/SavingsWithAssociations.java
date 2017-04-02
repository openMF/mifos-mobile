package org.mifos.selfserviceapp.models.accounts.savings;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.mifos.selfserviceapp.models.client.DepositType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vishwajeet
 * @since 22/06/16
 */

public class SavingsWithAssociations implements Parcelable {

    @SerializedName("id")
    Long id;

    @SerializedName("accountNo")
    String accountNo;

    @SerializedName("depositType")
    DepositType depositType;

    @SerializedName("externalId")
    String externalId;

    @SerializedName("clientId")
    Integer clientId;

    @SerializedName("clientName")
    String clientName;

    @SerializedName("savingsProductId")
    Integer savingsProductId;

    @SerializedName("savingsProductName")
    String savingsProductName;

    @SerializedName("fieldOfficerId")
    Integer fieldOfficerId;

    @SerializedName("status")
    Status status;

    @SerializedName("timeline")
    TimeLine timeline;

    @SerializedName("currency")
    Currency currency;

    @SerializedName("nominalAnnualInterestRate")
    Double nominalAnnualInterestRate;

    @SerializedName("minRequiredOpeningBalance")
    Double minRequiredOpeningBalance;

    @SerializedName("lockinPeriodFrequency")
    Double lockinPeriodFrequency;

    @SerializedName("withdrawalFeeForTransfers")
    Boolean withdrawalFeeForTransfers;

    @SerializedName("allowOverdraft")
    Boolean allowOverdraft;

    @SerializedName("enforceMinRequiredBalance")
    Boolean enforceMinRequiredBalance;

    @SerializedName("withHoldTax")
    Boolean withHoldTax;

    @SerializedName("lastActiveTransactionDate")
    List<Integer> lastActiveTransactionDate;

    @SerializedName("isDormancyTrackingActive")
    Boolean isDormancyTrackingActive;

    @SerializedName("summary")
    Summary summary;

    @SerializedName("transactions")
    List<Transactions> transactions = new ArrayList<>();

    public List<Transactions> getTransactions() {
        return transactions;
    }

    public void setTransactions(
            List<Transactions> transactions) {
        this.transactions = transactions;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getFieldOfficerId() {
        return fieldOfficerId;
    }

    public void setFieldOfficerId(Integer fieldOfficerId) {
        this.fieldOfficerId = fieldOfficerId;
    }

    public TimeLine getTimeline() {
        return timeline;
    }

    public void setTimeline(TimeLine timeline) {
        this.timeline = timeline;
    }

    public void setNominalAnnualInterestRate(Double nominalAnnualInterestRate) {
        this.nominalAnnualInterestRate = nominalAnnualInterestRate;
    }

    public Double getMinRequiredOpeningBalance() {
        return minRequiredOpeningBalance;
    }

    public void setMinRequiredOpeningBalance(Double minRequiredOpeningBalance) {
        this.minRequiredOpeningBalance = minRequiredOpeningBalance;
    }

    public Double getLockinPeriodFrequency() {
        return lockinPeriodFrequency;
    }

    public void setLockinPeriodFrequency(Double lockinPeriodFrequency) {
        this.lockinPeriodFrequency = lockinPeriodFrequency;
    }

    public Boolean getWithdrawalFeeForTransfers() {
        return withdrawalFeeForTransfers;
    }

    public void setWithdrawalFeeForTransfers(Boolean withdrawalFeeForTransfers) {
        this.withdrawalFeeForTransfers = withdrawalFeeForTransfers;
    }

    public Boolean getAllowOverdraft() {
        return allowOverdraft;
    }

    public void setAllowOverdraft(Boolean allowOverdraft) {
        this.allowOverdraft = allowOverdraft;
    }

    public Boolean getEnforceMinRequiredBalance() {
        return enforceMinRequiredBalance;
    }

    public void setEnforceMinRequiredBalance(Boolean enforceMinRequiredBalance) {
        this.enforceMinRequiredBalance = enforceMinRequiredBalance;
    }

    public Boolean getWithHoldTax() {
        return withHoldTax;
    }

    public void setWithHoldTax(Boolean withHoldTax) {
        this.withHoldTax = withHoldTax;
    }

    public List<Integer> getLastActiveTransactionDate() {
        return lastActiveTransactionDate;
    }

    public void setLastActiveTransactionDate(List<Integer> lastActiveTransactionDate) {
        this.lastActiveTransactionDate = lastActiveTransactionDate;
    }

    public Boolean getDormancyTrackingActive() {
        return isDormancyTrackingActive;
    }

    public void setDormancyTrackingActive(Boolean dormancyTrackingActive) {
        isDormancyTrackingActive = dormancyTrackingActive;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public DepositType getDepositType() {
        return depositType;
    }

    public void setDepositType(DepositType depositType) {
        this.depositType = depositType;
    }

    public boolean isRecurring() {
        return this.getDepositType() != null && this.getDepositType().isRecurring();
    }

    public double getNominalAnnualInterestRate() {
        return nominalAnnualInterestRate;
    }

    public void setNominalAnnualInterestRate(double nominalAnnualInterestRate) {
        this.nominalAnnualInterestRate = nominalAnnualInterestRate;
    }

    public String getSavingsProductName() {
        return savingsProductName;
    }

    public void setSavingsProductName(String savingsProductName) {
        this.savingsProductName = savingsProductName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Integer getSavingsProductId() {
        return savingsProductId;
    }

    public void setSavingsProductId(Integer savingsProductId) {
        this.savingsProductId = savingsProductId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SavingsWithAssociations() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.accountNo);
        dest.writeParcelable(this.depositType, flags);
        dest.writeString(this.externalId);
        dest.writeValue(this.clientId);
        dest.writeString(this.clientName);
        dest.writeValue(this.savingsProductId);
        dest.writeString(this.savingsProductName);
        dest.writeValue(this.fieldOfficerId);
        dest.writeParcelable(this.status, flags);
        dest.writeParcelable(this.timeline, flags);
        dest.writeParcelable(this.currency, flags);
        dest.writeValue(this.nominalAnnualInterestRate);
        dest.writeValue(this.minRequiredOpeningBalance);
        dest.writeValue(this.lockinPeriodFrequency);
        dest.writeValue(this.withdrawalFeeForTransfers);
        dest.writeValue(this.allowOverdraft);
        dest.writeValue(this.enforceMinRequiredBalance);
        dest.writeValue(this.withHoldTax);
        dest.writeList(this.lastActiveTransactionDate);
        dest.writeValue(this.isDormancyTrackingActive);
        dest.writeParcelable(this.summary, flags);
        dest.writeTypedList(this.transactions);
    }

    protected SavingsWithAssociations(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.accountNo = in.readString();
        this.depositType = in.readParcelable(DepositType.class.getClassLoader());
        this.externalId = in.readString();
        this.clientId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.clientName = in.readString();
        this.savingsProductId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.savingsProductName = in.readString();
        this.fieldOfficerId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.status = in.readParcelable(Status.class.getClassLoader());
        this.timeline = in.readParcelable(TimeLine.class.getClassLoader());
        this.currency = in.readParcelable(Currency.class.getClassLoader());
        this.nominalAnnualInterestRate = (Double) in.readValue(Double.class.getClassLoader());
        this.minRequiredOpeningBalance = (Double) in.readValue(Double.class.getClassLoader());
        this.lockinPeriodFrequency = (Double) in.readValue(Double.class.getClassLoader());
        this.withdrawalFeeForTransfers = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.allowOverdraft = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.enforceMinRequiredBalance = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.withHoldTax = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.lastActiveTransactionDate = new ArrayList<Integer>();
        in.readList(this.lastActiveTransactionDate, Integer.class.getClassLoader());
        this.isDormancyTrackingActive = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.summary = in.readParcelable(Summary.class.getClassLoader());
        this.transactions = in.createTypedArrayList(Transactions.CREATOR);
    }

    public static final Creator<SavingsWithAssociations> CREATOR =
            new Creator<SavingsWithAssociations>() {
                @Override
                public SavingsWithAssociations createFromParcel(Parcel source) {
                    return new SavingsWithAssociations(source);
                }

                @Override
                public SavingsWithAssociations[] newArray(int size) {
                    return new SavingsWithAssociations[size];
                }
            };
}
