package org.mifos.mobilebanking.models.accounts;

import org.mifos.mobilebanking.models.accounts.loan.LoanAccount;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vishwajeet
 * @since 13/08/16
 */
public class LoanAccountsListResponse {
    private List<LoanAccount> loanAccounts = new ArrayList<>();

    public List<LoanAccount> getLoanAccounts() {
        return loanAccounts;
    }

    public void setAccounts(List<LoanAccount> loanAccounts) {
        this.loanAccounts = loanAccounts;
    }
}
