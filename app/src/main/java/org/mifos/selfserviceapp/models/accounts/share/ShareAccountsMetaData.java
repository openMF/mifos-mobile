package org.mifos.selfserviceapp.models.accounts.share;

/**
 * Created by dilpreet on 20/6/17.
 */

public class ShareAccountsMetaData {
    private int pendingApproval = 0;
    private int approved = 0;
    private int active = 0;
    private int rejected = 0;
    private int closed = 0;
    private int approvedShares = 0;

    public int getApprovedShares() {
        return approvedShares;
    }

    public void addShare(int approvedShares) {
        this.approvedShares += approvedShares;
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

    public int getRejected() {
        return rejected;
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

    public void incRejected() {
        rejected++;
    }
}
