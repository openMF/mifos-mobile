package org.mifos.selfserviceapp.models.accounts.share;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.mifos.selfserviceapp.models.accounts.Account;
import org.mifos.selfserviceapp.models.accounts.savings.Currency;

public class ShareAccount extends Account implements Parcelable {

    @SerializedName("accountNo")
    @Expose
    private String accountNo;
    @SerializedName("totalApprovedShares")
    @Expose
    private Integer totalApprovedShares;
    @SerializedName("totalPendingForApprovalShares")
    @Expose
    private Integer totalPendingForApprovalShares;
    @SerializedName("productId")
    @Expose
    private Integer productId;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("shortProductName")
    @Expose
    private String shortProductName;
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("currency")
    @Expose
    private Currency currency;
    @SerializedName("timeline")
    @Expose
    private Timeline timeline;

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public Integer getTotalApprovedShares() {
        return totalApprovedShares;
    }

    public void setTotalApprovedShares(Integer totalApprovedShares) {
        this.totalApprovedShares = totalApprovedShares;
    }

    public Integer getTotalPendingForApprovalShares() {
        return totalPendingForApprovalShares;
    }

    public void setTotalPendingForApprovalShares(Integer totalPendingForApprovalShares) {
        this.totalPendingForApprovalShares = totalPendingForApprovalShares;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getShortProductName() {
        return shortProductName;
    }

    public void setShortProductName(String shortProductName) {
        this.shortProductName = shortProductName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accountNo);
        dest.writeValue(this.totalApprovedShares);
        dest.writeValue(this.totalPendingForApprovalShares);
        dest.writeValue(this.productId);
        dest.writeString(this.productName);
        dest.writeString(this.shortProductName);
        dest.writeParcelable(this.status, flags);
        dest.writeParcelable(this.currency, flags);
        dest.writeParcelable(this.timeline, flags);
        dest.writeLong(this.id);
    }

    public ShareAccount() {
    }

    protected ShareAccount(Parcel in) {
        this.accountNo = in.readString();
        this.totalApprovedShares = (Integer) in.readValue(Integer.class.getClassLoader());
        this.totalPendingForApprovalShares = (Integer) in.readValue(Integer.class.getClassLoader());
        this.productId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.productName = in.readString();
        this.shortProductName = in.readString();
        this.status = in.readParcelable(Status.class.getClassLoader());
        this.currency = in.readParcelable(Currency.class.getClassLoader());
        this.timeline = in.readParcelable(Timeline.class.getClassLoader());
        this.id = in.readLong();
    }

    public static final Parcelable.Creator<ShareAccount> CREATOR = new Parcelable.Creator
            <ShareAccount>() {
        @Override
        public ShareAccount createFromParcel(Parcel source) {
            return new ShareAccount(source);
        }

        @Override
        public ShareAccount[] newArray(int size) {
            return new ShareAccount[size];
        }
    };
}