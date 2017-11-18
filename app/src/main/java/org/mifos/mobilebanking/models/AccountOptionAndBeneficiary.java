package org.mifos.mobilebanking.models;

import org.mifos.mobilebanking.models.beneficary.Beneficiary;
import org.mifos.mobilebanking.models.templates.account.AccountOptionsTemplate;

import java.util.List;

/**
 * Created by dilpreet on 23/6/17.
 */

public class AccountOptionAndBeneficiary {

    private AccountOptionsTemplate accountOptionsTemplate;
    private List<Beneficiary> beneficiaryList;

    public AccountOptionAndBeneficiary(AccountOptionsTemplate accountOptionsTemplate,
                                       List<Beneficiary> beneficiaryList) {
        this.accountOptionsTemplate = accountOptionsTemplate;
        this.beneficiaryList = beneficiaryList;
    }

    public List<Beneficiary> getBeneficiaryList() {
        return beneficiaryList;
    }

    public AccountOptionsTemplate getAccountOptionsTemplate() {

        return accountOptionsTemplate;
    }
}
