package org.mifos.mobilebanking.models.accounts;

import org.mifos.mobilebanking.models.accounts.savings.SavingAccount;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vishwajeet
 * @since 13/08/16
 */
public class SavingAccountsListResponse {
    private List<SavingAccount> savingsAccounts = new ArrayList<>();

    public List<SavingAccount> getSavingsAccounts() {
        return savingsAccounts;
    }

    public void setAccounts(List<SavingAccount> savingsAccounts) {
        this.savingsAccounts = savingsAccounts;
    }
}
