package org.mifos.selfserviceapp;

import com.google.gson.reflect.TypeToken;

import org.mifos.selfserviceapp.models.Page;
import org.mifos.selfserviceapp.models.User;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.models.accounts.loan.LoanWithAssociations;
import org.mifos.selfserviceapp.models.beneficary.Beneficiary;
import org.mifos.selfserviceapp.models.client.Client;
import org.mifos.selfserviceapp.models.client.ClientAccounts;

import java.util.List;

/**
 * Created by dilpreet on 26/6/17.
 */

public class FakeRemoteDataSource {

    private static TestDataFactory mTestDataFactory = new TestDataFactory();

    public static ClientAccounts getClientAccounts() {
        return mTestDataFactory.getListTypePojo(new TypeToken<ClientAccounts>() {
            }, FakeJsonName.CLIENT_ACCOUNTS_JSON);
    }

    public static ClientAccounts getClientSavingsAccount() {
        return mTestDataFactory.getObjectTypePojo(ClientAccounts.class,
                FakeJsonName.CLIENT_SAVINGS_ACCOUNT_JSON);
    }

    public static ClientAccounts getClientLoanAccount() {
        return mTestDataFactory.getObjectTypePojo(ClientAccounts.class,
                FakeJsonName.CLIENT_LOAN_ACCOUNT_JSON);
    }

    public static ClientAccounts getClientShareAccount() {
        return mTestDataFactory.getObjectTypePojo(ClientAccounts.class,
                FakeJsonName.CLIENT_SHARE_ACCOUNT_JSON);
    }

    public static List<Beneficiary> getBeneficiaries() {
        return mTestDataFactory.getListTypePojo(new TypeToken<List<Beneficiary>>() {
            }, FakeJsonName.BENEFICIARIES_JSON);
    }

    public static LoanAccount getLoanAccount() {
        return mTestDataFactory.getObjectTypePojo(LoanAccount.class,
                FakeJsonName.LOAN_ACCOUNT_JSON);
    }

    public static LoanWithAssociations getLoanAccountWithTransaction() {
        return mTestDataFactory.getObjectTypePojo(LoanWithAssociations.class,
                FakeJsonName.LOAN_ACCOUNT_WITH_TRANSACTIONS_JSON);
    }

    public static LoanWithAssociations getLoanAccountWithEmptyTransaction() {
        return mTestDataFactory.getObjectTypePojo(LoanWithAssociations.class,
                FakeJsonName.LOAN_ACCOUNT_WITH_EMPTY_TRANSACTIONS_JSON);
    }

    public static LoanWithAssociations getLoanAccountRepaymentSchedule() {
        return mTestDataFactory.getObjectTypePojo(LoanWithAssociations.class,
                FakeJsonName.LOAN_ACCOUNT_WITH_REPAYMENT_SCHEDULE_JSON);
    }
    public static LoanWithAssociations getLoanAccountEmptyRepaymentSchedule() {
        return mTestDataFactory.getObjectTypePojo(LoanWithAssociations.class,
                FakeJsonName.LOAN_ACCOUNT_WITH_EMPTY_REPAYMENT_SCHEDULE_JSON);
    }

    public static User getUser() {
        return mTestDataFactory.getObjectTypePojo(User.class,
                FakeJsonName.USER_JSON);
    }

    public static Page<Client> getClients() {
        return mTestDataFactory.getListTypePojo(new TypeToken<Page<Client>>() {
            }, FakeJsonName.CLIENTS_JSON);
    }

    public static Page<Client> getNoClients() {
        return mTestDataFactory.getListTypePojo(new TypeToken<Page<Client>>() {
        }, FakeJsonName.CLIENTS_NOT_FOUND_JSON);
    }
}
