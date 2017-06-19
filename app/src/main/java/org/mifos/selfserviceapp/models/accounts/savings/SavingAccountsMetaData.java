package org.mifos.selfserviceapp.models.accounts.savings;

/**
 * Created by dilpreet on 20/6/17.
 */

public class SavingAccountsMetaData {
    private int pendingApproval = 0;
    private int approved = 0;
    private int active = 0;
    private int matured = 0;
    private int closed = 0;
    private double amount = 0;

    public double getAmount() {
        return amount;
    }

    public void addAmount(double amount) {
        this.amount += amount;
    }

    public int getApproved() {
        return approved;
    }

    public int getActive() {
        return active;
    }

    public int getPendingApproval() {
        return pendingApproval;
    }

    public int getClosed() {
        return closed;
    }

    public int getMatured() {
        return matured;
    }

    public void incApproved() {
        approved++;
    }

    public void incActive() {
        active++;
    }

    public void incPendingApproval() {
        pendingApproval++;
    }

    public void incClosed() {
        closed++;
    }

    public void incMatured() {
        matured++;
    }
}
