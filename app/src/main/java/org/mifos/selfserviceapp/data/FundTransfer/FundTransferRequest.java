package org.mifos.selfserviceapp.data.FundTransfer;

/**
 * @author Vishwajeet
 * @since 22/8/16
 */

public class FundTransferRequest {
    private long fromOfficeId;
    private long fromClientId;
    private long fromAccountType;
    private long fromAccountId;
    private long toOfficeId;
    private long toClientId;
    private long toAccountType;
    private long toAccountId;
    private String dateFormat;
    private String locale;
    private String transferDate;
    private String transferAmount;
    private String transferDescription;

    public long getFromClientId() {
        return fromClientId;
    }

    public void setFromClientId(long fromClientId) {
        this.fromClientId = fromClientId;
    }

    public long getFromOfficeId() {
        return fromOfficeId;
    }

    public void setFromOfficeId(long fromOfficeId) {
        this.fromOfficeId = fromOfficeId;
    }

    public long getFromAccountType() {
        return fromAccountType;
    }

    public void setFromAccountType(long fromAccountType) {
        this.fromAccountType = fromAccountType;
    }

    public long getToOfficeId() {
        return toOfficeId;
    }

    public void setToOfficeId(long toOfficeId) {
        this.toOfficeId = toOfficeId;
    }

    public long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public long getToClientId() {
        return toClientId;
    }

    public void setToClientId(long toClientId) {
        this.toClientId = toClientId;
    }

    public long getToAccountType() {
        return toAccountType;
    }

    public void setToAccountType(long toAccountType) {
        this.toAccountType = toAccountType;
    }

    public long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(String transferDate) {
        this.transferDate = transferDate;
    }

    public String getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getTransferDescription() {
        return transferDescription;
    }

    public void setTransferDescription(String transferDescription) {
        this.transferDescription = transferDescription;
    }
}
