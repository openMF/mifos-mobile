package org.mifos.mobilebanking.models.payload;

/**
 * Created by dilpreet on 19/03/18.
 */

public class AccountDetail {

    private String accountNumber;
    private String accountType;

    public AccountDetail(String accountNumber, String accountType) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
