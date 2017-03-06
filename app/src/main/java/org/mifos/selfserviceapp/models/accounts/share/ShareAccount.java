package org.mifos.selfserviceapp.models.accounts.share;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.mifos.selfserviceapp.models.accounts.savings.Currency;

public class ShareAccount {

    @SerializedName("id")
    @Expose
    private Integer id;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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


}