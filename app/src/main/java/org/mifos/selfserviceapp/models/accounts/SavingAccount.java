package org.mifos.selfserviceapp.models.accounts;

/**
 * @author Vishwajeet
 * @since 22/06/16
 */

public class SavingAccount {
    private long id;
    private long overdraftLimit;
    private long minRequiredBalance;

    private double accountBalance;
    private double totalDeposits;

    private String accountNo;
    private String productName;
    private String savingsProductName;
    private String clientName;
    private String savingsProductId;
    private double nominalAnnualInterestRate;

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
}
