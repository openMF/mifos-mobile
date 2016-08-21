package org.mifos.selfserviceapp.data.accounts;

/**
 * @author Vishwajeet
 * @since 22/06/16.
 */
public class LoanAccount {
    private long id;
    private String accountNo;
    private String productName;

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
}
