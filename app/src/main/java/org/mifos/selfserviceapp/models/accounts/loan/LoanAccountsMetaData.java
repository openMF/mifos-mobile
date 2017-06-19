package org.mifos.selfserviceapp.models.accounts.loan;

/**
 * Created by dilpreet on 19/6/17.
 */

public class LoanAccountsMetaData {
    private int pendingApproval = 0;
    private int active = 0;
    private int waitingForDisbursal = 0;
    private int closed = 0;
    private int overpaid = 0;
    private double amount = 0;

    public double getAmount() {
        return amount;
    }

    public void addAmount(double amount) {
        this.amount += amount;
    }

    public int getPendingApproval() {
        return pendingApproval;
    }

    public int getActive() {
        return active;
    }

    public int getWaitingForDisbursal() {
        return waitingForDisbursal;
    }

    public int getClosed() {
        return closed;
    }

    public int getOverpaid() {
        return overpaid;
    }

    public void incPendingApproval() {
        pendingApproval++;
    }

    public void incActive() {
        active++;
    }

    public void incWaitingForDisbursal() {
        waitingForDisbursal++;
    }

    public void incClosed() {
        closed++;
    }

    public void incOverpaid() {
        overpaid++;
    }
}
