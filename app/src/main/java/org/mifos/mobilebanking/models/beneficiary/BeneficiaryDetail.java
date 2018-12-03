package org.mifos.mobilebanking.models.beneficiary;

/*
 * Created by saksham on 18/June/2018
 */

public class BeneficiaryDetail {

    String accountNumber;
    String beneficiaryName;

    public BeneficiaryDetail(String accountNumber, String beneficiaryName) {
        this.accountNumber = accountNumber;
        this.beneficiaryName = beneficiaryName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }
}
