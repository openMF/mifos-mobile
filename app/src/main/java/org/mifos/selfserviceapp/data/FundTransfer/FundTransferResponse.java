package org.mifos.selfserviceapp.data.FundTransfer;

/**
 * @author Vishwajeet
 * @since 23/8/16.
 */

public class FundTransferResponse {
    private long resourceId;
    private long loanId;

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public long getSavingsId() {
        return savingsId;
    }

    public void setSavingsId(long savingsId) {
        this.savingsId = savingsId;
    }

    private long savingsId;

    public long getResourceId() {
        return resourceId;
    }

    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }
}
