package org.mifos.selfserviceapp.models.accounts.loan;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @author Vishwajeet
 * @since 22/06/16.
 */
public class LoanAccount implements Parcelable {

    @SerializedName("id")
    private long id;

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
    }

    public Integer getLoanCycle() {
        return loanCycle;
    }

    public void setLoanCycle(Integer loanCycle) {
        this.loanCycle = loanCycle;
    }

    public Boolean getInArrears() {
        return inArrears;
    }

    public void setInArrears(Boolean inArrears) {
        this.inArrears = inArrears;
    }

    public static Creator<LoanAccount> getCREATOR() {
        return CREATOR;
    }

    @SerializedName("loanProductId")
    private long loanProductId;

    @SerializedName("externalId")
    private String externalId;

    @SerializedName("numberOfRepayments")
    private long numberOfRepayments;

    @SerializedName("accountNo")
    private String accountNo;

    @SerializedName("productName")
    private String productName;

    @SerializedName("productId")
    private Integer productId;

    @SerializedName("loanProductName")
    private String loanProductName;

    @SerializedName("clientName")
    private String clientName;

    @SerializedName("loanProductDescription")
    private String loanProductDescription;

    @SerializedName("principal")
    private double principal;

    @SerializedName("annualInterestRate")
    private double annualInterestRate;

    @SerializedName("status")
    private Status status;

    @SerializedName("loanType")
    private LoanType loanType;

    @SerializedName("loanCycle")
    private Integer loanCycle;

    @SerializedName("inArrears")
    Boolean inArrears;

    @SerializedName("summary")
    private Summary summary;

    public String getLoanProductName() {
        return loanProductName;
    }

    public void setLoanProductName(String loanProductName) {
        this.loanProductName = loanProductName;
    }


    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public double getPrincipal() {
        return principal;
    }

    public void setPrincipal(double principal) {
        this.principal = principal;
    }

    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    public void setAnnualInterestRate(double annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }

    public long getNumberOfRepayments() {
        return numberOfRepayments;
    }

    public void setNumberOfRepayments(long numberOfRepayments) {
        this.numberOfRepayments = numberOfRepayments;
    }

    public long getLoanProductId() {
        return loanProductId;
    }

    public void setLoanProductId(long loanProductId) {
        this.loanProductId = loanProductId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getLoanProductDescription() {
        return loanProductDescription;
    }

    public void setLoanProductDescription(String loanProductDescription) {
        this.loanProductDescription = loanProductDescription;
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
        dest.writeLong(this.loanProductId);
        dest.writeLong(this.numberOfRepayments);
        dest.writeString(this.accountNo);
        dest.writeString(this.productName);
        dest.writeString(this.loanProductName);
        dest.writeString(this.clientName);
        dest.writeString(this.loanProductDescription);
        dest.writeDouble(this.principal);
        dest.writeDouble(this.annualInterestRate);
        dest.writeParcelable(this.summary, flags);
    }

    public LoanAccount() {
    }

    protected LoanAccount(Parcel in) {
        this.id = in.readLong();
        this.loanProductId = in.readLong();
        this.numberOfRepayments = in.readLong();
        this.accountNo = in.readString();
        this.productName = in.readString();
        this.loanProductName = in.readString();
        this.clientName = in.readString();
        this.loanProductDescription = in.readString();
        this.principal = in.readDouble();
        this.annualInterestRate = in.readDouble();
        this.summary = in.readParcelable(Summary.class.getClassLoader());
    }

    public static final Parcelable.Creator<LoanAccount> CREATOR =
            new Parcelable.Creator<LoanAccount>() {
                @Override
                public LoanAccount createFromParcel(Parcel source) {
                    return new LoanAccount(source);
                }

                @Override
                public LoanAccount[] newArray(int size) {
                    return new LoanAccount[size];
                }
            };
}
