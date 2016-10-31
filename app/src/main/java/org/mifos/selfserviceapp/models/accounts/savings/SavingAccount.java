package org.mifos.selfserviceapp.models.accounts.savings;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.mifos.selfserviceapp.models.client.DepositType;

/**
 * @author Vishwajeet
 * @since 22/06/16
 */

public class SavingAccount implements Parcelable {

    @SerializedName("id")
    private long id;

    @SerializedName("accountNo")
    private String accountNo;

    @SerializedName("productName")
    private String productName;

    @SerializedName("productId")
    private Integer productId;

    @SerializedName("overdraftLimit")
    private long overdraftLimit;

    @SerializedName("minRequiredBalance")
    private long minRequiredBalance;

    @SerializedName("accountBalance")
    private double accountBalance;

    @SerializedName("totalDeposits")
    private double totalDeposits;

    @SerializedName("savingsProductName")
    private String savingsProductName;

    @SerializedName("clientName")
    private String clientName;

    @SerializedName("savingsProductId")
    private String savingsProductId;

    @SerializedName("nominalAnnualInterestRate")
    private double nominalAnnualInterestRate;

    @SerializedName("status")
    private Status status;

    @SerializedName("currency")
    private Currency currency;

    @SerializedName("depositType")
    private DepositType depositType;

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

    public double getTotalDeposits() {
        return totalDeposits;
    }

    public void setTotalDeposits(double totalDeposits) {
        this.totalDeposits = totalDeposits;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
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

    public String getSavingsProductId() {
        return savingsProductId;
    }

    public void setSavingsProductId(String savingsProductId) {
        this.savingsProductId = savingsProductId;
    }

    public long getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(long overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    public long getMinRequiredBalance() {
        return minRequiredBalance;
    }

    public void setMinRequiredBalance(long minRequiredBalance) {
        this.minRequiredBalance = minRequiredBalance;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.accountNo);
        dest.writeString(this.productName);
        dest.writeValue(this.productId);
        dest.writeLong(this.overdraftLimit);
        dest.writeLong(this.minRequiredBalance);
        dest.writeDouble(this.accountBalance);
        dest.writeDouble(this.totalDeposits);
        dest.writeString(this.savingsProductName);
        dest.writeString(this.clientName);
        dest.writeString(this.savingsProductId);
        dest.writeDouble(this.nominalAnnualInterestRate);
        dest.writeParcelable(this.status, flags);
        dest.writeParcelable(this.currency, flags);
        dest.writeParcelable(this.depositType, flags);
    }

    public SavingAccount() {
    }

    protected SavingAccount(Parcel in) {
        this.id = in.readLong();
        this.accountNo = in.readString();
        this.productName = in.readString();
        this.productId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.overdraftLimit = in.readLong();
        this.minRequiredBalance = in.readLong();
        this.accountBalance = in.readDouble();
        this.totalDeposits = in.readDouble();
        this.savingsProductName = in.readString();
        this.clientName = in.readString();
        this.savingsProductId = in.readString();
        this.nominalAnnualInterestRate = in.readDouble();
        this.status = in.readParcelable(Status.class.getClassLoader());
        this.currency = in.readParcelable(Currency.class.getClassLoader());
        this.depositType = in.readParcelable(DepositType.class.getClassLoader());
    }

    public static final Parcelable.Creator<SavingAccount> CREATOR =
            new Parcelable.Creator<SavingAccount>() {
                @Override
                public SavingAccount createFromParcel(Parcel source) {
                    return new SavingAccount(source);
                }

                @Override
                public SavingAccount[] newArray(int size) {
                    return new SavingAccount[size];
                }
            };
}
