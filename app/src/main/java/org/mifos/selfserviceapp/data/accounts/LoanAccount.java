package org.mifos.selfserviceapp.data.accounts;

/**
 * @author Vishwajeet
 * @since 22/06/16.
 */
public class LoanAccount {
    private int id;
    private int loanProductId;
    private int numberOfRepayments;

    private String accountNo;
    private String productName;
    private String loanProductName;
    private String clientName;
    private String loanProductDescription;

    private double principal;
    private double annualInterestRate;

    private Summary summary;

    public int getLoanProductId() {
        return loanProductId;
    }

    public void setLoanProductId(int loanProductId) {
        this.loanProductId = loanProductId;
    }

    public int getNumberOfRepayments() {
        return numberOfRepayments;
    }

    public void setNumberOfRepayments(int numberOfRepayments) {
        this.numberOfRepayments = numberOfRepayments;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getLoanProductDescription() {
        return loanProductDescription;
    }

    public void setLoanProductDescription(String loanProductDescription) {
        this.loanProductDescription = loanProductDescription;
    }

    public static class Summary {
        private double principalDisbursed;
        private double principalPaid;
        private double interestCharged;
        private double interestPaid;

        public double getPrincipalDisbursed() {
            return principalDisbursed;
        }

        public void setPrincipalDisbursed(double principalDisbursed) {
            this.principalDisbursed = principalDisbursed;
        }

        public double getPrincipalPaid() {
            return principalPaid;
        }

        public void setPrincipalPaid(double principalPaid) {
            this.principalPaid = principalPaid;
        }

        public double getInterestCharged() {
            return interestCharged;
        }

        public void setInterestCharged(double interestCharged) {
            this.interestCharged = interestCharged;
        }

        public double getInterestPaid() {
            return interestPaid;
        }

        public void setInterestPaid(double interestPaid) {
            this.interestPaid = interestPaid;
        }

    }
}
